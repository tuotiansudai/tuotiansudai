package com.tuotiansudai.api.dto;

import com.tuotiansudai.repository.model.UserBillBusinessType;

public class UserBillRecordResponseDataDto extends BaseResponseDataDto{
    private String time;
    private String typeInfo;
    private String money;
    private String balance;
    private String frozenMoney;
    private String detail;
    private String typeInfoDesc;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTypeInfo() {
        return typeInfo;
    }

    public void setTypeInfo(UserBillBusinessType typeInfo) {
        if(UserBillBusinessType.LOAN_SUCCESS.equals(typeInfo)){
            this.typeInfo = "give_money_to_borrower";
        }else if(UserBillBusinessType.CANCEL_INVEST_PAYBACK.equals(typeInfo)){
            this.typeInfo = "cancel_loan";
        }else{
            this.typeInfo = typeInfo.name().toLowerCase();
        }

    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getFrozenMoney() {
        return frozenMoney;
    }

    public void setFrozenMoney(String frozenMoney) {
        this.frozenMoney = frozenMoney;
    }

    public String getTypeInfoDesc() {
        return typeInfoDesc;
    }

    public void setTypeInfoDesc(String typeInfoDesc) {
        this.typeInfoDesc = typeInfoDesc;
    }
}
