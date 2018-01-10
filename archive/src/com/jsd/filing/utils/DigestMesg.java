package com.jsd.filing.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class DigestMesg {
    protected char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',  
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    protected static ArrayList<String> kindArray = new ArrayList<String>();
    static {
    	kindArray.add("MD5");
    	kindArray.add("SHA-1");
    	
    }
    public  String getDigestStr(File file,String kind) throws IOException,NoSuchAlgorithmException { 
    	if(kind==null || kind.isEmpty()) throw new RuntimeException(" MD5,SHA-1 ONLY");
		if(!kindArray.contains(kind.toUpperCase())) throw new RuntimeException(" MD5,SHA-1 ONLY");
    	byte[] buf = new byte[4096]; //这个byte[]的长度可以是任意的。
        MessageDigest md;
        boolean fileIsNull = true;

        try {
            FileInputStream fis = new FileInputStream(file);
            int len = 0;
            md = MessageDigest.getInstance(kind);

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
	private String bufferToHex(byte bytes[]) {  
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
