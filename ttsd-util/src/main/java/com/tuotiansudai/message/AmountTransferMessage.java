package com.tuotiansudai.message;

import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;

public class AmountTransferMessage {

    private TransferType transferType;

    private String loginName;

    private long orderId;

    private long amount;

    private UserBillBusinessType businessType;

    private String operatorLoginName;

    private String interventionReason;

    private AmountTransferMessage next;

    public AmountTransferMessage() {

    }

    public AmountTransferMessage(TransferType transferType, String loginName, long orderId, long amount,
                                 UserBillBusinessType businessType, String operatorLoginName, String interventionReasonl) {
        this.transferType = transferType;
        this.loginName = loginName;
        this.orderId = orderId;
        this.amount = amount;
        this.businessType = businessType;
        this.operatorLoginName = operatorLoginName;
        this.interventionReason = interventionReasonl;
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

    public String getOperatorLoginName() {
        return operatorLoginName;
    }

    public void setOperatorLoginName(String operatorLoginName) {
        this.operatorLoginName = operatorLoginName;
    }

    public String getInterventionReason() {
        return interventionReason;
    }

    public void setInterventionReason(String interventionReason) {
        this.interventionReason = interventionReason;
    }

    public AmountTransferMessage getNext() {
        return next;
    }

    public void setNext(AmountTransferMessage next) {
        this.next = next;
    }
}
