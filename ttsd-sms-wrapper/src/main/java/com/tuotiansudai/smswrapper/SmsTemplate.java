package com.tuotiansudai.smswrapper;

import org.springframework.util.CollectionUtils;

import java.util.List;

public enum SmsTemplate {

    /*
    !!! ATTENTION !!!
    模板中的变量占位符在网易里使用的是 %s, 在阿里里使用的是 ${param0} ${param1}。

    请在阿里平台上配置的时候一定要注意，请严格按照变量在文本中出现的顺序进行编号，编号从0开始。
    请在阿里平台上配置的时候一定要注意，请严格按照变量在文本中出现的顺序进行编号，编号从0开始。
    请在阿里平台上配置的时候一定要注意，请严格按照变量在文本中出现的顺序进行编号，编号从0开始。
    !!! ATTENTION !!!
     */

    // 发送通道, 网易模板ID，网易模板，阿里模板ID，阿里模板
    SMS_REGISTER_CAPTCHA_TEMPLATE(SmsChannel.TryBoth,
            "7430", "尊敬的拓天速贷客户，您的注册验证码是：%s 。请勿泄露给他人！",
            "SMS_45345002", "尊敬的拓天速贷客户，您的注册验证码是：${param0} 。请勿泄露给他人！"),

    SMS_NO_PASSWORD_INVEST_CAPTCHA_TEMPLATE(SmsChannel.TryBoth,
            "7431", "尊敬的拓天速贷客户，您的验证码是：%s 。请勿泄露给他人！",
            "SMS_45395002", "尊敬的拓天速贷客户，您的验证码是：${param0} 。请勿泄露给他人！"),

    SMS_MOBILE_CAPTCHA_TEMPLATE(SmsChannel.TryBoth,
            "6484", "尊敬的拓天速贷客户，您的找回密码的验证码是：%s 。请勿泄露给他人！",
            "SMS_45370002", "尊敬的拓天速贷客户，您的找回密码的验证码是：${param0} 。请勿泄露给他人！"),

    SMS_PASSWORD_CHANGED_NOTIFY_TEMPLATE(SmsChannel.TryBoth,
            "6485", "尊敬的用户，您的登录密码已修改。如非本人操作，请速登录拓天速贷官网重置密码，或联系客服处理，电话：400-169-1188。",
            "SMS_45380002", "尊敬的用户，您的登录密码已修改。如非本人操作，请速登录拓天速贷官网重置密码，或联系客服处理，电话：400-169-1188。"),

    SMS_LOAN_REPAY_NOTIFY_TEMPLATE(SmsChannel.TryBoth,
            "6486", "您今天有 %s 元尚未还款，请及时登录系统还款。",
            "SMS_45420001", "您今天有 ${param0} 元尚未还款，请及时登录系统还款。"),

    SMS_FATAL_NOTIFY_TEMPLATE(SmsChannel.TryBoth,
            "8455", "%s 警报：%s",
            "SMS_45630075", "异常报告，环境：${param0}，错误码：${param1}。请尽快处理。"),

    SMS_PLATFORM_BALANCE_LOW_NOTIFY_TEMPLATE(SmsChannel.TryBoth,
            "3032242", "系统账户余额不足 %s 元，请立刻充值！",
            "SMS_45340002", "系统账户余额不足 ${param0} 元，请立刻充值！"),

    SMS_COUPON_NOTIFY_TEMPLATE(SmsChannel.Backup,
            "6487", "尊敬的拓天速贷客户，恭喜您获得%s，有效期%s，请及时投资体验。",
            "SMS_45505123", "尊敬的拓天速贷客户，您专属的${param0}已到账，有效期${param1}，请及时使用。"),

    SMS_BIRTHDAY_NOTIFY_TEMPLATE(SmsChannel.Backup,
            "6482", "亲爱的天宝生日快乐！生日月投资收益翻倍，最高26%，不要错过哟！",
            "SMS_45630093", "亲爱的天宝生日快乐！本月您专享生日优惠，不要错过哟，详询客服。"),

    SMS_EXPERIENCE_REPAY_NOTIFY_TEMPLATE(SmsChannel.TryBoth,
            "8510", "亲爱的用户，您投资的新手体验项目所得%s元现金红包奖励已发放，快来激活奖励吧！",
            "SMS_45415002", "亲爱的用户，您投资的新手体验项目所得${param0}元现金红包奖励已发放，快来激活奖励吧！"),

    SMS_CANCEL_TRANSFER_LOAN(SmsChannel.TryBoth,
            "3031027", "尊敬的拓天速贷客户，由于原始项目提前还款，您的债权转让项目%s已自动取消，请尽快登录平台进行查看。",
            "SMS_45340018", "尊敬的拓天速贷客户，由于原始项目提前还款，您的债权转让项目${param0}已自动取消，请尽快登录平台进行查看。"),

    SMS_IMPORT_RECEIVE_MEMBERSHIP(SmsChannel.Backup,
            "3031214", "尊敬的用户，拓天给您赠送了V%s会员，赶紧投资享受会员特权吧！",
            "SMS_45360016", "尊敬的用户，拓天给您赠送了V${param0}会员，赶紧投资享受会员特权吧！"),

    SMS_NEW_USER_RECEIVE_MEMBERSHIP(SmsChannel.Backup,
            "3031215", "尊敬的用户，欢迎您加入拓天，拓天给您赠送了V%s会员，赶紧投资享受会员特权吧！",
            "SMS_45355032", "尊敬的用户，欢迎您加入拓天，拓天给您赠送了V${param0}会员，赶紧投资享受会员特权吧！"),

    SMS_GENERATE_CONTRACT_ERROR_NOTIFY_TEMPLATE(SmsChannel.TryBoth,
            "3034360", "安心签合同生成失败，标的/债权 id : %s",
            "SMS_45320005", "安心签合同生成失败，业务 id : ${param0}"),

    SMS_LOAN_RAISING_COMPLETE_NOTIFY_TEMPLATE(SmsChannel.TryBoth,
            "3038299", "%s上线的%s天总额为%sw的项目于今日%s已满，%s;%s，30分钟内将完成复核。",
            "SMS_45610002", "${param0}上线的${param1}天总额为${param2}w的项目于今日${param3}已满，客户:${param4};代理人:${param5}，30分钟内将完成复核。");

    /*
    !!! ATTENTION !!!
    模板中的变量占位符在网易里使用的是 %s, 在阿里里使用的是 ${param0} ${param1}。

    请在阿里平台上配置的时候一定要注意，请严格按照变量在文本中出现的顺序进行编号，编号从0开始。
    请在阿里平台上配置的时候一定要注意，请严格按照变量在文本中出现的顺序进行编号，编号从0开始。
    请在阿里平台上配置的时候一定要注意，请严格按照变量在文本中出现的顺序进行编号，编号从0开始。
    !!! ATTENTION !!!
     */

    private final SmsChannel channel;

    private final String templateIdNetease;

    private final String templateNetease;

    private final String templateIdAlidayu;

    private final String templateAlidayu;

    SmsTemplate(SmsChannel channel
            , String templateIdNetease, String templateNetease
            , String templateIdAlidayu, String templateAlidayu) {
        this.channel = channel;
        this.templateIdNetease = templateIdNetease;
        this.templateIdAlidayu = templateIdAlidayu;
        this.templateNetease = templateNetease;
        this.templateAlidayu = templateAlidayu;
    }

    public SmsChannel getChannel() {
        return channel;
    }

    public String getTemplateIdNetease() {
        return templateIdNetease;
    }

    public String getTemplateNetease() {
        return templateNetease;
    }

    public String getTemplateIdAlidayu() {
        return templateIdAlidayu;
    }

    public String getTemplateAlidayu() {
        return templateAlidayu;
    }

    public String generateContent(List<String> templateParameters, SmsChannel channel) {
        if (channel == SmsChannel.Backup) {
            return generateContentAlidayu(templateParameters);
        } else {
            return generateContentNetease(templateParameters);
        }
    }

    private String generateContentNetease(List<String> paramList) {
        String content = this.templateNetease;
        if (CollectionUtils.isEmpty(paramList)) {
            return content;
        }

        for (String param : paramList) {
            content = content.replaceFirst("%s", param);
        }
        return content;
    }

    private String generateContentAlidayu(List<String> paramList) {
        String content = this.templateAlidayu;
        if (CollectionUtils.isEmpty(paramList))
            return content;

        for (String param : paramList) {
            content = content.replaceFirst("\\$\\{param\\d+\\}", param);
        }
        return content;
    }
}
