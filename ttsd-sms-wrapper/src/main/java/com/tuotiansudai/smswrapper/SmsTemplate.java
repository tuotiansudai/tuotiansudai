package com.tuotiansudai.smswrapper;

public enum SmsTemplate {

    /*
    !!! ATTENTION !!!
    请在阿里平台上配置的时候一定要注意，请严格按照变量在文本中出现的顺序进行编号，编号从0开始。
    请在阿里平台上配置的时候一定要注意，请严格按照变量在文本中出现的顺序进行编号，编号从0开始。
    请在阿里平台上配置的时候一定要注意，请严格按照变量在文本中出现的顺序进行编号，编号从0开始。
    !!! ATTENTION !!!
     */


    SMS_REGISTER_CAPTCHA_TEMPLATE(
            new SmsTemplateCell("SMS_45345002", "尊敬的拓天速贷客户，您的注册验证码是：${param0} 。请勿泄露给他人！"),
            new SmsTemplateCell("TTS_100735042", "尊敬的拓天速贷客户，您的注册验证码是：${param0} 。请勿泄露给他人！")),

    SMS_NO_PASSWORD_INVEST_CAPTCHA_TEMPLATE(
            new SmsTemplateCell("SMS_45395002", "尊敬的拓天速贷客户，您的验证码是：${param0} 。请勿泄露给他人！"),
            new SmsTemplateCell("TTS_100910049", "尊敬的拓天速贷客户，您的验证码是：${param0} 。请勿泄露给他人！")),

    SMS_MOBILE_CAPTCHA_TEMPLATE(
            new SmsTemplateCell("SMS_45370002", "尊敬的拓天速贷客户，您的找回密码的验证码是：${param0} 。请勿泄露给他人！"),
            new SmsTemplateCell("TTS_100850051", "尊敬的拓天速贷客户，您的找回密码的验证码是：${param0} 。请勿泄露给他人！")),

    SMS_PASSWORD_CHANGED_NOTIFY_TEMPLATE(
            new SmsTemplateCell("SMS_45380002", "尊敬的用户，您的登录密码已修改。如非本人操作，请速登录拓天速贷官网重置密码，或联系客服处理，电话：400-169-1188。"),
            null),

    SMS_LOAN_REPAY_NOTIFY_TEMPLATE(
            new SmsTemplateCell("SMS_45420001", "您今天有 ${param0} 元尚未还款，请及时登录系统还款。"),
            null),

    SMS_FATAL_NOTIFY_TEMPLATE(
            new SmsTemplateCell("SMS_45630075", "异常报告，环境：${param0}，错误码：${param1}。请尽快处理。"),
            null),

    SMS_EXPERIENCE_REPAY_NOTIFY_TEMPLATE(
            new SmsTemplateCell("SMS_118645015", "亲爱的用户，您投资的新手体验项目所得${param0}元投资红包奖励已发放，快来激活奖励吧！"),
            null),

    SMS_CANCEL_TRANSFER_LOAN(
            new SmsTemplateCell("SMS_45340018", "尊敬的拓天速贷客户，由于原始项目提前还款，您的债权转让项目${param0}已自动取消，请尽快登录平台进行查看。"),
            null),

    SMS_IMPORT_RECEIVE_MEMBERSHIP(
            new SmsTemplateCell("SMS_45360016", "尊敬的用户，拓天给您赠送了V${param0}会员，赶紧投资享受会员特权吧！"),
            null),

    SMS_NEW_USER_RECEIVE_MEMBERSHIP(
            new SmsTemplateCell("SMS_45355032", "尊敬的用户，欢迎您加入拓天，拓天给您赠送了V${param0}会员，赶紧投资享受会员特权吧！"),
            null),

    SMS_GENERATE_CONTRACT_ERROR_NOTIFY_TEMPLATE(
            new SmsTemplateCell("SMS_45320005", "安心签合同生成失败，业务 id : ${param0}"),
            null),

    SMS_LOAN_RAISING_COMPLETE_NOTIFY_TEMPLATE(
            new SmsTemplateCell("SMS_45610002", "${param0}上线的${param1}天总额为${param2}w的项目于今日${param3}已满，客户:${param4};代理人:${param5}，30分钟内将完成复核。"),
            null),

    SMS_COUPON_ASSIGN_SUCCESS_TEMPLATE(
            new SmsTemplateCell("SMS_46830018", "尊敬的用户，恭喜您获得了一张${param0}，请尽快使用拿奖励哦！"),
            null),

    SMS_COUPON_EXPIRED_NOTIFY_TEMPLATE(
            new SmsTemplateCell("SMS_46865002", "尊敬的用户，您的${param0}即将过期（有效期至：${param1}），请尽快使用。"),
            null),

    SMS_CREDIT_LOAN_BALANCE_ALERT_TEMPLATE(
            new SmsTemplateCell("SMS_101445012", "慧租账户余额不足，请及时登录后台充值。"),
            null);


    private SmsTemplateCell templateCellText;

    private SmsTemplateCell templateCellVoice;

    public SmsTemplateCell getTemplateCellText() {
        return templateCellText;
    }

    public SmsTemplateCell getTemplateCellVoice() {
        return templateCellVoice;
    }

    SmsTemplate(SmsTemplateCell templateCellText, SmsTemplateCell templateCellVoice) {
        this.templateCellText = templateCellText;
        this.templateCellVoice = templateCellVoice;
    }

    public SmsTemplateCell getTemplateCell(boolean isVoice) {
        return isVoice ? getTemplateCellVoice() : getTemplateCellText();
    }

}
