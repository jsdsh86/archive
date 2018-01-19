package com.jsd.filing.mainProc;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jsd.filing.beans.FileBean;
import com.jsd.filing.utils.Utils;

public class WriteFileBeansCall implements Callable<Boolean>{
	private ArrayList<FileBean> fileArray;
	private int beg;
	private int end;
	private static final Logger logger = LogManager.getLogger(WriteFileBeansCall.class);
	private UtensilTools utensilTools;
	public  WriteFileBeansCall(ArrayList<FileBean> fileArray,int beg,int end,UtensilTools utensilTools) {
		this.fileArray = fileArray;
		this.beg=beg;
		this.end=end;
		this.utensilTools=utensilTools;
	}
	@Override
	public Boolean call() throws Exception {
		Utils cpTools = utensilTools.getCpTools();
		for(int i=beg; i<=end; i++) {
			FileBean curFile = fileArray.get(i);
			cpTools.nioCopyFile(curFile.getSrcFilePath(), curFile.getDesFileName());
		}
		return true;
	}

}
