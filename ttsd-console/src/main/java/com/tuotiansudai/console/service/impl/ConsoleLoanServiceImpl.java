package com.tuotiansudai.console.service.impl;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.console.service.ConsoleLoanService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.utils.AmountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ConsoleLoanServiceImpl implements ConsoleLoanService {
    @Autowired
    private com.tuotiansudai.service.LoanService loanService;
    @Autowired
    private PayWrapperClient payWrapperClient;
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public BaseDto<PayDataDto> editLoan(LoanDto loanDto) {
        loanDto.setFirstTrialTime(new Date());
        BaseDto<PayDataDto> baseDto = loanParamValidate(loanDto);
        if (baseDto.getData().getStatus()) {
            return loanService.updateLoan(loanDto);
        }
        return baseDto;

    }

    @Override
    public BaseDto<PayDataDto> firstTrialPassed(LoanDto loanDto) {
        loanDto.setFirstTrialTime(new Date());
        BaseDto<PayDataDto> baseDto = loanParamValidate(loanDto);
        if (baseDto.getData().getStatus()) {
            return payWrapperClient.loan(loanDto);
        }
        return baseDto;
    }

    @Override
    public BaseDto<PayDataDto> firstTrialRefused(LoanDto loanDto) {
        loanDto.setFirstTrialTime(new Date());
        BaseDto<PayDataDto> baseDto = loanParamValidate(loanDto);
        if (baseDto.getData().getStatus()) {
            return loanService.updateLoan(loanDto);
        }
        return baseDto;
    }

    private BaseDto<PayDataDto> loanParamValidate(LoanDto loanDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto();
        PayDataDto payDataDto = new PayDataDto();
        if (loanDto.getFundraisingStartTime() == null || loanDto.getFundraisingEndTime() == null) {
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        long minInvestAmount = AmountUtil.convertStringToCent(loanDto.getMinInvestAmount());
        long maxInvestAmount = AmountUtil.convertStringToCent(loanDto.getMaxInvestAmount());
        long loanAmount = AmountUtil.convertStringToCent(loanDto.getLoanAmount());
        if (maxInvestAmount < minInvestAmount) {
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        if (maxInvestAmount > loanAmount) {
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        if (loanDto.getFundraisingEndTime().before(loanDto.getFundraisingStartTime())) {
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        String loanUserId = getLoginName(loanDto.getLoanerLoginName());
        if (loanUserId == null) {
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        String loanAgentId = getLoginName(loanDto.getAgentLoginName());
        if (loanAgentId == null) {
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        payDataDto.setStatus(true);
        baseDto.setData(payDataDto);
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
