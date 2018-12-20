package com.tuotiansudai.cfca.service.impl;

import com.tuotiansudai.cfca.contract.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qduljs2011 on 2018/9/5.
 */
@Service
public class CreateContractDataServiceV1Impl extends AbstractCreateContractDataService {

    @Value(value = "${anxin.loan.contract.template.v1}")
    private String anxinLoanContractTemplateV1;

    @Autowired
    private ContractService contractService;

    @Override
    public String getSupportContractVersion() {
        return anxinLoanContractTemplateV1;
    }

    @Override
    protected Map<String, String> getLoanDataMap(String investorLoginName, long loanId, long investId) {
        Map<String, String> dataModel = new HashMap<>();
        Map<String, String> investMap = contractService.collectInvestorContractModel(investorLoginName, loanId, investId);
        dataModel.put("agentMobile", investMap.get("agentMobile"));
        dataModel.put("agentIdentityNumber", investMap.get("agentIdentityNumber"));
        dataModel.put("investorMobile", investMap.get("investorMobile"));
        dataModel.put("investorIdentityNumber", investMap.get("investorIdentityNumber"));
        dataModel.put("loanerUserName", investMap.get("loanerUserName"));
        dataModel.put("loanerIdentityNumber", investMap.get("loanerIdentityNumber"));
        dataModel.put("loanAmount1", investMap.get("loanAmount"));
        dataModel.put("loanAmount2", investMap.get("investAmount"));
        dataModel.put("periods1", investMap.get("agentPeriods"));
        dataModel.put("periods2", investMap.get("leftPeriods"));
        dataModel.put("totalRate", investMap.get("totalRate"));
        dataModel.put("recheckTime1", investMap.get("recheckTime"));
        dataModel.put("recheckTime2", investMap.get("recheckTime"));
        dataModel.put("endTime1", investMap.get("endTime"));
        dataModel.put("endTime2", investMap.get("endTime"));
        dataModel.put("orderId", String.valueOf(investId));
        dataModel.put("pledge", investMap.get("pledge"));
        dataModel.put("purpose", investMap.get("purpose"));
        dataModel.put("repayType", investMap.get("repayType"));
        return dataModel;
    }
}
