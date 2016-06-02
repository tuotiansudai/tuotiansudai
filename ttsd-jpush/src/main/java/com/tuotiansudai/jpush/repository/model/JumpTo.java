package com.tuotiansudai.jpush.repository.model;

public enum JumpTo {
    INVEST("1","首页-我要投资"),
    MY_WEALTH("2","我的财富"),
    MY_WEALTH_DETAIL("3","我的财富-明细"),
    PERSONAL_CENTER("4","个人中心"),
    ACCOUNT("5","账户信息"),
    PASSWORD_MANAGE("6","密码管理"),
    ANNOUNCEMENT("7","公告列表"),
    ABOUTUS("8","关于我们"),
    INVEST_RECEIVABLES("9","我的投资-已收款页"),
    WITHDRAW_RECORDS("10","提现记录"),
    REFERRER_INVEST_LIST("11","推荐人投资列表"),
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
