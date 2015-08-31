package com.tuotiansudai.service.impl;

import com.tuotiansudai.dto.ApplyMaterialDto;
import com.tuotiansudai.dto.InvestRecordRequestDto;
import com.tuotiansudai.dto.InvestRecordResponseDto;
import com.tuotiansudai.dto.LoanDetailDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanTitleRelationMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanTitleRelationModel;
import com.tuotiansudai.service.LoanDetailService;

import com.tuotiansudai.utils.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LoanDetailServiceImpl implements LoanDetailService {

    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private LoanTitleRelationMapper loanTitleRelationMapper;

    @Override
    public LoanDetailDto getLoanDetail(String loanId) {
        String loginName = LoginUserInfo.getLoginName();
        LoanDetailDto dto = new LoanDetailDto();
        LoanModel loanModel = loanMapper.findById(Long.parseLong(loanId));
        if (loanModel == null) {
            dto.setStatus(false);
            return dto;
        }
        dto = convertModelToDto(loanModel, loginName);
        dto.setStatus(true);

        return dto;
    }

    private LoanDetailDto convertModelToDto(LoanModel loanModel, String loginName) {

        LoanDetailDto loanDetailDto = new LoanDetailDto();
        loanDetailDto.setId(loanModel.getId());
        loanDetailDto.setName(loanModel.getName());
        loanDetailDto.setAgentLoginName(loanModel.getAgentLoginName());
        loanDetailDto.setLoanerLoginName(loanModel.getLoanerLoginName());
        loanDetailDto.setLoanTypeDesc(loanModel.getType().getName());
        loanDetailDto.setPeriods(loanModel.getPeriods());
        loanDetailDto.setRepayUnit(loanModel.getType().getRepayTimeUnit());
        loanDetailDto.setDescriptionHtml(loanModel.getDescriptionHtml());
        loanDetailDto.setDescriptionText(loanModel.getDescriptionText());
        loanDetailDto.setLoanAmount(loanModel.getLoanAmount());
        loanDetailDto.setInvestIncreasingAmount(loanModel.getInvestIncreasingAmount());
        loanDetailDto.setActivityType(loanModel.getActivityType());
        loanDetailDto.setActivityRate(loanModel.getActivityRate());
        loanDetailDto.setBasicRate(loanModel.getBasicRate());
        loanDetailDto.setLoanStatus(loanModel.getStatus());
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (accountModel != null) {
            loanDetailDto.setBalance(accountModel.getBalance());
        }
        loanDetailDto.setPreheatSeconds(Long.parseLong(this.calculatorPreheatSeconds(loanModel.getFundraisingStartTime())));
        //TODO amountNeedRaised(可投金额)
        loanDetailDto.setAmountNeedRaised(1L);

        //TODO raiseCompletedRate 已投比例
        loanDetailDto.setRaiseCompletedRate(0.5D);
        loanDetailDto.setApplyMetarial(getApplyMaterial(loanModel.getId()));
        return loanDetailDto;
    }

    @Override
    public String getExpectedTotalIncome(String loanId, String investAmount) {
        return null;
    }

    @Override
    public InvestRecordResponseDto getInvests(InvestRecordRequestDto dto) {
        return null;
    }

    private List<ApplyMaterialDto> getApplyMaterial(long loanId) {
        List<LoanTitleRelationModel> loanTitleRelationModels = loanTitleRelationMapper.findByLoanId(loanId);
        List<ApplyMaterialDto> applyMaterialDtoList = new ArrayList<>();
        for (LoanTitleRelationModel loanTitleRelationModel : loanTitleRelationModels) {
            ApplyMaterialDto applyMaterialDto = new ApplyMaterialDto();
            applyMaterialDto.setTitle("" + loanTitleRelationModel.getTitleId());
            applyMaterialDto.setApplyMaterialUrl(loanTitleRelationModel.getApplyMetarialUrl());
            applyMaterialDtoList.add(applyMaterialDto);
        }

        return applyMaterialDtoList;
    }

    private String calculatorPreheatSeconds(Date fundraisingStartTime) {
        if (fundraisingStartTime == null) {
            return "0";
        }
        Long time = (fundraisingStartTime.getTime() - System
                .currentTimeMillis()) / 1000;
        if (time < 0) {
            return "0";
        }
        return time.toString();

    }
}
