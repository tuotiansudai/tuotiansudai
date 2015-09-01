package com.ttsd.special.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class InvestTopItem implements Serializable{
    private String userId;
    @JsonIgnore
    private String phoneNumber;
    private String corpus;
    private String interest;

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

    public String getCorpus() {
        return corpus;
    }

    public void setCorpus(String corpus) {
        this.corpus = corpus;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }
}
