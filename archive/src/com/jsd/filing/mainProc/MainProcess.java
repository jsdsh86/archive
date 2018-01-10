package com.jsd.filing.mainProc;

import com.jsd.filing.utils.DigestMesg;

public class MainProcess {
	private ThreadLocal<DigestMesg> digmsgTl = new ThreadLocal<DigestMesg>() {
		@Override
		protected DigestMesg initialValue() {
			// TODO Auto-generated method stub
			return new DigestMesg();
		}
	};
	public DigestMesg getDigmsg() {
		return digmsgTl.get();
	}
	public static void main(String[] args) {
	// 1, list all files
	// 2, create threads to get md5, sha1 and put into map
	// 3, 
	}
}
