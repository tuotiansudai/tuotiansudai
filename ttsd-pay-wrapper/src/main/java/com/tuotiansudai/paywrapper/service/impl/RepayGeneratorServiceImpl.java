package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.service.RepayGeneratorService;
import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.paywrapper.service.UserBillService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.IdGenerator;
import com.tuotiansudai.utils.InterestCalculator;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RepayGeneratorServiceImpl implements RepayGeneratorService {

    static Logger logger = Logger.getLogger(NormalRepayServiceImpl.class);


    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Override
    @Transactional
    public void generateRepay(long loanId) {
        LoanModel loanModel = loanMapper.findById(loanId);
        List<InvestModel> successInvestModels = investMapper.findSuccessInvestsByLoanId(loanId);
        boolean isPeriodUnitDay = LoanPeriodUnit.DAY == loanModel.getType().getLoanPeriodUnit();
        int totalPeriods = loanModel.calculateLoanRepayTimes();

        List<LoanRepayModel> loanRepayModels = Lists.newArrayList();
        List<InvestRepayModel> investRepayModels = Lists.newArrayList();

        DateTime lastRepayDate = new DateTime(loanModel.getRecheckTime()).minusDays(1).withTimeAtStartOfDay();
        for (int index = 0; index < totalPeriods; index++) {
            int period = index + 1;

            int currentPeriodDuration = isPeriodUnitDay ? loanModel.getPeriods() : lastRepayDate.plusDays(1).dayOfMonth().getMaximumValue();
            DateTime currentRepayDate = lastRepayDate.plusDays(currentPeriodDuration).withTimeAtStartOfDay();

            long expectedLoanInterest = InterestCalculator.calculateLoanRepayInterest(loanModel, successInvestModels, lastRepayDate, currentRepayDate);

            LoanRepayModel loanRepayModel = new LoanRepayModel(idGenerator.generate(), loanModel.getId(), period, expectedLoanInterest, currentRepayDate.toDate(), RepayStatus.REPAYING);

            long currentPeriodCorpus = 0;
            for (InvestModel successInvestModel : successInvestModels) {
                long expectedInvestInterest = InterestCalculator.calculateInvestRepayInterest(loanModel, successInvestModel, lastRepayDate, currentRepayDate);
                long expectedFee = new BigDecimal(expectedInvestInterest).setScale(0, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(loanModel.getInvestFeeRate())).longValue();

                InvestRepayModel investRepayModel = new InvestRepayModel(idGenerator.generate(), successInvestModel.getId(), period, expectedInvestInterest, expectedFee, currentRepayDate.toDate(), RepayStatus.REPAYING);
                if (period == totalPeriods) {
                    investRepayModel.setCorpus(successInvestModel.getAmount());
                }
                currentPeriodCorpus += investRepayModel.getCorpus();
                investRepayModels.add(investRepayModel);
            }

            loanRepayModel.setCorpus(currentPeriodCorpus);
            loanRepayModels.add(loanRepayModel);
            lastRepayDate = currentRepayDate;
        }

        loanRepayMapper.create(loanRepayModels);
        investRepayMapper.create(investRepayModels);
    }
}
