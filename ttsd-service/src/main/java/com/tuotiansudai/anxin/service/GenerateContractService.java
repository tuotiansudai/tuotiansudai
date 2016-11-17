package com.tuotiansudai.anxin.service;


import java.util.Map;

public interface GenerateContractService {

    Map<String, String> collectTransferContractModel(long transferApplicationId);

    Map<String, String> collectInvestorContractModel(String investorLoginName, long loanId, long investId);
}
