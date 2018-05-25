package com.tuotiansudai.message;

import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;

public class AmountTransferMessage {

    private TransferType transferType;

    private String loginName;

    private long orderId;

    private long amount;

    private UserBillBusinessType businessType;

    private AmountTransferMessage next;

    public AmountTransferMessage() {

    }

    public AmountTransferMessage(TransferType transferType, String loginName, long orderId, long amount, UserBillBusinessType businessType) {
        this.transferType = transferType;
        this.loginName = loginName;
        this.orderId = orderId;
        this.amount = amount;
        this.businessType = businessType;
    }

    public TransferType getTransferType() {
        return transferType;
    }

    public void setTransferType(TransferType transferType) {
        this.transferType = transferType;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public UserBillBusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(UserBillBusinessType businessType) {
        this.businessType = businessType;
    }

    public AmountTransferMessage getNext() {
        return next;
    }

    public void setNext(AmountTransferMessage next) {
        this.next = next;
    }
}
