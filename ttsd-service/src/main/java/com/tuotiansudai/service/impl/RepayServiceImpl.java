package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestRepayModel;
import com.tuotiansudai.repository.model.LoanRepayModel;
import com.tuotiansudai.service.RepayService;
import com.tuotiansudai.utils.LoginUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RepayServiceImpl implements RepayService {

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private InvestMapper investMapper;

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

    @Override
    public BaseDto<InvestRepayDataDto> findInvestorInvestRepay(long investId) {
        BaseDto<InvestRepayDataDto> baseDto = new BaseDto<>();
        InvestRepayDataDto dataDto = new InvestRepayDataDto();
        baseDto.setData(dataDto);

        String loginName = LoginUserInfo.getLoginName();
        if(StringUtils.isBlank(loginName)) {
            throw new InsufficientAuthenticationException("not login");
        }
        InvestModel investModel = investMapper.findById(investId);
        if(!StringUtils.equalsIgnoreCase(loginName, investModel.getLoginName())) {
            throw new InsufficientAuthenticationException("denied access the invest repay");
        }

        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestId(investId);
        if (CollectionUtils.isNotEmpty(investRepayModels)) {
            List<InvestRepayDataItemDto> records = Lists.transform(investRepayModels, new Function<InvestRepayModel, InvestRepayDataItemDto>() {
                @Override
                public InvestRepayDataItemDto apply(InvestRepayModel investRepayModel) {
                    return new InvestRepayDataItemDto(investRepayModel);
                }
            });
            dataDto.setStatus(true);
            dataDto.setRecords(records);
        }else{
            dataDto.setStatus(true);
            dataDto.setRecords(new ArrayList());
        }
        return baseDto;
    }
}
