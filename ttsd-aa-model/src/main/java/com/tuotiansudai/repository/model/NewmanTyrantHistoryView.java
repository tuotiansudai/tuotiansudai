package com.tuotiansudai.repository.model;

import java.io.Serializable;

public class NewmanTyrantHistoryView implements Serializable{
    private String currentDate;
    private long avgNewmanInvestAmount;
    private long avgTyrantInvestAmount;

    public NewmanTyrantHistoryView(){}

    public NewmanTyrantHistoryView(String currentDate,long avgNewmanInvestAmount,long avgTyrantInvestAmount){
        this.currentDate = currentDate;
        this.avgNewmanInvestAmount = avgNewmanInvestAmount;
        this.avgTyrantInvestAmount = avgTyrantInvestAmount;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public long getAvgNewmanInvestAmount() {
        return avgNewmanInvestAmount;
    }

    public void setAvgNewmanInvestAmount(long avgNewmanInvestAmount) {
        this.avgNewmanInvestAmount = avgNewmanInvestAmount;
    }

    public long getAvgTyrantInvestAmount() {
        return avgTyrantInvestAmount;
    }

    public void setAvgTyrantInvestAmount(long avgTyrantInvestAmount) {
        this.avgTyrantInvestAmount = avgTyrantInvestAmount;
    }
}