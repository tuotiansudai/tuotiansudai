package com.tuotiansudai.message;


public class InvestSuccessNewmanTyrantMessage {

    private InvestInfo investInfo;

    private UserInfoActivity userInfoActivity;

    public InvestSuccessNewmanTyrantMessage(){

    }

    public InvestSuccessNewmanTyrantMessage(InvestInfo investInfo, UserInfoActivity userInfoActivity) {
        this.investInfo = investInfo;
        this.userInfoActivity = userInfoActivity;
    }



    public InvestInfo getInvestInfo() {
        return investInfo;
    }

    public void setInvestInfo(InvestInfo investInfo) {
        this.investInfo = investInfo;
    }

    public UserInfoActivity getUserInfoActivity() {
        return userInfoActivity;
    }

    public void setUserInfoActivity(UserInfoActivity userInfoActivity) {
        this.userInfoActivity = userInfoActivity;
    }
}
