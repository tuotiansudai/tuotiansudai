package com.tuotiansudai.api.dto;

import com.tuotiansudai.repository.model.ReferrerManageView;
import com.tuotiansudai.util.AmountConverter;

import java.text.SimpleDateFormat;

public class ReferrerInvestResponseDataDto {
    private String userId;
    private String level;
    private String loanId;
    private String loanName;
    private String investMoney;
    private String deadline;
    private String investTime;
    private String rewardMoney;
    private String rewardTime;
    private String loanType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getInvestMoney() {
        return investMoney;
    }

    public void setInvestMoney(String investMoney) {
        this.investMoney = investMoney;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getInvestTime() {
        return investTime;
    }

    public void setInvestTime(String investTime) {
        this.investTime = investTime;
    }

    public String getRewardMoney() {
        return rewardMoney;
    }

    public void setRewardMoney(String rewardMoney) {
        this.rewardMoney = rewardMoney;
    }

    public String getRewardTime() {
        return rewardTime;
    }

    public void setRewardTime(String rewardTime) {
        this.rewardTime = rewardTime;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public ReferrerInvestResponseDataDto() {

    }

    public ReferrerInvestResponseDataDto(ReferrerManageView input) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.userId = input.getInvestName();
        this.level = "" + input.getLevel();
        this.loanName = input.getLoanName();
        this.investMoney = AmountConverter.convertCentToString(input.getInvestAmount());
        this.deadline = "" + input.getPeriods();
        this.investTime = simpleDateFormat.format(input.getInvestTime());
        this.rewardMoney = AmountConverter.convertCentToString(input.getRewardAmount());
        this.rewardTime = simpleDateFormat.format(input.getRewardTime());
        this.loanId = input.getLoanId();
        this.loanType = input.getProductType() != null ? input.getProductType().name() : "";
    }

}
