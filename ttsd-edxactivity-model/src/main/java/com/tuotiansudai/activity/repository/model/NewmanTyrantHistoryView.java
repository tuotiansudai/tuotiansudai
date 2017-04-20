package com.tuotiansudai.activity.repository.model;

import java.io.Serializable;
import java.util.Date;

public class NewmanTyrantHistoryView implements Serializable {
    private Date currentDate;
    private long avgNewmanInvestAmount;
    private long avgTyrantInvestAmount;

    public enum type {
        NEWMAN,
        TYRANT,
        NEWMAN_TYRANT
    }

    public NewmanTyrantHistoryView() {
    }

    public NewmanTyrantHistoryView(Date currentDate, long avgNewmanInvestAmount, long avgTyrantInvestAmount) {
        this.currentDate = currentDate;
        this.avgNewmanInvestAmount = avgNewmanInvestAmount;
        this.avgTyrantInvestAmount = avgTyrantInvestAmount;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
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

    public type obtainNewmanTyrantType() {
        if (this.avgTyrantInvestAmount > this.avgNewmanInvestAmount) return type.TYRANT;

        if (this.avgNewmanInvestAmount > this.avgTyrantInvestAmount) return type.NEWMAN;

        return type.NEWMAN_TYRANT;

    }


}