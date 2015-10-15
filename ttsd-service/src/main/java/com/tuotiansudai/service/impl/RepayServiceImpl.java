package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.LoanRepayModel;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.service.RepayService;
import com.tuotiansudai.utils.LoginUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RepayServiceImpl implements RepayService {

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Override
    public BaseDto<PayFormDataDto> repay(RepayDto repayDto) {
        return payWrapperClient.repay(repayDto);
    }

    @Override
    public BaseDto<LoanRepayDataDto> getLoanRepay(long loanId) {
        this.resetExpiredLoanRepay(loanId);

        String loginName = LoginUserInfo.getLoginName();

        BaseDto<LoanRepayDataDto> baseDto = new BaseDto<>();
        LoanRepayDataDto dataDto = new LoanRepayDataDto();
        baseDto.setData(dataDto);

        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByAgentAndLoanId(loginName, loanId);
        if (CollectionUtils.isNotEmpty(loanRepayModels)) {
            final LoanRepayModel enabledLoanRepayModel = loanRepayMapper.findEnabledLoanRepayByLoanId(loanId);
            List<LoanRepayDataItemDto> records = Lists.transform(loanRepayModels, new Function<LoanRepayModel, LoanRepayDataItemDto>() {
                @Override
                public LoanRepayDataItemDto apply(LoanRepayModel loanRepayModel) {
                    boolean isEnable = loanRepayModel.getId() == enabledLoanRepayModel.getId();
                    return new LoanRepayDataItemDto(loanRepayModel, isEnable);
                }
            });
            dataDto.setStatus(true);
            dataDto.setRecords(records);
        }

        return baseDto;
    }

    @Transactional
    private void resetExpiredLoanRepay(long loanId) {
        DateTime now = new DateTime();
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanId);

        if (CollectionUtils.isEmpty(loanRepayModels)) {
            return;
        }

        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            if (loanRepayModel.getStatus() == RepayStatus.CONFIRMING) {
                DateTime actualRepayDate = new DateTime(loanRepayModel.getActualRepayDate());
                if (actualRepayDate.plusMinutes(30).isAfter(now)) {
                    loanRepayModel.setStatus(RepayStatus.REPAYING);
                    loanRepayModel.setActualRepayDate(null);
                    loanRepayModel.setActualInterest(0);
                    loanRepayMapper.update(loanRepayModel);
                }
            }
        }
    }
}
