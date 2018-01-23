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
    private List<OperationDataInvestCityDataDto> investCityScaleTop3;
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

    public List<OperationDataInvestCityDataDto> getInvestCityScaleTop3() {
        return investCityScaleTop3;
    }

    public void setInvestCityScaleTop3(List<OperationDataInvestCityDataDto> investCityScaleTop3) {
        this.investCityScaleTop3 = investCityScaleTop3;
    }

    public List<OperationDataInvestAmountDataDto> getInvestAmountScaleTop3() {
        return investAmountScaleTop3;
    }

    public void setInvestAmountScaleTop3(List<OperationDataInvestAmountDataDto> investAmountScaleTop3) {
        this.investAmountScaleTop3 = investAmountScaleTop3;
    }
}
