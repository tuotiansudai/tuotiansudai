package com.tuotiansudai.utils;

import java.util.Map;

public enum SendCloudTemplate {
    LOAN_OUT_SUCCESSFUL_EMAIL("标的放款成功邮件提醒","尊敬的拓天速贷客户，您在平台的项目借款 “{loanName}” 已成功放款，您的投资金额为{money}元。【拓天速贷】");
    private String title;
    private String template;

    SendCloudTemplate(String title,String template) {
        this.title = title;
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

    public String getTitle() {
        return title;
    }
}
