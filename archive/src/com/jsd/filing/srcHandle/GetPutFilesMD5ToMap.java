package com.jsd.filing.srcHandle;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jsd.filing.beans.FileBean;
import com.jsd.filing.mainProc.ConstantNames;
import com.jsd.filing.utils.MD5Utils;

public class GetPutFilesMD5ToMap implements Callable<Integer> {
	private int beg;
	private int end;
	private List<String>sourceFileList;
	private ConcurrentHashMap<String,FileBean> sourceConHashMap;
	private ArrayBlockingQueue<FileBean> addQue;
	private Map<String,Object>paramMap;
	private static final Logger logger = LogManager.getLogger(GetPutFilesMD5ToMap.class);
	public GetPutFilesMD5ToMap(List<String>sourceFileList,int beg,int end,ConcurrentHashMap<String,FileBean> sourceConHashMap, ArrayBlockingQueue<FileBean> addQue, Map<String, Object> paramMap){
		this.sourceFileList=sourceFileList;
		this.beg=beg;
		this.end=end;
		this.sourceConHashMap = sourceConHashMap;
		this.addQue=addQue;
		this.paramMap=paramMap;
	}
	@Override
	public Integer call() throws Exception {
		int count=0;
		MD5Utils md5Util = new MD5Utils();
		for(int i=beg; i<end;i++) {
			String absloutPath = sourceFileList.get(i);
			logger.info("beg get MD5 of "+absloutPath+" ...");
			String md5 = md5Util.getMD5(new File(absloutPath));
			logger.info("end get MD5 of "+absloutPath+"MD5 is "+md5);
			FileBean fileBean = new FileBean(absloutPath);
			FileBean duplicateMD5 = sourceConHashMap.put(md5, fileBean);
			
			if(duplicateMD5!=null) {
				// sourceConHashMap has this md5 before
				logger.info(Thread.currentThread()+" find a duplicate MD5 : "+sourceFileList.get(i)+" and "+duplicateMD5.getSrcFileName());
			}else {
				addQue.put(fileBean);
				AtomicInteger cont =(AtomicInteger) paramMap.get(ConstantNames.PUTINTO_SIZE.getName());
				cont.incrementAndGet();
				logger.info("put "+fileBean.getSrcFileName()+" to queue success ...");
				count++;
			}
		}
		return count;
	}

}
