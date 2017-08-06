package com.jsd.filing.beans;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class FileBean {
	private String srcFileName;
	private long srcFileModifyTimeStamp;
	private String newFileName;
	private String newFileTimeStamp; //this timestamp is the lastmodifytime of storage file,this attr will occur after filing;
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
		this.srcFileName=srcFileName;
		String srcPath = file.getName();
		this.newFileName=System.nanoTime()+"_"+UUID.randomUUID().toString()+"_"+srcPath;
		this.isWrited=false; // 
	}
	
	
	

	/**used in read storage model
	 * @param name
	 * @param trim
	 */
	public FileBean(String name, String storageDetail) {
		String[] detail = storageDetail.split(",");
		this.newFileName=detail[0].trim();
		this.newFileTimeStamp=detail[1].trim();
		this.srcFileName=detail[2].trim();
		this.isWrited=true; // 
	}




	public String toDetailString() {
		return new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) +" | MD5=" + MD5  + " | newFileName=" + newFileName + ", newFileTimeStamp="
				+ newFileTimeStamp + ", srcFileName=" + srcFileName;
	}


	public String getSrcFileName() {
		return srcFileName;
	}



	public void setSrcFileName(String srcFileName) {
		this.srcFileName = srcFileName;
	}



	public long getSrcFileModifyTimeStamp() {
		return srcFileModifyTimeStamp;
	}



	public void setSrcFileModifyTimeStamp(long srcFileModifyTimeStamp) {
		this.srcFileModifyTimeStamp = srcFileModifyTimeStamp;
	}



	public String getNewFileName() {
		return newFileName;
	}



	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
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


	public String getNewFileTimeStamp() {
		return newFileTimeStamp;
	}


	public void setNewFileTimeStamp(String newFileTimeStamp) {
		this.newFileTimeStamp = newFileTimeStamp;
	}


}
