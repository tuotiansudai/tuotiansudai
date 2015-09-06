package com.tuotiansudai.service.impl;

import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.RepayService;
import com.tuotiansudai.utils.AmountUtil;
import com.tuotiansudai.utils.DateUtil;
import com.tuotiansudai.utils.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public void generateLoanRepay(LoanModel loanModel) {

    }

    @Override
    public void generateInvestRepay(LoanModel loanModel) {
        List<InvestModel> invests = investMapper.getSuccessInvests(loanModel.getId());
        LoanType loanType = loanModel.getType();
        for (InvestModel investModel : invests) {
            InvestRepayModel investRepayModel = new InvestRepayModel();
            if (("LOAN_TYPE_5").equals(loanType.name())) {
                investRepayModel.setId(idGenerator.generate());
                investRepayModel.setCorpus(investModel.getAmount());
                investRepayModel.setDefaultInterest(0);
                investRepayModel.setExpectInterest();
                investRepayModel.setActualInterest(0);
                investRepayModel.setExpectFee(AmountUtil.round(AmountUtil.mul(repay.getInterest(), loanModel.getInvestFeeRate()), 2));
                investRepayModel.setActualFee(0);
                investRepayModel.setInvestId(investModel.getId());
                investRepayModel.setPeriod(1);
                investRepayModel.setRepayDate(DateUtil.addDay(loanModel.getRecheckTime(),loanModel.getPeriods().intValue()));
                investRepayModel.setStatus(RepayStatus.REPAYING);
            } else if (("LOAN_TYPE_4").equals(loanType.name())){
                investRepayModel.setId(idGenerator.generate());
                investRepayModel.setCorpus(investModel.getAmount());
                investRepayModel.setDefaultInterest(0);
                investRepayModel.setExpectInterest();
                investRepayModel.setActualInterest(0);
                investRepayModel.setExpectFee(AmountUtil.round(AmountUtil.mul(repay.getInterest(), loanModel.getInvestFeeRate()), 2));
                investRepayModel.setActualFee(0);
                investRepayModel.setInvestId(investModel.getId());
                investRepayModel.setPeriod(1);
                investRepayModel.setRepayDate(DateUtil.addDay(loanModel.getRecheckTime(),loanModel.getPeriods().intValue()));
                investRepayModel.setStatus(RepayStatus.REPAYING);
            } else if (("LOAN_TYPE_3").equals(loanType.name())) {

            } else {

            }

        }
    }


}
