package com.tuotiansudai.repository.model;

public class LoanTitleRelationModel {
    private long id;
    private long loanId;/***借款标的***/
    private long titleId;/***申请材料标题***/
    private String applyMetarialUrl;

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

    public long getTitleId() {
        return titleId;
    }

    public void setTitleId(long titleId) {
        this.titleId = titleId;
    }

    /***申请材料存放路径***/


    public String getApplyMetarialUrl() {
        return applyMetarialUrl;
    }

    public void setApplyMetarialUrl(String applyMetarialUrl) {
        this.applyMetarialUrl = applyMetarialUrl;
    }
}
