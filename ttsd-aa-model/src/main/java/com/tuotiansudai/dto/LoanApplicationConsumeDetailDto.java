package com.tuotiansudai.dto;


import com.tuotiansudai.repository.model.*;

import java.util.List;
import java.util.Map;

public class LoanApplicationConsumeDetailDto {

    private LoanApplicationModel loanApplicationModel;

    private Map<String, List<String>> materialsList;

    public LoanApplicationModel getLoanApplicationModel() {
        return loanApplicationModel;
    }

    public void setLoanApplicationModel(LoanApplicationModel loanApplicationModel) {
        this.loanApplicationModel = loanApplicationModel;
    }

    public Map<String, List<String>> getMaterialsList() {
        return materialsList;
    }

    public void setMaterialsList(Map<String, List<String>> materialsList) {
        this.materialsList = materialsList;
    }
}
