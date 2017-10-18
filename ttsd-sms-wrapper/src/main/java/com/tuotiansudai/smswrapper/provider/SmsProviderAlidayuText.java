package com.tuotiansudai.smswrapper.provider;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import com.tuotiansudai.smswrapper.SmsTemplate;
import com.tuotiansudai.smswrapper.SmsTemplateCell;
import com.tuotiansudai.smswrapper.repository.model.SmsHistoryModel;
import org.apache.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SmsProviderAlidayuText extends SmsProviderAlidayuBase {

    private static Logger logger = Logger.getLogger(SmsProviderAlidayuText.class);

    @Override
    public List<SmsHistoryModel> sendSMS(List<String> mobileList, SmsTemplate smsTemplate, List<String> paramList) {
        SmsTemplateCell templateCell = smsTemplate.getTemplateCellText();
        List<SmsHistoryModel> smsHistoryModels = this.createSmsHistory(mobileList, templateCell, paramList, false);

        try {
            String templateId = templateCell.getTemplateId();
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
                    templateCell.generateContent(paramList),
                    rsp.getBody()));

            boolean success = rsp.isSuccess() && rsp.getResult().getSuccess();

            return this.updateSmsHistory(smsHistoryModels, success, rsp.getBody());
        } catch (Exception e) {
            logger.error(String.format("send sms, mobileList: [%s], channel: %s",
                    String.join(",", mobileList),
                    templateCell.generateContent(paramList)), e);
        }
        return smsHistoryModels;
    }

}
