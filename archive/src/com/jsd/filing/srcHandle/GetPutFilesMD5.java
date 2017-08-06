package com.jsd.filing.srcHandle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jsd.filing.beans.FileBean;

public class GetPutFilesMD5 {
	/**
	 * start threads to generate MD5
	 * thread No addrocing to process No.
	 * @param prcNum  --- process no
	 * @param sourceFileList  
	 * @param sourceConHashMap -- conHashMap<md5,filebean>
	 */
	private static final Logger logger = LogManager.getLogger(GetPutFilesMD5.class);
	private static final int SOURCE_KIND=1;
	private static final int STORE_KIND=2;

	
	public static List<FutureTask<Integer>> getAndAddFilesMD5(int prcNum,ArrayList<String> fileList,ConcurrentHashMap<String, FileBean> conHashMap, ArrayBlockingQueue<FileBean> addQue, Map<String, Object> paramMap) {
		ExecutorService srcMD5TrdPool = Executors.newFixedThreadPool(prcNum);
		int beg;
		int end;
		List<FutureTask<Integer>> getMD5FTResultList = new ArrayList<FutureTask<Integer>>();
		for(int i=0;i<prcNum;i++) {
			beg =  fileList.size()/prcNum*i;
			end = i==prcNum-1?fileList.size():(fileList.size()/prcNum*(i+1));
			getMD5FTResultList.add((FutureTask<Integer>) srcMD5TrdPool.submit(new GetPutFilesMD5ToMap(fileList, beg, end,conHashMap,addQue,paramMap)));
		}
		srcMD5TrdPool.shutdown();
		return getMD5FTResultList;
	}
	
	

}
