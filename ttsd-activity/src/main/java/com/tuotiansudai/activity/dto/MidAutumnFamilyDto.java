package com.tuotiansudai.activity.dto;


public class MidAutumnFamilyDto {

    private String name;

    private String investAmount;

    public MidAutumnFamilyDto(String name, String investAmount) {
        this.name = name;
        this.investAmount = investAmount;
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
}
