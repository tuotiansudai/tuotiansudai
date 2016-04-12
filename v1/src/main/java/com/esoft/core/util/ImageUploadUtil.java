package com.esoft.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.esoft.core.jsf.util.FacesUtil;
import com.sun.org.apache.bcel.internal.generic.I2F;
import com.ttsd.aliyun.AliyunUtils;
import com.ttsd.aliyun.PropertiesUtils;
import org.apache.commons.io.FileUtils;

public class ImageUploadUtil {

	private final static String UPLOAD_PATH = "/upload";
	private static SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
	
	
	public static String upload(InputStream is,String fileName){
		final String path = UPLOAD_PATH + "/" + formater.format(new Date());
		try {


				final String absPath = FacesUtil.getRealPath(path) ;
				fileName = getName(fileName);
				final String savefile = absPath +"/"+ fileName;
				mkdir(absPath);
				FileOutputStream out = new FileOutputStream(savefile);
				byte[] buffer = new byte[2048];
				int x = 0;
				while ((x = is.read(buffer)) != -1) {
					out.write(buffer, 0, x);
				}
				is.close();
				out.close();
				return path+"/"+fileName;


		} catch (Exception e) {
			e.printStackTrace();
		}
		return null ;
	}



	
	/**
	 * 根据字符串创建本地目录 并按照日期建立子目录返回
	 * @param path 
	 * @return 
	 */
	private static void mkdir(final String path) {
		
		File dir = new File( path);
		if (!dir.exists()) {
			try {
				dir.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 依据原始文件名生成新文件名
	 * @return
	 */
	private static String getName(String fileName) {
		Random random = new Random();
		return "" + random.nextInt(10000)
				+ System.currentTimeMillis() + getFileExt(fileName);
	}
	
	/**
	 * 获取文件扩展名
	 * 
	 * @return string
	 */
	public static String getFileExt(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}
}
