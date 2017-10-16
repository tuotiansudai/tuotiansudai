package com.tuotiansudai.activity.repository.model;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.util.List;

public enum ZeroShoppingPrize {

    Deerma_humidifier("德尔玛（Deerma）加湿器 5L大容量", 11900l, 160000l),
    Trolley_case("90分商旅两用拉杆箱", 34900l, 480000l),
    Philips_Shaver("飞利浦（PHILIPS）电动剃须刀", 64900l, 900000l),
    SK_II("SK-II 神仙水 晶透修护礼盒", 137000l, 1900000l),
    XiaoMi_5X("小米5X 4GB+64GB", 149900l, 2100000l),
    XiaPu_Television("夏普45英寸智能液晶电视", 209900l, 2900000l),
    Philips_Purifier("飞利浦空气净化器", 279900l, 3900000l),
    Sony_Camera("索尼DSC-RX100 M3 黑卡相机", 389900l, 5400000l),
    Apple_MacBook("Apple MacBook Air 13.3英寸笔记本电脑8GB内存/128GB闪存", 658800l, 9000000l),
    Iphone_X("iphone X 256GB", 968800l, 13500000l);

    private String description;
    private Long marketPrice;
    private Long investPrice;

    ZeroShoppingPrize(String description, Long marketPrice, Long investPrice) {
        this.description = description;
        this.marketPrice = marketPrice;
        this.investPrice = investPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Long getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Long marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Long getInvestPrice() {
        return investPrice;
    }

    public void setInvestPrice(Long investPrice) {
        this.investPrice = investPrice;
    }

    public static List<ZeroShoppingPrize> getTaskZeroShoppingPrize() {
        return Lists.newArrayList(ZeroShoppingPrize.values());
    }
}
