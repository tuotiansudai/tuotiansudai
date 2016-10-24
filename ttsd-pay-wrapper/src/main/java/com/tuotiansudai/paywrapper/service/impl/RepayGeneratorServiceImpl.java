package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.service.RepayGeneratorService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;

@Service
public class RepayGeneratorServiceImpl implements RepayGeneratorService {

    static Logger logger = Logger.getLogger(RepayGeneratorServiceImpl.class);


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
    @Transactional(rollbackFor = Exception.class)
    public void generateRepay(long loanId) throws PayException {
        logger.debug(MessageFormat.format("[Generate_Repay:] loanId:{0}",String.valueOf(loanId)));
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanId);
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByLoanId(loanId);

        if (CollectionUtils.isNotEmpty(loanRepayModels) || CollectionUtils.isNotEmpty(investRepayModels)) {
            logger.error(MessageFormat.format("Loan Repay is exist (loanId = {0})", String.valueOf(loanId)));
            throw new PayException(MessageFormat.format("Loan Repay is exist (loanId = {0})", String.valueOf(loanId)));
        }

        LoanModel loanModel = loanMapper.findById(loanId);
        List<InvestModel> successInvestModels = investMapper.findSuccessInvestsByLoanId(loanId);
        boolean isPeriodUnitDay = LoanPeriodUnit.DAY == loanModel.getType().getLoanPeriodUnit();
        int totalPeriods = loanModel.getPeriods();
        DateTime lastRepayDate = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay().minusSeconds(1);
        logger.debug(MessageFormat.format("[Generate_Repay:] loanId:{0} loanRepay generate begin",String.valueOf(loanId)));
        for (int index = 0; index < totalPeriods; index++) {
            logger.debug(MessageFormat.format("[Generate_Repay:] loanRepay generate loanId:{0} period:{1} begin",String.valueOf(loanId),index + 1));
            int period = index + 1;

            int currentPeriodDuration = isPeriodUnitDay ? loanModel.getDuration() : InterestCalculator.DAYS_OF_MONTH;
            DateTime currentRepayDate = lastRepayDate.plusDays(currentPeriodDuration);

            long currentPeriodCorpus = 0;
            for (InvestModel successInvestModel : successInvestModels) {
                logger.debug(MessageFormat.format("[Generate_Repay:]investRepay generate loanId:{0},investId:{1} period:{2}  end",String.valueOf(loanId),String.valueOf(successInvestModel.getId()),period));
                long expectedInvestInterest = InterestCalculator.calculateInvestRepayInterest(loanModel, successInvestModel, lastRepayDate, currentRepayDate);
                long expectedFee = new BigDecimal(expectedInvestInterest).setScale(0, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(successInvestModel.getInvestFeeRate())).longValue();

                InvestRepayModel investRepayModel = new InvestRepayModel(idGenerator.generate(),
                        successInvestModel.getId(),
                        period,
                        period == totalPeriods ? successInvestModel.getAmount() : 0,
                        expectedInvestInterest,
                        expectedFee,
                        currentRepayDate.toDate(),
                        RepayStatus.REPAYING);
                currentPeriodCorpus += investRepayModel.getCorpus();
                investRepayModels.add(investRepayModel);
                logger.debug(MessageFormat.format("[Generate_Repay:]investRepay generate loanId:{0},investId:{1} period:{2}  end",String.valueOf(loanId),String.valueOf(successInvestModel.getId()),period));
            }
            long expectedLoanInterest = InterestCalculator.calculateLoanRepayInterest(loanModel, successInvestModels, lastRepayDate, currentRepayDate);
            LoanRepayModel loanRepayModel = new LoanRepayModel(idGenerator.generate(), loanModel.getId(), period, currentPeriodCorpus, expectedLoanInterest, currentRepayDate.toDate(), RepayStatus.REPAYING);
            loanRepayModel.setCorpus(currentPeriodCorpus);
            loanRepayModels.add(loanRepayModel);
            lastRepayDate = currentRepayDate;
            logger.debug(MessageFormat.format("[Generate_Repay:] loanId:{0} loanRepay generate {1} period end",String.valueOf(loanId),index + 1));
        }
        logger.debug(MessageFormat.format("[Generate_Repay:] loanId:{0} loanRepayModels size:{1} investRepayModels size:{2} begin",
                String.valueOf(loanId),loanRepayModels == null?0:loanRepayModels.size(),investRepayModels == null?0:investRepayModels.size()));

        loanRepayMapper.create(loanRepayModels);
        investRepayMapper.create(investRepayModels);

        logger.debug(MessageFormat.format("[Generate_Repay:] loanId:{0} loanRepayModels size:{1} investRepayModels size:{2} end",
                String.valueOf(loanId),loanRepayModels == null?0:loanRepayModels.size(),investRepayModels == null?0:investRepayModels.size()));
    }
}
