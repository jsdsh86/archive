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
    	 byte[] buf = new byte[4096]; //这个byte[]的长度可以是任意的。
         MessageDigest md;
         boolean fileIsNull = true;

         try {
             FileInputStream fis = new FileInputStream(file);
             int len = 0;
             md = MessageDigest.getInstance("MD5");

             len = fis.read(buf);
             if (len > 0) {
                 fileIsNull = false;
                 while (len > 0){
                     md.update(buf, 0, len);
                     len = fis.read(buf);
                 }
             }
         } catch (Exception e) {
             return null;
         }

         if (fileIsNull)
             return null;
         else
             return new String(bufferToHex(md.digest()));
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
    public static void main(String[] args) {
    	File file = new File("C:\\Users\\cjqjsd\\Desktop\\2017-01-17_my_vocation.jpg");
		try {
			String md5 = new MD5Utils().getMD5(file);
			System.out.println(md5);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
