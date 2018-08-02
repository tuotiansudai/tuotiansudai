package com.tuotiansudai.cfca.contract;

import com.tuotiansudai.repository.model.AnxinContractType;

import java.util.Map;

public interface ContractService {
    String getContract(String code, Map<String, String> dataModel);

    String generateTransferContract(long transferApplicationId);

    Map<String, String> collectTransferContractModel(long transferApplicationId);

    Map<String, String> collectInvestorContractModel(String investorLoginName, long loanId, long investId,String fullTime);

    byte[] printContractPdf(AnxinContractType anxinContractType, String loginName, long OrderId, Long investId);
}
