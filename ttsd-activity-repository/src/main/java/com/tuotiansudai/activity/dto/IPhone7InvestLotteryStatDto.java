package com.tuotiansudai.activity.dto;

import com.tuotiansudai.activity.repository.model.IPhone7InvestLotteryStatView;

public class IPhone7InvestLotteryStatDto extends IPhone7InvestLotteryStatView {
    private String mobile;
    private String userName;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public IPhone7InvestLotteryStatDto(IPhone7InvestLotteryStatView view, String mobile, String userName) {
        this.setInvestAmountTotal(view.getInvestAmountTotal());
        this.setInvestCount(view.getInvestCount());
        this.setLoginName(view.getLoginName());
        this.setLotteryNumberArray(view.getLotteryNumberArray());
        this.setMobile(mobile);
        this.setUserName(userName);
    }
}
