package com.tuotiansudai.message;

import com.tuotiansudai.enums.UmpTransferType;
import com.tuotiansudai.enums.UserBillBusinessType;

public class UmpAmountTransferMessage {

    private UmpTransferType transferType;

    private String loginName;

    private long orderId;

    private long amount;

    private UserBillBusinessType businessType;

    public UmpAmountTransferMessage() {

    }

    public UmpAmountTransferMessage(UmpTransferType transferType, String loginName, long orderId, long amount,
                                    UserBillBusinessType businessType) {
        this.transferType = transferType;
        this.loginName = loginName;
        this.orderId = orderId;
        this.amount = amount;
        this.businessType = businessType;
    }

    public UmpTransferType getTransferType() {
        return transferType;
    }

    public void setTransferType(UmpTransferType transferType) {
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
}
