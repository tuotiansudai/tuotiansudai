package com.tuotiansudai.paywrapper.extrarate.service.impl;


import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.paywrapper.extrarate.service.LoanOutInvestCalculationService;
import com.tuotiansudai.repository.mapper.ExtraLoanRateMapper;
import com.tuotiansudai.repository.mapper.InvestExtraRateMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class LoanOutInvestCalculationServiceImpl implements LoanOutInvestCalculationService {

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    @Autowired
    private InvestExtraRateMapper investExtraRateMapper;

    @Override
    public void rateIncreases(long loanId) {
        List<ExtraLoanRateModel> extraLoanRateModels = extraLoanRateMapper.findByLoanId(loanId);
        LoanModel loanModel = loanMapper.findById(loanId);
        if (CollectionUtils.isEmpty(extraLoanRateModels)) {
            return;
        }
        List<InvestModel> investModels = investMapper.findSuccessInvestsByLoanId(loanId);
        for (InvestModel investModel : investModels) {
            for (ExtraLoanRateModel extraLoanRateModel : extraLoanRateModels) {
                if ((extraLoanRateModel.getMinInvestAmount() <= investModel.getAmount() && investModel.getAmount() < extraLoanRateModel.getMaxInvestAmount()) ||
                        (extraLoanRateModel.getMaxInvestAmount() == 0 && extraLoanRateModel.getMinInvestAmount() <= investModel.getAmount())) {
                    InvestExtraRateModel investExtraRateModel = new InvestExtraRateModel();
                    investExtraRateModel.setLoanId(loanId);
                    investExtraRateModel.setInvestId(investModel.getId());
                    investExtraRateModel.setAmount(investModel.getAmount());
                    investExtraRateModel.setExtraRate(extraLoanRateModel.getRate());
                    Date repayDate = new DateTime(loanModel.getRecheckTime()).plus(loanModel.getDuration()).toDate();
                    investExtraRateModel.setRepayDate(repayDate);

                    long expectedInterest = InterestCalculator.calculateExtraLoanRateInterest(loanModel, extraLoanRateModel.getRate(), investModel, repayDate);
                    investExtraRateModel.setExpectedInterest(expectedInterest);

                    MembershipModel membershipModel = userMembershipEvaluator.evaluate(investModel.getLoginName());
                    long expectedFee = new BigDecimal(membershipModel.getFee()).multiply(new BigDecimal(expectedInterest)).setScale(0, BigDecimal.ROUND_DOWN).longValue();

                    investExtraRateModel.setExpectedFee(expectedFee);
                    investExtraRateMapper.create(investExtraRateModel);
                }
            }
        }
    }

}
