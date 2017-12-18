package com.tuotiansudai.repository.model;

import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;
import java.math.BigDecimal;

public class ExperienceLoanDto implements Serializable {
    /***
     * 标的号
     ***/
    private long id;
    /***
     * 借款项目名称
     ***/
    private String name;
    /**
     * 借款天数
     */
    private int duration;

    /**
     * 项目金额
     */
    private String loanAmount;

    /***
     * 可投金额
     ***/
    private String investAmount;

    /***
     * 起投金额
     ***/
    private long minInvestAmount;

    /***
     * 基本利率
     ***/
    private double baseRate;

    /**
     * 进度
     */
    private long progress;

    private LoanStatus loanStatus;

    public ExperienceLoanDto(LoanModel loanModel, long experienceProgress, long investAmount) {
        this.id = loanModel.getId();
        this.name = loanModel.getName();
        this.duration = loanModel.getDuration();
        this.baseRate = new BigDecimal(String.valueOf(loanModel.getBaseRate())).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        this.progress = experienceProgress;
        this.investAmount = AmountConverter.convertCentToString(loanModel.getLoanAmount() - investAmount);
        this.loanAmount = AmountConverter.convertCentToString(loanModel.getLoanAmount());
        this.minInvestAmount = loanModel.getMinInvestAmount();
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public double getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(double baseRate) {
        this.baseRate = baseRate;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public LoanStatus getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public long getMinInvestAmount() {
        return minInvestAmount;
    }

    public void setMinInvestAmount(long minInvestAmount) {
        this.minInvestAmount = minInvestAmount;
    }

}
