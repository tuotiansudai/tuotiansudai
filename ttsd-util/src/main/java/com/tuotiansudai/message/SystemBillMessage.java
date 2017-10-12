package com.tuotiansudai.message;

import com.tuotiansudai.enums.SystemBillBusinessType;
import com.tuotiansudai.enums.SystemBillMessageType;

public class SystemBillMessage {

    private SystemBillMessageType messageType;

    private long orderId;

    private long amount;

    private SystemBillBusinessType businessType;

    private String detail;

    public SystemBillMessage() {
    }

    public SystemBillMessage(SystemBillMessageType messageType, long orderId, long amount, SystemBillBusinessType businessType, String detail) {
        this.messageType = messageType;
        this.orderId = orderId;
        this.amount = amount;
        this.businessType = businessType;
        this.detail = detail;
    }

    public SystemBillMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(SystemBillMessageType messageType) {
        this.messageType = messageType;
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

    public SystemBillBusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(SystemBillBusinessType businessType) {
        this.businessType = businessType;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
