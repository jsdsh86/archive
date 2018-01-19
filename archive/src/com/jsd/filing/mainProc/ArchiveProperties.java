package com.jsd.filing.mainProc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ArchiveProperties {
	private static Properties prop = new Properties();
	private static final String  confPath = "";
	private static final String sourceFile =""; // contains detail.log,total.log,exception.log and storeFile
	private static final String storage = "";
	private static final String detailLog="detail.log"; // successfuly write into storeFile
	private static final String totalLog="total.log"; //all info
	private static final String ecpLog="exception.log"; //excption log
	private static final String storeFile = "storeFile"; // all file archived dic
	static {
		try {
			prop.load(new FileInputStream(new File(confPath)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * plsuse
	 * @return
	 */
	@Deprecated 
	public String getSourceFile () {
		return prop.getProperty(sourceFile);
	}
	public String getStorage() {
		return prop.getProperty(storage);
	}
	public String getDetailLog() {
		return prop.getProperty(detailLog);
	}
	public String getTotalLog() {
		return prop.getProperty(totalLog);
	}
	public String getEcpLog() {
		return prop.getProperty(ecpLog);
	}
	public String getStoreFile() {
		return prop.getProperty(storeFile);
	}
}
