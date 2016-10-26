package com.tuotiansudai.diagnosis.bill.diagnoses;

import com.tuotiansudai.coupon.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.diagnosis.bill.UserBillBusinessDiagnosis;
import com.tuotiansudai.diagnosis.support.DiagnosisContext;
import com.tuotiansudai.diagnosis.support.SingleObjectDiagnosis;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.repository.model.UserBillModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.List;

public abstract class UserCouponDiagnosis extends UserBillBusinessDiagnosis {
    private static Logger logger = LoggerFactory.getLogger(UserCouponDiagnosis.class);

    protected final UserCouponMapper userCouponMapper;
    private final CouponRepayMapper couponRepayMapper;

    @Autowired
    public UserCouponDiagnosis(UserCouponMapper userCouponMapper, CouponRepayMapper couponRepayMapper) {
        this.userCouponMapper = userCouponMapper;
        this.couponRepayMapper = couponRepayMapper;
    }

    @Override
    public void diagnosis(UserBillModel userBillModel, DiagnosisContext context) {
        CouponRepayModel tracedObject = findTracedObject(userBillModel);
        diagnosisTracedObject(userBillModel, context, tracedObject);
    }

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
                .check(m -> m.getActualInterest() == userBillModel.getAmount(),
                        m -> String.format("wrong amount [expect: %d, actual: %d]", userBillModel.getAmount(), m.getActualInterest()))
                // result
                .fail(r -> onFail(userBillModel, context, r))
                .success(r -> onPass(userBillModel, context, buildTracedObjectId(tracedObject)));
    }

    public CouponRepayModel findTracedObject(UserBillModel userBillModel) {
        UserCouponModel userCouponModel = userCouponMapper.findById(userBillModel.getOrderId());
        String eventDateString = new SimpleDateFormat("yyyy-MM-dd").format(userBillModel.getCreatedTime());
        List<CouponRepayModel> couponRepayList = couponRepayMapper.findCouponRepayByInvestIdAndRepayDate(userBillModel.getLoginName(),
                userCouponModel.getInvestId(), null, null, eventDateString);
        return (couponRepayList.size() > 0) ? couponRepayList.get(0) : null;
    }

    protected String buildTracedObjectId(CouponRepayModel model) {
        return "CouponRepay:" + model.getId();
    }
}
