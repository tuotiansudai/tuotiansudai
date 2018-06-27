package com.tuotiansudai.fudian.download;


public enum QueryDownloadLogFilesType {
    recharge("充值对账"),
    withdraw("提现对账"),
    invest("投资对账"),
    repayment("还款对账"),
    loanBack("回款对账"),
    creditInvest("债权购买对账"),
    loanFull("满标放款对账");

    private String describe;

    QueryDownloadLogFilesType(String describe) {
        this.describe = describe;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
