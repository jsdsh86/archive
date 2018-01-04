package com.jsd.filing.utils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

public class Test {

	public static void main(String[] args) {
//		readPic();
//		String time ="Jan:02:09:19:53:CST:2018";
		String time ="Tue Jan 17 15:56:49 CST 2017";
//		time="Wed Sep 16 11:26:23 CST 2009";
//		LocalDateTime lt = LocalDateTime.now();
//		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");
//		LocalDateTime dateTime = LocalDateTime.parse(time,pattern);
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
		Date parse = null;
		try {
			parse = sdf.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(parse);
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
	        File jpegFile = new File("C:\\Users\\cjqjsd\\Desktop\\2017-01-17_my_vocation.jpg");
//		 File jpegFile = new File("C:\\Users\\cjqjsd\\Desktop\\微信图片_20180102091947.jpg");
	        Metadata metadata;
	        try {
	            metadata = JpegMetadataReader.readMetadata(jpegFile);
	            Iterator<Directory> it = metadata.getDirectories().iterator();
	            while (it.hasNext()) {
	                Directory exif = it.next();
	                Iterator<Tag> tags = exif.getTags().iterator();
	                while (tags.hasNext()) {
	                    Tag tag = (Tag) tags.next();
	                    System.out.println(tag);

	                }

	            }
	        } catch (JpegProcessingException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	 }
}
