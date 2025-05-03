package com.jhaps.clientrecords.util;

import java.io.File;

public enum ImageUploadPath {
	
	PATH("D:"+ File.separator +"imageUploads" + File.separator);

	private final String path;
	
	ImageUploadPath(String path) {
		this.path = path;
	}
	
	public String getPath() {
		return this.path;
	}
	
	
}
