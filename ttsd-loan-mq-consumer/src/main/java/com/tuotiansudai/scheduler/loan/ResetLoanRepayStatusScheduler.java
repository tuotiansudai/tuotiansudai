package com.tuotiansudai.scheduler.loan;

import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.LoanRepayModel;
import com.tuotiansudai.repository.model.RepayStatus;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResetLoanRepayStatusScheduler {
    private static Logger logger = LoggerFactory.getLogger(ResetLoanRepayStatusScheduler.class);

    private final LoanRepayMapper loanRepayMapper;

    @Autowired
    public ResetLoanRepayStatusScheduler(LoanRepayMapper loanRepayMapper) {
        this.loanRepayMapper = loanRepayMapper;
    }

    @Scheduled(fixedDelay = 1000 * 60 * 5, initialDelay = 1000 * 60, zone = "Asia/Shanghai")
    public void resetLoanRepayStatus() {
        List<LoanRepayModel> incompleteLoanRepays = loanRepayMapper.findIncompleteLoanRepay();
        for (LoanRepayModel incompleteLoanRepay : incompleteLoanRepays) {
            if (incompleteLoanRepay.getStatus() == RepayStatus.WAIT_PAY &&
                    new DateTime(incompleteLoanRepay.getActualRepayDate()).plusMinutes(30).isBefore(new DateTime())) {
                incompleteLoanRepay.setActualInterest(0);
                incompleteLoanRepay.setRepayAmount(0);
                incompleteLoanRepay.setStatus(incompleteLoanRepay.getActualRepayDate().before(incompleteLoanRepay.getRepayDate()) ? RepayStatus.REPAYING : RepayStatus.OVERDUE);
                incompleteLoanRepay.setActualRepayDate(null);
                loanRepayMapper.update(incompleteLoanRepay);
            }
        }
    }
}
