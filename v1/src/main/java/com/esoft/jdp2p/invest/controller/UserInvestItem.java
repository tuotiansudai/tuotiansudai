package com.esoft.jdp2p.invest.controller;

import java.util.Date;

public class UserInvestItem implements java.io.Serializable {

    private String loanId;
    private String loanName;
    private int loanDeadline;
    private String investorId;
    private String investorName;
    private Boolean isMerchandiser;
    private Date investTime;
    private Double money;
    private String source;
    private String channel;

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public int getLoanDeadline() {
        return loanDeadline;
    }

    public void setLoanDeadline(int loanDeadline) {
        this.loanDeadline = loanDeadline;
    }

    public Date getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Date investTime) {
        this.investTime = investTime;
    }

    public String getInvestorId() {
        return investorId;
    }

    public void setInvestorId(String investorId) {
        this.investorId = investorId;
    }

    public String getInvestorName() {
        return investorName;
    }

    public void setInvestorName(String investorName) {
        this.investorName = investorName;
    }

    public Boolean getIsMerchandiser() {
        return isMerchandiser;
    }

    public void setIsMerchandiser(Boolean isMerchandiser) {
        this.isMerchandiser = isMerchandiser;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
