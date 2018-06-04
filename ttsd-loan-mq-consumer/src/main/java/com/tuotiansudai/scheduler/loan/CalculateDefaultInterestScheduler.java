package com.tuotiansudai.scheduler.loan;

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
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findIncompleteLoanRepay();
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            try {
                calculateDefaultInterestEveryLoan(loanRepayModel);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
    }

    private void calculateDefaultInterestEveryLoan(LoanRepayModel loanRepayModel) {
        if (loanRepayModel.getRepayDate().after(new Date())) {
            return;
        }

        logger.info("[Default Interest] loan repay is overdue, loanId: {}, loanRepayId: {}", loanRepayModel.getLoanId(), loanRepayModel.getId());
        LoanModel loanModel = loanMapper.findById(loanRepayModel.getLoanId());
        List<InvestRepayModel> investRepayModels = investRepayMapper.findInvestRepayByLoanIdAndPeriod(loanModel.getId(), loanRepayModel.getPeriod());
        for (InvestRepayModel investRepayModel : investRepayModels) {
            if (investRepayModel.getRepayDate().before(new Date())) {
                if (isNeedCalculateDefaultInterestInvestRepay(investRepayModel)) {
                    long investRepayDefaultInterest = new BigDecimal(investMapper.findById(investRepayModel.getInvestId()).getAmount()).multiply(new BigDecimal(overdueFee))
                            .multiply(new BigDecimal(DateUtil.differenceDay(investRepayModel.getRepayDate(), new Date()) + 1L))
                            .setScale(0, BigDecimal.ROUND_DOWN).longValue();
                    investRepayModel.setDefaultInterest(investRepayDefaultInterest);
                }
                investRepayModel.setStatus(RepayStatus.OVERDUE);
                investRepayMapper.update(investRepayModel);
            }
        }
        loanRepayModel.setDefaultInterest(investRepayModels.stream().mapToLong(InvestRepayModel::getDefaultInterest).sum());
        loanRepayModel.setStatus(RepayStatus.OVERDUE);
        loanRepayMapper.update(loanRepayModel);
        loanModel.setStatus(LoanStatus.OVERDUE);
        loanMapper.update(loanModel);
    }

    private void calculateExtraRateDefaultInterest(LoanRepayModel loanRepayModel) {
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

    private void calculateCouponRepayDefaultInterest(LoanRepayModel loanRepayModel) {
        logger.info(MessageFormat.format("loanRepayId:{0} couponRepay status to overdue", loanRepayModel.getId()));
        List<CouponRepayModel> couponRepayModels = couponRepayMapper.findCouponRepayByLoanIdAndPeriod(loanRepayModel.getLoanId(), loanRepayModel.getPeriod());
        for (CouponRepayModel couponRepayModel : couponRepayModels) {
            if (couponRepayModel.getRepayDate().before(new Date())) {
                couponRepayModel.setStatus(RepayStatus.OVERDUE);
                couponRepayMapper.update(couponRepayModel);
            }
        }
    }

    private boolean isNeedCalculateDefaultInterestInvestRepay(InvestRepayModel investRepayModel) {
        long investId = investRepayModel.getInvestId();
        int period = investRepayModel.getPeriod();
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndLTPeriod(investId, period);
        return CollectionUtils.isEmpty(investRepayModels) || investRepayModels.stream().noneMatch(input -> input.getStatus() == RepayStatus.OVERDUE);
    }
}
