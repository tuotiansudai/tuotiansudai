package com.tuotiansudai.jpush.repository.model;

import com.google.common.collect.Lists;

import java.util.List;

public enum JumpTo {
    INVEST("1","首页-我要投资", Lists.newArrayList("jumpTo")),
    MY_WEALTH("2","我的财富",Lists.newArrayList("jumpTo")),
    MY_WEALTH_DETAIL("3","我的财富-明细",Lists.newArrayList("jumpTo")),
    PERSONAL_CENTER("4","个人中心",Lists.newArrayList("jumpTo")),
    ACCOUNT("5","账户信息",Lists.newArrayList("jumpTo")),
    PASSWORD_MANAGE("6","密码管理",Lists.newArrayList("jumpTo")),
    ANNOUNCEMENT("7","公告列表",Lists.newArrayList("jumpTo")),
    ABOUTUS("8","关于我们",Lists.newArrayList("jumpTo")),
    INVEST_RECEIVABLES("9","我的投资-已收款页",Lists.newArrayList("jumpTo")),
    WITHDRAW_RECORDS("10","提现记录",Lists.newArrayList("jumpTo")),
    REFERRER_INVEST_LIST("11","推荐人投资列表",Lists.newArrayList("jumpTo")),
    INVEST_REPAY("12","回款计划",Lists.newArrayList("jumpTo","investId","loanId","isCompleted")),
    OTHER("0","其他",Lists.newArrayList("jumpToLink"));

    private String index;

    private String description;

    private List<String> params;

    JumpTo(String index,String description, List<String> params){
        this.index = index;
        this.description = description;
        this.params = params;
    }

    public String getIndex() {
        return index;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getParams() { return params; }
}
