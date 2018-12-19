package com.tuotiansudai.dto;


import com.tuotiansudai.repository.model.*;

import java.util.List;

public class LoanApplicationConsumeDto {

    private LoanApplicationModel loanApplicationModel;

    private LoanCreateBaseRequestDto loan;

    private LoanCreateDetailsRequestDto loanDetails;

    private LoanCreateLoanerDetailsRequestDto loanerDetails;

    private LoanApplicationMaterialsModel loanApplicationMaterialsModel;

    private List<ExtraLoanRateModel> extraLoanRateModelList;

    private List<LoanRiskManagementTitleRelationModel> loanRiskManagementTitleRelationModelList;

    private List<LoanRiskManagementTitleModel> loanRiskManagementTitleModelList;

    public LoanApplicationModel getLoanApplicationModel() {
        return loanApplicationModel;
    }

    public void setLoanApplicationModel(LoanApplicationModel loanApplicationModel) {
        this.loanApplicationModel = loanApplicationModel;
    }

    public LoanCreateBaseRequestDto getLoan() {
        return loan;
    }

    public void setLoan(LoanCreateBaseRequestDto loan) {
        this.loan = loan;
    }

    public LoanCreateDetailsRequestDto getLoanDetails() {
        return loanDetails;
    }

    public void setLoanDetails(LoanCreateDetailsRequestDto loanDetails) {
        this.loanDetails = loanDetails;
    }

    public LoanCreateLoanerDetailsRequestDto getLoanerDetails() {
        return loanerDetails;
    }

    public void setLoanerDetails(LoanCreateLoanerDetailsRequestDto loanerDetails) {
        this.loanerDetails = loanerDetails;
    }

    public LoanApplicationMaterialsModel getLoanApplicationMaterialsModel() {
        return loanApplicationMaterialsModel;
    }

    public void setLoanApplicationMaterialsModel(LoanApplicationMaterialsModel loanApplicationMaterialsModel) {
        this.loanApplicationMaterialsModel = loanApplicationMaterialsModel;
    }

    public List<ExtraLoanRateModel> getExtraLoanRateModelList() {
        return extraLoanRateModelList;
    }

    public void setExtraLoanRateModelList(List<ExtraLoanRateModel> extraLoanRateModelList) {
        this.extraLoanRateModelList = extraLoanRateModelList;
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
