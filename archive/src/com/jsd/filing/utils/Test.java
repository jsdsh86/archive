package com.jsd.filing.utils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

public class Test {

	public static void main(String[] args) {
		Utils utils = new Utils();
		try {
			utils.nioCopyFile("C:\\Users\\cjqjsd\\Desktop\\SpiderJackson-master.zip", "C:\\Users\\cjqjsd\\Desktop\\a19.zip");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		ArrayList arr = new ArrayList();
//		for(int i =0;i<34;i++) {
//			 arr.add(i);
//		}
//		System.out.println(arr);
//		ArrayList jrr= new ArrayList();
//		int gap=4;
//		int pace=arr.size()/4;
//		int residue = arr.size()%4;
//		for(int j=0; j<4;j++) {
////			System.out.println(j);
//			System.out.println("beg "+arr.get(j*pace)  );
//			System.out.println("end "+arr.get((j+1)*pace>(arr.size()-1)?(arr.size()-1):(j+1)*pace));
//		}
//		if(residue!=0) {
//			System.out.println("beg residue"+arr.get(4*pace)  );
//			System.out.println("end residue"+arr.get(4*pace+residue-1));
//		}
//		System.out.println(arr.size());
//		
//		boolean a = true;
//		boolean b = false;
//		System.out.println(a&&b);
//		readPic();
//		String time ="Jan:02:09:19:53:CST:2018";
//		String time ="Tue Jan 17 15:56:49 CST 2017";
//		time="Wed Sep 16 11:26:23 CST 2009";
//		LocalDateTime lt = LocalDateTime.now();
//		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy",Locale.US);
//		LocalDateTime dateTime = LocalDateTime.parse(time,pattern);
//		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
//		Date parse = null;
//		System.out.println(dateTime+"_01");
//		File curFile=new File("C:\\Users\\cjqjsd\\Desktop\\da.jpg");
//		String srcFilePath = curFile.getAbsolutePath();
//		String suff = srcFilePath.substring(srcFilePath.lastIndexOf("."),srcFilePath.length()); 
//		System.out.println(suff);
		 // 定义一个任意格式的日期时间字符串  
//        String str1 = "2014==04==12 01时06分09秒";  
//        // 根据需要解析的日期、时间字符串定义解析所用的格式器  
//        DateTimeFormatter fomatter1 = DateTimeFormatter  
//            .ofPattern("yyyy==MM==dd HH时mm分ss秒");  
//        // 执行解析  
//        LocalDateTime dt1 = LocalDateTime.parse(str1, fomatter1);  
//        System.out.println(dt1); // 输出 2014-04-12T01:06:09  
        // ---下面代码再次解析另一个字符串---  
//        String str2 = "2014 四月 13 20小时";  
//        DateTimeFormatter fomatter2 = DateTimeFormatter  
//            .ofPattern("yyy MMM dd HH小时");  
//        LocalDateTime dt2 = LocalDateTime.parse(str2, fomatter2);  
//        System.out.println(dt2); // 输出 2014-04-13T20:00  
	}
	 private static void readPic() {
	        File jpegFile = new File("C:\\Users\\cjqjsd\\Desktop\\da.jpg");
//		 File jpegFile = new File("C:\\Users\\cjqjsd\\Desktop\\微信图片_20180102091947.jpg");
	        System.out.println(jpegFile.lastModified());
	        Metadata metadata;
	        try {
	            metadata = JpegMetadataReader.readMetadata(jpegFile);
	            Iterator<Directory> it = metadata.getDirectories().iterator();
	            while (it.hasNext()) {
	                Directory exif = it.next();
	                Iterator<Tag> tags = exif.getTags().iterator();
	                while (tags.hasNext()) {
	                    Tag tag = (Tag) tags.next();
	                    if(tag.getTagName()!=null && tag.getTagName().equals("File Modified Date")) {
	                    	String modifyStr = tag.getDescription();
	        				DateTimeFormatter pattern = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy",Locale.US);
	        				LocalDateTime dateTime = LocalDateTime.parse(modifyStr,pattern);
	        				System.out.println(dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
	                    }

	                }

	            }
	        } catch (JpegProcessingException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	 }
}
