package com.tuotiansudai.jpush.repository.model;

public enum JumpTo {
    INVEST("1","首页-我要投资"),
    MY_WEALTH("2","我的财富"),
    MY_INVEST("3","我的投资"),
    MY_WEALTH_DETAIL("4","我的财富-明细"),
    PERSONAL_CENTER("5","个人中心"),
    ACCOUNT("6","账户信息"),
    PASSWORD_MANAGE("7","密码管理"),
    ANNOUNCEMENT("8","公告列表"),
    ABOUT("9","关于我们"),
    OTHER("0","其他");

    private final String index;

    private final String description;

    JumpTo(String index,String description){
        this.index = index;
        this.description = description;
    }

    public String getIndex() {
        return index;
    }

    public String getDescription() {
        return description;
    }
}
