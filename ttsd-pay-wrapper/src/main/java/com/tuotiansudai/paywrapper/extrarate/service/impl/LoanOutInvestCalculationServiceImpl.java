package com.tuotiansudai.paywrapper.extrarate.service.impl;


import com.mysql.jdbc.StringUtils;
import com.tuotiansudai.paywrapper.extrarate.service.LoanOutInvestCalculationService;
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
    @Transactional
    public void rateIncreases(long loanId) {
        List<ExtraLoanRateModel> extraLoanRateModels = extraLoanRateMapper.findByLoanId(loanId);
        if (CollectionUtils.isEmpty(extraLoanRateModels)) {
            return;
        }
        LoanDetailsModel loanDetailsModel =  loanDetailsMapper.getLoanDetailsByLoanId(loanId);

        LoanModel loanModel = loanMapper.findById(loanId);
        Date repayDate = new DateTime(loanModel.getRecheckTime()).plusDays(loanModel.getDuration()).toDate();
        List<InvestModel> investModels = investMapper.findSuccessInvestsByLoanId(loanId);
        for (InvestModel investModel : investModels) {
            for (ExtraLoanRateModel extraLoanRateModel : extraLoanRateModels) {
                if ((extraLoanRateModel.getMinInvestAmount() <= investModel.getAmount() && investModel.getAmount() < extraLoanRateModel.getMaxInvestAmount()) ||
                        (extraLoanRateModel.getMaxInvestAmount() == 0 && extraLoanRateModel.getMinInvestAmount() <= investModel.getAmount())) {
                    InvestExtraRateModel investExtraRateModel = new InvestExtraRateModel();
                    investExtraRateModel.setLoanId(loanId);
                    investExtraRateModel.setInvestId(investModel.getId());
                    investExtraRateModel.setAmount(investModel.getAmount());
                    investExtraRateModel.setLoginName(investModel.getLoginName());
                    investExtraRateModel.setExtraRate(extraLoanRateModel.getRate());
                    investExtraRateModel.setRepayDate(repayDate);

                    long expectedInterest = InterestCalculator.calculateExtraLoanRateInterest(loanModel, extraLoanRateModel.getRate(), investModel, repayDate);
                    investExtraRateModel.setExpectedInterest(expectedInterest);

                    long expectedFee = new BigDecimal(investModel.getInvestFeeRate())
                            .multiply(new BigDecimal(expectedInterest))
                            .setScale(0, BigDecimal.ROUND_DOWN)
                            .longValue();
                    investExtraRateModel.setExpectedFee(expectedFee);

                    String investSource;
                    if("IOS".equals(investModel.getSource().name()) || "ANDROID".equals(investModel.getSource().name()) || "MOBILE".equals(investModel.getSource().name())){
                        investSource = "MOBILE";
                    }
                    else if("WEB".equals(investModel.getSource().name())){
                        investSource = "WEB";
                    }
                    else{
                        investSource = "AUTO";
                    }

                    if(!StringUtils.isNullOrEmpty(loanDetailsModel.getExtraSource()) && loanDetailsModel.getExtraSource().contains(investSource))
                    {
                        investExtraRateMapper.create(investExtraRateModel);
                    }

                }
            }
        }
    }

}
