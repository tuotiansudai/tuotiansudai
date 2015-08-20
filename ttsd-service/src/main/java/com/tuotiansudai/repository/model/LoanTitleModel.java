package com.tuotiansudai.repository.model;

import java.math.BigInteger;

public class LoanTitleModel {
    private BigInteger id;
    private BigInteger loanId;/***借款标的***/
    private BigInteger titleId;/***申请材料标题***/
    private String applyMetarialUrl;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getLoanId() {
        return loanId;
    }

    public void setLoanId(BigInteger loanId) {
        this.loanId = loanId;
    }

    public BigInteger getTitleId() {
        return titleId;
    }

    public void setTitleId(BigInteger titleId) {
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
