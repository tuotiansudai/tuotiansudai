package com.tuotiansudai.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.RepayService;
import com.tuotiansudai.utils.DateUtil;
import com.tuotiansudai.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/9/6.
 */
@Service
public class RepayServiceImpl implements RepayService{

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateLoanRepay(LoanModel loanModel) {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateInvestRepay(LoanModel loanModel) {
        int year = DateUtil.judgeYear(new Date());
        List<InvestModel> invests = investMapper.getSuccessInvests(loanModel.getId());
        LoanType loanType = loanModel.getType();
        List<InvestRepayModel> investRepayModels = Lists.newArrayList();
        for (InvestModel investModel : invests) {
            if (("LOAN_TYPE_5").equals(loanType.name())) {
                InvestRepayModel investRepayModel = new InvestRepayModel();
                int interestDays = loanModel.getPeriods().intValue();
                interestDays = interestDays - DateUtil.calculateIntervalDays(loanModel.getRecheckTime(), investModel.getCreatedTime()) - 1;
                long expectInterest = Math.round((loanModel.getBaseRate() + loanModel.getActivityRate())/year*investModel.getAmount()*interestDays);
                investRepayModel.setId(idGenerator.generate());
                investRepayModel.setCorpus(investModel.getAmount());
                investRepayModel.setDefaultInterest(0);
                investRepayModel.setExpectInterest(expectInterest);
                investRepayModel.setActualInterest(0);
                investRepayModel.setExpectFee(Math.round(expectInterest*loanModel.getInvestFeeRate()));
                investRepayModel.setActualFee(0);
                investRepayModel.setInvestId(investModel.getId());
                investRepayModel.setPeriod(1);
                investRepayModel.setRepayDate(DateUtil.addDay(loanModel.getRecheckTime(),loanModel.getPeriods().intValue()));
                investRepayModel.setStatus(RepayStatus.REPAYING);
                investRepayModels.add(investRepayModel);
            } else if (("LOAN_TYPE_4").equals(loanType.name())){
                InvestRepayModel investRepayModel = new InvestRepayModel();
                int interestDays = loanModel.getPeriods().intValue();
                long expectInterest = Math.round((loanModel.getBaseRate() + loanModel.getActivityRate())/year*investModel.getAmount()*interestDays);
                investRepayModel.setId(idGenerator.generate());
                investRepayModel.setCorpus(investModel.getAmount());
                investRepayModel.setDefaultInterest(0);
                investRepayModel.setExpectInterest(expectInterest);
                investRepayModel.setActualInterest(0);
                investRepayModel.setExpectFee(Math.round(expectInterest*loanModel.getInvestFeeRate()));
                investRepayModel.setActualFee(0);
                investRepayModel.setInvestId(investModel.getId());
                investRepayModel.setPeriod(1);
                investRepayModel.setRepayDate(DateUtil.addDay(loanModel.getRecheckTime(),loanModel.getPeriods().intValue()));
                investRepayModel.setStatus(RepayStatus.REPAYING);
                investRepayModels.add(investRepayModel);
            } else if (("LOAN_TYPE_3").equals(loanType.name())) {
                for (int i=1;i<=loanModel.getPeriods().intValue();i++) {
                    InvestRepayModel investRepayModel = new InvestRepayModel();
                    int interestDays = DateUtil.getIntervalDays(DateUtil.addMonth(loanModel.getRecheckTime(), (i - 1)), DateUtil.addMonth(loanModel.getRecheckTime(), i));
                    long expectInterest = Math.round((loanModel.getBaseRate() + loanModel.getActivityRate())/year*investModel.getAmount()*interestDays);
                    investRepayModel.setId(idGenerator.generate());
                    if (i == loanModel.getPeriods().intValue()) {
                        investRepayModel.setCorpus(investModel.getAmount());
                    } else {
                        investRepayModel.setCorpus(0);
                    }
                    investRepayModel.setDefaultInterest(0);
                    investRepayModel.setExpectInterest(expectInterest);
                    investRepayModel.setActualInterest(0);
                    investRepayModel.setExpectFee(Math.round(expectInterest*loanModel.getInvestFeeRate()));
                    investRepayModel.setActualFee(0);
                    investRepayModel.setInvestId(investModel.getId());
                    investRepayModel.setPeriod(i);
                    investRepayModel.setRepayDate(DateUtil.addMonth(loanModel.getRecheckTime(), i));
                    investRepayModel.setStatus(RepayStatus.REPAYING);
                    investRepayModels.add(investRepayModel);
                }
            } else {
                for (int i=1;i<=loanModel.getPeriods().intValue();i++) {
                    InvestRepayModel investRepayModel = new InvestRepayModel();
                    int interestDays = DateUtil.getIntervalDays(DateUtil.addMonth(loanModel.getRecheckTime(), (i - 1)), DateUtil.addMonth(loanModel.getRecheckTime(), i));
                    if (i==1) {
                        interestDays = interestDays - DateUtil.calculateIntervalDays(loanModel.getRecheckTime(), investModel.getCreatedTime());
                    }
                    long expectInterest = Math.round((loanModel.getBaseRate() + loanModel.getActivityRate())/year*investModel.getAmount()*interestDays);
                    investRepayModel.setId(idGenerator.generate());
                    if (i == loanModel.getPeriods().intValue()) {
                        investRepayModel.setCorpus(investModel.getAmount());
                    } else {
                        investRepayModel.setCorpus(0);
                    }
                    investRepayModel.setDefaultInterest(0);
                    investRepayModel.setExpectInterest(expectInterest);
                    investRepayModel.setActualInterest(0);
                    investRepayModel.setExpectFee(Math.round(expectInterest*loanModel.getInvestFeeRate()));
                    investRepayModel.setActualFee(0);
                    investRepayModel.setInvestId(investModel.getId());
                    investRepayModel.setPeriod(i);
                    investRepayModel.setRepayDate(DateUtil.addMonth(loanModel.getRecheckTime(), i));
                    investRepayModel.setStatus(RepayStatus.REPAYING);
                    investRepayModels.add(investRepayModel);
                }
            }
        }
        if (investRepayModels.size() > 0) {
            investRepayMapper.insertInvestRepay(investRepayModels);
        }
    }

}
