package com.tuotiansudai.message;


public class InvestSuccessMessage {

    private InvestInfo investInfo;

    private LoanDetailInfo loanDetailInfo;

    private UserInfo userInfo;

    public InvestSuccessMessage(){

    }

    public InvestSuccessMessage(InvestInfo investInfo, LoanDetailInfo loanDetailInfo, UserInfo userInfo) {
        this.investInfo = investInfo;
        this.loanDetailInfo = loanDetailInfo;
        this.userInfo = userInfo;
    }

    public InvestInfo getInvestInfo() {
        return investInfo;
    }

    public void setInvestInfo(InvestInfo investInfo) {
        this.investInfo = investInfo;
    }

    public LoanDetailInfo getLoanDetailInfo() {
        return loanDetailInfo;
    }

    public void setLoanDetailInfo(LoanDetailInfo loanDetailInfo) {
        this.loanDetailInfo = loanDetailInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
