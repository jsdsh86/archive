package com.jsd.filing.utils;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

public class PicUtils {
	private void readPic(File jpegFile) throws JpegProcessingException, IOException {
		Metadata metadata;

		metadata = JpegMetadataReader.readMetadata(jpegFile);
		Iterator<Directory> it = metadata.getDirectories().iterator();
		while (it.hasNext()) {
			Directory exif = it.next();
			Iterator<Tag> tags = exif.getTags().iterator();
			while (tags.hasNext()) {
				Tag tag = (Tag) tags.next();

			}

		}

	}
}
