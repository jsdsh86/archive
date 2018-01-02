package com.jsd.filing.beans;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class FileBean {
	private String srcFilePath;
	private long srcFileModifyTimeStamp;
	private String MD5;
	private String SHA1;
	private boolean isWrited;//after write into storage, will set to true
	
	

	/**
	 * used in get source model
	 * @param srcFileName
	 */
	public FileBean(String srcFileName) {
		File file = new File(srcFileName);
		this.srcFileModifyTimeStamp=file.lastModified();
		this.srcFilePath=srcFilePath;
		String srcPath = file.getName();
		this.isWrited=false; // 
	}
	
	
	

	/**used in read storage model
	 * @param name
	 * @param trim
	 */
	public FileBean(String name, String storageDetail) {
		String[] detail = storageDetail.split(",");
		this.srcFilePath=detail[2].trim();
		this.isWrited=true; // 
	}






	public String getSrcFilePath() {
		return srcFilePath;
	}



	public void setSrcFilePath(String srcFileName) {
		this.srcFilePath = srcFileName;
	}



	public long getSrcFileModifyTimeStamp() {
		return srcFileModifyTimeStamp;
	}



	public void setSrcFileModifyTimeStamp(long srcFileModifyTimeStamp) {
		this.srcFileModifyTimeStamp = srcFileModifyTimeStamp;
	}






	public String getMD5() {
		return MD5;
	}



	public void setMD5(String mD5) {
		MD5 = mD5;
	}



	public String getSHA1() {
		return SHA1;
	}



	public void setSHA1(String sHA1) {
		SHA1 = sHA1;
	}



	public boolean isWrited() {
		return isWrited;
	}



	public void setWrited(boolean isWrited) {
		this.isWrited = isWrited;
	}



}
