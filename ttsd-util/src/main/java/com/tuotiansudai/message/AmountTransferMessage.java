package com.tuotiansudai.message;

import com.tuotiansudai.enums.*;

public class AmountTransferMessage {

    private long businessId;

    private String loginName;

    private Role role;

    private long amount;

    private String bankOrderNo;

    private String bankOrderDate;

    private BillOperationType operationType;

    private BankUserBillBusinessType businessType;

    public AmountTransferMessage() {

    }

    public AmountTransferMessage(long businessId, String loginName, Role role, long amount, String bankOrderNo, String bankOrderDate, BillOperationType operationType, BankUserBillBusinessType businessType) {
        this.businessId = businessId;
        this.loginName = loginName;
        this.role = role;
        this.amount = amount;
        this.bankOrderNo = bankOrderNo;
        this.bankOrderDate = bankOrderDate;
        this.operationType = operationType;
        this.businessType = businessType;
    }

    public long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(long businessId) {
        this.businessId = businessId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
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

    public BillOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(BillOperationType operationType) {
        this.operationType = operationType;
    }

    public BankUserBillBusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BankUserBillBusinessType businessType) {
        this.businessType = businessType;
    }
}
