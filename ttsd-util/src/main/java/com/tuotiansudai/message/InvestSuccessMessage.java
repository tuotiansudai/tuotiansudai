package com.tuotiansudai.message;


public class InvestSuccessMessage {

    private InvestInfo investInfo;

    private LoanDetailInfo loanDetailInfo;

    public InvestSuccessMessage(){

    }

    public InvestSuccessMessage(InvestInfo investInfo, LoanDetailInfo loanDetailInfo) {
        this.investInfo = investInfo;
        this.loanDetailInfo = loanDetailInfo;
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

}
