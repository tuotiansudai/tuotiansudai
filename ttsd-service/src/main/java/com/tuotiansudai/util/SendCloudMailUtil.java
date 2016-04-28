package com.tuotiansudai.util;

import com.tuotiansudai.client.SendCloudClient;
import com.tuotiansudai.dto.SendCloudType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Component
public class SendCloudMailUtil {

    private static Logger logger = Logger.getLogger(SendCloudMailUtil.class);

    @Autowired
    private SendCloudClient sendCloudClient;


    public boolean sendMailByLoanOut(String toAddress, Map<String, String> map) {
        try {
            String content = SendCloudTemplate.LOAN_OUT_SUCCESSFUL_EMAIL.generateContent(map);
            sendCloudClient.sendMailBySendCloud(toAddress, SendCloudTemplate.LOAN_OUT_SUCCESSFUL_EMAIL.getTitle(), content, SendCloudType.CONTENT);
            return true;
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    public boolean sendMailByRepayCompleted(String toAddress, Map<String, String> map) {
        try {
            String content = SendCloudTemplate.REPAY_COMPLETE_EMAIL.generateContent(map);
            sendCloudClient.sendMailBySendCloud(toAddress, SendCloudTemplate.REPAY_COMPLETE_EMAIL.getTitle(), content, SendCloudType.CONTENT);
            return true;
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    public boolean sendActiveEmail(String toAddress, Map<String, String> map){
        try {
            String content = SendCloudTemplate.ACTIVE_EMAIL.generateContent(map);
            sendCloudClient.sendMailBySendCloud(toAddress, SendCloudTemplate.ACTIVE_EMAIL.getTitle(), content, SendCloudType.CONTENT);
            return true;
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return false;
    }
}
