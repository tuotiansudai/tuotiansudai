package com.tuotiansudai.dto;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.enums.Source;
import com.tuotiansudai.repository.model.LoanDetailsModel;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

public class LoanCreateDetailsRequestDto {

    @NotEmpty
    private String declaration;

    private boolean activity;

    private String activityDesc;

    private List<Source> extraSource = Lists.newArrayList();

    private List<Long> extraRateRuleIds = Lists.newArrayList();

    public LoanCreateDetailsRequestDto() {
    }

    public LoanCreateDetailsRequestDto(LoanDetailsModel loanDetailsModel) {
        this.declaration = loanDetailsModel.getDeclaration();
        this.activity = loanDetailsModel.isActivity();
        this.activityDesc = loanDetailsModel.getActivityDesc();
        if (!Strings.isNullOrEmpty(loanDetailsModel.getExtraSource())) {
            this.extraSource = Lists.transform(Lists.newArrayList(loanDetailsModel.getExtraSource().split(",")), Source::valueOf);
        }
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
}
