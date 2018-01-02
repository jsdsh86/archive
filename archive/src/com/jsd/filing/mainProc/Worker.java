package com.jsd.filing.mainProc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Worker {
	public List<File> getAllFiles(String fileString) {
		String[] split = fileString.split(";");
		List<File> allFiles = new ArrayList<File>();
		for(int i=0;i<split.length;i++) {
			File file = new File(split[i]);
			if(file.isDirectory()) {
				List<File> dicFiles = new ArrayList<File>();
				getAllDicFiles(file,dicFiles);
				allFiles.addAll(dicFiles);
			}else {
				allFiles.add(file);
			}
		}
		
		return allFiles;
		
	}

	public List<File> getAllDicFiles(File file, List<File> dicFiles) {

		File[] listFiles = file.listFiles();
		for(File f:listFiles) {
			if(f.isDirectory()) {
				getAllDicFiles(f,dicFiles);
			}else {
				dicFiles.add(f);
			}
		}
		return dicFiles;
	}
	 
}
