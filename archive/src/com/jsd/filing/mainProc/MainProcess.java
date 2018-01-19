package com.jsd.filing.mainProc;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jsd.filing.beans.FileBean;
import com.jsd.filing.utils.ConstansBox;
import com.jsd.filing.utils.Utils;

/**
 * @author cjqjsd
 * concurrenthashmap should like [md5+sha1,bean]
 * 
 *
 */
public class MainProcess {
public static ConcurrentHashMap<String, FileBean> fileBeanCurMap = new ConcurrentHashMap<>(); // only used for 
private static final Logger logger = LogManager.getLogger(MainProcess.class);
public static void main(String[] args) {
	// 1, list all files
		String dic= new String();
		dic="C:\\Users\\cjqjsd\\Desktop\\无锡市豫达换热器有限公司_files";
		Worker worker = new Worker();
		ArrayList<File> allFiles = worker.getAllFiles(dic);
	// 2, generate filebeans and put into map
		int threadNum = Utils.getThreadNum();
		ExecutorService threadPool = Executors.newFixedThreadPool(threadNum);
		List<Future<Boolean>> beanResultList = new ArrayList<Future<Boolean>>();  
		int gap=threadNum;
		int pace=allFiles.size()/threadNum;
		int residue = allFiles.size()%threadNum;
		for(int i=0; i<threadNum;i++) {
			FutureTask<Boolean> beanfutureTask = new FutureTask<Boolean>(new GenFileBeansCall(allFiles,i*pace,((i+1)*pace>(allFiles.size()-1)?(allFiles.size()-1):(i+1)*pace),new UtensilTools()));
			threadPool.submit(beanfutureTask);
			beanResultList.add(beanfutureTask);
		}
		if(residue!=0) {
			FutureTask<Boolean> beanfutureTask = new FutureTask<Boolean>(new GenFileBeansCall(allFiles,gap*pace,(gap*pace+residue-1),new UtensilTools()));
			threadPool.submit(beanfutureTask);
			beanResultList.add(beanfutureTask);
		}
		// 3, when generate finished, write to new dest
		boolean finish=true;
		for(Future<Boolean> beanResult :beanResultList ) {
			try {
				finish = finish && (beanResult.get()); 
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				finish = false;
			}
		}
		if(finish) {
			List<Future<Boolean>> copyFileResultList = new ArrayList<Future<Boolean>>();  
			Collection<FileBean> values = ConstansBox.fileBeanCon.values();
			ArrayList<FileBean> fileArray = new ArrayList<FileBean>();
			Iterator<FileBean> iterator = values.iterator();
			while(iterator.hasNext()) {
				fileArray.add(iterator.next());
			}
			pace = fileArray.size()/threadNum;
			residue = fileArray.size()%threadNum;
			for(int i=0; i<threadNum;i++) {
				FutureTask<Boolean> copyBeanfutureTask = new FutureTask<Boolean>(new WriteFileBeansCall(fileArray,i*pace,((i+1)*pace>(allFiles.size()-1)?(allFiles.size()-1):(i+1)*pace),new UtensilTools()));
				threadPool.submit(copyBeanfutureTask);
				copyFileResultList.add(copyBeanfutureTask);
			}
			if(residue!=0) {
				FutureTask<Boolean> copyBeanfutureTask = new FutureTask<Boolean>(new WriteFileBeansCall(fileArray,gap*pace,(gap*pace+residue-1),new UtensilTools()));
				threadPool.submit(copyBeanfutureTask);
				copyFileResultList.add(copyBeanfutureTask);
			}
			for(Future<Boolean> copyBeanResult :copyFileResultList ) {
				try {
					finish = finish && (copyBeanResult.get()); 
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					finish = false;
				}
			}
			if(finish)logger.info("finish archieve");
		}
	}
}
