package com.tuotiansudai.diagnosis.bill.diagnoses;

import com.tuotiansudai.diagnosis.bill.UserBillBusinessDiagnosis;
import com.tuotiansudai.diagnosis.support.DiagnosisContext;
import com.tuotiansudai.diagnosis.support.SingleObjectDiagnosis;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.repository.mapper.UserBillMapper;
import com.tuotiansudai.repository.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InvestTransferInDiagnosis extends UserBillBusinessDiagnosis {
    private static Logger logger = LoggerFactory.getLogger(InvestTransferInDiagnosis.class);

    private final InvestMapper investMapper;

    private final TransferApplicationMapper transferApplicationMapper;

    private final UserBillMapper userBillMapper;

    @Autowired
    public InvestTransferInDiagnosis(InvestMapper investMapper, TransferApplicationMapper transferApplicationMapper, UserBillMapper userBillMapper) {
        this.investMapper = investMapper;
        this.transferApplicationMapper = transferApplicationMapper;
        this.userBillMapper = userBillMapper;
    }

    @Override
    public UserBillBusinessType getSupportedBusinessType() {
        return UserBillBusinessType.INVEST_TRANSFER_IN;
    }

    @Override
    public void diagnosis(UserBillModel userBillModel, DiagnosisContext context) {
        TransferApplicationModel tracedObject = transferApplicationMapper.findByInvestId(userBillModel.getOrderId());
        if (tracedObject == null
                && investMapper.findById(userBillModel.getOrderId()).getStatus() == InvestStatus.OVER_INVEST_PAYBACK
                && userBillMapper.findByOrderIdAndBusinessType(userBillModel.getOrderId(), UserBillBusinessType.OVER_INVEST_PAYBACK).size() == 1
                && userBillMapper.findByOrderIdAndBusinessType(userBillModel.getOrderId(), UserBillBusinessType.INVEST_TRANSFER_IN).size() == 1){
                return;
        }
        String investLoginName = traceInvestLoginName(tracedObject);
        SingleObjectDiagnosis
                // exist
                .init(userBillModel, tracedObject, this::buildTracedObjectId)
                // status
                .check(m -> m.getStatus() == TransferStatus.SUCCESS,
                        m -> String.format("wrong status [expect:SUCCESS, actual:%s]", m.getStatus()))
                // owner
                .check(m -> userBillModel.getLoginName().equals(investLoginName),
                        m -> String.format("wrong owner [expect:%s, actual:%s]", userBillModel.getLoginName(), investLoginName))
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

    private String traceInvestLoginName(TransferApplicationModel transferApplicationModel) {
        if (transferApplicationModel != null) {
            InvestModel investModel = investMapper.findById(transferApplicationModel.getInvestId());
            return investModel == null ? null : investModel.getLoginName();
        }
        return null;
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
        return "TransferApplication:In:" + transferApplicationModel.getId();
    }
}
