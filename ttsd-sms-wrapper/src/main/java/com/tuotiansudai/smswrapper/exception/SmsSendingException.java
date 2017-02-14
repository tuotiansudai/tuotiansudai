package com.tuotiansudai.smswrapper.exception;

import com.tuotiansudai.smswrapper.SmsTemplate;

import java.util.List;

public class SmsSendingException extends Exception {
    private final List<String> mobileList;
    private final SmsTemplate smsTemplate;
    private final List<String> paramList;

    public SmsSendingException(List<String> mobileList, SmsTemplate smsTemplate, List<String> paramList, String message) {
        this(mobileList, smsTemplate, paramList, message, null);
    }

    public SmsSendingException(List<String> mobileList, SmsTemplate smsTemplate, List<String> paramList, String message, Throwable cause) {
        super(message, cause);
        this.mobileList = mobileList;
        this.smsTemplate = smsTemplate;
        this.paramList = paramList;
    }

    public List<String> getMobileList() {
        return mobileList;
    }

    public SmsTemplate getSmsTemplate() {
        return smsTemplate;
    }

    public List<String> getParamList() {
        return paramList;
    }
}
