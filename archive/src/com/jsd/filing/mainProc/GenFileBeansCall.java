package com.jsd.filing.mainProc;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import com.jsd.filing.utils.DigestMesg;

public class GenFileBeansCall implements Callable<Boolean>{
	private ArrayList<File>allFiles;
	private int beg;
	private int end;
	private UtensilTools utensilTools;
	public  GenFileBeansCall(ArrayList<File>allFiles,int beg,int end,UtensilTools utensilTools) {
		this.allFiles=allFiles;
		this.beg=beg;
		this.end=end;
		this.utensilTools=utensilTools;
	}
	@Override
	public Boolean call() throws Exception {
		DigestMesg digmsg = utensilTools.getDigmsg();

		for(int i=beg; i<=end; i++) {
			String md5Str = digmsg.getDigestStr(allFiles.get(i), "MD5");
			String shaStr = digmsg.getDigestStr(allFiles.get(i), "SHA-1");
			//get file's create time
			//give file a new name
			//add into concurentmap
		}
		return true;
	}

}
