package com.tuotiansudai.smswrapper;

import java.util.List;

public enum SmsTemplate {
    SMS_REGISTER_CAPTCHA_TEMPLATE(7430, "尊敬的拓天速贷客户，您的注册验证码是：%s 。请勿泄露给他人！"),

    SMS_NO_PASSWORD_INVEST_CAPTCHA_TEMPLATE(7431, "尊敬的拓天速贷客户，您的验证码是：%s 。请勿泄露给他人！"),

    SMS_INVEST_NOTIFY_TEMPLATE(6483, "尊敬的拓天速贷客户，您在平台投资的项目 “%s” 已成功放款，您的投资金额为%s元。"),

    SMS_MOBILE_CAPTCHA_TEMPLATE(6484, "尊敬的拓天速贷客户，您的找回密码的验证码是：%s 。请勿泄露给他人！"),

    SMS_PASSWORD_CHANGED_NOTIFY_TEMPLATE(6485, "尊敬的用户，您的登录密码已修改。如非本人操作，请速登录拓天速贷官网重置密码，或联系客服处理，电话：400-169-1188。"),

    SMS_LOAN_REPAY_NOTIFY_TEMPLATE(6486, "您今天有 %s 元尚未还款，请及时登录系统还款。"),

    SMS_FATAL_NOTIFY_TEMPLATE(8455, "%s 警报：%s"),

    SMS_COUPON_NOTIFY_TEMPLATE(6487, "尊敬的拓天速贷客户，恭喜您获得%s，有效期%s，请及时投资体验。"),

    SMS_BIRTHDAY_NOTIFY_TEMPLATE(6482, "亲爱的天宝生日快乐！生日月投资收益翻倍，最高26%，不要错过哟！");

    private long templateId;

    private String content;

    SmsTemplate(long templateId, String content) {
        this.templateId = templateId;
        this.content = content;
    }

    public long getTemplateId() {
        return templateId;
    }

    public String generateContent(List<String> templateParameters) {
        if (templateParameters == null || templateParameters.size() == 0)
            return this.content;

        String content = this.content;
        for (String param : templateParameters) {
            content = content.replaceFirst("%s", param);
        }
        return content;
    }
}
