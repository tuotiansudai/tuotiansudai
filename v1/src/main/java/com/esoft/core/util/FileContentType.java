package com.esoft.core.util;

public class FileContentType {
	public static String getContentType(String filePath) {
		String contentType = "";
		String postfix = "";
		postfix = filePath.substring(filePath.lastIndexOf("."),
				filePath.length());
		postfix = postfix.toUpperCase();
		if (postfix.equalsIgnoreCase(".XLS")
				|| postfix.equalsIgnoreCase(".XLT")
				|| postfix.equalsIgnoreCase(".XLW")
				|| postfix.equalsIgnoreCase(".CSV")) {
			contentType = "application/vnd.ms-excel";
		} else if (postfix.equalsIgnoreCase(".DOC")) {
			contentType = "application/msword";
		} else if (postfix.equalsIgnoreCase(".RTF")) {
			contentType = "application/rtf";
		} else if (postfix.equalsIgnoreCase(".TXT")) {
			contentType = "text/plain";
		} else if (postfix.equalsIgnoreCase(".XML")) {
			contentType = "text/xml";
		} else if (postfix.equalsIgnoreCase(".BMP")) {
			contentType = "image/bmp";
		} else if (postfix.equalsIgnoreCase(".JPG")
				|| postfix.equalsIgnoreCase(".JPEG")) {
			contentType = "image/jpeg";
		} else if (postfix.equalsIgnoreCase(".GIF")) {
			contentType = "image/gif";
		} else if (postfix.equalsIgnoreCase(".AVI")) {
			contentType = "video/x-msvideo";
		} else if (postfix.equalsIgnoreCase(".MP3")) {
			contentType = "audio/mpeg";
		} else if (postfix.equalsIgnoreCase(".MPA")
				|| postfix.equalsIgnoreCase(".MPE")
				|| postfix.equalsIgnoreCase(".MPEG")
				|| postfix.equalsIgnoreCase(".MPG")) {
			contentType = "video/mpeg";
		} else if (postfix.equalsIgnoreCase(".PPT")
				|| postfix.equalsIgnoreCase(".PPS")) {
			contentType = "application/vnd.ms-powerpoint";
		} else if (postfix.equalsIgnoreCase(".PDF")) {
			contentType = "application/pdf";
		} else if (postfix.equalsIgnoreCase(".ZIP")
				|| postfix.equalsIgnoreCase(".RAR")) {
			contentType = "application/zip";
		}
		return contentType;
	}
}
