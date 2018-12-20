package com.tuotiansudai.repository.model;

import java.io.Serializable;

public class LoanRiskManagementTitleModel implements Serializable {
    private long id;
    private String title;

    public LoanRiskManagementTitleModel() {
    }

    public LoanRiskManagementTitleModel(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
