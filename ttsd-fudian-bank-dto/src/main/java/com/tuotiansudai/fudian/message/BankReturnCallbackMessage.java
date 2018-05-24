package com.tuotiansudai.fudian.message;

public class BankReturnCallbackMessage extends BankBaseMessage {

    private String bankOrderNo;

    public BankReturnCallbackMessage() {
    }

    public BankReturnCallbackMessage(boolean status, String message, String bankOrderNo) {
        super(status, message);
        this.bankOrderNo = bankOrderNo;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public void setBankOrderNo(String bankOrderNo) {
        this.bankOrderNo = bankOrderNo;
    }
}
