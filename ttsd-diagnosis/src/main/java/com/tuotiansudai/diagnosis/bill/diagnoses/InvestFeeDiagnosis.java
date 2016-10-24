package com.tuotiansudai.diagnosis.bill.diagnoses;

import com.tuotiansudai.diagnosis.bill.UserBillBusinessDiagnosis;
import com.tuotiansudai.diagnosis.support.DiagnosisContext;
import com.tuotiansudai.diagnosis.support.SingleObjectDiagnosis;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.model.InvestRepayModel;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.repository.model.UserBillModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InvestFeeDiagnosis extends UserBillBusinessDiagnosis {
    private static Logger logger = LoggerFactory.getLogger(InvestFeeDiagnosis.class);

    private final InvestRepayMapper investRepayMapper;

    @Autowired
    public InvestFeeDiagnosis(InvestRepayMapper investRepayMapper) {
        this.investRepayMapper = investRepayMapper;
    }

    @Override
    public UserBillBusinessType getSupportedBusinessType() {
        return UserBillBusinessType.INVEST_FEE;
    }

    @Override
    public void diagnosis(UserBillModel userBillModel, DiagnosisContext context) {
        InvestRepayModel tracedObject = investRepayMapper.findById(userBillModel.getOrderId());
        SingleObjectDiagnosis
                // exist
                .init(tracedObject, this::buildTracedObjectId)
                // status
                .check(m -> m.getStatus() == RepayStatus.COMPLETE,
                        m -> String.format("wrong status [expect:COMPLETE, actual:%s]", m.getStatus()))
                // unique
                .check(m -> !context.hasAlreadyTraced(buildTracedObjectId(m)),
                        m -> String.format("has already traced by UserBill#%d", context.getUserBillId(buildTracedObjectId(m))))
                // amount
                .check(m -> userBillModel.getAmount() == m.getActualFee(),
                        m -> String.format("wrong amount [expect: %d, actual: %d]", userBillModel.getAmount(), m.getActualFee()))
                // result
                .fail(r -> onFail(userBillModel, context, r))
                .success(r -> onPass(userBillModel, context, buildTracedObjectId(tracedObject)));
    }

    private String buildTracedObjectId(InvestRepayModel investRepayModel) {
        return "InvestRepay:Fee:" + investRepayModel.getId();
    }
}
