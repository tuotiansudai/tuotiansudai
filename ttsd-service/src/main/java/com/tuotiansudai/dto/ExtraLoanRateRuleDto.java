package com.tuotiansudai.dto;


import com.tuotiansudai.repository.model.ExtraLoanRateRuleModel;

import java.util.List;

public class ExtraLoanRateRuleDto extends BaseDataDto {

    private List<ExtraLoanRateRuleModel> extraLoanRateRuleModels;

    public List<ExtraLoanRateRuleModel> getExtraLoanRateRuleModels() {
        return extraLoanRateRuleModels;
    }

    public void setExtraLoanRateRuleModels(List<ExtraLoanRateRuleModel> extraLoanRateRuleModels) {
        this.extraLoanRateRuleModels = extraLoanRateRuleModels;
    }

}
