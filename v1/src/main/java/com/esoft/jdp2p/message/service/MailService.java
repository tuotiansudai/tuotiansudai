package com.esoft.jdp2p.message.service;

import com.esoft.jdp2p.message.exception.MailSendErrorException;

/**
 * 发送邮件
 * 
 * @author Administrator
 * 
 */
public interface MailService {
	
	/**
	 * 发送邮件
	 * @param toAddress 收件人地址
	 * @param personal 发件人称呼
	 * @param title 邮件标题
	 * @param content 邮件内容
	 * 
	 * @throws MailSendErrorException 邮件发送出错
	 */
	public void sendMail(String toAddress, String personal, String title,
			String content) throws MailSendErrorException;

	/**
	 * 发送邮件
	 * @param toAddress 收件人地址
	 * @param personal 发件人称呼
	 * @param title 邮件标题
	 * @param content 邮件内容
	 *
	 * @throws MailSendErrorException 邮件发送出错
	 */
	public void sendMailException(String toAddress, String personal, String title,
						 String content) throws MailSendErrorException;

	/**
	 * 发送邮件
	 * @param toAddress 收件人地址
	 * @param title 邮件标题
	 * @param content 邮件内容
	 * 
	 * @throws MailSendErrorException 邮件发送出错
	 */
	public void sendMail(String toAddress, String title,
			String content) throws MailSendErrorException;
}
