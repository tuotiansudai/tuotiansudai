package com.tuotiansudai.diagnosis.bill.diagnoses;

import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.mapper.WithdrawMapper;
import com.tuotiansudai.repository.model.WithdrawModel;
import com.tuotiansudai.repository.model.WithdrawStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class WithdrawApplyDiagnosis extends WithdrawDiagnosis {
    private static Logger logger = LoggerFactory.getLogger(WithdrawApplyDiagnosis.class);

    @Autowired
    public WithdrawApplyDiagnosis(WithdrawMapper withdrawMapper) {
        super(withdrawMapper);
    }

    @Override
    public UserBillBusinessType getSupportedBusinessType() {
        return UserBillBusinessType.APPLY_WITHDRAW;
    }

    @Override
    protected boolean checkStatus(WithdrawModel withdrawModel) {
        return Arrays
                .asList(WithdrawStatus.SUCCESS, WithdrawStatus.APPLY_SUCCESS, WithdrawStatus.FAIL)
                .contains(withdrawModel.getStatus());
    }

    @Override
    protected String buildTracedObjectId(WithdrawModel withdrawModel) {
        return "Withdraw:Apply:" + withdrawModel.getId();
    }
}
