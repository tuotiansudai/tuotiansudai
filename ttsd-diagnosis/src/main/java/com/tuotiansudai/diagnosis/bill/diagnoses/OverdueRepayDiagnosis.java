package com.tuotiansudai.diagnosis.bill.diagnoses;

import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OverdueRepayDiagnosis extends NormalRepayDiagnosis {
    private static Logger logger = LoggerFactory.getLogger(OverdueRepayDiagnosis.class);

    @Autowired
    public OverdueRepayDiagnosis(InvestRepayMapper investRepayMapper, LoanRepayMapper loanRepayMapper) {
        super(investRepayMapper, loanRepayMapper);
    }

    @Override
    public UserBillBusinessType getSupportedBusinessType() {
        return UserBillBusinessType.OVERDUE_REPAY;
    }
}
