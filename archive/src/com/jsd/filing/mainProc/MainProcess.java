package com.jsd.filing.mainProc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.jsd.filing.beans.FileBean;
import com.jsd.filing.utils.Utils;

/**
 * @author cjqjsd
 * concurrenthashmap should like [md5+sha1,bean]
 * 
 *
 */
public class MainProcess {
public static ConcurrentHashMap<String, FileBean> fileBeanCurMap = new ConcurrentHashMap<>(); // only used for 
	public static void main(String[] args) {
	// 1, list all files
		String dic= new String();
		dic="";
		Worker worker = new Worker();
		List<File> allFiles = worker.getAllFiles(dic);
	// 2, generate filebeans and put into map
		int threadNum = Utils.getThreadNum();
		ExecutorService genBeanPool = Executors.newFixedThreadPool(threadNum);
		List<Future<String>> beanResultList = new ArrayList<Future<String>>();  
		for(int i=0; i<threadNum;i++) {
//			genBeanPool.submit(new );
		}
		// 3, when generate finished, write to new dest
		
	}
}
