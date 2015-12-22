package com.tuotiansudai.job;

import com.google.common.base.Strings;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;

@Component
public class RefreshRepayAmountJob implements Job {

    static Logger logger = Logger.getLogger(RefreshRepayAmountJob.class);

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Value("${common.environment}")
    private Environment environment;

    @Override
    @Transactional
    public void execute(JobExecutionContext context) throws JobExecutionException {
        if (Environment.PRODUCTION != environment) {
            logger.error(MessageFormat.format("{0} environment can not run refresh repay job", environment.name()));
            return;
        }

        String key = "job:refreshrepay";
        if (!Strings.isNullOrEmpty(redisWrapperClient.get(key))) {
            logger.error("Refresh repay job has already been run");
            return;
        }

        logger.info("Refresh repay data is starting");
        List<LoanModel> repayingLoanModels = loanMapper.findByStatus(LoanStatus.REPAYING);

        for (LoanModel repayingLoanModel : repayingLoanModels) {
            long loanId = repayingLoanModel.getId();
            double loanRate = repayingLoanModel.getBaseRate() + repayingLoanModel.getActivityRate();
            double investFeeRate = repayingLoanModel.getInvestFeeRate();

            List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanId);
            List<InvestModel> investModels = investMapper.findSuccessInvestsByLoanId(loanId);

            for (LoanRepayModel loanRepayModel : loanRepayModels) {
                if (loanRepayModel.getStatus() == RepayStatus.REPAYING) {
                    DateTime lastRepayDate = new DateTime(repayingLoanModel.getRecheckTime()).minusDays(1).withTimeAtStartOfDay();

                    if (loanRepayModel.getPeriod() > 1) {
                        lastRepayDate = new DateTime(loanRepayModels.get(loanRepayModel.getPeriod() - 1 - 1).getRepayDate()).withTimeAtStartOfDay();
                    }

                    int periodDuration = Days.daysBetween(lastRepayDate, new DateTime(loanRepayModel.getRepayDate()).withTimeAtStartOfDay()).getDays();
                    long repayAmount = 0;
                    for (InvestModel investModel : investModels) {
                        InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(investModel.getId(), loanRepayModel.getPeriod());
                        long investInterest = new BigDecimal(investModel.getAmount() * periodDuration).multiply(new BigDecimal(loanRate)).divide(new BigDecimal(365), 0, BigDecimal.ROUND_DOWN).longValue();
                        long investFee = new BigDecimal(investInterest).setScale(0, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(investFeeRate)).longValue();

                        logger.info(MessageFormat.format("investId={0} period={1} oldInterest={2} newInterest={3} oldFee={4} newFee={5}",
                                String.valueOf(investModel.getId()),
                                String.valueOf(investRepayModel.getPeriod()),
                                String.valueOf(investRepayModel.getExpectedInterest()),
                                investInterest,
                                String.valueOf(investRepayModel.getExpectedFee()),
                                investFee));

                        investRepayModel.setExpectedInterest(investInterest);
                        investRepayModel.setExpectedFee(investFee);
                        investRepayModel.setRepayDate(new DateTime(investRepayModel.getRepayDate()).withTimeAtStartOfDay().plusDays(1).minusSeconds(1).toDate());
                        investRepayMapper.update(investRepayModel);
                        repayAmount += investModel.getAmount();
                    }

                    long loanInterest = new BigDecimal(repayAmount * periodDuration).multiply(new BigDecimal(loanRate)).divide(new BigDecimal(365), 0, BigDecimal.ROUND_DOWN).longValue();
                    logger.info(MessageFormat.format("loanId={0} period={1} oldInterest={2} newInterest={3}",
                            String.valueOf(loanRepayModel.getLoanId()),
                            String.valueOf(loanRepayModel.getPeriod()),
                            String.valueOf(loanRepayModel.getExpectedInterest()),
                            loanInterest));
                    loanRepayModel.setExpectedInterest(loanInterest);
                    loanRepayModel.setRepayDate(new DateTime(loanRepayModel.getRepayDate()).withTimeAtStartOfDay().plusDays(1).minusSeconds(1).toDate());

                    loanRepayMapper.update(loanRepayModel);
                }
            }
        }

        logger.info("Refresh repay data is completed");
        redisWrapperClient.set(key, "completed");
    }
}