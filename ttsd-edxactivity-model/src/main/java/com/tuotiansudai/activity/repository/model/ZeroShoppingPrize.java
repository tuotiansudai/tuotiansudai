package com.tuotiansudai.activity.repository.model;


import com.google.common.collect.Lists;

import java.util.List;

public enum ZeroShoppingPrize {

    Medihea_Mask("Mediheal可莱丝美迪惠尔水润保湿面膜10片", 180000l),
    Thailand_VIP("泰国奢华游VIP卡2张", 280000l),
    Philips_Shaver("飞利浦（PHILIPS）电动剃须刀", 900000l),
    SK_II("SK-II 神仙水 晶透修护礼盒", 1900000l),
    XiaoMi_5X("小米5X 4GB+64GB", 2100000l),
    XiaPu_Television("夏普45英寸智能液晶电视", 2900000l),
    Philips_Purifier("飞利浦空气净化器", 3900000l),
    Sony_Camera("索尼DSC-RX100 M3 黑卡相机", 5400000l),
    Apple_MacBook("Apple MacBook Air 13.3英寸笔记本电脑8GB内存/128GB闪存", 900000l),
    Iphone_X("iphone X 256GB", 13500000l);

    private String description;
    private Long investPrice;

    ZeroShoppingPrize(String description, Long investPrice) {
        this.description = description;
        this.investPrice = investPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getInvestPrice() {
        return investPrice;
    }

    public void setInvestPrice(Long investPrice) {
        this.investPrice = investPrice;
    }

    public static List<ZeroShoppingPrize> getTaskZeroShoppingPrize(){
        return Lists.newArrayList(ZeroShoppingPrize.values());
    }
}
