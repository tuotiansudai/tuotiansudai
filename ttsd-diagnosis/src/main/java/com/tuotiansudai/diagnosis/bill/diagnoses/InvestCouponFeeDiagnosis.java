package com.tuotiansudai.diagnosis.bill.diagnoses;

import com.tuotiansudai.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.CouponRepayModel;
import com.tuotiansudai.diagnosis.support.DiagnosisContext;
import com.tuotiansudai.diagnosis.support.SingleObjectDiagnosis;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.repository.model.UserBillModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InvestCouponFeeDiagnosis extends UserCouponDiagnosis {
    private static Logger logger = LoggerFactory.getLogger(InvestCouponFeeDiagnosis.class);

    @Autowired
    public InvestCouponFeeDiagnosis(UserCouponMapper userCouponMapper, CouponRepayMapper couponRepayMapper) {
        super(userCouponMapper, couponRepayMapper);
    }

    @Override
    public UserBillBusinessType getSupportedBusinessType() {
        return UserBillBusinessType.INVEST_FEE;
    }

    @Override
    public void diagnosisTracedObject(UserBillModel userBillModel, DiagnosisContext context, CouponRepayModel tracedObject) {
        SingleObjectDiagnosis
                // exist
                .init(userBillModel, tracedObject, this::buildTracedObjectId)
                // status
                .check(m -> m.getStatus() == RepayStatus.COMPLETE,
                        m -> String.format("wrong status [expect:COMPLETE, actual:%s]", m.getStatus()))
                // owner
                .check(m -> userBillModel.getLoginName().equals(m.getLoginName()),
                        m -> String.format("wrong owner [expect:%s, actual:%s]", userBillModel.getLoginName(), m.getLoginName()))
                // unique
                .check(m -> !context.hasAlreadyTraced(buildTracedObjectId(m)),
                        m -> String.format("has already traced by UserBill#%d", context.getUserBillId(buildTracedObjectId(m))))
                // amount
                .check(m -> m.getActualFee() == userBillModel.getAmount(),
                        m -> String.format("wrong amount [expect: %d, actual: %d]", userBillModel.getAmount(), m.getActualInterest()))
                // result
                .fail(r -> onFail(userBillModel, context, r))
                .success(r -> onPass(userBillModel, context, buildTracedObjectId(tracedObject)));
    }

    protected String buildTracedObjectId(CouponRepayModel model) {
        return "CouponRepay:Fee:" + model.getId();
    }
}
