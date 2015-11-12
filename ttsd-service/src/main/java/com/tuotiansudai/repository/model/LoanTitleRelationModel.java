package com.tuotiansudai.repository.model;

import java.io.Serializable;

public class LoanTitleRelationModel implements Serializable {

    private long id;

    private long loanId;/***借款标的***/

    private long titleId;/***申请材料标题***/

    private String applicationMaterialUrls;
    
    private String title;
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
    public String getApplicationMaterialUrls() {
        return applicationMaterialUrls;
    }

    public void setApplicationMaterialUrls(String applicationMaterialUrls) {
        this.applicationMaterialUrls = applicationMaterialUrls;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
