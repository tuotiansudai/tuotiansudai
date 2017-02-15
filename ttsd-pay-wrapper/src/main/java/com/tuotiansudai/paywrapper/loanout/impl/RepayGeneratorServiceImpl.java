package com.tuotiansudai.paywrapper.loanout.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.loanout.RepayGeneratorService;
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
        logger.info(MessageFormat.format("[Generate_Repay:] loanId:{0}",String.valueOf(loanId)));
        List<LoanRepayModel> loanRepayModels = Lists.newArrayList();
        List<InvestRepayModel> investRepayModels = Lists.newArrayList();

        LoanModel loanModel = loanMapper.findById(loanId);
        List<InvestModel> successInvestModels = investMapper.findSuccessInvestsByLoanId(loanId);
        boolean isPeriodUnitDay = LoanPeriodUnit.DAY == loanModel.getType().getLoanPeriodUnit();
        int totalPeriods = loanModel.getPeriods();
        DateTime lastRepayDate = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay().minusSeconds(1);
        logger.info(MessageFormat.format("[Generate_Repay:] loanId:{0} loanRepay generate begin",String.valueOf(loanId)));
        for (int index = 0; index < totalPeriods; index++) {
            logger.info(MessageFormat.format("[Generate_Repay:] loanRepay generate loanId:{0} period:{1} begin",String.valueOf(loanId),index + 1));
            int period = index + 1;

            int currentPeriodDuration = isPeriodUnitDay ? loanModel.getDuration() : InterestCalculator.DAYS_OF_MONTH;
            DateTime currentRepayDate = lastRepayDate.plusDays(currentPeriodDuration);

            long currentPeriodCorpus = 0;
            for (InvestModel successInvestModel : successInvestModels) {
                logger.info(MessageFormat.format("[Generate_Repay:]investRepay generate loanId:{0},investId:{1} period:{2}  end",String.valueOf(loanId),String.valueOf(successInvestModel.getId()),period));
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
                if(investRepayMapper.findByInvestIdAndPeriod(investRepayModel.getInvestId(),investRepayModel.getPeriod()) == null){
                    investRepayModels.add(investRepayModel);
                }else{
                    logger.info(MessageFormat.format("[Generate_Repay:]investRepay is exist loanId:{0},investId:{1} period:{2}  end", String.valueOf(loanId), String.valueOf(successInvestModel.getId()), period));
                }
                logger.info(MessageFormat.format("[Generate_Repay:]investRepay generate repeat loanId:{0},investId:{1} period:{2}  end", String.valueOf(loanId), String.valueOf(successInvestModel.getId()), period));
            }
            long expectedLoanInterest = InterestCalculator.calculateLoanRepayInterest(loanModel, successInvestModels, lastRepayDate, currentRepayDate);
            LoanRepayModel loanRepayModel = new LoanRepayModel(idGenerator.generate(), loanModel.getId(), period, currentPeriodCorpus, expectedLoanInterest, currentRepayDate.toDate(), RepayStatus.REPAYING);
            loanRepayModel.setCorpus(currentPeriodCorpus);

            if(loanRepayMapper.findByLoanIdAndPeriod(loanId, period) == null){
                loanRepayModels.add(loanRepayModel);
            }else{
                logger.error(MessageFormat.format("[Generate_Repay:] Loan Repay is exist (loanId = {0}, period = {1})", String.valueOf(loanRepayModel.getLoanId()), String.valueOf(loanRepayModel.getPeriod())));
            }
            lastRepayDate = currentRepayDate;
            logger.info(MessageFormat.format("[Generate_Repay:] loanId:{0} loanRepay generate {1} period end", String.valueOf(loanId),index + 1));
        }
        logger.info(MessageFormat.format("[Generate_Repay:] loanId:{0} loanRepayModels size:{1} investRepayModels size:{2} begin",
                String.valueOf(loanId), loanRepayModels == null ? 0 : loanRepayModels.size(), investRepayModels == null ? 0 : investRepayModels.size()));

        if(CollectionUtils.isNotEmpty(loanRepayModels)){
            loanRepayMapper.create(loanRepayModels);
        }
        if(CollectionUtils.isNotEmpty(investRepayModels)){
            investRepayMapper.create(investRepayModels);
        }

        logger.info(MessageFormat.format("[Generate_Repay:] loanId:{0} loanRepayModels size:{1} investRepayModels size:{2} end",
                String.valueOf(loanId),loanRepayModels == null?0:loanRepayModels.size(),investRepayModels == null?0:investRepayModels.size()));
    }
}
