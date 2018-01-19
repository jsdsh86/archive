package com.jsd.filing.mainProc;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jsd.filing.beans.FileBean;
import com.jsd.filing.utils.ConstansBox;
import com.jsd.filing.utils.DigestMesg;

public class GenFileBeansCall implements Callable<Boolean>{
	private ArrayList<File>allFiles;
	private int beg;
	private int end;
	private static final Logger logger = LogManager.getLogger(GenFileBeansCall.class);
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
		System.out.println(Thread.currentThread()+" beg genfilebean");
		for(int i=beg; i<=end; i++) {
			File curFile = allFiles.get(i);
			String md5Str = digmsg.getDigestStr(curFile, "MD5");
			String shaStr = digmsg.getDigestStr(curFile, "SHA-1");
			//get file's lastmodifytime 
			long lastModified = curFile.lastModified();
			String srcFilePath = curFile.getAbsolutePath();
			String suff = srcFilePath.substring(srcFilePath.lastIndexOf("."),srcFilePath.length()); 
			//give file a new name
			String desFileName = ConstansBox.prefix+ConstansBox.curModifyCont(lastModified)+suff;
			//add into concurentmap
			FileBean kickoffBean = ConstansBox.fileBeanCon.put(md5Str+shaStr, new FileBean(srcFilePath,lastModified,md5Str,shaStr,desFileName,false));
			if(kickoffBean!=null) {
				logger.info("kickoff one more file "+srcFilePath+" already have "+kickoffBean.getDesFileName());
			}else {
				logger.info(srcFilePath+" already add into map ");
			}
		}
		return true;
	}

}
