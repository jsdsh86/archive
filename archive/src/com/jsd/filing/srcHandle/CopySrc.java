package com.jsd.filing.srcHandle;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jsd.filing.beans.FileBean;
import com.jsd.filing.mainProc.ConstantNames;
import com.jsd.filing.storageHandle.StorageUtils;
import com.jsd.filing.utils.Utils;

public class CopySrc implements Callable<Integer> {
	private Map<String, Object> paramMap;
	private ArrayBlockingQueue<FileBean> addQue;
	private static final Logger logger = LogManager.getLogger(CopySrc.class);
	public CopySrc(Map<String, Object> paramMap, ArrayBlockingQueue<FileBean> addQue) {
		this.paramMap=paramMap;
		this.addQue=addQue;
	}
	@Override
	public Integer call() throws Exception {
		Integer result=0;
		FileBean fileBean ;
		boolean status=true;
		while(status) {
			fileBean= addQue.take();
			if(Utils.copy(fileBean,paramMap)) {
				StorageUtils.writeFBToDetLog(fileBean,paramMap);
				result+=1;
			}else {
				logger.error(fileBean.toDetailString()+" occoured error in copy process");
			}
			AtomicInteger cont =(AtomicInteger) paramMap.get(ConstantNames.PUTINTO_SIZE.getName());
			int get = cont.decrementAndGet();
			if(get<=0)status=false;
		}
		return result;
	}

}
