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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OverdueRepayDiagnosis extends NormalRepayDiagnosis {
    private static Logger logger = LoggerFactory.getLogger(OverdueRepayDiagnosis.class);

    private final InvestMapper investMapper;
    private final InvestRepayMapper investRepayMapper;
    private final LoanMapper loanMapper;
    private final LoanRepayMapper loanRepayMapper;

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
        // 逾期还款实际交易金额 = 当期利息 + 罚息 + 投资本金
        // 其中 当期利息 = 上期利息 + 逾期天数产生的利息
        InvestModel investModel = investMapper.findById(investRepayModel.getInvestId());
        if (investModel == null) {
            return -2;
        }
        return investModel.getAmount() + investRepayModel.getActualInterest();
    }

    @Override
    protected long calcExpectLoanRepayAmount(LoanRepayModel loanRepayModel) {
        // 逾期还款实际交易金额 = 当期利息 + 罚息 + 投资本金
        // 其中 当期利息 = 上期利息 + 逾期天数产生的利息
        LoanModel loanModel = loanMapper.findById(loanRepayModel.getLoanId());
        if (loanModel == null) {
            return -1;
        }
        return loanModel.getLoanAmount() + loanRepayModel.getActualInterest();
    }
}
