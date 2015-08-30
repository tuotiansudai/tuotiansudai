package com.tuotiansudai.console.service.impl;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.console.service.LoanService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.utils.AmountUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class LoanServiceImpl implements LoanService {
    @Autowired
    private com.tuotiansudai.service.LoanService loanService;
    @Autowired
    private PayWrapperClient payWrapperClient;
    @Autowired
    private AccountMapper accountMapper;
    @Override
    public BaseDto<PayFormDataDto> editLoan(LoanDto loanDto) {
        return loanService.updateLoan(loanDto);
    }

    @Override
    public BaseDto<PayFormDataDto> firstTrialPassed(LoanDto loanDto) {
        loanDto.setFirstTrialTime(new Date());
        BaseDto<PayFormDataDto> baseDto = loanParamValidate(loanDto);
        if (baseDto.getData().getStatus()){
            return payWrapperClient.loan(loanDto);
        }
        return baseDto;
    }

    @Override
    public BaseDto<PayFormDataDto> firstTrialRefused(LoanDto loanDto) {
        loanDto.setFirstTrialTime(new Date());
        BaseDto<PayFormDataDto> baseDto = loanParamValidate(loanDto);
        if (baseDto.getData().getStatus()){
            return loanService.updateLoan(loanDto);
        }
        return baseDto;
    }

    private BaseDto<PayFormDataDto> loanParamValidate(LoanDto loanDto){
        BaseDto<PayFormDataDto> baseDto = new BaseDto();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        if (loanDto.getFundraisingStartTime() == null || loanDto.getFundraisingEndTime() == null) {
            payFormDataDto.setStatus(false);
            baseDto.setData(payFormDataDto);
            return baseDto;
        }
        long minInvestAmount = AmountUtil.convertStringToCent(loanDto.getMinInvestAmount());
        long maxInvestAmount = AmountUtil.convertStringToCent(loanDto.getMaxInvestAmount());
        long loanAmount = AmountUtil.convertStringToCent(loanDto.getLoanAmount());
        if (maxInvestAmount < minInvestAmount) {
            payFormDataDto.setStatus(false);
            baseDto.setData(payFormDataDto);
            return baseDto;
        }
        if (maxInvestAmount > loanAmount){
            payFormDataDto.setStatus(false);
            baseDto.setData(payFormDataDto);
            return baseDto;
        }
        if (loanDto.getFundraisingEndTime().before(loanDto.getFundraisingStartTime())) {
            payFormDataDto.setStatus(false);
            baseDto.setData(payFormDataDto);
            return baseDto;
        }
        String loanUserId = getLoginName(loanDto.getLoanerLoginName());
        if (loanUserId == null) {
            payFormDataDto.setStatus(false);
            baseDto.setData(payFormDataDto);
            return baseDto;
        }
        String loanAgentId = getLoginName(loanDto.getAgentLoginName());
        if (loanAgentId == null) {
            payFormDataDto.setStatus(false);
            baseDto.setData(payFormDataDto);
            return baseDto;
        }
        payFormDataDto.setStatus(true);
        baseDto.setData(payFormDataDto);
        return baseDto;
    }

    private String getLoginName(String loginName) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        String loanUserId = null;
        if (accountModel != null) {
            loanUserId = accountModel.getPayUserId();
        }
        return loanUserId;
    }
}
