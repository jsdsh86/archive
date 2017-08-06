package com.jsd.filing.storageHandle;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jsd.filing.beans.FileBean;
import com.jsd.filing.srcHandle.GetPutFilesMD5ToMap;
import com.jsd.filing.utils.MD5Utils;

public class GetStoreFileMD5 implements Callable<Map<String,FileBean>> {
	private int beg;
	private int end;
	private File[] fileList;
	private static final Logger logger = LogManager.getLogger(GetPutFilesMD5ToMap.class);
	public GetStoreFileMD5(File[] fileList,int beg,int end){
		this.fileList=fileList;
		this.beg=beg;
		this.end=end;
	}
	@Override
	public Map<String,FileBean> call() throws Exception {
		MD5Utils md5Util = new MD5Utils();
		Map<String,FileBean> map = new HashMap<String,FileBean>();
		for(int i=beg; i<end;i++) {
			String absloutPath = fileList[i].getAbsolutePath();
			logger.info("beg get MD5 of "+absloutPath+" ...");
			String md5 = md5Util.getMD5(new File(absloutPath));
			logger.info("end get MD5 of "+absloutPath+"MD5 is "+md5);
			FileBean fileBean = new FileBean(absloutPath);
			map.put(md5, fileBean);
		}
		return map;
	}
}
