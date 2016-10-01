package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Source;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

public class LoanCreateDetailsRequestDto {

    @NotEmpty
    private String declaration;

    private boolean activity;

    private String activityDesc;

    private List<Source> extraSource;

    private List<Long> extraRateIds;

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

    public List<Long> getExtraRateIds() {
        return extraRateIds;
    }

    public void setExtraRateIds(List<Long> extraRateIds) {
        this.extraRateIds = extraRateIds;
    }
}
