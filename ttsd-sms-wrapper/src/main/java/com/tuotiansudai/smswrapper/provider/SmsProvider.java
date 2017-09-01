package com.tuotiansudai.smswrapper.provider;

import com.tuotiansudai.smswrapper.SmsTemplate;
import com.tuotiansudai.smswrapper.exception.SmsSendingException;
import com.tuotiansudai.smswrapper.repository.mapper.BaseMapper;
import com.tuotiansudai.smswrapper.repository.model.SmsHistoryModel;
import org.springframework.context.ApplicationContextAware;

import java.util.List;

public interface SmsProvider {

    List<SmsHistoryModel> sendSMS(List<String> mobileList, SmsTemplate smsTemplate, List<String> paramList);

}
