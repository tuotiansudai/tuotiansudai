package com.tuotiansudai.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OperationDataDto implements Serializable {
    private int operationDays;
    private long usersCount;
    private String TradeAmount;
    private String totalInterest;
    private String maleScale;
    private String femaleScale;
    private List<String> month = new ArrayList<>();
    private List<String> money = new ArrayList<>();
    private List<OperationDataAgeDataDto> ageDistribution;
    private List<OperationDataInvestCityDataDto> investCityScaleTop5;
    private List<OperationDataLoanerCityDataDto> loanerCityScaleTop5;
    private List<OperationDataInvestAmountDataDto> investAmountScaleTop3;

    public int getOperationDays() {
        return operationDays;
    }

    public void setOperationDays(int operationDays) {
        this.operationDays = operationDays;
    }

    public long getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(long usersCount) {
        this.usersCount = usersCount;
    }

    public String getTradeAmount() {
        return TradeAmount;
    }

    public void setTradeAmount(String tradeAmount) {
        TradeAmount = tradeAmount;
    }

    public List<String> getMonth() {
        return month;
    }

    public void setMonth(List<String> month) {
        this.month = month;
    }

    public List<String> getMoney() {
        return money;
    }

    public void setMoney(List<String> money) {
        this.money = money;
    }

    public String getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(String totalInterest) {
        this.totalInterest = totalInterest;
    }

    public String getMaleScale() {
        return maleScale;
    }

    public void setMaleScale(String maleScale) {
        this.maleScale = maleScale;
    }

    public String getFemaleScale() {
        return femaleScale;
    }

    public void setFemaleScale(String femaleScale) {
        this.femaleScale = femaleScale;
    }

    public List<OperationDataAgeDataDto> getAgeDistribution() {
        return ageDistribution;
    }

    public void setAgeDistribution(List<OperationDataAgeDataDto> ageDistribution) {
        this.ageDistribution = ageDistribution;
    }

    public List<OperationDataInvestCityDataDto> getInvestCityScaleTop5() {
        return investCityScaleTop5;
    }

    public void setInvestCityScaleTop5(List<OperationDataInvestCityDataDto> investCityScaleTop5) {
        this.investCityScaleTop5 = investCityScaleTop5;
    }

    public List<OperationDataInvestAmountDataDto> getInvestAmountScaleTop3() {
        return investAmountScaleTop3;
    }

    public void setInvestAmountScaleTop3(List<OperationDataInvestAmountDataDto> investAmountScaleTop3) {
        this.investAmountScaleTop3 = investAmountScaleTop3;
    }

    public List<OperationDataLoanerCityDataDto> getLoanerCityScaleTop5() {
        return loanerCityScaleTop5;
    }

    public void setLoanerCityScaleTop5(List<OperationDataLoanerCityDataDto> loanerCityScaleTop5) {
        this.loanerCityScaleTop5 = loanerCityScaleTop5;
    }
}
