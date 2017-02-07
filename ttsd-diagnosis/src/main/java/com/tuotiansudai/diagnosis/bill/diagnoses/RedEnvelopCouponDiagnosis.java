package com.tuotiansudai.diagnosis.bill.diagnoses;

import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.diagnosis.support.DiagnosisContext;
import com.tuotiansudai.diagnosis.support.SingleObjectDiagnosis;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.UserBillModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RedEnvelopCouponDiagnosis extends UserCouponDiagnosis {
    private static Logger logger = LoggerFactory.getLogger(RedEnvelopCouponDiagnosis.class);

    private final CouponMapper couponMapper;

    @Autowired
    public RedEnvelopCouponDiagnosis(UserCouponMapper userCouponMapper, CouponRepayMapper couponRepayMapper, CouponMapper couponMapper) {
        super(userCouponMapper, couponRepayMapper);
        this.couponMapper = couponMapper;
    }

    @Override
    public UserBillBusinessType getSupportedBusinessType() {
        return UserBillBusinessType.RED_ENVELOPE;
    }

    @Override
    public void diagnosis(UserBillModel userBillModel, DiagnosisContext context) {
        UserCouponModel tracedObject = userCouponMapper.findById(userBillModel.getOrderId());
        CouponModel couponModel = (tracedObject != null) ? couponMapper.findById(tracedObject.getCouponId()) : null;
        SingleObjectDiagnosis
                // exist
                .init(userBillModel, tracedObject, this::buildTracedObjectId)
                // status
                .check(m -> m.getStatus() == InvestStatus.SUCCESS,
                        m -> String.format("wrong status [expect:SUCCESS, actual:%s]", m.getStatus()))
                // owner
                .check(m -> userBillModel.getLoginName().equals(m.getLoginName()),
                        m -> String.format("wrong owner [expect:%s, actual:%s]", userBillModel.getLoginName(), m.getLoginName()))
                // unique
                .check(m -> !context.hasAlreadyTraced(buildTracedObjectId(m)),
                        m -> String.format("has already traced by UserBill#%d", context.getUserBillId(buildTracedObjectId(m))))
                // amount
                .check(m -> couponModel != null && userBillModel.getAmount() == couponModel.getAmount(),
                        m -> String.format("wrong amount [expect: %d, actual: %d]", userBillModel.getAmount(), couponModel.getAmount()))
                // result
                .fail(r -> onFail(userBillModel, context, r))
                .success(r -> onPass(userBillModel, context, buildTracedObjectId(tracedObject)));
    }

    private String buildTracedObjectId(UserCouponModel model) {
        return "UserCoupon:" + model.getId();
    }
}
