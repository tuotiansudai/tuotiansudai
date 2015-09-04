package com.tuotiansudai.service.impl;
import com.tuotiansudai.client.SendCloudClient;
import com.tuotiansudai.service.SendCloudMailService;
import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Service("sendCloudMailService")
public class SendCloudMailServiceImpl implements SendCloudMailService {
    static Logger logger = Logger.getLogger(UserServiceImpl.class);
    @Resource
    SendCloudClient sendCloudClient;


    @Override
    public boolean sendMail(String toAddress, String title, String content) {

        try {
            sendCloudClient.sendMailBySendCloud(toAddress,title,content,"content");
            return true;
        } catch (MessagingException e) {
            logger.error(e.getLocalizedMessage(),e);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(),e);
        }
        return false;
    }
}
