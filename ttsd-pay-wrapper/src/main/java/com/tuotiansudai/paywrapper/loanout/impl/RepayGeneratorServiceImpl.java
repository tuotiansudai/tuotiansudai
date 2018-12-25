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
import com.tuotiansudai.util.LoanPeriodCalculator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class RepayGeneratorServiceImpl implements RepayGeneratorService {

    private final static Logger logger = Logger.getLogger(RepayGeneratorServiceImpl.class);

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
        logger.info(MessageFormat.format("[Generate_Repay:] loanId:{0} starting...", String.valueOf(loanId)));

        LoanModel loanModel = loanMapper.findById(loanId);

        if (loanModel == null) {
            logger.error(MessageFormat.format("[Generate_Repay:] loanId:{0} 回款计划生成失败，标的不存在", String.valueOf(loanId)));
            throw new PayException("回款计划生成失败，标的不存在");
        }

        if (loanModel.getPledgeType() == PledgeType.NONE && loanModel.getDeadline() == null){
            loanModel.setDeadline(new DateTime(loanModel.getRecheckTime()).plusDays(loanModel.getOriginalDuration() + 1).withTimeAtStartOfDay().minusSeconds(1).toDate());
            loanMapper.updateDeadline(loanModel.getId(), loanModel.getDeadline());
        }

        // 放款当天到借款截止时间之间的天数以30天为一期
        int totalPeriods = LoanPeriodCalculator.calculateLoanPeriods(loanModel.getRecheckTime(), loanModel.getDeadline(), loanModel.getType());
        if (totalPeriods == 0) {
            logger.error(MessageFormat.format("[Generate_Repay:] loanId:{0} recheckTime is null or deadline is null or recheckTime is after deadline", String.valueOf(loanId)));
            throw new PayException("计算标的期数失败，放款时间大于筹款截止时间");
        }

        List<Integer> daysOfPerPeriod = LoanPeriodCalculator.calculateDaysOfPerPeriod(loanModel.getRecheckTime(), loanModel.getDeadline(), loanModel.getType());
        if (CollectionUtils.isEmpty(daysOfPerPeriod)) {
            logger.error(MessageFormat.format("[Generate_Repay:] loanId:{0} 计算标的每期天数失败", String.valueOf(loanId)));
            throw new PayException("计算标的每期天数失败");
        }

        // 上期回款时间，初始值为放款前一天23:59:59
        DateTime lastRepayDate = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay().minusSeconds(1);

        List<InvestModel> successInvestModels = investMapper.findSuccessInvestsByLoanId(loanId); // 成功出借
        List<LoanRepayModel> loanRepayModels = Lists.newArrayList(); // 借款人回款计划
        List<InvestRepayModel> investRepayModels = Lists.newArrayList(); // 出借人回款计划

        for (int index = 0; index < totalPeriods; index++) {
            logger.info(MessageFormat.format("[Generate_Repay:] loanRepay generate loanId:{0} period:{1} starting...", String.valueOf(loanId), index + 1));

            int period = index + 1; //当前计算的期数
            int currentPeriodDuration = daysOfPerPeriod.get(index); //当期的借款天数
            DateTime currentRepayDate = lastRepayDate.plusDays(currentPeriodDuration); //当前回款时间
            long currentPeriodCorpus = 0; //当期回款本金

            for (InvestModel successInvestModel : successInvestModels) {
                logger.info(MessageFormat.format("[Generate_Repay:] investRepay generate loanId:{0}, investId:{1}, period:{2} starting...", String.valueOf(loanId), String.valueOf(successInvestModel.getId()), period));

                if (investRepayMapper.findByInvestIdAndPeriod(successInvestModel.getId(), period) != null) {
                    logger.warn(MessageFormat.format("[Generate_Repay:] investRepay is exist, loanId:{0}, investId:{1}, period:{2} end", String.valueOf(loanId), String.valueOf(successInvestModel.getId()), period));
                    continue;
                }

                long expectedInvestInterest = InterestCalculator.calculateInvestRepayInterest(loanModel, successInvestModel.getAmount(), successInvestModel.getTradingTime(), lastRepayDate, currentRepayDate);
                long expectedFee = new BigDecimal(expectedInvestInterest).setScale(0, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(successInvestModel.getInvestFeeRate())).longValue();

                InvestRepayModel investRepayModel = new InvestRepayModel(IdGenerator.generate(),
                        successInvestModel.getId(),
                        period,
                        period == totalPeriods ? successInvestModel.getAmount() : 0,
                        expectedInvestInterest,
                        expectedFee,
                        currentRepayDate.toDate(),
                        RepayStatus.REPAYING);
                currentPeriodCorpus += investRepayModel.getCorpus();

                investRepayModels.add(investRepayModel);

                logger.info(MessageFormat.format("[Generate_Repay:] investRepay generate, loanId:{0}, investId:{1}, period:{2} success", String.valueOf(loanId), String.valueOf(successInvestModel.getId()), period));
            }

            if (loanRepayMapper.findByLoanIdAndPeriod(loanId, period) != null) {
                logger.warn(MessageFormat.format("[Generate_Repay:] Loan Repay is exist (loanId = {0}, period = {1})", String.valueOf(loanId), String.valueOf(period)));
                continue;
            }

            long expectedLoanInterest = InterestCalculator.calculateLoanRepayInterest(loanModel, successInvestModels, lastRepayDate, currentRepayDate);

            LoanRepayModel loanRepayModel = new LoanRepayModel(IdGenerator.generate(), loanModel.getId(), period, currentPeriodCorpus, expectedLoanInterest, currentRepayDate.toDate(), RepayStatus.REPAYING);
            loanRepayModels.add(loanRepayModel);

            lastRepayDate = currentRepayDate;

            logger.info(MessageFormat.format("[Generate_Repay:] loanRepay generate, loanId:{0}, period{1} success", String.valueOf(loanId), period));
        }

        if (CollectionUtils.isNotEmpty(loanRepayModels)) {
            loanRepayMapper.create(loanRepayModels);
        }

        if (CollectionUtils.isNotEmpty(investRepayModels)) {
            investRepayMapper.create(investRepayModels);
        }

        logger.info(MessageFormat.format("[Generate_Repay:] loanId:{0} loanRepayModels size:{1} investRepayModels size:{2} success",
                String.valueOf(loanId), loanRepayModels.size(), investRepayModels.size()));
    }
}
