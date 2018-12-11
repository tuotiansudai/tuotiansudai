package com.tuotiansudai.diagnosis.bill.diagnoses;

import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestRepayModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanRepayModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdvanceRepayDiagnosis extends NormalRepayDiagnosis {
    private static Logger logger = LoggerFactory.getLogger(AdvanceRepayDiagnosis.class);

    private final InvestMapper investMapper;
    private final LoanMapper loanMapper;
    private final UserBillMapper userBillMapper;

    @Autowired
    public AdvanceRepayDiagnosis(InvestRepayMapper investRepayMapper,
                                 LoanRepayMapper loanRepayMapper,
                                 InvestMapper investMapper,
                                 LoanMapper loanMapper,
                                 UserBillMapper userBillMapper) {
        super(investRepayMapper, loanRepayMapper, investMapper, loanMapper, userBillMapper);
        this.investMapper = investMapper;
        this.loanMapper = loanMapper;
        this.userBillMapper = userBillMapper;
    }

    @Override
    public UserBillBusinessType getSupportedBusinessType() {
        return UserBillBusinessType.ADVANCE_REPAY;
    }

    @Override
    protected long calcExpectInvestRepayAmount(InvestRepayModel investRepayModel) {
        // 提前还款实际交易金额 = 当期利息 + 出借本金
        InvestModel investModel = investMapper.findById(investRepayModel.getInvestId());
        if (investModel == null) {
            return -2;
        }
        return investModel.getAmount() + investRepayModel.getActualInterest();
    }

    @Override
    protected long calcExpectLoanRepayAmount(LoanRepayModel loanRepayModel) {
        // 提前还款实际交易金额 = 当期利息 + 本金
        LoanModel loanModel = loanMapper.findById(loanRepayModel.getLoanId());
        if (loanModel == null) {
            return -1;
        }
        return loanModel.getLoanAmount() + loanRepayModel.getActualInterest();
    }
}
