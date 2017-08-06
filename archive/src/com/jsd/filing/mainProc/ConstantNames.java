package com.jsd.filing.mainProc;

public enum ConstantNames {
	STORAGE("storage"),STORAGE_TOTAL_LOG("total.log"),STORAGE_DETAIL_LOG("detail.log"),STORAGE_EXCEPTION_LOG("exception.log"),SOTRE_FILES("storeFiles"),READ_MODE("readMode"),
		DETIAL_LOG_PW("detailLogPw"),DATE_FORMAT("dateFormat"),SRC_MD5_LIST("srcMd5List"),PUTINTO_SIZE("putinSize");
	private ConstantNames(String name) {
		this.name=name;
	}
	private String name;
	public String getName() {
		return name;
	}
}
