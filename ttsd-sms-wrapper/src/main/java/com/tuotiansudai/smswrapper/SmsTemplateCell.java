package com.tuotiansudai.smswrapper;

import org.springframework.util.CollectionUtils;

import java.util.List;

public class SmsTemplateCell {

    private String templateId;

    private String template;

    public SmsTemplateCell() {
    }

    public SmsTemplateCell(String templateId, String template) {
        this.templateId = templateId;
        this.template = template;
    }

    public String getTemplateId() {
        return templateId;
    }

    public String getTemplate() {
        return template;
    }

    public String generateContent(List<String> templateParameters) {

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
