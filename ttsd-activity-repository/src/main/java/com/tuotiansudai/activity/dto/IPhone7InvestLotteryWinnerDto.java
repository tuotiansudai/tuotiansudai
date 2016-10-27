package com.tuotiansudai.activity.dto;

import com.tuotiansudai.activity.repository.model.IPhone7InvestLotteryWinnerView;

public class IPhone7InvestLotteryWinnerDto extends IPhone7InvestLotteryWinnerView {
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

    public IPhone7InvestLotteryWinnerDto(IPhone7InvestLotteryWinnerView view, String mobile, String userName) {
        this.setLoginName(view.getLoginName());
        this.setLotteryNumber(view.getLotteryNumber());
        this.setEffectiveTime(view.getEffectiveTime());
        this.mobile = mobile;
        this.userName = userName;
    }
}
