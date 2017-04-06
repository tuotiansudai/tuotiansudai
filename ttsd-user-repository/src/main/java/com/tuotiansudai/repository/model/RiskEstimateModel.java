package com.tuotiansudai.repository.model;

import com.tuotiansudai.enums.riskestimation.*;

import java.util.Date;
import java.util.List;

public class RiskEstimateModel {
    private long id;

    private String loginName;

    private Age age;

    private Income income;

    private Investment investment;

    private Experience experience;

    private Attitude attitude;

    private Duration duration;

    private Loss loss;

    private Rate rate;

    private Estimate estimate;

    private Date createdTime;

    private Date updatedTime;

    public RiskEstimateModel() {
    }

    public RiskEstimateModel(String loginName, List<Integer> answers) {
        this.loginName = loginName;
        this.age = Age.values()[answers.get(0)];
        this.income = Income.values()[answers.get(1)];
        this.investment = Investment.values()[answers.get(2)];
        this.experience = Experience.values()[answers.get(3)];
        this.attitude = Attitude.values()[answers.get(4)];
        this.duration = Duration.values()[answers.get(5)];
        this.loss = Loss.values()[answers.get(6)];
        this.rate = Rate.values()[answers.get(7)];
        this.estimate = Estimate.estimate(age.getPoints() + income.getPoints() + investment.getPoints() + experience.getPoints() + attitude.getPoints() + duration.getPoints() + loss.getPoints() + rate.getPoints());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Age getAge() {
        return age;
    }

    public void setAge(Age age) {
        this.age = age;
    }

    public Income getIncome() {
        return income;
    }

    public void setIncome(Income income) {
        this.income = income;
    }

    public Investment getInvestment() {
        return investment;
    }

    public void setInvestment(Investment investment) {
        this.investment = investment;
    }

    public Experience getExperience() {
        return experience;
    }

    public void setExperience(Experience experience) {
        this.experience = experience;
    }

    public Attitude getAttitude() {
        return attitude;
    }

    public void setAttitude(Attitude attitude) {
        this.attitude = attitude;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Loss getLoss() {
        return loss;
    }

    public void setLoss(Loss loss) {
        this.loss = loss;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public Estimate getEstimate() {
        return estimate;
    }

    public void setEstimate(Estimate estimate) {
        this.estimate = estimate;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}
