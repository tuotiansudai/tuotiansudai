package com.tuotiansudai.smswrapper.provider;

import com.tuotiansudai.smswrapper.SmsTemplate;
import com.tuotiansudai.smswrapper.exception.SmsSendingException;

import java.util.List;

public interface SmsProvider {
    void sendSMS(List<String> mobileList, SmsTemplate smsTemplate, List<String> paramList) throws SmsSendingException;
}
