package com.jsd.filing.utils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Locale;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.jsd.filing.beans.FileBean;

public class PicUtils {
	private void readPic(File jpegFile) throws JpegProcessingException, IOException {
		Metadata metadata;
		metadata = JpegMetadataReader.readMetadata(jpegFile);
		Iterator<Directory> it = metadata.getDirectories().iterator();
		String srcFilePath = jpegFile.getAbsolutePath();
		//String srcFilePath, String srcFileLastModifyTime, String mD5, String sHA1, String desFileName,boolean isWrited
		boolean flag=true;
		while (it.hasNext()) {
			Directory exif = it.next();
			Iterator<Tag> tags = exif.getTags().iterator();
			if(flag) {
				while (tags.hasNext()) {
					Tag tag = (Tag) tags.next();
					if(tag.getTagName()!=null && tag.getTagName().equals("File Modified Date")) {
						
						String modifyStr = tag.getDescription();
						DateTimeFormatter pattern = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy",Locale.US);
						LocalDateTime dateTime = LocalDateTime.parse(modifyStr,pattern);
					}
				}
				
			}

		}

	}
}
