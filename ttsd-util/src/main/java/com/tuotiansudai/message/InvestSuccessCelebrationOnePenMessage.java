package com.tuotiansudai.message;


public class InvestSuccessCelebrationOnePenMessage {

    private InvestInfo investInfo;

    public InvestSuccessCelebrationOnePenMessage(){}

    public InvestSuccessCelebrationOnePenMessage(InvestInfo investInfo){
        this.investInfo=investInfo;
    }

    public InvestInfo getInvestInfo() {
        return investInfo;
    }

    public void setInvestInfo(InvestInfo investInfo) {
        this.investInfo = investInfo;
    }
}
