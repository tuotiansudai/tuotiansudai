package com.tuotiansudai.client;

import com.sun.mail.smtp.SMTPTransport;
import com.tuotiansudai.dto.SendCloudType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class SendCloudClient {
    static Logger logger = Logger.getLogger(SendCloudClient.class);

    @Value("${common.sendcloud.host}")
    private String sendCloudSmtpHost;

    @Value("${common.sendcloud.port}")
    private Integer sendCloudSmtpPort;

    @Value("${common.sendcloud.username}")
    private String sendCloudUserName;

    @Value("${common.sendcloud.password}")
    private String sendCloudPassword;

    @Value("${common.sendcloud.from}")
    private String sendCloudFrom;

    private Session getMailSession() {
        // 根据属性新建一个邮件会话
        return Session.getInstance(getProperties(), new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sendCloudUserName, sendCloudPassword);
            }
        });

    }

    private Properties getProperties() {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", sendCloudSmtpHost);
        props.put("mail.smtp.port", sendCloudSmtpPort);
        props.setProperty("mail.smtp.auth", "true");
        props.put("mail.smtp.connectiontimeout", 180);
        props.put("mail.smtp.timeout", 600);
        props.setProperty("mail.mime.encodefilename", "true");

        return props;
    }

    public void sendMailBySendCloud(String toAddress, String title, String content, SendCloudType type,int remainCount) throws UnsupportedEncodingException {


        try {
            if(remainCount <= 0){
                return;
            }
            Session mailSession = getMailSession();
            SMTPTransport transport = null;

            transport = (SMTPTransport) mailSession.getTransport("smtp");
            MimeMessage message = new MimeMessage(mailSession);
            // 发信人
            message.setFrom(new InternetAddress(sendCloudFrom, "拓天速贷", "UTF-8"));
            // 收件人地址
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
            // 邮件主题
            message.setSubject(title, "UTF-8");
            Multipart multipart = new MimeMultipart("alternative");
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setHeader("Content-Type", "text/html;charset=UTF-8");
            contentPart.setHeader("Content-Transfer-Encoding", "base64");
            if (SendCloudType.TEXT.equals(type)) {
                contentPart.setText(content);
            } else {
                contentPart.setContent(content, "text/html;charset=UTF-8");
            }
            multipart.addBodyPart(contentPart);
            message.setContent(multipart);
            // 连接sendcloud服务器，发送邮件
            transport.connect();
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
        }catch (MessagingException e){
            this.sendMailBySendCloud(toAddress,title,content,type,--remainCount);
        }

    }

    public void setSendCloudSmtpHost(String sendCloudSmtpHost) {
        this.sendCloudSmtpHost = sendCloudSmtpHost;
    }

    public void setSendCloudSmtpPort(Integer sendCloudSmtpPort) {
        this.sendCloudSmtpPort = sendCloudSmtpPort;
    }
}
