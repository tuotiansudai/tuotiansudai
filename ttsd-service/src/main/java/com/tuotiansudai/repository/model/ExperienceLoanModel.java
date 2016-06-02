package com.tuotiansudai.repository.model;

import java.io.Serializable;

public class ExperienceLoanModel implements Serializable {
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

    /***
     * 项目描述（纯文本）
     ***/
    private String descriptionText;

    /***
     * 可投金额
     ***/
    private long investAmount;

    /***
     * 基本利率
     ***/
    private double baseRate;

    /**
     * 进度
     */
    private long Progress;

    public ExperienceLoanModel(LoanModel loanModel) {
        this.id = loanModel.getId();
        this.name = loanModel.getName();
        this.duration = loanModel.getDuration();
        this.descriptionText = loanModel.getDescriptionText();
        this.investAmount = loanModel.getLoanAmount();
        this.baseRate = loanModel.getBaseRate();
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

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public double getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(double baseRate) {
        this.baseRate = baseRate;
    }

    public long getProgress() {
        return Progress;
    }

    public void setProgress(long progress) {
        Progress = progress;
    }
}
