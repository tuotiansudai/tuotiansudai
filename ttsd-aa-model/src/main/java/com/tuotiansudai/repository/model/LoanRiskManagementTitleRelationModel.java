package com.tuotiansudai.repository.model;

import java.io.Serializable;

public class LoanRiskManagementTitleRelationModel implements Serializable {
    private long id;
    private Long loanId;
    private Long loanApplicationId;
    private Long titleId;
    private String detail;

    public LoanRiskManagementTitleRelationModel() {
    }

    public LoanRiskManagementTitleRelationModel(long id, Long loanId, Long loanApplicationId, Long titleId, String detail) {
        this.id = id;
        this.loanId = loanId;
        this.loanApplicationId = loanApplicationId;
        this.titleId = titleId;
        this.detail = detail;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
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
