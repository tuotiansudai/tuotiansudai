package com.tuotiansudai.repository.model;

/**
 * Created by tuotian on 15/8/17.
 */
public class LoanTitle {
    private String id;
    private String loanId;/***借款标的***/
    private String titleId;/***申请材料标题***/
    private String applyMetarialUrl;/***申请材料存放路径***/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getApplyMetarialUrl() {
        return applyMetarialUrl;
    }

    public void setApplyMetarialUrl(String applyMetarialUrl) {
        this.applyMetarialUrl = applyMetarialUrl;
    }
}
