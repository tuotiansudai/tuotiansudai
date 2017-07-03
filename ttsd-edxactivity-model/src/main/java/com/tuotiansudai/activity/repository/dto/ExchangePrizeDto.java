package com.tuotiansudai.activity.repository.dto;

import com.tuotiansudai.activity.repository.model.ExchangePrize;

public class ExchangePrizeDto extends BannerDto{

    private int returnCode;

    private String prize;

    private String prizeValue;

    private long amount;

    public ExchangePrizeDto(int returnCode, String prize, String prizeValue, long amount) {
        this.returnCode = returnCode;
        this.prize = prize;
        this.prizeValue = prizeValue;
        this.amount = amount;
    }

    public ExchangePrizeDto(int returnCode, String prize, String prizeValue) {
        this.returnCode = returnCode;
        this.prize = prize;
        this.prizeValue = prizeValue;
    }

    public ExchangePrizeDto(int returnCode){
        this.returnCode=returnCode;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getPrizeValue() {
        return prizeValue;
    }

    public void setPrizeValue(String prizeValue) {
        this.prizeValue = prizeValue;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
