package com.tuotiansudai.dto.sms;


import org.springframework.util.CollectionUtils;

import java.util.List;

public enum JianZhouSmsTemplate {

    SMS_REGISTER_CAPTCHA_TEMPLATE(
            "尊敬的用户，您的注册验证码是：${param0} 。请勿泄露给他人！",
            "尊敬的用户，您的注册验证码是：${param0} 。请勿泄露给他人！"),

    SMS_REGISTER_SUCCESS_TEMPLATE(
            "恭喜您注册成功！1000元新人红包及6888元体验金已到账！登录拓天速贷，新客独享10%约定年化收益！",
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
            "尊敬的用户，您投资的新手体验项目所得${param0}元奖励已发放至账户，登录拓天速贷激活奖励吧！",
            null),

    SMS_CANCEL_TRANSFER_LOAN_TEMPLATE(
            "尊敬的拓天速贷客户，由于原始项目提前还款，您的债权转让项目${param0}已自动取消，请尽快登录平台进行查看。",
            null),

    SMS_TRANSFER_LOAN_SUCCESS_TEMPLATE(
            "尊敬的用户，您发起的转让项目${param0}已经转让成功，资金已经到达您的账户，感谢您选择拓天速贷！",
            null),

    SMS_TRANSFER_LOAN_OVERDUE_TEMPLATE(
            "尊敬的用户，我们遗憾地通知您，您发起的转让项目没有转让成功，请尽快登录平台进行查看。如有疑问，请致电客服热线400-169-1188。",
            null),

    SMS_MEMBERSHIP_UPGRADE_TEMPLATE(
            "尊敬的用户，您的会员等级已升至V${param0}，投资、积分商城积分兑换等可享受V${param1}会员特权，登录拓天速贷查看吧。",
            null),

    SMS_MEMBERSHIP_PRIVILEGE_BUY_SUCCESS_TEMPLATE(
            "尊敬的用户，您购买的增值特权已开始生效，可享受服务费7折优惠。",
            null),

    SMS_MEMBERSHIP_PRIVILEGE_EXPIRED_TEMPLATE(
            "尊敬的用户，您购买的增值特权已过期，增值特权可享受服务费7折优惠，如有需要，请及时续费。",
            null),

    SMS_ADVANCED_REPAY_TEMPLATE(
            "尊敬的用户，您投资的${param0}因借款人放弃借款而提前终止，您的收益与本金已返还至您的账户，请悉知！",
            null),

    SMS_GENERATE_CONTRACT_ERROR_NOTIFY_TEMPLATE(
            "安心签合同生成失败，业务 id : ${param0}",
            null),

    SMS_LOAN_RAISING_COMPLETE_NOTIFY_TEMPLATE(
            "${param0}上线的${param1}天总额为${param2}万的项目于今日${param3}已满，借款人:${param4};代理人:${param5}，30分钟内将完成复核。",
            null),

    SMS_LOAN_OUT_COMPLETE_NOTIFY_TEMPLATE(
            "尊敬的用户，您投资的${param0}项目已经满额放款，约定年化收益${param1}。",
            null),

    SMS_COUPON_ASSIGN_SUCCESS_TEMPLATE(
            "尊敬的用户，恭喜您获得了一张${param0}，${param1}天有效期，请尽快使用！",
            null),

    SMS_COUPON_EXPIRED_NOTIFY_TEMPLATE(
            "尊敬的用户，您账户内有${param0}张优惠券，将在2天后过期，请尽快使用！",
            null),

    SMS_USE_POINT_NOTIFY_TEMPLATE(
            "尊敬的用户，您在积分商城消费${param0}积分，剩余积分${param1}，如有疑问，请致电客服热线400-169-1188，感谢您选择拓天速贷。",
            null),

    SMS_FATAL_NOTIFY_TEMPLATE(
            "异常报告，环境：${param0}，错误码：${param1}。请尽快处理。",
            null),

    SMS_PLATFORM_BALANCE_LOW_NOTIFY_TEMPLATE(
            "系统账户余额不足 ${param0} 元，请立刻充值！",
            null),

    SMS_CREDIT_LOAN_BALANCE_ALERT_TEMPLATE(
            "慧租账户余额不足，请及时登录后台充值。",
            null),

    SMS_PAYROLL_TEMPLATE(
            "${param0}已发放到您的账户余额中，请注意查收。",
            null),;

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
