package com.tuotiansudai.fudian.download;


public enum QueryDownloadLogFilesType {
    RECHARGE("recharge", "充值对账"),
    WITHDRAW("withdraw", "提现对账"),
    LOAN_INVEST("invest", "投资对账"),
    LOAN_REPAY("repayment", "还款对账"),
    LOAN_CALLBACK("loanBack", "回款对账"),
    LOAN_CREDIT_INVEST("creditInvest", "债权购买对账"),
    LOAN_FULL("loanFull", "满标放款对账");

    private String type;
    private String describe;

    QueryDownloadLogFilesType(String type, String describe) {
        this.type = type;
        this.describe = describe;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
