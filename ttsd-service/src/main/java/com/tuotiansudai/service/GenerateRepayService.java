package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.fudian.message.BankLoanFullMessage;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.InterestCalculator;
import com.tuotiansudai.util.LoanPeriodCalculator;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GenerateRepayService {

    private final static Logger logger = LoggerFactory.getLogger(GenerateRepayService.class);

    private final LoanMapper loanMapper;

    private final InvestMapper investMapper;

    private final LoanRepayMapper loanRepayMapper;

    private final InvestRepayMapper investRepayMapper;

    @Autowired
    public GenerateRepayService(LoanMapper loanMapper, InvestMapper investMapper, LoanRepayMapper loanRepayMapper, InvestRepayMapper investRepayMapper) {
        this.loanMapper = loanMapper;
        this.investMapper = investMapper;
        this.loanRepayMapper = loanRepayMapper;
        this.investRepayMapper = investRepayMapper;
    }

    @Transactional
    public void generateRepay(BankLoanFullMessage bankLoanFullMessage) {
        logger.info("[Loan Full] process generate repay, loanId: {}", bankLoanFullMessage.getLoanId());

        LoanModel loanModel = loanMapper.findById(bankLoanFullMessage.getLoanId());
        List<InvestModel> successInvests = investMapper.findSuccessInvestsByLoanId(loanModel.getId()); // 成功出借

        logger.info("[Loan Full] generate loan repay and invest repay, loanId: {}", loanModel.getId());

        List<Integer> daysOfPerPeriod = LoanPeriodCalculator.calculateDaysOfPerPeriod(loanModel.getRecheckTime(), loanModel.getDeadline(), loanModel.getType());

        // 上期回款时间，初始值为放款前一天23:59:59
        DateTime lastRepayDate = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay().minusSeconds(1);

        List<LoanRepayModel> loanRepayModels = Lists.newArrayList(); // 借款人回款计划
        List<InvestRepayModel> investRepayModels = Lists.newArrayList(); // 出借人回款计划

        for (int index = 0; index < loanModel.getPeriods(); index++) {
            logger.info("[Loan Full] loanRepay generate loanId:{} period:{} starting...", loanModel.getId(), index + 1);

            int period = index + 1; //当前计算的期数
            int currentPeriodDuration = daysOfPerPeriod.get(index); //当期的借款天数
            DateTime currentRepayDate = lastRepayDate.plusDays(currentPeriodDuration); //当前回款时间
            long currentPeriodCorpus = 0; //当期回款本金

            long expectedLoanInterest = 0;

            for (InvestModel successInvestModel : successInvests) {
                logger.info("[Loan Full] investRepay generate loanId:{}, investId:{}, period:{} starting...", loanModel.getId(), successInvestModel.getId(), period);

                if (investRepayMapper.findByInvestIdAndPeriod(successInvestModel.getId(), period) != null) {
                    logger.warn("[Loan Full] investRepay is exist, loanId:{}, investId:{}, period:{} end", loanModel.getId(), successInvestModel.getId(), period);
                    continue;
                }

                long expectedInvestInterest = InterestCalculator.calculateInvestRepayInterest(loanModel, successInvestModel.getAmount(), successInvestModel.getTradingTime(), lastRepayDate, currentRepayDate);
                long expectedFee = new BigDecimal(expectedInvestInterest).setScale(0, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(successInvestModel.getInvestFeeRate())).longValue();

                InvestRepayModel investRepayModel = new InvestRepayModel(IdGenerator.generate(),
                        successInvestModel.getId(),
                        period,
                        period == loanModel.getPeriods() ? successInvestModel.getAmount() : 0,
                        expectedInvestInterest,
                        expectedFee,
                        currentRepayDate.toDate(),
                        RepayStatus.REPAYING);
                currentPeriodCorpus += investRepayModel.getCorpus();

                expectedLoanInterest += expectedInvestInterest;

                investRepayModels.add(investRepayModel);

                logger.info("[Loan Full] investRepay generate, loanId:{}, investId:{}, period:{} success", loanModel.getId(), successInvestModel.getId(), period);
            }

            if (loanRepayMapper.findByLoanIdAndPeriod(loanModel.getId(), period) != null) {
                logger.warn("[Loan Full] Loan Repay is exist (loanId = {}, period = {})", loanModel.getId(), period);
                continue;
            }

            LoanRepayModel loanRepayModel = new LoanRepayModel(IdGenerator.generate(), loanModel.getId(), period, currentPeriodCorpus, expectedLoanInterest, currentRepayDate.toDate(), RepayStatus.REPAYING);
            loanRepayModels.add(loanRepayModel);

            lastRepayDate = currentRepayDate;

            logger.info("[Loan Full] loanRepay generate, loanId:{}, period{} success", loanModel.getId(), period);
        }

        if (CollectionUtils.isNotEmpty(loanRepayModels)) {
            loanRepayMapper.create(loanRepayModels);
        }

        if (CollectionUtils.isNotEmpty(investRepayModels)) {
            investRepayMapper.create(investRepayModels);
        }

        logger.info("[Loan Full] loanId: {}, loan repay total: {} invest repay total: {} success", loanModel.getId(), loanRepayModels.size(), investRepayModels.size());
    }
}
