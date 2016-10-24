package com.tuotiansudai.diagnosis.bill.diagnoses;

import com.tuotiansudai.diagnosis.bill.UserBillBusinessDiagnosis;
import com.tuotiansudai.diagnosis.support.DiagnosisContext;
import com.tuotiansudai.diagnosis.support.SingleObjectDiagnosis;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.UserBillModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OverInvestPayBackDiagnosis extends UserBillBusinessDiagnosis {
    private static Logger logger = LoggerFactory.getLogger(OverInvestPayBackDiagnosis.class);

    private final InvestMapper investMapper;

    @Autowired
    public OverInvestPayBackDiagnosis(InvestMapper investMapper) {
        this.investMapper = investMapper;
    }

    @Override
    public UserBillBusinessType getSupportedBusinessType() {
        return UserBillBusinessType.OVER_INVEST_PAYBACK;
    }

    @Override
    public void diagnosis(UserBillModel userBillModel, DiagnosisContext context) {
        InvestModel tracedObject = investMapper.findById(userBillModel.getOrderId());
        SingleObjectDiagnosis
                // exist
                .init(tracedObject, this::buildTracedObjectId)
                // status
                .check(m -> m.getStatus() == InvestStatus.OVER_INVEST_PAYBACK,
                        m -> String.format("wrong status [expect:OVER_INVEST_PAYBACK, actual:%s]", m.getStatus()))
                // unique
                .check(m -> !context.hasAlreadyTraced(buildTracedObjectId(m)),
                        m -> String.format("has already traced by UserBill#%d", context.getUserBillId(buildTracedObjectId(m))))
                // amount
                .check(m -> userBillModel.getAmount() == m.getAmount(),
                        m -> String.format("wrong amount [expect: %d, actual: %d]", userBillModel.getAmount(), m.getAmount()))
                // result
                .fail(r -> onFail(userBillModel, context, r))
                .success(r -> onPass(userBillModel, context, buildTracedObjectId(tracedObject)));
    }

    private String buildTracedObjectId(InvestModel investModel) {
        return "Invest:Over:" + investModel.getId();
    }
}
