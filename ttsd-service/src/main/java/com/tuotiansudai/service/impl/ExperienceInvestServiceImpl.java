package com.tuotiansudai.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.ExperienceInvestService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ExperienceBillMapper experienceBillMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseDto<BaseDataDto> invest(InvestDto investDto) {
        BaseDataDto dataDto = new BaseDataDto();
        BaseDto<BaseDataDto> dto = new BaseDto<>();
        dto.setData(dataDto);
        if (!isUserExperienceRequired(investDto)) {
            return dto;
        }
        this.generateInvest(investDto);
        dataDto.setStatus(true);
        return dto;
    }

    private InvestModel generateInvest(InvestDto investDto) {
        LoanModel loanModel = loanMapper.findById(Long.parseLong(investDto.getLoanId()));
        long amount = Long.parseLong(investDto.getAmount());

        InvestModel investModel = new InvestModel(idGenerator.generate(), Long.parseLong(investDto.getLoanId()), null, amount, investDto.getLoginName(), new Date(), investDto.getSource(), investDto.getChannel(), 0);
        investModel.setStatus(InvestStatus.SUCCESS);
        investModel.setTransferStatus(TransferStatus.NONTRANSFERABLE);
        investMapper.create(investModel);
        Date repayDate = new DateTime().plusDays(loanModel.getDuration()).withTimeAtStartOfDay().minusSeconds(1).toDate();
        long expectedInterest = InterestCalculator.estimateExperienceExpectedInterest(amount, loanModel);

        InvestRepayModel investRepayModel = new InvestRepayModel(idGenerator.generate(), investModel.getId(), 1, 0, expectedInterest, 0, repayDate, RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(investRepayModel));

        this.updateUserExperienceBalanceByLoginName(Long.parseLong(investDto.getAmount()), investDto.getLoginName(), ExperienceBillOperationType.OUT, ExperienceBillBusinessType.INVEST_LOAN);
        return investModel;
    }

    public void updateUserExperienceBalanceByLoginName(long experienceAmount, String loginName, ExperienceBillOperationType experienceBillOperationType, ExperienceBillBusinessType experienceBusinessType){
        UserModel userModel = userMapper.findByLoginName(loginName);
        userModel.setExperienceBalance(userModel.getExperienceBalance() - experienceAmount);
        userMapper.updateUser(userModel);

        ExperienceBillModel experienceBillModel = new ExperienceBillModel(loginName,
                experienceBillOperationType,
                experienceAmount,
                experienceBusinessType,
                MessageFormat.format(this.experienceBillNote(experienceBusinessType),
                        AmountConverter.convertCentToString(experienceAmount),
                        new Date()));

        experienceBillMapper.create(experienceBillModel);
    }

    private String experienceBillNote(ExperienceBillBusinessType experienceBusinessType){
        switch (experienceBusinessType){
            case INVEST_LOAN:
                return "您投资了拓天体验金项目，投资体验金金额：{0}元, 投资时间：{1}";
            case REGISTER:
                return "新手注册成功，获得体验金：{0}元, 注册时间：{1}";
            case MONEY_TREE:
                return "恭喜您在摇钱树活动中摇中了：{0}元体验金，摇奖时间：{1}";
        }
        return "";

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
}
