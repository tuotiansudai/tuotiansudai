package com.tuotiansudai.enums;

/**
 * Created by qduljs2011 on 2018/7/9.
 */
public enum AccountType {
    UMP("联动优势"),
    BANK_INVESTOR("富滇银行-出借人"),
    BANK_LOANER("富滇银行-借款人");

    final private String description;

    AccountType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
