package com.tuotiansudai.diagnosis.bill.diagnoses;

import com.tuotiansudai.diagnosis.bill.UserBillBusinessDiagnosis;
import com.tuotiansudai.diagnosis.repository.UserBillExtMapper;
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

import java.util.List;

@Component
public class InvestDiagnosis extends UserBillBusinessDiagnosis {
    private static Logger logger = LoggerFactory.getLogger(InvestDiagnosis.class);

    private final InvestMapper investMapper;
    private final UserBillExtMapper userBillExtMapper;

    @Autowired
    public InvestDiagnosis(InvestMapper investMapper, UserBillExtMapper userBillExtMapper) {
        this.investMapper = investMapper;
        this.userBillExtMapper = userBillExtMapper;
    }

    @Override
    public UserBillBusinessType getSupportedBusinessType() {
        return UserBillBusinessType.INVEST_SUCCESS;
    }

    @Override
    public void diagnosis(UserBillModel userBillModel, DiagnosisContext context) {
        InvestModel tracedObject = investMapper.findById(userBillModel.getOrderId());
        SingleObjectDiagnosis
                // exist
                .init(userBillModel, tracedObject, this::buildTracedObjectId)
                // status
                .check(this::investTraceCheck,
                        m -> String.format("wrong status [expect:SUCCESS, actual:%s]", m.getStatus()))
                // owner
                .check(m -> userBillModel.getLoginName().equals(m.getLoginName()),
                        m -> String.format("wrong owner [expect:%s, actual:%s]", userBillModel.getLoginName(), m.getLoginName()))
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

    private boolean investTraceCheck(InvestModel investModel) {
        if (investModel.getStatus() == InvestStatus.CANCEL_INVEST_PAYBACK) {
            return cancelInvestPayBackTraceCheck(investModel);
        }
        if (investModel.getStatus() == InvestStatus.OVER_INVEST_PAYBACK) {
            return overInvestPayBackTraceCheck(investModel);
        }
        return investModel.getStatus() == InvestStatus.SUCCESS;
    }

    private boolean cancelInvestPayBackTraceCheck(InvestModel investModel) {
        List<UserBillModel> userBillModelList = userBillExtMapper.findByOrderIdAndBusinessType(investModel.getId(), UserBillBusinessType.CANCEL_INVEST_PAYBACK);
        return userBillModelList.size() == 1 && userBillModelList.get(0).getAmount() == investModel.getAmount();
    }

    private boolean overInvestPayBackTraceCheck(InvestModel investModel) {
        List<UserBillModel> successUserBillModelList = userBillExtMapper.findByOrderIdAndBusinessType(investModel.getId(), UserBillBusinessType.INVEST_SUCCESS);
        List<UserBillModel> payBackUserBillModelList = userBillExtMapper.findByOrderIdAndBusinessType(investModel.getId(), UserBillBusinessType.OVER_INVEST_PAYBACK);
        return successUserBillModelList.size() == 1 && successUserBillModelList.get(0).getAmount() == investModel.getAmount() &&
                payBackUserBillModelList.size() == 1 && payBackUserBillModelList.get(0).getAmount() == investModel.getAmount();
    }

    private String buildTracedObjectId(InvestModel investModel) {
        return "Invest:" + investModel.getId();
    }
}
