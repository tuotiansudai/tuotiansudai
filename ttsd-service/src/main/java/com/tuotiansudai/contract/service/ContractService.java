package com.tuotiansudai.contract.service;

import java.io.OutputStream;
import java.util.Map;

public interface ContractService {
    String getContract(String code, Map<String, String> dataModel);

    String generateInvestorContract(String loginName, long loanId, long investId);

    void generateContractPdf(String pdfString, OutputStream outputStream);

    String generateTransferContract(long transferApplicationId);

    Map<String, String> collectTransferContractModel(long transferApplicationId);

    Map<String, String> collectInvestorContractModel(String investorLoginName, long loanId, long investId);
}
