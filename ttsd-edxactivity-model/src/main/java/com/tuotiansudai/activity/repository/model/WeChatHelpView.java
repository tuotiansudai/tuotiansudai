package com.tuotiansudai.activity.repository.model;


public class WeChatHelpView extends WeChatHelpModel{

    private String nickName;
    private String rate;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
