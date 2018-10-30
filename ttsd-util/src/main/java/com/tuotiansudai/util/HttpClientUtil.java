package com.tuotiansudai.util;

import com.google.common.base.Strings;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;

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
			if(Strings.isNullOrEmpty(charset)){
				charset = get.getResponseCharSet();
			}
			return new String(get.getResponseBody(), charset);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null ;
	}
}
