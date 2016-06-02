package com.tuotiansudai.repository.model;

import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;

public class InvestDataView implements Serializable {

    private String productName;

    private String totalInvestAmount;

    private int countInvest;

    private String avgInvestAmount;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTotalInvestAmount() {
        return totalInvestAmount;
    }

    public void setTotalInvestAmount(String totalInvestAmount) {
        this.totalInvestAmount = totalInvestAmount;
    }

    public int getCountInvest() {
        return countInvest;
    }

    public void setCountInvest(int countInvest) {
        this.countInvest = countInvest;
    }

    public String getAvgInvestAmount() {
        return avgInvestAmount;
    }

    public void setAvgInvestAmount(String avgInvestAmount) {
        this.avgInvestAmount = avgInvestAmount;
    }


    public String  ConvertInvestDataViewToString(){
        return this.getProductName() + "|" + AmountConverter.convertCentToString(Long.parseLong(this.getTotalInvestAmount())) + "|" + this.getCountInvest() + "|" + AmountConverter.convertCentToString(Long.parseLong(this.getAvgInvestAmount()));

    }
}
