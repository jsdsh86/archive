package com.jsd.filing.mainProc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Worker {
	public List<File> getAllFiles(String fileString) {
		try {
			List<File> allFiles = new ArrayList<File>();
			String[] split = fileString.split(";");
			for (int i = 0; i < split.length; i++) {
				File file = new File(split[i]);
				if (file.isDirectory()) {
					List<File> dicFiles = new ArrayList<File>();
					getAllDicFiles(file, dicFiles);
					allFiles.addAll(dicFiles);
				} else {
					allFiles.add(file);
				}
			}

			return allFiles;

		} catch (Exception e) {
			return null;
		}

	}

	public List<File> getAllDicFiles(File file, List<File> dicFiles) {

		File[] listFiles = file.listFiles();
		for (File f : listFiles) {
			if (f.isDirectory()) {
				getAllDicFiles(f, dicFiles);
			} else {
				dicFiles.add(f);
			}
		}
		return dicFiles;
	}
	// public static void main(String[] args) {
	// Worker worker = new Worker();
	// List<File> allFiles =
	// worker.getAllFiles("C:\\Users\\cjqjsd\\Desktop\\�Ͼ�����cmdsRate��־;C:\\Users\\cjqjsd\\Desktop\\SpiderJackson-master");
	// for(File f:allFiles) {
	// System.out.println(f);
	// }
	// }
}
