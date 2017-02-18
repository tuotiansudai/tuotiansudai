package com.tuotiansudai.cfca.service;

import com.tuotiansudai.repository.model.AnxinContractType;

import java.util.Map;

public interface ContractService {
    String getContract(String code, Map<String, String> dataModel);

    String generateInvestorContract(String loginName, long loanId, long investId);

    String generateTransferContract(long transferApplicationId);

    Map<String, String> collectTransferContractModel(long transferApplicationId);

    Map<String, String> collectInvestorContractModel(String investorLoginName, long loanId, long investId);

    byte[] printContractPdf(AnxinContractType anxinContractType, String loginName, long OrderId, Long investId);
}
