package com.tuotiansudai.smswrapper;

public class SmsTemplateCell {

    private SmsChannel smsChannel;

    private String templateId;

    private String template;

    public SmsTemplateCell() {
    }

    public SmsTemplateCell(SmsChannel smsChannel, String templateId, String template) {
        this.smsChannel = smsChannel;
        this.templateId = templateId;
        this.template = template;
    }

    public SmsChannel getSmsChannel() {
        return smsChannel;
    }

    public String getTemplateId() {
        return templateId;
    }

    public String getTemplate() {
        return template;
    }
}
