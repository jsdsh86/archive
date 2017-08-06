package com.jsd.filing.mainProc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jsd.filing.beans.FileBean;
import com.jsd.filing.srcHandle.CopySrc;
import com.jsd.filing.srcHandle.GetPutFilesMD5;
import com.jsd.filing.storageHandle.StorageUtils;
import com.jsd.filing.utils.Utils;

public class MainProcess {
	protected ConcurrentHashMap<String,FileBean> sourceConHashMap; // source map<md5,filebean>
	protected ArrayBlockingQueue<FileBean> addQue; // source file that not exist in storage will add into addqueue only once
	
	private static final Logger logger = LogManager.getLogger(MainProcess.class);
	private String storage = null;
	private String sourceFile=null;
	private Map<String,Object> paramMap = new HashMap<String,Object>();
	public int prcNum;
	private static final String storageStr = ConstantNames.STORAGE.getName();
	private static final String storageTotalLogPath = ConstantNames.STORAGE_TOTAL_LOG.getName();
	private static final String storageDetailLogPath = ConstantNames.STORAGE_DETAIL_LOG.getName();
	private static final String storageExceptionLogPath = ConstantNames.STORAGE_TOTAL_LOG.getName();
	private static final String storeFilesPath = ConstantNames.SOTRE_FILES.getName();
	
	public void init() {
		sourceConHashMap = new  ConcurrentHashMap<String,FileBean>();
		addQue = new ArrayBlockingQueue<FileBean>(64);
		// read config.cfg
		ArchiveProperties ap = new ArchiveProperties();
		storage = ap.getStorage();
		sourceFile = ap.getSourceFile();
		paramMap.put(storageStr, storage);
		Map<String, File> storageFilesMap = Utils.getStorageFilesMap(storage);
		if(storageFilesMap!=null) {
			
			File storageTotalLog = storageFilesMap.get(storageTotalLogPath);
			File storageDetailLog = storageFilesMap.get(storageDetailLogPath);
			File sotreExceptionLog = storageFilesMap.get(storageExceptionLogPath);
			File storeFiles = storageFilesMap.get(storeFilesPath);
			paramMap.put(storageTotalLogPath, storageTotalLog);
			paramMap.put(storageDetailLogPath, storageDetailLog);
			paramMap.put(storageExceptionLogPath, sotreExceptionLog);
			paramMap.put(storeFilesPath, storeFiles);
		}
		paramMap.put(ConstantNames.DATE_FORMAT.getName(), new SimpleDateFormat("yyyy-MM-dd-HH:mm:SS"));
		// get process num system have
		prcNum = Runtime.getRuntime().availableProcessors();
		if(prcNum<2)prcNum=2;
		
		// start thread to self-check of storefile dictionary;
		PrintWriter detailLogPw;
		try {
			detailLogPw = new PrintWriter(new FileOutputStream(storageDetailLogPath,true));
			paramMap.put(ConstantNames.DETIAL_LOG_PW.getName(), detailLogPw);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		boolean storageCheckStatus = StorageUtils.storageCheck(storage,sourceConHashMap);
		if(!storageCheckStatus) { //storage file is neither normal or not modified
			logger.debug("totalLog and store File does not matched, start to check store file and detail.log");
			//storage is modified, so ,delete total.log and detail.log,generate new one
			 StorageUtils.genStorageFiles(prcNum,paramMap,sourceConHashMap);
			// must startMove after genStorageFiles finished
			 logger.debug("finish file store and detial log's selfcheck ");
		}else {
			logger.info("finish storage check");
		}
		
		// start thread to read sourcefiles and get MD5,write to sourceConHashMap
		List<FutureTask<Integer>> srcMD5resultList = startGenSrcMd5(prcNum,sourceFile,paramMap);
		paramMap.put(ConstantNames.SRC_MD5_LIST.getName(), srcMD5resultList);
		paramMap.put(ConstantNames.PUTINTO_SIZE.getName(), new AtomicInteger()); // queue size
		
	}
	

	// move source file to strage and write into log

	private int startMove(int prcNum, Map<String, Object> paramMap, ArrayBlockingQueue<FileBean> addQue) throws InterruptedException, ExecutionException {
		ExecutorService cpTp = Executors.newFixedThreadPool(prcNum);
		List<Future<Integer>> cpResultList= new ArrayList<Future<Integer>>();
		for(int i=0;i<prcNum;i++) {
			cpResultList.add(cpTp.submit(new CopySrc(paramMap,addQue)));
		}
		cpTp.shutdown();
		int cont=0;
		for(Future<Integer> fu : cpResultList) {
			cont+=fu.get();
		}
		return cont;
	}
	
	private int getSrcCnt() throws InterruptedException, ExecutionException {
		List<Future<Integer>> srcFutList =(List<Future<Integer>>) paramMap.get(ConstantNames.SRC_MD5_LIST.getName());
		int srcCont=0;
		for(Future<Integer> fu : srcFutList) {
			srcCont+=fu.get();
		}
		return srcCont;
	}

	private void finish() {
		PrintWriter pw =(PrintWriter)paramMap.get(ConstantNames.DETIAL_LOG_PW.getName());
		pw.close();
		
	}

	/**
	 * get source file's MD5 and add to sourceConHashMap, if sourceConHashMap doesn't contians, add to addQue
	 * @param prcNum process No.
	 * @param sourceFile 
	 * @param paramMap 
	 */
	private List<FutureTask<Integer>> startGenSrcMd5(int prcNum, String sourceFile, Map<String, Object> paramMap) {
		//add all files into sourceFileList
		ArrayList<String> sourceFileList = Utils.listAllFiles(sourceFile);
		logger.info("---start to get MD5 for source file ---");
		List<FutureTask<Integer>> srcMD5FTResultList = GetPutFilesMD5.getAndAddFilesMD5(prcNum, sourceFileList, sourceConHashMap,addQue,paramMap);
		logger.info("---end get MD5 for source file ---");
		return srcMD5FTResultList;
	}
	
	public void start() {
		init();
		try {
			int	cpCont=startMove( prcNum,paramMap,addQue);
			int srcCont=getSrcCnt();
			// if cpCont is not equal to srcCont, means take queue is still on,continue
			while(cpCont!=srcCont) {
				cpCont+=startMove( prcNum,paramMap,addQue);
				System.out.println("cpcont "+cpCont+" srcCont "+srcCont);
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		finish();
	}
}
