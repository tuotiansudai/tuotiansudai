package com.tuotiansudai.service;

import java.io.OutputStream;
import java.util.Map;

public interface ContractService {
    String getContract(String code, Map<String, Object> dataModel);

    String generateInvestorContract(String loginName, long loanId, long investId);

    void generateContractPdf(String pdfString, OutputStream outputStream);

    String generateTransferContract(long transferApplicationId);
}
