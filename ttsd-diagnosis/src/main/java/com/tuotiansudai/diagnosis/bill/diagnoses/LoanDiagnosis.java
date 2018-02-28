package com.tuotiansudai.diagnosis.bill.diagnoses;

import com.tuotiansudai.diagnosis.bill.UserBillBusinessDiagnosis;
import com.tuotiansudai.diagnosis.support.DiagnosisContext;
import com.tuotiansudai.diagnosis.support.SingleObjectDiagnosis;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class LoanDiagnosis extends UserBillBusinessDiagnosis {
    private static Logger logger = LoggerFactory.getLogger(LoanDiagnosis.class);

    private final InvestMapper investMapper;

    private final LoanMapper loanMapper;

    @Autowired
    public LoanDiagnosis(InvestMapper investMapper, LoanMapper loanMapper) {
        this.investMapper = investMapper;
        this.loanMapper = loanMapper;
    }

    @Override
    public UserBillBusinessType getSupportedBusinessType() {
        return UserBillBusinessType.LOAN_SUCCESS;
    }

    @Override
    public void diagnosis(UserBillModel userBillModel, DiagnosisContext context) {
        if (userBillModel.getOperationType() == UserBillOperationType.TO_FREEZE) {
            diagnosisInvest(userBillModel, context);
        }
        if (userBillModel.getOperationType() == UserBillOperationType.TI_BALANCE) {
            diagnosisLoan(userBillModel, context);
        }
    }

    private void diagnosisInvest(UserBillModel userBillModel, DiagnosisContext context) {
        InvestModel tracedObject = investMapper.findById(userBillModel.getOrderId());
        SingleObjectDiagnosis
                // exist
                .init(userBillModel, tracedObject, this::buildTracedObjectIdInvest)
                // status
                .check(m -> m.getStatus() == InvestStatus.SUCCESS,
                        m -> String.format("wrong status [expect:SUCCESS, actual:%s]", m.getStatus()))
                // owner
                .check(m -> userBillModel.getLoginName().equals(m.getLoginName()),
                        m -> String.format("wrong owner [expect:%s, actual:%s]", userBillModel.getLoginName(), m.getLoginName()))
                // unique
                .check(m -> !context.hasAlreadyTraced(buildTracedObjectIdInvest(m)),
                        m -> String.format("has already traced by UserBill#%d", context.getUserBillId(buildTracedObjectIdInvest(m))))
                // amount
                .check(m -> userBillModel.getAmount() == m.getAmount(),
                        m -> String.format("wrong amount [expect: %d, actual: %d]", userBillModel.getAmount(), m.getAmount()))
                // result
                .fail(r -> onFail(userBillModel, context, r))
                .success(r -> onPass(userBillModel, context, buildTracedObjectIdInvest(tracedObject)));
    }

    private void diagnosisLoan(UserBillModel userBillModel, DiagnosisContext context) {
        LoanModel tracedObject = loanMapper.findById(userBillModel.getOrderId());
        SingleObjectDiagnosis
                // exist
                .init(userBillModel, tracedObject, this::buildTracedObjectIdLoan)
                // status
                .check(m -> !(Arrays.asList(LoanStatus.WAITING_VERIFY, LoanStatus.PREHEAT, LoanStatus.CANCEL).contains(m.getStatus())),
                        m -> String.format("wrong status [expect:(RAISING|RECHECK|REPAYING|COMPLETE|OVERDUE),actual:%s]", m.getStatus()))
                // owner
                .check(m -> userBillModel.getLoginName().equals(m.getAgentLoginName()),
                        m -> String.format("wrong owner [expect:%s, actual:%s]", userBillModel.getLoginName(), m.getAgentLoginName()))
                // unique
                .check(m -> !context.hasAlreadyTraced(buildTracedObjectIdLoan(m)),
                        m -> String.format("has already traced by UserBill#%d", context.getUserBillId(buildTracedObjectIdLoan(m))))
                // amount
                .check(m -> userBillModel.getAmount() <= m.getLoanAmount(),
                        m -> String.format("wrong amount [expect grate than or equal with %d, actual: %d]", userBillModel.getAmount(), m.getLoanAmount()))
                // result
                .fail(r -> onFail(userBillModel, context, r))
                .success(r -> onPass(userBillModel, context, buildTracedObjectIdLoan(tracedObject)));
    }


    private String buildTracedObjectIdInvest(InvestModel investModel) {
        return "Invest:Freeze:" + investModel.getId();
    }

    private String buildTracedObjectIdLoan(LoanModel loanModel) {
        return "Loan:Balance:" + loanModel.getId();
    }
}
