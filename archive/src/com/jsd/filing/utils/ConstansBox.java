package com.jsd.filing.utils;

import java.util.concurrent.ConcurrentHashMap;

import com.jsd.filing.beans.FileBean;

public class ConstansBox {
	public static ConcurrentHashMap<Long, Integer>modifyContCon=new ConcurrentHashMap<Long,Integer>();
	public static ConcurrentHashMap<String,FileBean>fileBeanCon=new ConcurrentHashMap<String,FileBean>();
	public static String prefix = "C:\\Users\\cjqjsd\\Desktop\\cp1";
	public static Integer curModifyCont(Long curIntTime) {
		int curTime=0;
		if(modifyContCon.get(curIntTime)!=null) {
			curTime=modifyContCon.get(curIntTime);
			modifyContCon.put(curIntTime, ++curTime);
		}
		return curTime;

	}
}
