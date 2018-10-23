package com.tuotiansudai.scheduler.loan;

import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.DateUtil;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Component
public class CalculateDefaultInterestScheduler {
    private static Logger logger = LoggerFactory.getLogger(CalculateDefaultInterestScheduler.class);

    @Value("${pay.overdue.fee}")
    private double overdueFee;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestExtraRateMapper investExtraRateMapper;

    @Autowired
    private CouponRepayMapper couponRepayMapper;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

//    @Scheduled(cron = "0 0 1 * * ?", zone = "Asia/Shanghai")
    @Scheduled(cron = "0 0/10 * * * ?", zone = "Asia/Shanghai")
    public void calculateDefaultInterest() {
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findNotCompleteLoanRepay();
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            try {
                calculateDefaultInterestEveryLoan(loanRepayModel);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
        //最后一期逾期额外计算利息
        List<LoanRepayModel> overdueLoanRepays = loanRepayMapper.findOverdueLoanRepay();
        for (LoanRepayModel loanRepayModel : overdueLoanRepays) {
            calculateOverdueInterestEveryLoan(loanRepayModel);
        }
    }

    //计算最后一期逾期利息
    public void calculateOverdueInterestEveryLoan(LoanRepayModel loanRepayModel) {
        LoanModel loanModel = loanMapper.findById(loanRepayModel.getLoanId());
        if (loanModel.getPeriods() != loanRepayModel.getPeriod()) {
            return;
        }
        List<InvestRepayModel> investRepayModels = investRepayMapper.findInvestRepayByLoanIdAndPeriod(loanModel.getId(), loanRepayModel.getPeriod());
        for (InvestRepayModel investRepayModel : investRepayModels) {
            long overdueInterest = InterestCalculator.calculateLoanInterest(loanModel.getBaseRate(), investMapper.findById(investRepayModel.getInvestId()).getAmount(), new DateTime(investRepayModel.getRepayDate()), new DateTime());
            investRepayModel.setOverdueInterest(overdueInterest);
            //计算逾期利息 和逾期利息手续费
            InvestModel investModel = investMapper.findById(investRepayModel.getInvestId());
            //如果是转让项目，需要从转让日开始计算
            if (investModel.getTransferInvestId() != null) {
                TransferApplicationModel transferApplicationModel = transferApplicationMapper.findByInvestId(investModel.getId());
                overdueInterest = InterestCalculator.calculateLoanInterest(loanModel.getBaseRate(), investModel.getAmount(), new DateTime(transferApplicationModel.getTransferTime()), new DateTime());
            }
            long overdueFeeValue = new BigDecimal(overdueInterest).setScale(0, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(investModel.getInvestFeeRate())).longValue();
            investRepayModel.setOverdueFee(overdueFeeValue);
            investRepayMapper.update(investRepayModel);
        }
        long repayOverdueInterest = InterestCalculator.calculateLoanInterest(loanModel.getBaseRate(), loanModel.getLoanAmount(), new DateTime(loanRepayModel.getRepayDate()), new DateTime());
        loanRepayModel.setOverdueInterest(repayOverdueInterest);
        loanRepayMapper.update(loanRepayModel);
    }

    /**
     * 计算罚息
     *
     * @param loanRepayModel
     * @throws Exception
     */
    private void calculateDefaultInterestEveryLoan(LoanRepayModel loanRepayModel) throws Exception {
        LoanModel loanModel = loanMapper.findById(loanRepayModel.getLoanId());
        List<InvestRepayModel> investRepayModels = investRepayMapper.findInvestRepayByLoanIdAndPeriod(loanModel.getId(), loanRepayModel.getPeriod());
        for (InvestRepayModel investRepayModel : investRepayModels) {
            if (investRepayModel.getRepayDate().before(new Date())) {
                investRepayModel.setStatus(RepayStatus.OVERDUE);
                if (isNeedCalculateDefaultInterestInvestRepay(investRepayModel)) {
                    InvestModel investModel = investMapper.findById(investRepayModel.getInvestId());
                    long investRepayDefaultInterest = new BigDecimal(investModel.getAmount()).multiply(new BigDecimal(overdueFee))
                            .multiply(new BigDecimal(DateUtil.differenceDay(investRepayModel.getRepayDate(), new Date()) + 1L))
                            .setScale(0, BigDecimal.ROUND_DOWN).longValue();
                    investRepayModel.setDefaultInterest(investRepayDefaultInterest);
                    //如果是债券转让,罚息手续费需要从转让日计算
                    if (investModel.getTransferInvestId() != null) {
                        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findByInvestId(investModel.getId());
                        investRepayDefaultInterest = InterestCalculator.calculateLoanInterest(overdueFee, investModel.getAmount(), new DateTime(transferApplicationModel.getTransferTime()), new DateTime());
                    }
                    long investRepayDefaultFee = new BigDecimal(investRepayDefaultInterest).setScale(0, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(investModel.getInvestFeeRate())).longValue();
                    investRepayModel.setDefaultFee(investRepayDefaultFee);
                }
                investRepayMapper.update(investRepayModel);
            }
        }
        if (loanRepayModel.getRepayDate().before(new Date())) {
            loanRepayModel.setStatus(RepayStatus.OVERDUE);
            if (isNeedCalculateDefaultInterestLoanRepay(loanRepayModel)) {
                long loanRepayDefaultInterest = new BigDecimal(loanModel.getLoanAmount()).multiply(new BigDecimal(overdueFee))
                        .multiply(new BigDecimal(DateUtil.differenceDay(loanRepayModel.getRepayDate(), new Date()) + 1L)).setScale(0, BigDecimal.ROUND_DOWN).longValue();
                loanRepayModel.setDefaultInterest(loanRepayDefaultInterest);
            }
            loanRepayMapper.update(loanRepayModel);
            loanModel.setStatus(LoanStatus.OVERDUE);
            loanMapper.update(loanModel);
            boolean isLastPeriod = loanRepayMapper.findLastLoanRepay(loanRepayModel.getLoanId()).getPeriod() == loanRepayModel.getPeriod();
            if (isLastPeriod) {
                List<InvestExtraRateModel> investExtraRateModels = investExtraRateMapper.findByLoanId(loanRepayModel.getLoanId());
                investExtraRateModels.forEach(investExtraRateModel -> {
                    investExtraRateModel.setStatus(RepayStatus.OVERDUE);
                    investExtraRateMapper.update(investExtraRateModel);
                });
                logger.info("investExtraRate status to overdue");
            }
        }
        logger.info(MessageFormat.format("loanRepayId:{0} couponRepay status to overdue", loanRepayModel.getId()));
        List<CouponRepayModel> couponRepayModels = couponRepayMapper.findCouponRepayByLoanIdAndPeriod(loanModel.getId(), loanRepayModel.getPeriod());
        for (CouponRepayModel couponRepayModel : couponRepayModels) {
            if (couponRepayModel.getRepayDate().before(new Date())) {
                couponRepayModel.setStatus(RepayStatus.OVERDUE);
                couponRepayMapper.update(couponRepayModel);
            }
        }
    }

    private boolean isNeedCalculateDefaultInterestLoanRepay(LoanRepayModel loanRepayModel) {
        long loanId = loanRepayModel.getLoanId();
        int period = loanRepayModel.getPeriod();
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdAndLTPeriod(loanId, period);
        return CollectionUtils.isEmpty(loanRepayModels) || loanRepayModels.stream().noneMatch(input -> input.getStatus() == RepayStatus.OVERDUE);
    }

    private boolean isNeedCalculateDefaultInterestInvestRepay(InvestRepayModel investRepayModel) {
        long investId = investRepayModel.getInvestId();
        int period = investRepayModel.getPeriod();
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndLTPeriod(investId, period);
        return CollectionUtils.isEmpty(investRepayModels) || investRepayModels.stream().noneMatch(input -> input.getStatus() == RepayStatus.OVERDUE);
    }

}
