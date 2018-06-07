package com.tuotiansudai.smswrapper.provider;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcTtsNumSinglecallRequest;
import com.taobao.api.response.AlibabaAliqinFcTtsNumSinglecallResponse;
import com.tuotiansudai.smswrapper.SmsTemplate;
import com.tuotiansudai.smswrapper.SmsTemplateCell;
import com.tuotiansudai.smswrapper.repository.model.SmsHistoryModel;
import org.apache.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SmsProviderAlidayuVoice extends SmsProviderAlidayuBase {

    private static Logger logger = Logger.getLogger(SmsProviderAlidayuVoice.class);

    @Override
    public List<SmsHistoryModel> sendSMS(List<String> mobileList, SmsTemplate smsTemplate, List<String> paramList) {
        SmsTemplateCell templateCell = smsTemplate.getTemplateCellVoice();

        List<SmsHistoryModel> smsHistoryModels = new ArrayList<>();
        for (String mobile : mobileList) {
            smsHistoryModels.add(sendSMS(mobile, templateCell, paramList));
        }
        return smsHistoryModels;
    }

    private SmsHistoryModel sendSMS(String mobile, SmsTemplateCell smsTemplate, List<String> paramList) {
        SmsHistoryModel smsHistoryModel = this.createSmsHistory(mobile, smsTemplate, paramList, true);

        try {
            String templateId = smsTemplate.getTemplateId();
            TaobaoClient client = new DefaultTaobaoClient(url, appKey, appSecret);
            AlibabaAliqinFcTtsNumSinglecallRequest req = new AlibabaAliqinFcTtsNumSinglecallRequest();
            req.setExtend(MDC.get("requestId"));
            req.setCalledNum(mobile);
            req.setCalledShowNum("02566040680");
            req.setTtsCode(templateId);
            req.setTtsParamString(generateParamString(paramList));
            AlibabaAliqinFcTtsNumSinglecallResponse rsp = client.execute(req);

            logger.info(String.format("send sms, mobile: [%s], content: %s, response: %s",
                    mobile,
                    smsTemplate.generateContent(paramList),
                    rsp.getBody()));

            boolean success = rsp.isSuccess() && rsp.getResult().getSuccess();

            return this.updateSmsHistory(smsHistoryModel, success, rsp.getBody());
        } catch (Exception e) {
            logger.error(String.format("send sms, mobile: [%s], channel: %s",
                    mobile,
                    smsTemplate.generateContent(paramList)), e);
        }
        return smsHistoryModel;
    }

}
