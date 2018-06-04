package com.tuotiansudai.fudian.message;

public class BankMerchantTransferMessage extends BankBaseMessage {

    private String bankOrderNo;

    private String bankOrderDate;

    public BankMerchantTransferMessage(boolean status, String message) {
        super(status, message);
    }

    public BankMerchantTransferMessage(String bankOrderNo, String bankOrderDate) {
        super(true, null);
        this.bankOrderNo = bankOrderNo;
        this.bankOrderDate = bankOrderDate;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public String getBankOrderDate() {
        return bankOrderDate;
    }
}
