package com.tuotiansudai.service;

import com.tuotiansudai.repository.model.ContractType;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface ContractService {
    String getContract(String code, Map<String, Object> dataModel);

    String generateInvestorContract(long loanId,ContractType contractType);

    void generateContractPdf(String pdfString,HttpServletResponse response);
}
