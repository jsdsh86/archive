package com.jsd.filing.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.jsd.filing.beans.FileBean;
import com.jsd.filing.mainProc.ConstantNames;

public class Utils {

	public static ArrayList<String> listAllFiles(String sourceFile) {
		String[] sourceFileArr = sourceFile.split(";");
		ArrayList<String> sourceFileList = new ArrayList<String>();
		for(int i=0; i<sourceFileArr.length;i++) {
			String srcFileStr = sourceFileArr[i];
			getAllFiles(srcFileStr,sourceFileList);
		}
		return sourceFileList;		
	}
	
	public static void getAllFiles(String src, ArrayList<String> sourceFileList) {
		File dic = new File(src);
		File[] dicList = dic.listFiles();
		for(int i=0; i<dicList.length;i++) {
			if(dicList[i].isDirectory()) {
				getAllFiles(dicList[i].getAbsolutePath(),sourceFileList);
			}else {
				sourceFileList.add(dicList[i].getAbsolutePath());
			}
		}
	}
	
	public static Map<String,File> getStorageFilesMap(String storage){
		Map<String,File> storageFilesMap = new HashMap<String,File>();
		File storageFile = new File(storage);
		if(storageFile.exists()) {
			File[] storageFileList = storageFile.listFiles();
			for(int i=0; i<storageFileList.length;i++) {
				storageFilesMap.put(storageFileList[i].getName(), storageFileList[i]);
			}
			return storageFilesMap;
		}else {
			return null;
		}
	}
	public static void main(String[] args) {
		System.out.println(Utils.listAllFiles("C:\\"));
	}

	public static boolean checFutureFinished(List<FutureTask<Integer>> futureList) {
		boolean status=false;
		int futureResult=0;
		for(FutureTask<Integer> srcFut:futureList) {
			try {
				futureResult+=srcFut.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		if(futureResult==futureList.size()) {
			status  = true ;
		}

		return status;
	}

	public static boolean copy(FileBean fileBean, Map<String, Object> paramMap) {
		boolean success = true;
		String resource = fileBean.getSrcFileName();
		String storageDir = (String) paramMap.get(ConstantNames.STORAGE.getName());
		String destination = storageDir+"/"+fileBean.getNewFileName();
		try {
			nioCopyFile(resource,destination);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			success = false;
			e.printStackTrace();
		}
		return success;
	}
	
	public static void nioCopyFile(String resource, String destination) throws IOException {
		FileInputStream fis = new FileInputStream(resource);
		FileOutputStream fos = new FileOutputStream(destination);
		FileChannel readChannel = fis.getChannel(); 
		FileChannel writeChannel = fos.getChannel(); 
		ByteBuffer buffer = ByteBuffer.allocate(1024*1024); 
		while (true) {
			buffer.clear();
			int len = readChannel.read(buffer); 
			if (len == -1) {
				break; // finsh read
			}
			buffer.flip();
			writeChannel.write(buffer); // write
		}
		readChannel.close();
		writeChannel.close();
	}
}
