package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.client.SendCloudClient;
import com.tuotiansudai.dto.SendCloudType;
import com.tuotiansudai.paywrapper.service.SendCloudMailService;
import com.tuotiansudai.utils.SendCloudTemplate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@Service
public class SendCloudMailServiceImpl implements SendCloudMailService {
    static Logger logger = Logger.getLogger(SendCloudMailServiceImpl.class);
    @Autowired
    SendCloudClient sendCloudClient;


    @Override
    public boolean sendMailByLoanOut(String toAddress, Map<String, String> map) {

        try {

            String content = SendCloudTemplate.LOAN_OUT_SUCCESSFUL_EMAIL.generateContent(map);
            sendCloudClient.sendMailBySendCloud(toAddress, SendCloudTemplate.LOAN_OUT_SUCCESSFUL_EMAIL.getTitle(), content, SendCloudType.CONTENT);
            return true;
        } catch (MessagingException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return false;
    }
}
