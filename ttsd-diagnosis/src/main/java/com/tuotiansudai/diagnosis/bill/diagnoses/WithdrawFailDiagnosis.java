package com.tuotiansudai.diagnosis.bill.diagnoses;

import com.tuotiansudai.diagnosis.bill.UserBillBusinessDiagnosis;
import com.tuotiansudai.diagnosis.support.DiagnosisContext;
import com.tuotiansudai.diagnosis.support.SingleObjectDiagnosis;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.mapper.WithdrawMapper;
import com.tuotiansudai.repository.model.UserBillModel;
import com.tuotiansudai.repository.model.WithdrawModel;
import com.tuotiansudai.repository.model.WithdrawStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WithdrawFailDiagnosis extends UserBillBusinessDiagnosis {
    private static Logger logger = LoggerFactory.getLogger(WithdrawFailDiagnosis.class);

    private final WithdrawMapper withdrawMapper;

    @Autowired
    public WithdrawFailDiagnosis(WithdrawMapper withdrawMapper) {
        this.withdrawMapper = withdrawMapper;
    }

    @Override
    public UserBillBusinessType getSupportedBusinessType() {
        return UserBillBusinessType.WITHDRAW_FAIL;
    }

    @Override
    public void diagnosis(UserBillModel userBillModel, DiagnosisContext context) {
        WithdrawModel tracedObject = withdrawMapper.findById(userBillModel.getOrderId());
        SingleObjectDiagnosis
                // exist
                .init(userBillModel, tracedObject, this::buildTracedObjectId)
                // status
                .check(m -> m.getStatus() == WithdrawStatus.FAIL,
                        m -> String.format("wrong status [expect:FAIL, actual:%s]", m.getStatus()))
                // owner
                .check(m -> userBillModel.getLoginName().equals(m.getLoginName()),
                        m -> String.format("wrong owner [expect:%s, actual:%s]", userBillModel.getLoginName(), m.getLoginName()))
                // unique
                .check(m -> !context.hasAlreadyTraced(buildTracedObjectId(m)),
                        m -> String.format("has already traced by UserBill#%d", context.getUserBillId(buildTracedObjectId(m))))
                // amount
                .check(m -> userBillModel.getAmount() == m.getAmount(),
                        m -> String.format("wrong amount [expect: %d, actual: %d]", userBillModel.getAmount(), m.getAmount()))
                // on fail
                .fail(r -> onFail(userBillModel, context, r))
                .success(r -> onPass(userBillModel, context, buildTracedObjectId(tracedObject)));
    }

    protected String buildTracedObjectId(WithdrawModel withdrawModel) {
        return "Withdraw:" + withdrawModel.getId();
    }
}
