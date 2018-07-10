package com.tuotiansudai.console.dto;

/**
 * Created by qduljs2011 on 2018/7/9.
 */
public enum AccountType {
    UMP("联动优势"),
    INVESTOR("富滇银行-出借人"),
    LOANER("富滇银行-借款人");

    final private String description;

    AccountType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
