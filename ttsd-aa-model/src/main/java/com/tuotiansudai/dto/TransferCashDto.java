package com.tuotiansudai.dto;


import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.model.SystemBillBusinessType;
import com.tuotiansudai.repository.model.SystemBillDetailTemplate;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class TransferCashDto implements Serializable {

    @NotEmpty
    private String loginName;

    @NotEmpty
    private String orderId;

    @NotEmpty
    private String amount;

    @NotNull
    private UserBillBusinessType userBillBusinessType;

    @NotNull
    private SystemBillBusinessType systemBillBusinessType;

    @NotNull
    private SystemBillDetailTemplate systemBillDetailTemplate;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public UserBillBusinessType getUserBillBusinessType() {
        return userBillBusinessType;
    }

    public void setUserBillBusinessType(UserBillBusinessType userBillBusinessType) {
        this.userBillBusinessType = userBillBusinessType;
    }

    public SystemBillBusinessType getSystemBillBusinessType() {
        return systemBillBusinessType;
    }

    public void setSystemBillBusinessType(SystemBillBusinessType systemBillBusinessType) {
        this.systemBillBusinessType = systemBillBusinessType;
    }

    public SystemBillDetailTemplate getSystemBillDetailTemplate() {
        return systemBillDetailTemplate;
    }

    public void setSystemBillDetailTemplate(SystemBillDetailTemplate systemBillDetailTemplate) {
        this.systemBillDetailTemplate = systemBillDetailTemplate;
    }

    public TransferCashDto(String loginName, String orderId, String amount, UserBillBusinessType userBillBusinessType, SystemBillBusinessType systemBillBusinessType, SystemBillDetailTemplate systemBillDetailTemplate) {
        this.loginName = loginName;
        this.orderId = orderId;
        this.amount = amount;
        this.userBillBusinessType = userBillBusinessType;
        this.systemBillBusinessType = systemBillBusinessType;
        this.systemBillDetailTemplate = systemBillDetailTemplate;
    }

    public TransferCashDto() {

    }

}
