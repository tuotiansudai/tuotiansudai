package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.RepayService;
import com.tuotiansudai.utils.AmountUtil;
import com.tuotiansudai.utils.InterestCalculator;
import com.tuotiansudai.utils.LoginUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RepayServiceImpl implements RepayService {

    @Autowired
    private LoanMapper loanMapper;

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
    public BaseDto<LoanRepayDataDto> getLoanRepay(long loanId) {
        BaseDto<LoanRepayDataDto> baseDto = new BaseDto<>();
        LoanRepayDataDto dataDto = new LoanRepayDataDto();
        baseDto.setData(dataDto);

        LoanModel loanModel = loanMapper.findById(loanId);

        if (loanModel == null) {
            return baseDto;
        }

        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByAgentAndLoanId(LoginUserInfo.getLoginName(), loanId);

        this.resetExpiredLoanRepay(loanRepayModels);


        dataDto.setHasConfirmingLoanRepay(Iterators.any(loanRepayModels.iterator(), new Predicate<LoanRepayModel>() {
            @Override
            public boolean apply(LoanRepayModel input) {
                return input.getStatus() == RepayStatus.CONFIRMING;
            }
        }));

        if (loanModel.getStatus() == LoanStatus.REPAYING) {
            DateTime now  = new DateTime();
            List<InvestModel> investModels = investMapper.findSuccessInvestsByLoanId(loanId);
            DateTime lastSuccessRepayDate = InterestCalculator.getLastSuccessRepayDate(loanModel, loanRepayModels, now);
            long interest = InterestCalculator.calculateLoanRepayInterest(loanModel, investModels, lastSuccessRepayDate, now);
            dataDto.setExpectedAdvanceRepayAmount(AmountUtil.convertCentToString(loanModel.getLoanAmount() + interest));
        }

        if (CollectionUtils.isNotEmpty(loanRepayModels)) {
            final LoanRepayModel enabledLoanRepayModel = loanRepayMapper.findEnabledLoanRepayByLoanId(loanId);
            List<LoanRepayDataItemDto> records = Lists.transform(loanRepayModels, new Function<LoanRepayModel, LoanRepayDataItemDto>() {
                @Override
                public LoanRepayDataItemDto apply(LoanRepayModel loanRepayModel) {
                    boolean isEnable = false;
                    if (enabledLoanRepayModel != null) {
                        isEnable = loanRepayModel.getId() == enabledLoanRepayModel.getId();
                    }
                    return new LoanRepayDataItemDto(loanRepayModel, isEnable);
                }
            });
            dataDto.setStatus(true);
            dataDto.setRecords(records);
        }

        return baseDto;
    }

    @Transactional
    private void resetExpiredLoanRepay(List<LoanRepayModel> loanRepayModels) {
        if (CollectionUtils.isEmpty(loanRepayModels)) {
            return;
        }

        DateTime now = new DateTime();
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            if (loanRepayModel.getStatus() == RepayStatus.CONFIRMING) {
                DateTime actualRepayDate = new DateTime(loanRepayModel.getActualRepayDate());
                if (actualRepayDate.plusMinutes(30).isBefore(now)) {
                    loanRepayModel.setStatus(RepayStatus.REPAYING);
                    loanRepayModel.setActualRepayDate(null);
                    loanRepayModel.setActualInterest(0);
                    loanRepayMapper.update(loanRepayModel);
                }
            }
        }
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
            dataDto.setRecords(Lists.<InvestRepayDataItemDto>newArrayList());
        }
        return baseDto;
    }
}
