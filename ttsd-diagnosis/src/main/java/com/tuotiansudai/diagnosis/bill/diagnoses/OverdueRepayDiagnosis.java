package com.tuotiansudai.diagnosis.bill.diagnoses;

import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestRepayModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanRepayModel;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class OverdueRepayDiagnosis extends NormalRepayDiagnosis {
    private static Logger logger = LoggerFactory.getLogger(OverdueRepayDiagnosis.class);

    private final InvestMapper investMapper;
    private final InvestRepayMapper investRepayMapper;
    private final LoanMapper loanMapper;
    private final LoanRepayMapper loanRepayMapper;
    private static final Date VERSION_UPDATING_DATE = DateTime.parse("2016-05-01 00:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

    @Autowired
    public OverdueRepayDiagnosis(InvestRepayMapper investRepayMapper,
                                 LoanRepayMapper loanRepayMapper,
                                 InvestMapper investMapper,
                                 LoanMapper loanMapper) {
        super(investRepayMapper, loanRepayMapper, investMapper, loanMapper);
        this.investMapper = investMapper;
        this.investRepayMapper = investRepayMapper;
        this.loanMapper = loanMapper;
        this.loanRepayMapper = loanRepayMapper;
    }

    @Override
    public UserBillBusinessType getSupportedBusinessType() {
        return UserBillBusinessType.OVERDUE_REPAY;
    }

    @Override
    protected long calcExpectInvestRepayAmount(InvestRepayModel investRepayModel) {
        // 逾期还款实际交易金额 = 当期利息 + 罚息 + 当期应还投资本金
        // 其中 当期利息 = 上期利息 + 逾期天数产生的利息
        InvestModel investModel = investMapper.findById(investRepayModel.getInvestId());
        if (investModel == null) {
            return -2;
        }

        long overdueDefaultInterest = investRepayModel.getActualRepayDate().before(VERSION_UPDATING_DATE) ?
                investRepayMapper.findByInvestIdAndPeriodAsc(investRepayModel.getInvestId())
                .stream()
                .map(InvestRepayModel::getDefaultInterest)
                .filter(defaultInterest -> defaultInterest > 0)
                .findAny()
                .orElse(0L) : 0L;
        return investRepayModel.getCorpus() + investRepayModel.getActualInterest() + overdueDefaultInterest;
    }

    @Override
    protected long calcExpectLoanRepayAmount(LoanRepayModel loanRepayModel) {
        // 逾期还款实际交易金额 = 当期利息 + 罚息 + 当期应还投资本金
        // 其中 当期利息 = 上期利息 + 逾期天数产生的利息
        LoanModel loanModel = loanMapper.findById(loanRepayModel.getLoanId());
        if (loanModel == null) {
            return -1;
        }
        long overdueDefaultInterest = loanRepayModel.getActualRepayDate().before(VERSION_UPDATING_DATE) ? loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanRepayModel.getLoanId())
                .stream()
                .map(LoanRepayModel::getDefaultInterest)
                .filter(defaultInterest -> defaultInterest > 0)
                .findAny()
                .orElse(0L) : 0L;
        return loanRepayModel.getCorpus() + loanRepayModel.getActualInterest() + overdueDefaultInterest;
    }
}
