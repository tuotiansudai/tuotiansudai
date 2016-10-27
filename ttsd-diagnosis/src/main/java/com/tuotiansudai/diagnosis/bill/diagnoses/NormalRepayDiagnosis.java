package com.tuotiansudai.diagnosis.bill.diagnoses;

import com.tuotiansudai.diagnosis.bill.UserBillBusinessDiagnosis;
import com.tuotiansudai.diagnosis.support.DiagnosisContext;
import com.tuotiansudai.diagnosis.support.SingleObjectDiagnosis;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NormalRepayDiagnosis extends UserBillBusinessDiagnosis {
    private static Logger logger = LoggerFactory.getLogger(NormalRepayDiagnosis.class);

    private final InvestRepayMapper investRepayMapper;
    private final LoanRepayMapper loanRepayMapper;
    private final InvestMapper investMapper;
    private final LoanMapper loanMapper;

    @Autowired
    public NormalRepayDiagnosis(InvestRepayMapper investRepayMapper,
                                LoanRepayMapper loanRepayMapper,
                                InvestMapper investMapper,
                                LoanMapper loanMapper) {
        this.investRepayMapper = investRepayMapper;
        this.loanRepayMapper = loanRepayMapper;
        this.investMapper = investMapper;
        this.loanMapper = loanMapper;
    }

    @Override
    public UserBillBusinessType getSupportedBusinessType() {
        return UserBillBusinessType.NORMAL_REPAY;
    }

    @Override
    public void diagnosis(UserBillModel userBillModel, DiagnosisContext context) {
        if (userBillModel.getOperationType() == UserBillOperationType.TO_BALANCE) {
            diagnosisLoanRepay(userBillModel, context);
        }
        if (userBillModel.getOperationType() == UserBillOperationType.TI_BALANCE) {
            diagnosisInvestRepay(userBillModel, context);
        }
    }

    private void diagnosisInvestRepay(UserBillModel userBillModel, DiagnosisContext context) {
        InvestRepayModel tracedObject = investRepayMapper.findById(userBillModel.getOrderId());
        String investLoginName = traceInvestLoginName(tracedObject);
        SingleObjectDiagnosis
                // exist
                .init(userBillModel, tracedObject, this::buildTracedObjectIdInvestRepay)
                // status
                .check(m -> m.getStatus() == RepayStatus.COMPLETE,
                        m -> String.format("wrong status [expect:SUCCESS, actual:%s]", m.getStatus()))
                // owner
                .check(m -> userBillModel.getLoginName().equals(investLoginName),
                        m -> String.format("wrong owner [expect:%s, actual:%s]", userBillModel.getLoginName(), investLoginName))
                // unique
                .check(m -> !context.hasAlreadyTraced(buildTracedObjectIdInvestRepay(m)),
                        m -> String.format("has already traced by UserBill#%d", context.getUserBillId(buildTracedObjectIdInvestRepay(m))))
                // amount
                .check(m -> userBillModel.getAmount() == calcExpectRepayAmount(m),
                        m -> String.format("wrong amount [expect: %d, actual: %d]", userBillModel.getAmount(), calcExpectRepayAmount(m)))
                // result
                .fail(r -> onFail(userBillModel, context, r))
                .success(r -> onPass(userBillModel, context, buildTracedObjectIdInvestRepay(tracedObject)));
    }

    protected long calcExpectRepayAmount(InvestRepayModel investRepayModel) {
        return investRepayModel.getCorpus() + investRepayModel.getActualInterest();
    }

    private void diagnosisLoanRepay(UserBillModel userBillModel, DiagnosisContext context) {
        LoanRepayModel tracedObject = loanRepayMapper.findById(userBillModel.getOrderId());
        String loanAgentLoginName = traceLoanAgentLoginName(tracedObject);
        SingleObjectDiagnosis
                // exist
                .init(userBillModel, tracedObject, this::buildTracedObjectIdLoanRepay)
                // status
                .check(m -> m.getStatus() == RepayStatus.COMPLETE,
                        m -> String.format("wrong status [expect:SUCCESS, actual:%s]", m.getStatus()))
                // owner
                .check(m -> userBillModel.getLoginName().equals(loanAgentLoginName),
                        m -> String.format("wrong owner [expect:%s, actual:%s]", userBillModel.getLoginName(), loanAgentLoginName))
                // unique
                .check(m -> !context.hasAlreadyTraced(buildTracedObjectIdLoanRepay(m)),
                        m -> String.format("has already traced by UserBill#%d", context.getUserBillId(buildTracedObjectIdLoanRepay(m))))
                // amount
                .check(m -> userBillModel.getAmount() == m.getRepayAmount(),
                        m -> String.format("wrong amount [expect: %d, actual: %d]", userBillModel.getAmount(), m.getRepayAmount()))
                // result
                .fail(r -> onFail(userBillModel, context, r))
                .success(r -> onPass(userBillModel, context, buildTracedObjectIdLoanRepay(tracedObject)));
    }

    private String traceInvestLoginName(InvestRepayModel investRepayModel) {
        if (investRepayModel != null) {
            InvestModel investModel = investMapper.findById(investRepayModel.getInvestId());
            return investModel == null ? null : investModel.getLoginName();
        }
        return null;
    }

    private String traceLoanAgentLoginName(LoanRepayModel loanRepayModel) {
        if (loanRepayModel != null) {
            LoanModel loanModel = loanMapper.findById(loanRepayModel.getLoanId());
            return loanModel == null ? null : loanModel.getAgentLoginName();
        }
        return null;
    }

    private String buildTracedObjectIdInvestRepay(InvestRepayModel investRepayModel) {
        return "InvestRepay:TI:" + investRepayModel.getId();
    }

    private String buildTracedObjectIdLoanRepay(LoanRepayModel loanRepayModel) {
        return "LoanRepay:TO:" + loanRepayModel.getId();
    }
}
