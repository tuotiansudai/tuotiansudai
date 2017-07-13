package com.tuotiansudai.activity.repository.dto;

public class ExchangePrizeDto extends BannerDto{

    private int returnCode;

    private String prize;

    private String prizeValue;

    private String amount;

    public ExchangePrizeDto(int returnCode, String prize, String prizeValue, String amount) {
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
