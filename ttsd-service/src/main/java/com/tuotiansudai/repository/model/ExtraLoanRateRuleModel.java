package com.tuotiansudai.repository.model;


import java.io.Serializable;

public class ExtraLoanRateRuleModel implements Serializable {

    private long id;

    private String name;

    private ProductType productType;

    private int level;

    private long minInvestAmount;

    private long maxInvestAmount;

    private double rate;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public long getMinInvestAmount() {
        return minInvestAmount;
    }

    public void setMinInvestAmount(long minInvestAmount) {
        this.minInvestAmount = minInvestAmount;
    }

    public long getMaxInvestAmount() {
        return maxInvestAmount;
    }

    public void setMaxInvestAmount(long maxInvestAmount) {
        this.maxInvestAmount = maxInvestAmount;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

}
