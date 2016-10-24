package com.tuotiansudai.diagnosis.bill.diagnoses;

import com.tuotiansudai.coupon.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.diagnosis.bill.UserBillBusinessDiagnosis;
import com.tuotiansudai.diagnosis.repository.UserBillExtMapper;
import com.tuotiansudai.diagnosis.support.DiagnosisContext;
import com.tuotiansudai.diagnosis.support.SingleObjectDiagnosis;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.repository.model.UserBillModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class UserCouponDiagnosis extends UserBillBusinessDiagnosis {
    private static Logger logger = LoggerFactory.getLogger(UserCouponDiagnosis.class);

    protected final UserCouponMapper userCouponMapper;
    private final CouponRepayMapper couponRepayMapper;
    @Autowired
    private UserBillExtMapper userBillExtMapper;

    @Autowired
    public UserCouponDiagnosis(UserCouponMapper userCouponMapper, CouponRepayMapper couponRepayMapper) {
        this.userCouponMapper = userCouponMapper;
        this.couponRepayMapper = couponRepayMapper;
    }

    @Override
    public void diagnosis(UserBillModel userBillModel, DiagnosisContext context) {
        UserCouponModel tracedObject = userCouponMapper.findById(userBillModel.getOrderId());
        long expectAmount = getExpectAmount(tracedObject);
        long actualAmount = getActualAmount(tracedObject);
        SingleObjectDiagnosis
                // exist
                .init(tracedObject, this::buildTracedObjectId)
                // status
                .check(m -> m.getStatus() == InvestStatus.SUCCESS,
                        m -> String.format("wrong status [expect:SUCCESS, actual:%s]", m.getStatus()))
                // unique
                .check(m -> !context.hasAlreadyTraced(buildTracedObjectId(m)),
                        m -> String.format("has already traced by UserBill#%d", context.getUserBillId(buildTracedObjectId(m))))
                // amount
                .check(m -> expectAmount == actualAmount,
                        m -> String.format("wrong amount [expect: %d, actual: %d]", expectAmount, actualAmount))
                // result
                .fail(r -> onFail(userBillModel, context, r))
                .success(r -> onPass(userBillModel, context, buildTracedObjectId(tracedObject)));
    }

    private long getExpectAmount(UserCouponModel userCouponModel) {
        List<UserBillModel> userBillList = userBillExtMapper.findByOrderIdAndBusinessType(userCouponModel.getId(), getSupportedBusinessType());
        return userBillList.stream()
                .reduce(0L, (amount, userbill) -> amount + userbill.getAmount(), Math::addExact);
    }

    private long getActualAmount(UserCouponModel userCouponModel) {
        if (userCouponModel == null) {
            return 0L;
        }
        List<CouponRepayModel> couponRepayList = couponRepayMapper.findByUserCouponByInvestId(userCouponModel.getInvestId());
        return couponRepayList.stream()
                .filter(r -> r.getStatus() == RepayStatus.COMPLETE)
                .reduce(0L, (amount, repay) -> amount + repay.getActualInterest(), Math::addExact);
    }

    protected String buildTracedObjectId(UserCouponModel model) {
        return "UserCoupon:" + model.getId();
    }
}
