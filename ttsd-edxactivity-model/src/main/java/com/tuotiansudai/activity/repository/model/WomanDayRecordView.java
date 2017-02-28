package com.tuotiansudai.activity.repository.model;


import java.io.Serializable;

public class WomanDayRecordView implements Serializable{

    private String mobile;
    private String name;
    private int totalLeaves;
    private int investLeaves;
    private int signLeaves;
    private int referrerLeaves;
    private String prize;

    public WomanDayRecordView(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalLeaves() {
        return totalLeaves;
    }

    public void setTotalLeaves(int totalLeaves) {
        this.totalLeaves = totalLeaves;
    }

    public int getInvestLeaves() {
        return investLeaves;
    }

    public void setInvestLeaves(int investLeaves) {
        this.investLeaves = investLeaves;
    }

    public int getSignLeaves() {
        return signLeaves;
    }

    public void setSignLeaves(int signLeaves) {
        this.signLeaves = signLeaves;
    }

    public int getReferrerLeaves() {
        return referrerLeaves;
    }

    public void setReferrerLeaves(int referrerLeaves) {
        this.referrerLeaves = referrerLeaves;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }
}
