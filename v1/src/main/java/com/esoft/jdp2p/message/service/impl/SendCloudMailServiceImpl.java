package com.esoft.jdp2p.message.service.impl;

import com.esoft.core.annotations.Logger;
import com.esoft.jdp2p.message.exception.MailSendErrorException;
import com.esoft.jdp2p.message.service.SendCloudMailService;
import com.ttsd.aliyun.PropertiesUtils;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.*;

import com.sun.mail.smtp.SMTPTransport;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service("sendCloudMailService")
public class SendCloudMailServiceImpl implements SendCloudMailService {

    @Resource
    SendCloudClient sendCloudClient;
    @Logger
    static Log log;

    @Override
    public boolean sendMailException(String toAddress, String title, String content) {
        try {
            sendCloudClient.sendMailBySendCloud(toAddress,title,content,"text");
            return true;
        } catch (MessagingException e) {
            log.error(e.getStackTrace());
        } catch (UnsupportedEncodingException e) {
            log.error(e.getStackTrace());
        }
        return false;
    }

    @Override
    public boolean sendMail(String toAddress, String title, String content) {

        try {
            sendCloudClient.sendMailBySendCloud(toAddress,title,content,"content");
            return true;
        } catch (MessagingException e) {
            log.error(e.getStackTrace());
        } catch (UnsupportedEncodingException e) {
            log.error(e.getStackTrace());
        }
        return false;
    }
}
