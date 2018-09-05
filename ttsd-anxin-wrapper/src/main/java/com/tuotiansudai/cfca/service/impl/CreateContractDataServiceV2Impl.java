package com.tuotiansudai.cfca.service.impl;

import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qduljs2011 on 2018/9/5.
 */
public class CreateContractDataServiceV2Impl extends AbstractCreateContractDataService {
    @Value(value = "${anxin.loan.contract.template.v2}")
    private String supportTemp;

    @Autowired
    private UserMapper userMapper;

    @Override
    public String getSupportContractVersion() {
        return supportTemp;
    }

    @Override
    protected Map<String, String> getLoanDataMap(String investorLoginName, long loanId, long investId) {
        Map<String, String> dataModel = new HashMap<>();
        LoanModel loanModel = loanMapper.findById(loanId);
        UserModel agentModel = userMapper.findByLoginName(loanModel.getAgentLoginName());
        UserModel investorModel = userMapper.findByLoginName(investorLoginName);
        InvestModel investModel = investMapper.findById(investId);
        //
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
        dataModel.put("investorIdentityNumber", investorModel.getIdentityNumber());
        dataModel.put("loanerIdentityNumber", agentModel.getIdentityNumber());
        dataModel.put("loanName", loanModel.getName());
        String amountUpper = AmountConverter.getRMBStr(investModel.getAmount());
        amountUpper = amountUpper.endsWith("元") ? amountUpper.replace("元", "") : amountUpper;
        dataModel.put("amountUpper", amountUpper);
        dataModel.put("amount", AmountConverter.convertCentToString(investModel.getAmount()));
        dataModel.put("totalRate", decimalFormat.format((loanModel.getBaseRate() + loanModel.getActivityRate()) * 100));
        //根据标的类型判断借款开始时间
        if (LoanType.INVEST_INTEREST_MONTHLY_REPAY.equals(loanModel.getType()) || LoanType.INVEST_INTEREST_LUMP_SUM_REPAY.equals(loanModel.getType())) {
            DateTime recheckTimeYear = new DateTime(investModel.getCreatedTime());
            dataModel.put("recheckTimeYear", String.valueOf(recheckTimeYear.getYear()));
            dataModel.put("recheckTimeMonth", String.valueOf(recheckTimeYear.getMonthOfYear()));
            dataModel.put("recheckTimeDay", String.valueOf(recheckTimeYear.getDayOfMonth()));
        } else {
            DateTime fullTimeDate = new DateTime(loanModel.getRecheckTime());
            dataModel.put("recheckTimeYear", String.valueOf(fullTimeDate.getYear()));
            dataModel.put("recheckTimeMonth", String.valueOf(fullTimeDate.getMonthOfYear()));
            dataModel.put("recheckTimeDay", String.valueOf(fullTimeDate.getDayOfMonth()));
        }
        DateTime endTimeDate = new DateTime(loanModel.getDeadline());
        dataModel.put("endTimeYear", String.valueOf(endTimeDate.getYear()));
        dataModel.put("endTimeMonth", String.valueOf(endTimeDate.getMonthOfYear()));
        dataModel.put("endTimeDay", String.valueOf(endTimeDate.getDayOfMonth()));
        dataModel.put("periods", loanModel.getPeriods() + "");
        dataModel.put("loanType", loanModel.getType().getName());
        dataModel.put("investorName", investorModel.getUserName());
        dataModel.put("orderId", String.valueOf(investId));
        return dataModel;
    }
}
