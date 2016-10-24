package com.tuotiansudai.diagnosis.bill;

import com.tuotiansudai.diagnosis.support.DiagnosisContext;
import com.tuotiansudai.diagnosis.support.SingleObjectDiagnosisResult;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.model.UserBillModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class UserBillBusinessDiagnosis {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public abstract UserBillBusinessType getSupportedBusinessType();

    public abstract void diagnosis(UserBillModel userBillModel, DiagnosisContext context);

    protected void onFail(UserBillModel userBillModel, DiagnosisContext context, SingleObjectDiagnosisResult result) {
        context.addProblem(result.getProblem());
        logger.error("diagnosis UserBill [{}#{}] abnormal: {}", userBillModel.getBusinessType(), userBillModel.getId(), result.getProblem());
    }

    protected void onPass(UserBillModel userBillModel, DiagnosisContext context, String tracedObjectId) {
        context.addTracedObject(userBillModel.getId(), tracedObjectId);
        logger.debug("diagnosis UserBill [{}#{}] fine", userBillModel.getBusinessType(), userBillModel.getId());
    }
}
