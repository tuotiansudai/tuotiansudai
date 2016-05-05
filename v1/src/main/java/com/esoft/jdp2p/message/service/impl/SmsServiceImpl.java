package com.esoft.jdp2p.message.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.esoft.core.annotations.Logger;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

import com.esoft.jdp2p.message.exception.SmsSendErrorException;
import com.esoft.jdp2p.message.service.SmsService;

/**
 * 发短信 返回信息详见文档。
 * 
 * @author Administrator
 * 
 */
@Service("smsService")
public class SmsServiceImpl extends SmsService {

	@Logger
	Log log;
	
	private static Properties props = new Properties(); 
	static{
		try {
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("zucp_sms_config.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(String content, String mobileNumber) {
		String sn = props.getProperty("sn");
		String pwd = props.getProperty("password");
		if (sn == null || pwd == null) {
			throw new SmsSendErrorException("短信发送失败，sn或password未定义");
		}
		try {
			content = URLEncoder.encode(content, "utf-8");
			ZucpWebServiceClient client = new ZucpWebServiceClient(sn, pwd);
			String result_mt = client.mdSmsSend_u(mobileNumber, content, "",
					"", "");
			if (result_mt.startsWith("-") || result_mt.equals(""))// 发送短信，如果是以负号开头就是发送失败。
			{
				throw new SmsSendErrorException("短信发送失败，错误代码：" + result_mt);
			}
		} catch (UnsupportedEncodingException e) {
			throw new SmsSendErrorException(null, e);
		}
	}

	//	public static void main(String[] args) {
//		new SmsServiceImpl().send("test【金鼎海汇】", "18600238751");
//	}
}
