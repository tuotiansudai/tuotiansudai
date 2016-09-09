package com.tuotiansudai.activity.dto;


public class MidAutumnFamilyDto {

    private String name;

    private String investAmount;

    private long amount;

    public MidAutumnFamilyDto(String name, String investAmount,long amount) {
        this.name = name;
        this.investAmount = investAmount;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
