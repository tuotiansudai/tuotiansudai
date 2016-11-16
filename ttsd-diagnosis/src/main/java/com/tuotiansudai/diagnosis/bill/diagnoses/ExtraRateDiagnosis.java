package com.tuotiansudai.diagnosis.bill.diagnoses;

import com.tuotiansudai.diagnosis.bill.UserBillBusinessDiagnosis;
import com.tuotiansudai.diagnosis.repository.InvestExtraRateExtMapper;
import com.tuotiansudai.diagnosis.support.DiagnosisContext;
import com.tuotiansudai.diagnosis.support.SingleObjectDiagnosis;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestExtraRateModel;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.UserBillModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExtraRateDiagnosis extends UserBillBusinessDiagnosis {
    private static Logger logger = LoggerFactory.getLogger(ExtraRateDiagnosis.class);

    private final InvestMapper investMapper;
    private final InvestExtraRateExtMapper investExtraRateExtMapper;

    @Autowired
    public ExtraRateDiagnosis(InvestMapper investMapper, InvestExtraRateExtMapper investExtraRateExtMapper) {
        this.investMapper = investMapper;
        this.investExtraRateExtMapper = investExtraRateExtMapper;
    }

    @Override
    public UserBillBusinessType getSupportedBusinessType() {
        return UserBillBusinessType.EXTRA_RATE;
    }

    @Override
    public void diagnosis(UserBillModel userBillModel, DiagnosisContext context) {
        InvestExtraRateModel tracedObject = investExtraRateExtMapper.findById(userBillModel.getOrderId());
        SingleObjectDiagnosis
                // exist
                .init(userBillModel, tracedObject, this::buildTracedObjectId)
                // status
                .check(this::checkInvestStatus,
                        m -> String.format("wrong status [expect:SUCCESS, actual:%s]", this.getInvestStatus(m)))
                // owner
                .check(m -> userBillModel.getLoginName().equals(m.getLoginName()),
                        m -> String.format("wrong owner [expect:%s, actual:%s]", userBillModel.getLoginName(), m.getLoginName()))
                // unique
                .check(m -> !context.hasAlreadyTraced(buildTracedObjectId(m)),
                        m -> String.format("has already traced by UserBill#%d", context.getUserBillId(buildTracedObjectId(m))))
                // amount
                .check(m -> userBillModel.getAmount() == m.getActualInterest() - m.getActualFee(),
                        m -> String.format("wrong amount [expect: %d, actual: %d]", userBillModel.getAmount(), m.getActualInterest() - m.getActualFee()))
                // result
                .fail(r -> onFail(userBillModel, context, r))
                .success(r -> onPass(userBillModel, context, buildTracedObjectId(tracedObject)));
    }

    private InvestStatus getInvestStatus(InvestExtraRateModel investExtraRateModel) {
        InvestModel investModel = investMapper.findById(investExtraRateModel.getInvestId());
        return investModel == null ? null : investModel.getStatus();
    }

    private boolean checkInvestStatus(InvestExtraRateModel investExtraRateModel) {
        InvestModel investModel = investMapper.findById(investExtraRateModel.getInvestId());
        return investModel != null && investModel.getStatus() == InvestStatus.SUCCESS;
    }

    private String buildTracedObjectId(InvestExtraRateModel investExtraRateModel) {
        return "InvestExtraRate:" + investExtraRateModel.getId();
    }
}
