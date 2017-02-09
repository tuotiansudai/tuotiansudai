package com.tuotiansudai.mq.consumer.email;

import com.sun.mail.smtp.SMTPTransport;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

@Service
public class SendCloudClient {
    private static Logger logger = Logger.getLogger(SendCloudClient.class);

    private Properties properties;

    private Authenticator authenticator;

    private InternetAddress internetAddress;

    @Autowired
    public SendCloudClient(@Value("${common.sendcloud.host}") String sendCloudSMTPHost,
                           @Value("${common.sendcloud.port}") Integer sendCloudSMTPPort,
                           @Value("${common.sendcloud.username}") String sendCloudUserName,
                           @Value("${common.sendcloud.password}") String sendCloudPassword,
                           @Value("${common.sendcloud.from}") String sendCloudFrom) throws UnsupportedEncodingException {
        this.properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.mime.encodefilename", "true");
        properties.setProperty("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", sendCloudSMTPPort);
        properties.put("mail.smtp.host", sendCloudSMTPHost);
        properties.put("mail.smtp.connectiontimeout", 180);
        properties.put("mail.smtp.timeout", 600);

        authenticator = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sendCloudUserName, sendCloudPassword);
            }
        };

        internetAddress = new InternetAddress(sendCloudFrom, "拓天速贷", StandardCharsets.UTF_8.name());
    }

    boolean sendMailBySendCloud(List<String> addresses, String title, String content) {
        // 根据属性新建一个邮件会话
        Session mailSession = Session.getInstance(properties, authenticator);
        MimeMessage message = new MimeMessage(mailSession);
        // 发信人
        try {
            message.setFrom(internetAddress);
            // 收件人地址
            addresses.forEach(address -> {
                try {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(address));
                } catch (MessagingException e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
            });
            // 邮件主题
            message.setSubject(title, StandardCharsets.UTF_8.name());
            Multipart multipart = new MimeMultipart("alternative");
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setHeader("Content-Type", "text/html;charset=UTF-8");
            contentPart.setHeader("Content-Transfer-Encoding", "base64");
            contentPart.setContent(content, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);
            message.setContent(multipart);
        } catch (MessagingException e) {
            logger.error(e.getLocalizedMessage(), e);
            return false;
        }

        SMTPTransport transport;
        try {
            transport = (SMTPTransport) mailSession.getTransport("smtp");
        } catch (NoSuchProviderException e) {
            logger.error(e.getLocalizedMessage(), e);
            return false;
        }

        boolean sendSuccess = false;
        int tryTimes = 0;
        do {
            tryTimes++;
            try {
                // 连接sendcloud服务器，发送邮件
                transport.connect();
                transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
                sendSuccess = true;
            } catch (MessagingException e) {
                logger.error("send cloud is fail by: " + e.getMessage());
            } finally {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
            }
        } while (!sendSuccess && tryTimes < 3);

        return sendSuccess;
    }
}
