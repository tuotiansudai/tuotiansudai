package com.tuotiansudai.smswrapper;


import org.springframework.util.CollectionUtils;

import java.util.List;

public enum  JianZhouSmsTemplate {

    SMS_REGISTER_CAPTCHA_TEMPLATE(
            "尊敬的用户，您的注册验证码是：${param0} 。请勿泄露给他人！",
            "尊敬的用户，您的注册验证码是：${param0} 。请勿泄露给他人！"),

    SMS_REGISTER_SUCCESS_TEMPLATE(
            "恭喜您注册成功！1000元新人红包及6888元体验金已到账！登录拓天速贷，新客独享10%约定年化收益！",
            null),

    SMS_REGISTER_SUCCESS_REFERRER_TEMPLATE(
            "尊敬的用户，您推荐的好友${param0}已成功注册，(邀请好友投资)您还能再拿0.5%现金奖励哦！",
            null),

    SMS_NO_PASSWORD_INVEST_CAPTCHA_TEMPLATE(
            "尊敬的用户，您的验证码是：${param0} 。请勿泄露给他人！",
            "尊敬的用户，您的验证码是：${param0} 。请勿泄露给他人！"),

    SMS_MOBILE_CAPTCHA_TEMPLATE(
            "尊敬的用户，您的找回密码的验证码是：${param0} 。请勿泄露给他人！",
            "尊敬的用户，您的找回密码的验证码是：${param0} 。请勿泄露给他人！"),

    SMS_PASSWORD_CHANGED_NOTIFY_TEMPLATE(
           "尊敬的用户，您的登录密码已修改。如非本人操作，请速登录拓天速贷官网重置密码，或联系客服处理，电话：400-169-1188。",
            null),

    SMS_LOAN_REPAY_NOTIFY_TEMPLATE(
            "尊敬的用户，您今天有 ${param0} 元尚未还款，请及时登录系统还款。",
            null),

    SMS_EXPERIENCE_REPAY_NOTIFY_TEMPLATE(
           "亲爱的用户，您投资的新手体验项目所得${param0}元投资红包奖励已发放，快来激活奖励吧！",
            null),

    SMS_CANCEL_TRANSFER_LOAN(
            "尊敬的拓天速贷客户，由于原始项目提前还款，您的债权转让项目${param0}已自动取消，请尽快登录平台进行查看。",
            null),

    SMS_IMPORT_RECEIVE_MEMBERSHIP(
            "尊敬的用户，拓天给您赠送了V${param0}会员，赶紧投资享受会员特权吧！",
            null),

    SMS_NEW_USER_RECEIVE_MEMBERSHIP(
            "尊敬的用户，欢迎您加入拓天，拓天给您赠送了V${param0}会员，赶紧投资享受会员特权吧！",
            null),

    SMS_GENERATE_CONTRACT_ERROR_NOTIFY_TEMPLATE(
            "安心签合同生成失败，业务 id : ${param0}",
            null),

    SMS_LOAN_RAISING_COMPLETE_NOTIFY_TEMPLATE(
            "${param0}上线的${param1}天总额为${param2}万的项目于今日${param3}已满，借款人:${param4};代理人:${param5}，30分钟内将完成复核。",
            null),

    SMS_COUPON_ASSIGN_SUCCESS_TEMPLATE(
            "尊敬的用户，恭喜您获得了一张${param0}，请尽快使用拿奖励哦！",
            null),

    SMS_COUPON_EXPIRED_NOTIFY_TEMPLATE(
            "尊敬的用户，您的${param0}即将过期（有效期至：${param1}），请尽快使用。",
            null),

    SMS_FATAL_NOTIFY_TEMPLATE(
            "异常报告，环境：${param0}，错误码：${param1}。请尽快处理。",
            null),

    SMS_PLATFORM_BALANCE_LOW_NOTIFY_TEMPLATE(
            "系统账户余额不足 ${param0} 元，请立刻充值！",
            null),

    SMS_CREDIT_LOAN_BALANCE_ALERT_TEMPLATE(
            "慧租账户余额不足，请及时登录后台充值。",
            null)

    ;

    private String templateText;

    private String templateVoice;

    JianZhouSmsTemplate() {
    }

    JianZhouSmsTemplate(String templateText, String templateVoice) {
        this.templateText = templateText;
        this.templateVoice = templateVoice;
    }

    public String getTemplateText() {
        return templateText;
    }

    public String getTemplateVoice() {
        return templateVoice;
    }

    public String generateContent(boolean isVoice, List<String> templateParameters) {
        String template = isVoice ? getTemplateVoice() : getTemplateText();

        if (CollectionUtils.isEmpty(templateParameters)) {
            return template;
        }

        String content = template;
        for (String param : templateParameters) {
            content = content.replaceFirst("\\$\\{param\\d+\\}", param);
        }
        return content;
    }
}