package com.tuotiansudai.service;

import com.tuotiansudai.repository.model.ContractType;

import java.io.OutputStream;
import java.util.Map;

public interface ContractService {
    String getContract(String code, Map<String, Object> dataModel);

    String generateInvestorContract(String loginName,long loanId,ContractType contractType);

    void generateContractPdf(String pdfString, OutputStream outputStream);

    String generateTransferContract(long transferApplicationId);

    String generateTransferAgreement();
}
