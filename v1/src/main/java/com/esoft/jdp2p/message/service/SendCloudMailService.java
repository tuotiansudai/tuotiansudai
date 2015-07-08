package com.esoft.jdp2p.message.service;

import com.esoft.jdp2p.message.exception.MailSendErrorException;

public interface SendCloudMailService {


	public void sendMailException(String toAddress, String personal, String title,
								  String content) throws MailSendErrorException;

	public void sendMail(String toAddress, String title,
						 String content) throws MailSendErrorException;
}
