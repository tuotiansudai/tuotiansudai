package com.ttsd.special.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class InvestTopItem implements Serializable{
    @JsonIgnore
    private String userId;
    private String phone;
    private String corpus;
    private String interest;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonIgnore
    public String getRealPhone() {
        return phone;
    }

    public String getPhone() {
        StringBuffer sb = new StringBuffer();
        sb.append(phone.substring(0,3));
        sb.append("****");
        sb.append(phone.substring(7));
        return sb.toString();
    }
    public void setPhone(String phone) {
        this.phone = phone;
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
