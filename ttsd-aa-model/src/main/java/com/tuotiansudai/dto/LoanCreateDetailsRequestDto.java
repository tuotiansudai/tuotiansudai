package com.tuotiansudai.dto;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.LoanDetailsModel;
import com.tuotiansudai.repository.model.Source;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

public class LoanCreateDetailsRequestDto {

    @NotEmpty
    private String declaration;

    private boolean activity;

    private String activityDesc;

    private List<Source> extraSource = Lists.newArrayList();

    private List<Long> extraRateRuleIds = Lists.newArrayList();

    private boolean nonTransferable;

    private boolean nonUseCoupon;

    private String pushMessage;

    public LoanCreateDetailsRequestDto() {
    }

    public LoanCreateDetailsRequestDto(LoanDetailsModel loanDetailsModel) {
        this.declaration = loanDetailsModel.getDeclaration();
        this.activity = loanDetailsModel.isActivity();
        this.activityDesc = loanDetailsModel.getActivityDesc();
        this.extraSource = loanDetailsModel.getExtraSource();
        this.nonTransferable = loanDetailsModel.getNonTransferable();
        this.pushMessage = loanDetailsModel.getPushMessage();
        this.nonUseCoupon = loanDetailsModel.getNonUseCoupon();
    }

    public boolean getNonTransferable() {
        return nonTransferable;
    }

    public void setNonTransferable(boolean nonTransferable) {
        this.nonTransferable = nonTransferable;
    }

    public String getDeclaration() {
        return declaration;
    }

    public void setDeclaration(String declaration) {
        this.declaration = declaration;
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

    public List<Source> getExtraSource() {
        return extraSource;
    }

    public void setExtraSource(List<Source> extraSource) {
        this.extraSource = extraSource;
    }

    public List<Long> getExtraRateRuleIds() {
        return extraRateRuleIds;
    }

    public void setExtraRateRuleIds(List<Long> extraRateRuleIds) {
        this.extraRateRuleIds = extraRateRuleIds;
    }

    public String getPushMessage() {
        return pushMessage;
    }

    public void setPushMessage(String pushMessage) {
        this.pushMessage = pushMessage;
    }

    public boolean getNonUseCoupon() {
        return nonUseCoupon;
    }

    public void setNonUseCoupon(boolean nonUseCoupon) {
        this.nonUseCoupon = nonUseCoupon;
    }
}
