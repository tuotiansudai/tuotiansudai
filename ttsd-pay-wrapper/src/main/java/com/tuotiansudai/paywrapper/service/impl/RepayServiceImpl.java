package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.paywrapper.service.RepayService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.DateUtil;
import com.tuotiansudai.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class RepayServiceImpl implements RepayService {

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Transactional(rollbackFor = Exception.class)
    private void generateLoanRepay(LoanModel loanModel,List<List<InvestRepayModel>> allInvestRepays) {
        List<LoanRepayModel> loanRepayModels = Lists.newArrayList();
        for (int i=1;i<=loanModel.getPeriods();i++) {
            LoanRepayModel loanRepayModel = new LoanRepayModel();
            loanRepayModel.setId(idGenerator.generate());
            loanRepayModel.setDefaultInterest(0);
            loanRepayModel.setActualInterest(0);
            loanRepayModel.setPeriod(i);
            loanRepayModel.setStatus(RepayStatus.REPAYING);
            loanRepayModel.setLoanId(loanModel.getId());
            loanRepayModels.add(loanRepayModel);
            if (("LOAN_TYPE_5").equals(loanModel.getType().name()) || ("LOAN_TYPE_4").equals(loanModel.getType().name())) {
                loanRepayModel.setRepayDate(DateUtil.addDay(loanModel.getRecheckTime(), loanModel.getPeriods().intValue()));
                break;
            } else {
                loanRepayModel.setRepayDate(DateUtil.addMonth(loanModel.getRecheckTime(), i));
            }
        }
        for (List<InvestRepayModel> irs : allInvestRepays) {
            for (InvestRepayModel ir : irs) {
                loanRepayModels.get(Integer.parseInt(String.valueOf(ir.getPeriod())) - 1).setCorpus(loanRepayModels.get(Integer.parseInt(String.valueOf(ir.getPeriod())) - 1).getCorpus() + ir.getCorpus());
                loanRepayModels.get(Integer.parseInt(String.valueOf(ir.getPeriod())) - 1).setExpectInterest(loanRepayModels.get(Integer.parseInt(String.valueOf(ir.getPeriod())) - 1).getExpectInterest() + ir.getExpectInterest());
            }
        }
        if (loanRepayModels.size() > 0) {
            loanRepayMapper.insertLoanRepay(loanRepayModels);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateInvestRepay(LoanModel loanModel, List<InvestModel> invests) {
        List<List<InvestRepayModel>> allInvestRepays = Lists.newArrayList();
        List<InvestRepayModel> investRepayModels = this.getInvestRepayModels(loanModel, invests);
        if (investRepayModels.size() > 0) {
            investRepayMapper.insertInvestRepay(investRepayModels);
            allInvestRepays.add(investRepayModels);
        }
        this.generateLoanRepay(loanModel,allInvestRepays);
    }

    public List<InvestRepayModel> getInvestRepayModels(LoanModel loanModel, List<InvestModel> invests) {
        List<InvestRepayModel> investRepayModels = Lists.newArrayList();
        LoanType loanType = loanModel.getType();
        int year = DateUtil.judgeYear(new Date());
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
        return investRepayModels;
    }

}
