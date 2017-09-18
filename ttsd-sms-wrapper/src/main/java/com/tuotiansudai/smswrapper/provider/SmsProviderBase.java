package com.tuotiansudai.smswrapper.provider;

import com.google.common.collect.Lists;
import com.tuotiansudai.smswrapper.SmsChannel;
import com.tuotiansudai.smswrapper.SmsTemplate;
import com.tuotiansudai.smswrapper.repository.mapper.BaseMapper;
import com.tuotiansudai.smswrapper.repository.mapper.SmsHistoryMapper;
import com.tuotiansudai.smswrapper.repository.model.SmsHistoryModel;
import com.tuotiansudai.smswrapper.repository.model.SmsModel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.beans.Introspector;
import java.util.List;

public abstract class SmsProviderBase implements SmsProvider {

    @Autowired
    protected SmsHistoryMapper smsHistoryMapper;

    protected List<SmsHistoryModel> createSmsHistory(List<String> mobileList, SmsTemplate template, List<String> paramList, SmsChannel successChannel) {
        String content = template.generateContent(paramList, successChannel);
        List<SmsHistoryModel> models = Lists.newArrayList();
        for (String mobile : mobileList) {
            SmsHistoryModel model = new SmsHistoryModel(mobile, content, successChannel);
            smsHistoryMapper.create(model);
            models.add(model);
        }
        return models;
    }

    protected List<SmsHistoryModel> updateSmsHistory(List<SmsHistoryModel> models, boolean isSuccess, String response) {
        for (SmsHistoryModel model : models) {
            model.setSuccess(isSuccess);
            model.setResponse(response);
            smsHistoryMapper.update(model);
        }
        return models;
    }
}
