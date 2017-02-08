package com.tuotiansudai.scheduler.loan;

import com.tuotiansudai.coupon.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.DateUtil;
import org.apache.commons.collections.CollectionUtils;
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

    @Scheduled(cron = "0 0 1 * * ?", zone = "Asia/Shanghai")
    public void calculateDefaultInterest() {
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findNotCompleteLoanRepay();
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            try {
                calculateDefaultInterestEveryLoan(loanRepayModel);

            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
    }

    private void calculateDefaultInterestEveryLoan(LoanRepayModel loanRepayModel) throws Exception {
        LoanModel loanModel = loanMapper.findById(loanRepayModel.getLoanId());
        List<InvestRepayModel> investRepayModels = investRepayMapper.findInvestRepayByLoanIdAndPeriod(loanModel.getId(), loanRepayModel.getPeriod());
        for (InvestRepayModel investRepayModel : investRepayModels) {
            if (investRepayModel.getRepayDate().before(new Date())) {
                investRepayModel.setStatus(RepayStatus.OVERDUE);
                if (isNeedCalculateDefaultInterestInvestRepay(investRepayModel)) {
                    long investRepayDefaultInterest = new BigDecimal(investMapper.findById(investRepayModel.getInvestId()).getAmount()).multiply(new BigDecimal(overdueFee))
                            .multiply(new BigDecimal(DateUtil.differenceDay(investRepayModel.getRepayDate(), new Date()) + 1L))
                            .setScale(0, BigDecimal.ROUND_DOWN).longValue();
                    investRepayModel.setDefaultInterest(investRepayDefaultInterest);
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
