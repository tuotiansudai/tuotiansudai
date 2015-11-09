package com.tuotiansudai.utils;

import java.util.Map;

public enum SendCloudTemplate {
    REPAY_COMPLETE_EMAIL("标的还款成功邮件提醒","尊敬的拓天速贷客户，您参与的平台投标项目 “{loanName}”，第{periods}期于{repayDate} 还款{amount}元，请注意查收。【拓天速贷】"),
    LOAN_OUT_SUCCESSFUL_EMAIL("标的放款成功邮件提醒","尊敬的拓天速贷客户，您在平台的项目借款 “{loanName}” 已成功放款，您的投资金额为{money}元。【拓天速贷】"),
    ACTIVE_EMAIL("邮箱激活","尊敬的用户{loginName}:</br>恭喜您，</br>已经进入邮箱绑定流程：</br>请点击下面的链接<a href='{activeUrl}'>验证并绑定</a>该邮箱:</br>{activeUrl}【拓天速贷】");
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
