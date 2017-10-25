package com.tuotiansudai.smswrapper.provider;

import com.google.common.collect.Lists;
import com.tuotiansudai.smswrapper.SmsTemplateCell;
import com.tuotiansudai.smswrapper.repository.mapper.SmsHistoryMapper;
import com.tuotiansudai.smswrapper.repository.model.SmsHistoryModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

abstract class SmsProviderBase implements SmsProvider {

    @Autowired
    private SmsHistoryMapper smsHistoryMapper;

    List<SmsHistoryModel> createSmsHistory(List<String> mobileList, SmsTemplateCell template, List<String> paramList, boolean isVoice) {
        String content = template.generateContent(paramList);
        List<SmsHistoryModel> models = Lists.newArrayList();
        for (String mobile : mobileList) {
            SmsHistoryModel model = new SmsHistoryModel(mobile, content, isVoice);
            smsHistoryMapper.create(model);
            models.add(model);
        }
        return models;
    }

    SmsHistoryModel createSmsHistory(String mobile, SmsTemplateCell template, List<String> paramList, boolean isVoice) {
        String content = template.generateContent(paramList);
        SmsHistoryModel model = new SmsHistoryModel(mobile, content, isVoice);
        smsHistoryMapper.create(model);
        return model;
    }

    SmsHistoryModel updateSmsHistory(SmsHistoryModel model, boolean isSuccess, String response) {
        model.setSuccess(isSuccess);
        model.setResponse(response);
        smsHistoryMapper.update(model);
        return model;
    }

    List<SmsHistoryModel> updateSmsHistory(List<SmsHistoryModel> models, boolean isSuccess, String response) {
        for (SmsHistoryModel model : models) {
            updateSmsHistory(model, isSuccess, response);
        }
        return models;
    }
}
