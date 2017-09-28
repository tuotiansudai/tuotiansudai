package com.tuotiansudai.smswrapper.provider;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import com.tuotiansudai.smswrapper.SmsTemplateCell;
import com.tuotiansudai.smswrapper.repository.model.SmsHistoryModel;
import org.apache.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SmsProviderAlidayuVoice extends SmsProviderAlidayuBase {

    private static Logger logger = Logger.getLogger(SmsProviderAlidayuVoice.class);

    @Override
    public List<SmsHistoryModel> sendSMS(List<String> mobileList, SmsTemplateCell smsTemplate, List<String> paramList) {
        List<SmsHistoryModel> smsHistoryModels = this.createSmsHistory(mobileList, smsTemplate, paramList);

        try {
            String templateId = smsTemplate.getTemplateId();
            TaobaoClient client = new DefaultTaobaoClient(url, appKey, appSecret);
            AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
            req.setExtend(MDC.get("requestId"));
            req.setSmsType("normal");
            req.setSmsFreeSignName(SIGN_NAME);
            req.setRecNum(String.join(",", mobileList));
            req.setSmsTemplateCode(templateId);
            req.setSmsParamString(generateParamString(paramList));
            AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);

            logger.info(String.format("send sms, mobileList: [%s], content: %s, response: %s",
                    String.join(",", mobileList),
                    smsTemplate.generateContent(paramList),
                    rsp.getBody()));

            boolean success = rsp.isSuccess() && rsp.getResult().getSuccess();

            return this.updateSmsHistory(smsHistoryModels, success, rsp.getBody());
        } catch (Exception e) {
            logger.error(String.format("send sms, mobileList: [%s], content: %s",
                    String.join(",", mobileList),
                    smsTemplate.generateContent(paramList)), e);
        }
        return smsHistoryModels;
    }

}
