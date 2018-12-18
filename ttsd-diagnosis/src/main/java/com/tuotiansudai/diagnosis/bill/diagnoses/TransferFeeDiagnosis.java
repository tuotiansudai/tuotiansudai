package com.tuotiansudai.diagnosis.bill.diagnoses;

import com.tuotiansudai.diagnosis.bill.UserBillBusinessDiagnosis;
import com.tuotiansudai.diagnosis.support.DiagnosisContext;
import com.tuotiansudai.diagnosis.support.SingleObjectDiagnosis;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.mapper.UserBillMapper;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.repository.model.UserBillModel;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.repository.model.TransferApplicationModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransferFeeDiagnosis extends UserBillBusinessDiagnosis {
    private static Logger logger = LoggerFactory.getLogger(TransferFeeDiagnosis.class);

    private final TransferApplicationMapper transferApplicationMapper;

    private final UserBillMapper userBillMapper;

    @Autowired
    public TransferFeeDiagnosis(TransferApplicationMapper transferApplicationMapper, UserBillMapper userBillMapper) {
        this.transferApplicationMapper = transferApplicationMapper;
        this.userBillMapper = userBillMapper;
    }

    @Override
    public UserBillBusinessType getSupportedBusinessType() {
        return UserBillBusinessType.TRANSFER_FEE;
    }

    @Override
    public void diagnosis(UserBillModel userBillModel, DiagnosisContext context) {
        TransferApplicationModel tracedObject = transferApplicationMapper.findById(userBillModel.getOrderId());
        long transferInterestAmount = userBillMapper.findByOrderIdAndBusinessType(userBillModel.getOrderId(), UserBillBusinessType.TRANSFER_INVEST_FEE).stream().mapToLong(UserBillModel::getAmount).sum();
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
                .check(m -> userBillModel.getAmount() + transferInterestAmount == m.getTransferFee() + m.getInterestFee(),
                        m -> String.format("wrong amount [expect: %d, actual: %d]", userBillModel.getAmount() + transferInterestAmount, m.getTransferFee() + m.getInterestFee()))
                // result
                .fail(r -> onFail(userBillModel, context, r))
                .success(r -> onPass(userBillModel, context, buildTracedObjectId(tracedObject)));
    }

    private String buildTracedObjectId(TransferApplicationModel transferApplicationModel) {
        return "Transfer:Fee:" + transferApplicationModel.getId();
    }
}
