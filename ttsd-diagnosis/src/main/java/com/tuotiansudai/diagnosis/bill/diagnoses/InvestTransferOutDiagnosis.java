package com.tuotiansudai.diagnosis.bill.diagnoses;

import com.tuotiansudai.diagnosis.bill.UserBillBusinessDiagnosis;
import com.tuotiansudai.diagnosis.support.DiagnosisContext;
import com.tuotiansudai.diagnosis.support.SingleObjectDiagnosis;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.repository.model.UserBillModel;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.repository.model.TransferApplicationModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InvestTransferOutDiagnosis extends UserBillBusinessDiagnosis {
    private static Logger logger = LoggerFactory.getLogger(InvestTransferOutDiagnosis.class);

    private final InvestMapper investMapper;

    private final TransferApplicationMapper transferApplicationMapper;

    @Autowired
    public InvestTransferOutDiagnosis(InvestMapper investMapper, TransferApplicationMapper transferApplicationMapper) {
        this.investMapper = investMapper;
        this.transferApplicationMapper = transferApplicationMapper;
    }

    @Override
    public UserBillBusinessType getSupportedBusinessType() {
        return UserBillBusinessType.INVEST_TRANSFER_OUT;
    }

    @Override
    public void diagnosis(UserBillModel userBillModel, DiagnosisContext context) {
        TransferApplicationModel tracedObject = transferApplicationMapper.findById(userBillModel.getOrderId());
        SingleObjectDiagnosis
                // exist
                .init(userBillModel, tracedObject, this::buildTracedObjectId)
                // status
                .check(m -> m.getStatus() == TransferStatus.SUCCESS,
                        m -> String.format("wrong status [expect:SUCCESS, actual:%s]", m.getStatus()))
                // owner
                .check(m -> userBillModel.getLoginName().equals(m.getLoginName()),
                        m -> String.format("wrong owner [expect:%s, actual:%s]", userBillModel.getLoginName(), m.getLoginName()))
                // unique
                .check(m -> !context.hasAlreadyTraced(buildTracedObjectId(m)),
                        m -> String.format("has already traced by UserBill#%d", context.getUserBillId(buildTracedObjectId(m))))
                // amount
                .check(m -> userBillModel.getAmount() == m.getTransferAmount(),
                        m -> String.format("wrong amount [expect: %d, actual: %d]", userBillModel.getAmount(), m.getTransferAmount()))
                // transfer in
                .check(this::checkTransferInOutExists, "can not match success transfer in/out invest record")
                // result
                .fail(r -> onFail(userBillModel, context, r))
                .success(r -> onPass(userBillModel, context, buildTracedObjectId(tracedObject)));
    }

    private boolean checkTransferInOutExists(TransferApplicationModel transferApplicationModel) {
        InvestModel transferOutInvest = investMapper.findById(transferApplicationModel.getTransferInvestId());
        InvestModel transferInInvest = investMapper.findById(transferApplicationModel.getInvestId());
        return transferOutInvest != null &&
                transferInInvest != null &&
                transferOutInvest.getStatus() == InvestStatus.SUCCESS &&
                transferInInvest.getStatus() == InvestStatus.SUCCESS &&
                transferInInvest.getAmount() == transferOutInvest.getAmount();
    }

    private String buildTracedObjectId(TransferApplicationModel transferApplicationModel) {
        return "TransferApplication:Out:" + transferApplicationModel.getId();
    }
}
