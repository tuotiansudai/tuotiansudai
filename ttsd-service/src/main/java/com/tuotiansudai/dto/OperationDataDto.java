package com.tuotiansudai.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huoxuanbo on 16/5/10.
 */
public class OperationDataDto {
    private int operationDays;
    private long usersCount;
    private String TradeAmount;
    private List<String> month = new ArrayList<>();
    private List<String> money = new ArrayList<>();

    public int getOperationDays() {
        return operationDays;
    }

    public void setOperationDays(int operationDays) {
        this.operationDays = operationDays;
    }

    public long getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(long usersCount) {
        this.usersCount = usersCount;
    }

    public String getTradeAmount() {
        return TradeAmount;
    }

    public void setTradeAmount(String tradeAmount) {
        TradeAmount = tradeAmount;
    }

    public List<String> getMonth() {
        return month;
    }

    public void setMonth(List<String> month) {
        this.month = month;
    }

    public List<String> getMoney() {
        return money;
    }

    public void setMoney(List<String> money) {
        this.money = money;
    }
}
