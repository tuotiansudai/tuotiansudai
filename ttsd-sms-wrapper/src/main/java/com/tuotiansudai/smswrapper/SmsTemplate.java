package com.tuotiansudai.smswrapper;

import java.util.Map;

public enum SmsTemplate {
    SMS_REGISTER_CAPTCHA_TEMPLATE("尊敬的拓天速贷客户，您的注册验证码是：{captcha} 。请勿泄露给他人!【拓天速贷】"),
    SMS_INVEST_NOTIFY_TEMPLATE("尊敬的拓天速贷客户，您在平台的项目借款 “{loanName}” 已成功放款，您的投资金额为{amount}元。【拓天速贷】"),
    SMS_MOBILE_CAPTCHA_TEMPLATE("尊敬的拓天速贷客户，您的找回密码的验证码是：{captcha} 。请勿泄露给他人!【拓天速贷】"),
    SMS_PASSWORD_CHANGED_NOTIFY_TEMPLATE("尊敬的用户，您的登录密码已修改。如非本人操作，请速登录拓天速贷官网重置密码，或联系客服处理，电话：400-169-1188。【拓天速贷】"),
    SMS_INVEST_FATAL_NOTIFY_TEMPLATE("警报：投资业务错误。详细信息：{errMsg}【拓天速贷】"),
    SMS_JOB_FATAL_NOTIFY_TEMPLATE("警报：JOB执行错误。详细信息：{errMsg}【拓天速贷】"),
    SMS_COUPON_NOTIFY_TEMPLATE("尊敬的拓天速贷客户，恭喜您获得{amount}元{couponType}，有效期截至{expiredDate}，请及时投资体验。【拓天速贷】");

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
