package com.tuotiansudai.dto;


import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.LoanRiskManagementTitleRelationModel;

import java.util.List;

public class LoanApplicationUpdateDto {

    private long id;
    private String address;
    private String loanUsage;
    private List<LoanRiskManagementTitleRelationModel> relationModels = Lists.newArrayList();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLoanUsage() {
        return loanUsage;
    }

    public void setLoanUsage(String loanUsage) {
        this.loanUsage = loanUsage;
    }

    public List<LoanRiskManagementTitleRelationModel> getRelationModels() {
        return relationModels;
    }

    public void setRelationModels(List<LoanRiskManagementTitleRelationModel> relationModels) {
        this.relationModels = relationModels;
    }
}
