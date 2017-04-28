package com.tuotiansudai.activity.repository.model;

public enum WechatLotteryPrize {

    Bedclothes("欧式奢华贡缎床品四件套"),
    Bag("时尚百搭真皮子母包"),
    Headgear("简约吊坠百搭锁骨链"),
    Towel("精品定制毛巾礼盒"),
    RedEnvelop20("20元红包");

    String desc;

    WechatLotteryPrize(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
