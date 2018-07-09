package com.tuotiansudai.repository.model;

/**
 * Created by qduljs2011 on 2018/7/6.
 */
public enum FundPlatform {
    UMP("联动优势"),
    FUDIAN("富滇银行");

    private String description;
    FundPlatform(String description){
        this.description=description;
    }

    public String getDescription(){
        return description;
    }
}
