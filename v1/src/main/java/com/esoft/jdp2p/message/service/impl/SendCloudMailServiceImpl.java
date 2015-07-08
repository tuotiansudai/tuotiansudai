package com.esoft.jdp2p.message.service.impl;

import com.esoft.core.annotations.Logger;
import com.esoft.jdp2p.message.exception.MailSendErrorException;
import com.esoft.jdp2p.message.service.SendCloudMailService;
import com.ttsd.aliyun.PropertiesUtils;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;

import com.sun.mail.smtp.SMTPTransport;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service("sendCloudMailService")
public class SendCloudMailServiceImpl implements SendCloudMailService {
    private static final String SENDCLOUD_SMTP_HOST = getSendCloudConfig("sendCloud.smtp.host");
    private static final int SENDCLOUD_SMTP_PORT = Integer.parseInt(getSendCloudConfig("sendCloud.smtp.port"));
    private static final String apiUser = getSendCloudConfig("sendCloud.smtp.apiUser");
    private static final String apiKey = getSendCloudConfig("sendCloud.smtp.apiKey");
    private static final String SENDCLOUD_SMTP_FORM = getSendCloudConfig("sendCloud.smtp.from");

    @Logger
    Log log;

    private static Properties getProperties() {
        Properties props = System.getProperties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", SENDCLOUD_SMTP_HOST);
        props.put("mail.smtp.port", SENDCLOUD_SMTP_PORT);
        props.setProperty("mail.smtp.auth", "true");
        props.put("mail.smtp.connectiontimeout", 180);
        props.put("mail.smtp.timeout", 600);
        props.setProperty("mail.mime.encodefilename", "true");

        return props;

    }
    public static String getSendCloudConfig(String key){
        return PropertiesUtils.getPro(key,"sendCloud.properties");
    }

    private static Session getMailSession() {
        // 根据属性新建一个邮件会话
        return Session.getInstance(getProperties(), new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(apiUser, apiKey);
            }
        });

    }


    @Override
    public void sendMailException(String toAddress, String personal, String title, String content) throws MailSendErrorException {
        Session mailSession = getMailSession();
        SMTPTransport transport = null;
        try {
            transport = (SMTPTransport) mailSession.getTransport("smtp");
            MimeMessage message = new MimeMessage(mailSession);
            // 发信人
            message.setFrom(new InternetAddress(SENDCLOUD_SMTP_FORM, "拓天速贷", "UTF-8"));
            // 收件人地址
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
            // 邮件主题
            message.setSubject(title, "UTF-8");
            Multipart multipart = new MimeMultipart("alternative");
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setHeader("Content-Type", "text/html;charset=UTF-8");
            contentPart.setHeader("Content-Transfer-Encoding", "base64");
            contentPart.setText(content);
            multipart.addBodyPart(contentPart);
            message.setContent(multipart);
            // 连接sendcloud服务器，发送邮件
            transport.connect();
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
        } catch (NoSuchProviderException e) {
            log.error(e.getStackTrace());
        } catch (AddressException e) {
            log.error(e.getStackTrace());
        } catch (MessagingException e) {
            log.error(e.getStackTrace());
        } catch (UnsupportedEncodingException e) {
            log.error(e.getStackTrace());
        }
    }

    @Override
    public void sendMail(String toAddress, String title, String content) {
        Session mailSession = getMailSession();
        SMTPTransport transport = null;
        try {
            transport = (SMTPTransport) mailSession.getTransport("smtp");
            MimeMessage message = new MimeMessage(mailSession);
            // 发信人
            message.setFrom(new InternetAddress(SENDCLOUD_SMTP_FORM, "拓天速贷", "UTF-8"));
            // 收件人地址
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
            // 邮件主题
            message.setSubject(title, "UTF-8");
            Multipart multipart = new MimeMultipart("alternative");
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setHeader("Content-Type", "text/html;charset=UTF-8");
            contentPart.setHeader("Content-Transfer-Encoding", "base64");
            contentPart.setContent(content, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);
            message.setContent(multipart);
            // 连接sendcloud服务器，发送邮件
            transport.connect();
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
        } catch (NoSuchProviderException e) {
            log.error(e.getStackTrace());
        } catch (AddressException e) {
            log.error(e.getStackTrace());
        } catch (MessagingException e) {
            log.error(e.getStackTrace());
        } catch (UnsupportedEncodingException e) {
            log.error(e.getStackTrace());
        }
    }
}
