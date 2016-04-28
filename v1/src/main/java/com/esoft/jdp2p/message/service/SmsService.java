package com.esoft.jdp2p.message.service;

import com.esoft.jdp2p.message.exception.SmsSendErrorException;

import java.util.Map;


/**
 * 发短信
 * 返回信息详见文档。
 * @author Administrator
 * 
 */
public abstract class SmsService {

	/**
	 * 发送短信
	 * @param content
	 * @param mobileNumber
	 * @throws SmsSendErrorException
	 */
	public abstract void send(String content, String mobileNumber) throws SmsSendErrorException;

}
