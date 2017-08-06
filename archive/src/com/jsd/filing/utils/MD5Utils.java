package com.jsd.filing.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MD5Utils {
	private static final Logger logger = LogManager.getLogger(MD5Utils.class);
    protected char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',  
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };  
    public  String getMD5(File file) throws IOException,NoSuchAlgorithmException { 
    	 MessageDigest md5 = MessageDigest.getInstance("MD5");
    	 FileInputStream in = new FileInputStream(file);  
         FileChannel ch = in.getChannel();  
         MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,  
                 file.length());  
         md5.update(byteBuffer); 
         String md5String = bufferToHex(md5.digest());
         logger.info(Thread.currentThread().getName()+" : File | "+file.getAbsolutePath()+" last modified in | "+new Date(file.lastModified())+" 's MD5 is | "+md5String);
         return md5String;  
    }
    private  String bufferToHex(byte bytes[]) {  
        return bufferToHex(bytes, 0, bytes.length);  
    } 
    
    private String bufferToHex(byte bytes[], int m, int n) {  
        StringBuffer stringbuffer = new StringBuffer(2 * n);  
        int k = m + n;  
        for (int l = m; l < k; l++) {  
            appendHexPair(bytes[l], stringbuffer);  
        }  
        return stringbuffer.toString();  
    }  
    private  void appendHexPair(byte bt, StringBuffer stringbuffer) {  
        char c0 = hexDigits[(bt & 0xf0) >> 4];  
        char c1 = hexDigits[bt & 0xf];  
        stringbuffer.append(c0);  
        stringbuffer.append(c1);  
    }  
}
