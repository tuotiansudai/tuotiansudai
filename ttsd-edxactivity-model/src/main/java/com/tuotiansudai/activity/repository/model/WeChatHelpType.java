package com.tuotiansudai.activity.repository.model;


public enum WeChatHelpType {

    INVEST_HELP("出借助力"),
    EVERYONE_HELP("人人助力"),
    ;

    private String name;

    WeChatHelpType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
