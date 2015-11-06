package com.tuotiansudai.repository.model;

//TODO : puzzle
public enum SystemBillMessageTemplate {

    REFERRER_REWARD_MESSAGE_TEMPLATE("投资推荐人奖励");

    private final String template;

    SystemBillMessageTemplate(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }
}
