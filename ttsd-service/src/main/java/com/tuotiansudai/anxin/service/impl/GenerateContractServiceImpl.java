package com.tuotiansudai.anxin.service.impl;


import com.tuotiansudai.anxin.service.GenerateContractService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.mapper.TransferRuleMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.transfer.repository.model.TransferRuleModel;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class GenerateContractServiceImpl implements GenerateContractService {

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoanerDetailsMapper loanerDetailsMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private TransferRuleMapper transferRuleMapper;

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    public Map<String, String> collectTransferContractModel(long transferApplicationId) {
        Map<String, String> dataModel = new HashMap<>();

        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(transferApplicationId);
        if (null == transferApplicationModel) {
            return dataModel;
        }

        LoanModel loanModel = loanMapper.findById(transferApplicationModel.getLoanId());
        if (loanModel == null) {
            return dataModel;
        }

        UserModel transferUserModel = userMapper.findByLoginName(transferApplicationModel.getLoginName());
        dataModel.put("transferUserName", transferUserModel.getUserName());
        dataModel.put("transferMobile", transferUserModel.getMobile());
        dataModel.put("transferIdentityNumber", transferUserModel.getIdentityNumber());

        InvestModel investModel = investMapper.findById(transferApplicationModel.getInvestId());
        UserModel investUserModel = userMapper.findByLoginName(investModel.getLoginName());
        dataModel.put("transfereeUserName", investUserModel.getUserName());
        dataModel.put("transfereeMobile", investUserModel.getMobile());
        dataModel.put("transfereeIdentityNumber", investUserModel.getIdentityNumber());

        dataModel.put("loanerUserName", loanerDetailsMapper.getByLoanId(loanModel.getId()).getUserName());
        dataModel.put("loanerIdentityNumber", loanModel.getLoanerIdentityNumber());
        dataModel.put("loanAmount", AmountConverter.convertCentToString(loanModel.getLoanAmount()) + "元");
        dataModel.put("totalRate", String.valueOf((loanModel.getBaseRate() + loanModel.getActivityRate() * 100)) + "%");
        dataModel.put("periods", String.valueOf(loanModel.getPeriods() * 30) + "天");

        if (transferApplicationModel.getPeriod() != 1) {
            InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(transferApplicationModel.getTransferInvestId(), transferApplicationModel.getPeriod() - 1);
            dataModel.put("transferStartTime", simpleDateFormat.format(new LocalDate(investRepayModel.getRepayDate()).plusDays(1).toDate()));
        } else {
            if (loanModel.getType().equals(LoanType.INVEST_INTEREST_LUMP_SUM_REPAY) || loanModel.getType().equals(LoanType.INVEST_INTEREST_MONTHLY_REPAY)) {
                dataModel.put("transferStartTime", simpleDateFormat.format(investModel.getInvestTime()));
            } else if (loanModel.getType().equals(LoanType.LOAN_INTEREST_MONTHLY_REPAY) || loanModel.getType().equals(LoanType.LOAN_INTEREST_LUMP_SUM_REPAY)) {
                dataModel.put("transferStartTime", simpleDateFormat.format(loanModel.getRecheckTime()));
            }
        }

        InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(investModel.getId(), loanModel.getPeriods());
        dataModel.put("transferEndTime", simpleDateFormat.format(investRepayModel.getRepayDate()));

        dataModel.put("investAmount", AmountConverter.convertCentToString(transferApplicationModel.getInvestAmount()) + "元");
        dataModel.put("transferTime", simpleDateFormat.format(transferApplicationModel.getTransferTime()));
        dataModel.put("leftPeriod", String.valueOf(transferApplicationModel.getLeftPeriod()));

        TransferRuleModel transferRuleModel = transferRuleMapper.find();
        String msg1;
        String msg2;
        String msg3;
        if (transferRuleModel.getLevelOneFee() != 0) {
            msg1 = MessageFormat.format("甲方持有债权30天以内的，收取转让本金的{0}%作为服务费用。", transferRuleModel.getLevelOneFee());
        } else {
            msg1 = "甲方持有债权30天以内的，暂不收取转服务费用。";
        }

        if (transferRuleModel.getLevelTwoFee() != 0) {
            msg2 = MessageFormat.format("甲方持有债权30天以上，90天以内的，收取转让本金的{0}%作为服务费用。", transferRuleModel.getLevelOneFee());
        } else {
            msg2 = "甲方持有债权30天以上，90天以内的，暂不收取转服务费用。";
        }

        if (transferRuleModel.getLevelThreeFee() != 0) {
            msg3 = MessageFormat.format("甲方持有债权90天以上的，收取转让本金的{0}%作为服务费用。", transferRuleModel.getLevelOneFee());
        } else {
            msg3 = "甲方持有债权90天以上的，暂不收取转服务费用。";
        }
        dataModel.put("msg1", msg1);
        dataModel.put("msg2", msg2);
        dataModel.put("msg3", msg3);

        return dataModel;
    }

    @Override
    public Map<String, String> collectInvestorContractModel(String investorLoginName, long loanId, long investId) {
        Map<String, String> dataModel = new HashMap<>();
        LoanModel loanModel = loanMapper.findById(loanId);
        UserModel agentModel = userMapper.findByLoginName(loanModel.getAgentLoginName());
        UserModel investorModel = userMapper.findByLoginName(investorLoginName);
        InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(investId, loanModel.getPeriods());
        LoanerDetailsModel loanerDetailsModel = loanerDetailsMapper.getByLoanId(loanId);
        InvestModel investModel = investMapper.findById(investId);
        dataModel.put("agentMobile", agentModel.getMobile());
        dataModel.put("agentIdentityNumber", agentModel.getIdentityNumber());
        dataModel.put("investorMobile", investorModel.getMobile());
        dataModel.put("investorIdentityNumber", agentModel.getIdentityNumber());
        dataModel.put("loanerUserName", loanerDetailsModel == null ? "" : loanerDetailsModel.getUserName());
        dataModel.put("loanerIdentityNumber", loanerDetailsModel == null ? "" : loanerDetailsModel.getIdentityNumber());
        dataModel.put("loanAmount", AmountConverter.convertCentToString(loanModel.getLoanAmount()));
        dataModel.put("investAmount", AmountConverter.convertCentToString(investModel.getAmount()));
        dataModel.put("agentPeriods", String.valueOf(loanModel.getPeriods() * 30) + "天");
        dataModel.put("leftPeriods", String.valueOf(loanModel.getPeriods()) + "期");
        dataModel.put("totalRate", String.valueOf((loanModel.getBaseRate() + loanModel.getActivityRate()) * 100) + "%");
        dataModel.put("recheckTime", simpleDateFormat.format(loanModel.getRecheckTime()));
        dataModel.put("endTime", simpleDateFormat.format(investRepayModel.getRepayDate()));
        if (loanModel.getPledgeType().equals(PledgeType.HOUSE)) {
            dataModel.put("pledge", "房屋");
        } else if (loanModel.getPledgeType().equals(PledgeType.VEHICLE)) {
            dataModel.put("pledge", "车辆");
        }
        return dataModel;
    }
}
