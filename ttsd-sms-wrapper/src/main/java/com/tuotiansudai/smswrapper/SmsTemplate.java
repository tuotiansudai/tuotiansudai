package com.tuotiansudai.smswrapper;

import java.util.Map;

public enum SmsTemplate {
    SMS_REGISTER_CAPTCHA_TEMPLATE("尊敬的用户，您的注册验证码是：{captcha} 。请勿泄露给他人!【拓天速贷】"),
    SMS_INVEST_NOTIFY_TEMPLATE("尊敬的拓天速贷客户，您在平台的项目借款 “{loanName}” 已成功放款，您的投资金额为{amount}元。【拓天速贷】"),
    SMS_MOBILE_CAPTCHA_TEMPLATE("尊敬的用户，您的找回密码的验证码是：{captcha} 。请勿泄露给他人!【拓天速贷】");

    private String template;

    SmsTemplate(String template) {
        this.template = template;
    }

    public String generateContent(Map<String, String> templateParameters) {
        String content = this.template;
        for (Map.Entry<String, String> entry : templateParameters.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            content = content.replace("{" + key + "}", value);
        }
        return content;
    }
}
