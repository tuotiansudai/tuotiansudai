package com.ttsd.special.dto;

import java.io.Serializable;

public class InvestTopItem implements Serializable{
    private String userId;
    private String phoneNumber;
    private Double corpus;
    private Double interest;
    private String region;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Double getCorpus() {
        return corpus;
    }

    public void setCorpus(Double corpus) {
        this.corpus = corpus;
    }

    public Double getInterest() {
        return interest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
