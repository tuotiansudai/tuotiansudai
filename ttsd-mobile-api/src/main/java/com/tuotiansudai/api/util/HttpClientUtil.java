package com.tuotiansudai.api.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class HttpClientUtil {
		
	public final static int SUCCESS = 1 ;
	
	public final static int FAIL = 0 ;


	public static String getResponseBodyAsString(String url ){
		return getResponseBodyAsString(url, null);
	}

	public static String getResponseBodyAsString(String url, String charset) {
		GetMethod get = new GetMethod(url);
		
		HttpClient client = new HttpClient();
		try {
			client.executeMethod(get);
			if(StringUtils.isBlank(charset)){
				charset = get.getResponseCharSet();
			}
			return new String(get.getResponseBody(), charset);

		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null ;
	}
	
	public static int saveHtmlFromRemoteSite(String url ,String filePath){
		File file = new File(filePath);
			
		return saveHtmlFromRemoteSite(url ,file) ;
		
	}
	
	
	public static int saveHtmlFromRemoteSite(String url ,File file){
		if(!file.exists()){
			try {
				File temp = file.getParentFile();
				if(!temp.exists()){
					temp.mkdirs();
				}
				file.createNewFile();
				
			} catch (IOException e) {
				e.printStackTrace();
				return FAIL ;
			}
		}
		
		try {
			final String response = getResponseBodyAsString(url);
			FileUtils.writeByteArrayToFile(file, response.getBytes("utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
			return FAIL ;
		}
		
		return SUCCESS ;
	}
	
	public static String requestParametersToString(ServletRequest request) {
		StringBuffer sb = new StringBuffer();
		Map map = request.getParameterMap();
		for (Object str : map.keySet()) {
			sb.append(str);
			sb.append(":");
			sb.append(request.getParameter(str.toString()));
			sb.append("  ");
		}
		return sb.toString();
	}
}
