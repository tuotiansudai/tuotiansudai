package com.tuotiansudai.message;

import com.tuotiansudai.enums.SystemBillBusinessType;
import com.tuotiansudai.enums.SystemBillMessageType;

public class BankSystemBillMessage {

    private SystemBillMessageType messageType;

    private long businessId;

    private String bankOrderNo;

    private String bankOrderDate;

    private long amount;

    private SystemBillBusinessType businessType;

    private String detail;

    public BankSystemBillMessage() {
    }

    public BankSystemBillMessage(SystemBillMessageType messageType,long businessId ,String bankOrderNo, String bankOrderDate, long amount, SystemBillBusinessType businessType, String detail) {
        this.messageType = messageType;
        this.bankOrderNo = bankOrderNo;
        this.amount = amount;
        this.businessType = businessType;
        this.bankOrderDate = bankOrderDate;
        this.detail = detail;
        this.businessId=businessId;
    }

    public SystemBillMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(SystemBillMessageType messageType) {
        this.messageType = messageType;
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

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public void setBankOrderNo(String bankOrderNo) {
        this.bankOrderNo = bankOrderNo;
    }

    public String getBankOrderDate() {
        return bankOrderDate;
    }

    public void setBankOrderDate(String bankOrderDate) {
        this.bankOrderDate = bankOrderDate;
    }

    public long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(long businessId) {
        this.businessId = businessId;
    }
}
