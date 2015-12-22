package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.RepayService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public BaseDto<PayFormDataDto> repay(RepayDto repayDto) {
        return payWrapperClient.repay(repayDto);
    }

    @Override
    public BaseDto<LoanRepayDataDto> getLoanRepay(String loginName, long loanId) {
        BaseDto<LoanRepayDataDto> baseDto = new BaseDto<>();
        LoanRepayDataDto dataDto = new LoanRepayDataDto();
        baseDto.setData(dataDto);

        LoanModel loanModel = loanMapper.findById(loanId);

        if (loanModel == null) {
            return baseDto;
        }

        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByAgentAndLoanId(loginName, loanId);

        this.resetExpiredLoanRepay(loanRepayModels);

        dataDto.setHasWaitPayLoanRepay(Iterators.any(loanRepayModels.iterator(), new Predicate<LoanRepayModel>() {
            @Override
            public boolean apply(LoanRepayModel input) {
                return input.getStatus() == RepayStatus.WAIT_PAY;
            }
        }));

        final LoanRepayModel enabledLoanRepayModel = loanRepayMapper.findEnabledLoanRepayByLoanId(loanId);

        if (Lists.newArrayList(LoanStatus.REPAYING, LoanStatus.OVERDUE).contains(loanModel.getStatus())) {
            DateTime now = new DateTime();
            DateTime lastSuccessRepayDate = InterestCalculator.getLastSuccessRepayDate(loanModel, loanRepayModels, now);
            long interest = InterestCalculator.calculateLoanRepayInterest(loanModel, investMapper.findSuccessInvestsByLoanId(loanId), lastSuccessRepayDate, now);
            long defaultInterest = 0;
            for (LoanRepayModel loanRepayModel : loanRepayModels) {
                defaultInterest += loanRepayModel.getDefaultInterest();
            }
            dataDto.setLoanAgentBalance(accountMapper.findByLoginName(loginName).getBalance());
            dataDto.setExpectedNormalRepayAmount(enabledLoanRepayModel.getCorpus() + interest + defaultInterest);
            dataDto.setExpectedAdvanceRepayAmount(loanRepayModels.get(loanRepayModels.size() - 1).getCorpus() + interest + defaultInterest);
        }

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

        return baseDto;
    }

    @Transactional
    private void resetExpiredLoanRepay(List<LoanRepayModel> loanRepayModels) {
        if (CollectionUtils.isEmpty(loanRepayModels)) {
            return;
        }

        DateTime now = new DateTime();
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            if (loanRepayModel.getStatus() == RepayStatus.WAIT_PAY) {
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
    public BaseDto<InvestRepayDataDto> findInvestorInvestRepay(String loginName, long investId) {
        BaseDto<InvestRepayDataDto> baseDto = new BaseDto<>();
        InvestRepayDataDto dataDto = new InvestRepayDataDto();
        dataDto.setStatus(true);
        dataDto.setRecords(Lists.<InvestRepayDataItemDto>newArrayList());
        baseDto.setData(dataDto);

        List<InvestRepayModel> investRepayModels = investRepayMapper.findByLoginNameAndInvestId(loginName, investId);
        if (CollectionUtils.isNotEmpty(investRepayModels)) {
            List<InvestRepayDataItemDto> records = Lists.transform(investRepayModels, new Function<InvestRepayModel, InvestRepayDataItemDto>() {
                @Override
                public InvestRepayDataItemDto apply(InvestRepayModel investRepayModel) {
                    return new InvestRepayDataItemDto(investRepayModel);
                }
            });
            dataDto.setRecords(records);
        }

        return baseDto;
    }
}
