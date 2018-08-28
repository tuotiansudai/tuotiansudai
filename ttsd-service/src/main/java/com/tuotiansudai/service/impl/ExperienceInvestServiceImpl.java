package com.tuotiansudai.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.repository.mapper.ExperienceAccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.ExperienceBillService;
import com.tuotiansudai.service.ExperienceInvestService;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class ExperienceInvestServiceImpl implements ExperienceInvestService {

    static Logger logger = Logger.getLogger(ExperienceInvestServiceImpl.class);

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private ExperienceBillService experienceBillService;

    @Autowired
    private ExperienceAccountMapper experienceAccountMapper;

    @Value(value = "${pay.interest.fee}")
    private double defaultFee;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseDto<BaseDataDto> invest(InvestDto investDto) {
        BaseDataDto dataDto = new BaseDataDto();
        BaseDto<BaseDataDto> dto = new BaseDto<>();
        dto.setData(dataDto);
        if (!isUserExperienceRequired(investDto)) {
            return dto;
        }

        if (!isEnoughExperienceBalance(investDto)) {
            return dto;
        }

        experienceBillService.updateUserExperienceBalanceByLoginName(Long.parseLong(investDto.getAmount()),
                investDto.getLoginName(),
                ExperienceBillOperationType.OUT,
                ExperienceBillBusinessType.INVEST_LOAN,
                MessageFormat.format(ExperienceBillBusinessType.INVEST_LOAN.getContentTemplate(), investDto.getAmount(), DateTime.now().toDate()));

        this.generateInvest(investDto);
        dataDto.setStatus(true);
        return dto;
    }

    private InvestModel generateInvest(InvestDto investDto) {
        LoanModel loanModel = loanMapper.findById(Long.parseLong(investDto.getLoanId()));
        long amount = Long.parseLong(investDto.getAmount());

        InvestModel investModel = new InvestModel(IdGenerator.generate(), Long.parseLong(investDto.getLoanId()), null, amount, investDto.getLoginName(), new Date(), investDto.getSource(), investDto.getChannel(), defaultFee);
        investModel.setTransferStatus(TransferStatus.NONTRANSFERABLE);
        investModel.setStatus(InvestStatus.SUCCESS);
        investMapper.create(investModel);
        Date repayDate = new DateTime().plusDays(loanModel.getDuration()).withTimeAtStartOfDay().minusSeconds(1).toDate();
        long expectedInterest = InterestCalculator.estimateExperienceExpectedInterest(amount, loanModel);

        InvestRepayModel investRepayModel = new InvestRepayModel(IdGenerator.generate(), investModel.getId(), 1, 0, expectedInterest, 0, repayDate, RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(investRepayModel));

        return investModel;
    }

    private boolean isUserExperienceRequired(InvestDto investDto) {
        if (StringUtils.isEmpty(investDto.getLoginName())) {
            logger.error("[Experience Invest] the login name is null");
            return false;
        }

        LoanModel loanModel = loanMapper.findById(Long.parseLong(investDto.getLoanId()));
        long investAmount = Long.parseLong(investDto.getAmount());

        if (loanModel == null) {
            logger.error(MessageFormat.format("[Experience Invest] the loan({0}) is investing is not exist", String.valueOf(investDto.getLoanId())));
            return false;
        }

        long loanId = loanModel.getId();
        if (loanModel.getProductType() != ProductType.EXPERIENCE || loanModel.getActivityType() != ActivityType.NEWBIE) {
            logger.error(MessageFormat.format("[Experience Invest] the loan({0}) product type is {1} and activity type is {2}",
                    String.valueOf(loanId), loanModel.getProductType(), loanModel.getActivityType()));
            return false;
        }

        if (investAmount <= 0) {
            logger.error(MessageFormat.format("[Experience Invest] user({0}) invest experience amount({1}) is 0",
                    investDto.getLoginName(), investDto.getAmount()));
            return false;
        }

        return true;
    }

    private boolean isEnoughExperienceBalance(InvestDto investDto) {
        experienceAccountMapper.lockByLoginName(investDto.getLoginName());
        long experienceBalance = experienceAccountMapper.getExperienceBalance(investDto.getLoginName());
        long amount = Long.parseLong(investDto.getAmount());
        if (experienceBalance < amount) {
            logger.warn(MessageFormat.format("[Experience Invest] experience_balance[0] less investAmount[1]", experienceBalance, amount));
            return false;
        }
        return true;
    }

}
