package com.tuotiansudai.transfer.repository.model;

import java.io.Serializable;
import java.util.Date;

public class TransferRuleModel implements Serializable {

    private long id;

    private double levelOneFee;

    private int levelOneLower;

    private int levelOneUpper;

    private double levelTwoFee;

    private int levelTwoLower;

    private int levelTwoUpper;

    private double levelThreeFee;

    private int levelThreeLower;

    private int levelThreeUpper;

    private double discount;

    private int daysLimit;

    private String updatedBy;

    private Date updatedTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLevelOneFee() {
        return levelOneFee;
    }

    public void setLevelOneFee(double levelOneFee) {
        this.levelOneFee = levelOneFee;
    }

    public int getLevelOneLower() {
        return levelOneLower;
    }

    public void setLevelOneLower(int levelOneLower) {
        this.levelOneLower = levelOneLower;
    }

    public int getLevelOneUpper() {
        return levelOneUpper;
    }

    public void setLevelOneUpper(int levelOneUpper) {
        this.levelOneUpper = levelOneUpper;
    }

    public double getLevelTwoFee() {
        return levelTwoFee;
    }

    public void setLevelTwoFee(double levelTwoFee) {
        this.levelTwoFee = levelTwoFee;
    }

    public int getLevelTwoLower() {
        return levelTwoLower;
    }

    public void setLevelTwoLower(int levelTwoLower) {
        this.levelTwoLower = levelTwoLower;
    }

    public int getLevelTwoUpper() {
        return levelTwoUpper;
    }

    public void setLevelTwoUpper(int levelTwoUpper) {
        this.levelTwoUpper = levelTwoUpper;
    }

    public double getLevelThreeFee() {
        return levelThreeFee;
    }

    public void setLevelThreeFee(double levelThreeFee) {
        this.levelThreeFee = levelThreeFee;
    }

    public int getLevelThreeLower() {
        return levelThreeLower;
    }

    public void setLevelThreeLower(int levelThreeLower) {
        this.levelThreeLower = levelThreeLower;
    }

    public int getLevelThreeUpper() {
        return levelThreeUpper;
    }

    public void setLevelThreeUpper(int levelThreeUpper) {
        this.levelThreeUpper = levelThreeUpper;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getDaysLimit() {
        return daysLimit;
    }

    public void setDaysLimit(int daysLimit) {
        this.daysLimit = daysLimit;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}
