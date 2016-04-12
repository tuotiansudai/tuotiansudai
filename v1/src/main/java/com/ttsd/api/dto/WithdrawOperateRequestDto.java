package com.ttsd.api.dto;

public class WithdrawOperateRequestDto extends BaseParamDto{
    private double money;

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
