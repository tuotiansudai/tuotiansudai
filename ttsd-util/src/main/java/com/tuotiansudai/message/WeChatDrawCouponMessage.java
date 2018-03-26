package com.tuotiansudai.message;


import com.tuotiansudai.enums.WeChatDrawCoupon;

import java.io.Serializable;

public class WeChatDrawCouponMessage implements Serializable {

    private String loginName;
    private WeChatDrawCoupon weChatDrawCoupon;

    public WeChatDrawCouponMessage() {
    }

    public WeChatDrawCouponMessage(String loginName, WeChatDrawCoupon weChatDrawCoupon) {
        this.loginName = loginName;
        this.weChatDrawCoupon = weChatDrawCoupon;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public WeChatDrawCoupon getWeChatDrawCoupon() {
        return weChatDrawCoupon;
    }

    public void setWeChatDrawCoupon(WeChatDrawCoupon weChatDrawCoupon) {
        this.weChatDrawCoupon = weChatDrawCoupon;
    }
}
