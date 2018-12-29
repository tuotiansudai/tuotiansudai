package com.tuotiansudai.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LoanRiskManagementTitleRelationCreateDto implements Serializable {
    private Long loanApplicationId;
    private Long titleId;
    private String detail;

    public LoanRiskManagementTitleRelationCreateDto() {
    }

    public LoanRiskManagementTitleRelationCreateDto(Long loanApplicationId, Long titleId, String detail) {
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
