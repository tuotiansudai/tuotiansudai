package com.tuotiansudai.util;

import com.tuotiansudai.client.SendCloudClient;
import com.tuotiansudai.dto.SendCloudType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
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

    public boolean sendActiveEmail(String toAddress, Map<String, String> map) {
        try {
            String content = SendCloudTemplate.ACTIVE_EMAIL.generateContent(map);
            sendCloudClient.sendMailBySendCloud(toAddress, SendCloudTemplate.ACTIVE_EMAIL.getTitle(), content, SendCloudType.CONTENT);
            return true;
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    public boolean sendUserBalanceCheckingResult(String toAddress, Map<String, Object> map) {

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("startTime", (String) map.get("startTime"));
        headerMap.put("endTime", (String) map.get("endTime"));

        String contentHeader = SendCloudTemplate.USER_BALANCE_CHECK_RESULT_HEADER.generateContent(headerMap);

        StringBuffer bodySb = new StringBuffer();
        List<String> mismatchUserList = (List<String>) map.get("userList");
        for (String userInfo : mismatchUserList) {
            String[] userInfoArr = userInfo.split("-");
            Map<String, String> bodyLineMap = new HashMap<>();
            bodyLineMap.put("loginName", userInfoArr[0]);
            bodyLineMap.put("ttsdBalance", AmountConverter.convertCentToString(Long.parseLong(userInfoArr[1])));
            bodyLineMap.put("umpayBalance", AmountConverter.convertCentToString(Long.parseLong(userInfoArr[2])));
            bodySb.append(SendCloudTemplate.USER_BALANCE_CHECK_RESULT_BODY.generateContent(bodyLineMap));
        }

        String contentTail = SendCloudTemplate.USER_BALANCE_CHECK_RESULT_TAIL.getTemplate();

        try {
            sendCloudClient.sendMailBySendCloud(toAddress, SendCloudTemplate.USER_BALANCE_CHECK_RESULT_BODY.getTitle(), contentHeader + bodySb.toString() + contentTail, SendCloudType.CONTENT);
            return true;
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return false;
    }
}
