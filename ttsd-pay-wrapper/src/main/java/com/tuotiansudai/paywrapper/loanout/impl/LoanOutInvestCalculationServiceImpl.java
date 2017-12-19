package com.tuotiansudai.paywrapper.loanout.impl;


import com.google.common.collect.Lists;
import com.tuotiansudai.paywrapper.loanout.LoanOutInvestCalculationService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class LoanOutInvestCalculationServiceImpl implements LoanOutInvestCalculationService {

    static Logger logger = Logger.getLogger(LoanOutInvestCalculationServiceImpl.class);

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private InvestExtraRateMapper investExtraRateMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rateIncreases(long loanId) {
        List<ExtraLoanRateModel> extraLoanRateModels = extraLoanRateMapper.findByLoanId(loanId);
        if (CollectionUtils.isEmpty(extraLoanRateModels)) {
            return false;
        }
        LoanDetailsModel loanDetailsModel =  loanDetailsMapper.getByLoanId(loanId);

        LoanModel loanModel = loanMapper.findById(loanId);
        Date repayDate = loanModel.getDeadline();
        List<InvestModel> investModels = investMapper.findSuccessInvestsByLoanId(loanId);
        for (InvestModel investModel : investModels) {
            for (ExtraLoanRateModel extraLoanRateModel : extraLoanRateModels) {
                if(investExtraRateMapper.findByInvestId(investModel.getId()) != null){
                    continue;
                }

                if ((extraLoanRateModel.getMinInvestAmount() <= investModel.getAmount() && investModel.getAmount() < extraLoanRateModel.getMaxInvestAmount()) ||
                        (extraLoanRateModel.getMaxInvestAmount() == 0 && extraLoanRateModel.getMinInvestAmount() <= investModel.getAmount())) {
                    InvestExtraRateModel investExtraRateModel = new InvestExtraRateModel();
                    investExtraRateModel.setLoanId(loanId);
                    investExtraRateModel.setInvestId(investModel.getId());
                    investExtraRateModel.setAmount(investModel.getAmount());
                    investExtraRateModel.setLoginName(investModel.getLoginName());
                    investExtraRateModel.setExtraRate(extraLoanRateModel.getRate());
                    investExtraRateModel.setRepayDate(repayDate);
                    investExtraRateModel.setStatus(RepayStatus.REPAYING);

                    long expectedInterest = InterestCalculator.calculateExtraLoanRateInterest(loanModel, extraLoanRateModel.getRate(), investModel, repayDate);
                    investExtraRateModel.setExpectedInterest(expectedInterest);

                    long expectedFee = new BigDecimal(investModel.getInvestFeeRate())
                            .multiply(new BigDecimal(expectedInterest))
                            .setScale(0, BigDecimal.ROUND_DOWN)
                            .longValue();
                    investExtraRateModel.setExpectedFee(expectedFee);

                    Source investSource = Source.WEB;
                    if (Lists.newArrayList(Source.IOS, Source.ANDROID, Source.MOBILE).contains(investModel.getSource())) {
                        investSource = Source.MOBILE;
                    }

                    if (!CollectionUtils.isEmpty(loanDetailsModel.getExtraSource()) && loanDetailsModel.getExtraSource().contains(investSource))
                    {
                        investExtraRateMapper.create(investExtraRateModel);
                    }

                    logger.info(MessageFormat.format("[标的放款]创建阶梯加息 loanId:{0},investId:{1}",loanId,investModel.getId()));
                }
            }
        }
        return true;
    }

}
