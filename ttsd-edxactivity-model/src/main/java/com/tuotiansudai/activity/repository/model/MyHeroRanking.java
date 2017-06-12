package com.tuotiansudai.activity.repository.model;


import java.io.Serializable;

public class MyHeroRanking implements Serializable{

    private Integer investRanking;
    private Long investAmount;
    public void setInvestRanking(Integer investRanking) {
        this.investRanking = investRanking;
    }

    public void setInvestAmount(Long investAmount) {
        this.investAmount = investAmount;
    }

    public Integer getInvestRanking() {
        return investRanking;
    }

    public Long getInvestAmount() {
        return investAmount;
    }





}
