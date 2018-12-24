package com.tuotiansudai.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LoanRiskManagementTitleRelationCreateDto implements Serializable {
    private Long loanApplicationId;
    private Long titleId;
    private List<String> detail = new ArrayList<>();

    public LoanRiskManagementTitleRelationCreateDto() {
    }

    public LoanRiskManagementTitleRelationCreateDto(Long loanApplicationId, Long titleId, List<String> detail) {
        this.loanApplicationId = loanApplicationId;
        this.titleId = titleId;
        this.detail = detail;
    }

    public Long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(Long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public Long getTitleId() {
        return titleId;
    }

    public void setTitleId(Long titleId) {
        this.titleId = titleId;
    }

    public List<String> getDetail() {
        return detail;
    }

    public void setDetail(List<String> detail) {
        this.detail = detail;
    }
}
