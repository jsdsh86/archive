package com.jsd.filing.beans;

import java.time.LocalDateTime;

public class FileBean {
	private String srcFilePath;
	private Long srcFileLastModifyTime;
	private String desFileName;
	private String MD5;
	private String SHA1;
	private boolean isWrited;//after write into storage, will set to true
	
	

	public FileBean(String srcFilePath, Long srcFileLastModifyTime, String mD5, String sHA1, String desFileName,boolean isWrited) {
		super();
		this.srcFilePath = srcFilePath;
		this.srcFileLastModifyTime = srcFileLastModifyTime;
		this.MD5 = mD5;
		this.SHA1 = sHA1;
		this.isWrited = isWrited;
		this.desFileName=desFileName;
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






	public String getDesFileName() {
		return desFileName;
	}






	public void setDesFileName(String desFileName) {
		this.desFileName = desFileName;
	}






	public String getSrcFilePath() {
		return srcFilePath;
	}



	public void setSrcFilePath(String srcFileName) {
		this.srcFilePath = srcFileName;
	}



	public Long getsrcFileLastModifyTime() {
		return srcFileLastModifyTime;
	}






	public void setsrcFileLastModifyTime(Long srcFileLastModifyTime) {
		this.srcFileLastModifyTime = srcFileLastModifyTime;
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
