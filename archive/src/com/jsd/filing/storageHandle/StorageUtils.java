package com.jsd.filing.storageHandle;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jsd.filing.beans.FileBean;
import com.jsd.filing.mainProc.ConstantNames;
import com.jsd.filing.utils.Utils;

public class StorageUtils {
	private static final String storageTotalLogPath = ConstantNames.STORAGE_TOTAL_LOG.getName();
	private static final String storageDetailLogPath = ConstantNames.STORAGE_DETAIL_LOG.getName();
	private static final String storageExceptionLogPath = ConstantNames.STORAGE_TOTAL_LOG.getName();
	private static final String storeFilesPath = ConstantNames.SOTRE_FILES.getName();
	private static final Logger logger = LogManager.getLogger();
	
	public static boolean storageCheck(String storage, ConcurrentHashMap<String, FileBean> sourceConHashMap) {
		Map<String, File> storageFilesMap = Utils.getStorageFilesMap(storage);
		boolean status = false;
		if(storageFilesMap!=null) {
			File storageTotalLog = storageFilesMap.get(storageTotalLogPath);
			File storageDetailLog = storageFilesMap.get(storageDetailLogPath);
			File storeFiles = storageFilesMap.get(storeFilesPath);
			long curStoreFilesLength = storeFiles.length();
			String[] curStoreFiles = storeFiles.list();
			int curStoreFilesNum = curStoreFiles.length;
			DataInputStream storageTotalIS = null;
			BufferedReader storeDetailLogReader=null;
			try {
				storageTotalIS = new DataInputStream(new FileInputStream(storageTotalLog));
				long lengthInTolLog = storageTotalIS.readLong();
				long numInTolLog = storageTotalIS.readLong();
				if(lengthInTolLog==curStoreFilesLength && numInTolLog==curStoreFilesNum) {
					status=true;
					//read record to sourceMap
					storeDetailLogReader = new BufferedReader(new FileReader(storageDetailLog));
					String readLine = null;
					while((readLine =storeDetailLogReader.readLine())!=null) {
						String[] split = readLine.split("\\|");
						sourceConHashMap.put(split[1].trim(), new FileBean(ConstantNames.READ_MODE.getName(),split[2].trim()));
					};
				}
				
			} catch (IOException e) {
				logger.info(" somebody deleted storage's important files, so, these files will generate again by current storeFiles. ");
				DataOutputStream totaloutput = null;
				try {
					totaloutput= new DataOutputStream( new FileOutputStream(storageTotalLog));
					totaloutput.writeLong(curStoreFilesLength); // write length first
					totaloutput.writeLong(curStoreFilesNum); // write num second
				} catch (FileNotFoundException e1) {
					logger.error(e1.getMessage());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					logger.error(e1.getMessage());
				}finally {
					try {
						totaloutput.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				e.printStackTrace();
			}finally {
				try {
					storageTotalIS.close();
					storeDetailLogReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else {
			logger.info("first time to start app, copy all file from source to storage");
			//TODO start move source file to storage
			status=true;
		}
		return status;
	}
	/**
	 * read detail file,compare with sotrefiles, if detail exist and storefile don't have ,delete detail record; if storefile exist, detail don't have, add record
	 * @param prcNum process no.
	 * @param paramMap
	 * @param sourceConHashMap used to store all storefile's hash 
	 * @return
	 */
	public static boolean  genStorageFiles(int prcNum, Map<String, Object> paramMap, ConcurrentHashMap<String, FileBean> sourceConHashMap ) {
		boolean flag=true;
		File storageDetailLog = (File) paramMap.get(storageDetailLogPath);
		File storeFiles = (File) paramMap.get(storageExceptionLogPath);
		File sotreExceptionLog = (File) paramMap.get(storageDetailLogPath);
		
		ExecutorService readStoreDetailTp = Executors.newFixedThreadPool(1);
		
		logger.debug("beg generate storage files' get detail log to map");
		Future<Map<String, FileBean>> getDetailContent = readStoreDetailTp.submit(new Callable< Map<String,FileBean>>() {
			@Override
			public Map<String,FileBean> call() throws Exception {
				Map<String,FileBean> map = new HashMap<String,FileBean>();
				BufferedReader storeDetailLogReader=null;
				String readLine = null;
				storeDetailLogReader = new BufferedReader(new FileReader(storageDetailLog));
				while((readLine =storeDetailLogReader.readLine())!=null) {
					String[] split = readLine.split("\\|");
					map.put(split[1].trim(), new FileBean(ConstantNames.READ_MODE.getName(),split[2].trim()));
				};
				storeDetailLogReader.close();
				return map;
			}
		});
		readStoreDetailTp.shutdown();
		logger.debug("generate storage files' get detail log to map submitted..");
		
		logger.debug("beg generate storage files' MD5 to map ...");
		ExecutorService genStorageTp = Executors.newFixedThreadPool(prcNum);
		File[]fileList = storeFiles.listFiles();
		List<Future<Map<String, FileBean>>> storeFilesFutList = new ArrayList<Future<Map<String, FileBean>>>();
		int beg=0;int end=0;
		for(int i=0;i<prcNum;i++) {
			beg =  fileList.length/prcNum*i;
			end = i==prcNum-1?fileList.length:(fileList.length/prcNum*(i+1));
			storeFilesFutList.add(genStorageTp.submit(new GetStoreFileMD5(fileList, beg, end)));
		}
		genStorageTp.shutdown();
		logger.debug("generate storage files'MD5 map submitted..");
		
		//COMPARE AND RECORD to detial log +  PUT TO MAP
		PrintWriter expPw = null;
		try {
			expPw = new PrintWriter(new FileOutputStream(sotreExceptionLog,true));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			Map<String, FileBean> storeFileMap = new HashMap<String,FileBean>();
			for(int i=0; i<storeFilesFutList.size();i++) {
				Map<String, FileBean> sfFutureMap = storeFilesFutList.get(i).get();
				for(Entry<String,FileBean> entry: sfFutureMap.entrySet()) {
					String key = entry.getKey();
					FileBean value = entry.getValue();
					FileBean putResult = storeFileMap.put(key, value);
					if(putResult!=null) {
						expPw.println(key+" has duplicat in "+value.toDetailString()+" and "+putResult.toDetailString()+" only "+value.toDetailString()+" is keep");
					}
				}

			}
			Map<String, FileBean> detailMap = getDetailContent.get();
			Set<String> sfKeySet = storeFileMap.keySet();
			Set<String> detailKeySet = detailMap.keySet();
			Iterator<String> sfKeyIt = sfKeySet.iterator();
			
			boolean hasEcp = false;
			while(sfKeyIt.hasNext()) {
				String sfKeyStr = sfKeyIt.next();
				if(detailKeySet.contains(sfKeyStr)) {
					 detailKeySet.remove(sfKeyStr); // remove detailKeySet if store file contains already
				}else {
					hasEcp=true; // when store file contains MD5 detail log hasn't should re-write detaillog
				}
				
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd-hh:MM:ss");
			// if detailMap still remains , these are already deleted in store file, write in excepiotn.log
			Iterator<String> detKeyIt = detailKeySet.iterator();
			while(detKeyIt.hasNext()) {
				hasEcp=true; // if detialLog has MD5 storefile hasn't should re-write detial log
				String detKeyStr = detKeyIt.next();
				expPw.println(sdf.format(new Date())+" | store file already delete but detail log remains | "+detKeyStr+" | "+detailMap.get(detKeyStr).toDetailString()+" | deleted in new detail log");
			}
			expPw.close();
			// if detial log and store file doesn't match, re-write hole detail log
			if(hasEcp) {
				PrintWriter detailPw = new PrintWriter(new FileOutputStream(storageDetailLog,false));
				while(sfKeyIt.hasNext()) {
					String sfKeyStr = sfKeyIt.next();
					detailPw.println(sdf.format(new Date())+" | "+sfKeyStr+" | "+storeFileMap.get(sfKeyStr).toDetailString());
				}
				detailPw.close();
			}
		} catch (InterruptedException | ExecutionException e) {
			flag=false;
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			flag=false;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
		
		
	}
	public static void writeFBToDetLog(FileBean fileBean, Map<String, Object> paramMap) {
			PrintWriter pw = (PrintWriter) paramMap.get(ConstantNames.DETIAL_LOG_PW.getName());
			SimpleDateFormat sdf = (SimpleDateFormat) paramMap.get(ConstantNames.DATE_FORMAT.getName());
			pw.println(sdf.format(new Date())+" | "+fileBean.getMD5()+" | "+fileBean.toDetailString());
			pw.flush();

		
	}
}
