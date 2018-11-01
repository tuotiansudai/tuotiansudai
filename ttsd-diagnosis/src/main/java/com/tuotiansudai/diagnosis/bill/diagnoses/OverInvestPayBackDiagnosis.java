package com.tuotiansudai.diagnosis.bill.diagnoses;

import com.tuotiansudai.diagnosis.bill.UserBillBusinessDiagnosis;
import com.tuotiansudai.diagnosis.support.DiagnosisContext;
import com.tuotiansudai.diagnosis.support.SingleObjectDiagnosis;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.TransferApplicationModel;
import com.tuotiansudai.repository.model.UserBillModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OverInvestPayBackDiagnosis extends UserBillBusinessDiagnosis {
    private static Logger logger = LoggerFactory.getLogger(OverInvestPayBackDiagnosis.class);

    private final InvestMapper investMapper;

    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    public OverInvestPayBackDiagnosis(InvestMapper investMapper,TransferApplicationMapper transferApplicationMapper) {
        this.investMapper = investMapper;
        this.transferApplicationMapper=transferApplicationMapper;
    }

    @Override
    public UserBillBusinessType getSupportedBusinessType() {
        return UserBillBusinessType.OVER_INVEST_PAYBACK;
    }

    @Override
    public void diagnosis(UserBillModel userBillModel, DiagnosisContext context) {
        InvestModel tracedObject = investMapper.findById(userBillModel.getOrderId());
        if(tracedObject.getTransferInvestId() != null){
            TransferApplicationModel currentInvestTransferModel= transferApplicationMapper.findTransfersDescByTransferInvestId(tracedObject.getTransferInvestId())
                    .stream()
                    .filter(transferApplicationModel -> tracedObject.getCreatedTime().after(transferApplicationModel.getApplicationTime())).findFirst().orElse(null);
            tracedObject.setAmount(currentInvestTransferModel.getTransferAmount());
        }
        SingleObjectDiagnosis
                // exist
                .init(userBillModel, tracedObject, this::buildTracedObjectId)
                // status
                .check(m -> m.getStatus() == InvestStatus.OVER_INVEST_PAYBACK,
                        m -> String.format("wrong status [expect:OVER_INVEST_PAYBACK, actual:%s]", m.getStatus()))
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

    private String buildTracedObjectId(InvestModel investModel) {
        return "Invest:Over:" + investModel.getId();
    }
}
