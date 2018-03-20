package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.LoanCreateDetailsRequestDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class LoanDetailsModel implements Serializable {
    private long id;
    private long loanId;
    private String declaration;
    private List<Source> extraSource;
    private boolean activity;
    private String activityDesc;
    private boolean nonTransferable;
    private boolean disableCoupon;
    private String pushMessage;
    private boolean disableReward;
    private double rewardRate;

    public LoanDetailsModel() {
    }

    public LoanDetailsModel(long loanId, String declaration, List<Source> extraSource, boolean activity, String activityDesc) {
        this.loanId = loanId;
        this.declaration = declaration;
        this.extraSource = extraSource;
        this.activity = activity;
        this.activityDesc = activityDesc;
    }

    public LoanDetailsModel(long loanId, LoanCreateDetailsRequestDto loanDetails) {
        this.loanId = loanId;
        this.declaration = loanDetails.getDeclaration();
        this.extraSource = loanDetails.getExtraSource();
        this.activity = loanDetails.isActivity();
        this.activityDesc = loanDetails.isActivity() ? loanDetails.getActivityDesc() : null;
        this.nonTransferable = loanDetails.getNonTransferable();
        this.disableCoupon = loanDetails.getDisableCoupon();
        this.pushMessage = loanDetails.getPushMessage();
        this.disableReward = loanDetails.getDisableReward();
        this.rewardRate = Double.parseDouble(rateStrDivideOneHundred(loanDetails.getRewardRate()));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public String getDeclaration() {
        return declaration;
    }

    public void setDeclaration(String declaration) {
        this.declaration = declaration;
    }

    public List<Source> getExtraSource() {
        return extraSource;
    }

    public void setExtraSource(List<Source> extraSource) {
        this.extraSource = extraSource;
    }

    public boolean isActivity() {
        return activity;
    }

    public void setActivity(boolean activity) {
        this.activity = activity;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public boolean getNonTransferable() {
        return nonTransferable;
    }

    public void setNonTransferable(boolean nonTransferable) {
        this.nonTransferable = nonTransferable;
    }

    public String getPushMessage() {
        return pushMessage;
    }

    public void setPushMessage(String pushMessage) {
        this.pushMessage = pushMessage;
    }

    public boolean getDisableCoupon() {
        return disableCoupon;
    }

    public void setDisableCoupon(boolean disableCoupon) {
        this.disableCoupon = disableCoupon;
    }

    public boolean getDisableReward() {
        return disableReward;
    }

    public void setDisableReward(boolean disableReward) {
        this.disableReward = disableReward;
    }

    public double getRewardRate() {
        return rewardRate;
    }

    public void setRewardRate(double rewardRate) {
        this.rewardRate = rewardRate;
    }

    private String rateStrDivideOneHundred(String rate) {
        BigDecimal rateBigDecimal = new BigDecimal(rate);
        return String.valueOf(rateBigDecimal.divide(new BigDecimal(100), 4, BigDecimal.ROUND_DOWN).doubleValue());
    }
}
