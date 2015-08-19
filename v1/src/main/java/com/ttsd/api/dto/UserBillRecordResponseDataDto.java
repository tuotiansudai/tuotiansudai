package com.ttsd.api.dto;
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

    public void setTypeInfo(String typeInfo) {
        this.typeInfo = typeInfo;
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
