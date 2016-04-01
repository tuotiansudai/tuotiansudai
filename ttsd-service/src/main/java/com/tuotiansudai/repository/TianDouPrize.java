package com.tuotiansudai.repository;

public enum TianDouPrize {

    MacBook("MacBook Air"),
    Iphone6s("iPhone 6s Plus"),
    JingDong300("300元京东购物卡"),
    Cash20("20元现金"),
    InterestCoupon5("0.5%加息券");

    String name;

    TianDouPrize(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
