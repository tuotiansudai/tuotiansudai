package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.LoanRepayModel;
import com.tuotiansudai.service.RepayService;
import com.tuotiansudai.utils.LoginUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public BaseDto<LoanRepayDataDto> findLoanerLoanRepay(long loanId) {
        String loginName = LoginUserInfo.getLoginName();

        BaseDto<LoanRepayDataDto> baseDto = new BaseDto<>();
        LoanRepayDataDto dataDto = new LoanRepayDataDto();
        baseDto.setData(dataDto);

        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanerAndLoanId(loginName, loanId);
        if (CollectionUtils.isNotEmpty(loanRepayModels)) {
            final LoanRepayModel enabledLoanRepayModel = loanRepayMapper.findEnabledRepayByLoanId(loanId);
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
}
