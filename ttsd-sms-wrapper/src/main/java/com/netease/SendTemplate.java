package com.netease;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 发送模板短信
 * @author netease
 *
 */
public class SendTemplate {
	public static void main(String[] args) throws Exception {
			DefaultHttpClient httpClient = new DefaultHttpClient();
	        String url = "https://api.netease.im/sms/sendtemplate.action";
	        HttpPost httpPost = new HttpPost(url);

			String appKey = "fdc49198696ed3611e036a133bea931f";
			String appSecret = "b287a0452a5b";
			String nonce =  "12341234";
	        String curTime = String.valueOf((new Date()).getTime() / 1000L);//time
	        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce ,curTime);//参考计算CheckSum的java代码

	        // 设置请求的header
	        httpPost.addHeader("AppKey", appKey);
	        httpPost.addHeader("Nonce", nonce);
	        httpPost.addHeader("CurTime", curTime);
	        httpPost.addHeader("CheckSum", checkSum);
	        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
	        
	        //设置请求的的参数
	        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
	        //								模板id			
			nvps.add(new BasicNameValuePair("templateid", "6482"));
			//								参数			jsonArray形式
			nvps.add(new BasicNameValuePair("mobiles", "['18611445119']"));
			
			nvps.add(new BasicNameValuePair("params", "['0000']"));
	        
	        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
	        
	        // 执行请求
	        HttpResponse response = httpClient.execute(httpPost);
	        
	        // 打印执行结果
	        System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
	       
	    }
}

