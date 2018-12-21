package com.tuotiansudai.dto;


import com.tuotiansudai.repository.model.*;

import java.util.List;

public class LoanApplicationConsumeDetailDto {

    private LoanApplicationModel loanApplicationModel;

    private LoanApplicationMaterialsModel loanApplicationMaterialsModel;

    private List<LoanRiskManagementTitleRelationModel> loanRiskManagementTitleRelationModelList;

    private List<LoanRiskManagementTitleModel> loanRiskManagementTitleModelList;

    public LoanApplicationModel getLoanApplicationModel() {
        return loanApplicationModel;
    }

    public void setLoanApplicationModel(LoanApplicationModel loanApplicationModel) {
        this.loanApplicationModel = loanApplicationModel;
    }

    public LoanApplicationMaterialsModel getLoanApplicationMaterialsModel() {
        return loanApplicationMaterialsModel;
    }

    public void setLoanApplicationMaterialsModel(LoanApplicationMaterialsModel loanApplicationMaterialsModel) {
        this.loanApplicationMaterialsModel = loanApplicationMaterialsModel;
    }

    public List<LoanRiskManagementTitleRelationModel> getLoanRiskManagementTitleRelationModelList() {
        return loanRiskManagementTitleRelationModelList;
    }

    public void setLoanRiskManagementTitleRelationModelList(List<LoanRiskManagementTitleRelationModel> loanRiskManagementTitleRelationModelList) {
        this.loanRiskManagementTitleRelationModelList = loanRiskManagementTitleRelationModelList;
    }

    public List<LoanRiskManagementTitleModel> getLoanRiskManagementTitleModelList() {
        return loanRiskManagementTitleModelList;
    }

    public void setLoanRiskManagementTitleModelList(List<LoanRiskManagementTitleModel> loanRiskManagementTitleModelList) {
        this.loanRiskManagementTitleModelList = loanRiskManagementTitleModelList;
    }
}
