package com.tuotiansudai.diagnosis.bill.diagnoses;

import com.tuotiansudai.diagnosis.bill.UserBillBusinessDiagnosis;
import com.tuotiansudai.diagnosis.support.DiagnosisContext;
import com.tuotiansudai.diagnosis.support.SingleObjectDiagnosis;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.mapper.InvestReferrerRewardMapper;
import com.tuotiansudai.repository.model.InvestReferrerRewardModel;
import com.tuotiansudai.repository.model.ReferrerRewardStatus;
import com.tuotiansudai.repository.model.UserBillModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReferrerRewardDiagnosis extends UserBillBusinessDiagnosis {
    private static Logger logger = LoggerFactory.getLogger(ReferrerRewardDiagnosis.class);

    private final InvestReferrerRewardMapper investReferrerRewardMapper;

    @Autowired
    public ReferrerRewardDiagnosis(InvestReferrerRewardMapper investReferrerRewardMapper) {
        this.investReferrerRewardMapper = investReferrerRewardMapper;
    }

    @Override
    public UserBillBusinessType getSupportedBusinessType() {
        return UserBillBusinessType.REFERRER_REWARD;
    }

    @Override
    public void diagnosis(UserBillModel userBillModel, DiagnosisContext context) {
        InvestReferrerRewardModel tracedObject = investReferrerRewardMapper.findById(userBillModel.getOrderId());
        SingleObjectDiagnosis
                // exist
                .init(tracedObject, this::buildTracedObjectId)
                // status
                .check(m -> m.getStatus() == ReferrerRewardStatus.SUCCESS,
                        m -> String.format("wrong status [expect:SUCCESS, actual:%s]", m.getStatus()))
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

    private String buildTracedObjectId(InvestReferrerRewardModel investReferrerRewardModel) {
        return "Referrer:Reward:" + investReferrerRewardModel.getId();
    }
}
