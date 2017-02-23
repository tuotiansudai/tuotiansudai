package com.tuotiansudai.service.impl;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.ExperienceInvestService;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.commons.collections4.CollectionUtils;
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
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Value(value = "${pay.interest.fee}")
    private double defaultFee;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseDto<BaseDataDto> invest(InvestDto investDto) {
        BaseDataDto dataDto = new BaseDataDto();
        BaseDto<BaseDataDto> dto = new BaseDto<>();
        dto.setData(dataDto);

        if (!isUserCouponRequired(investDto)) {
            return dto;
        }

        long userCouponId = investDto.getUserCouponIds().get(0);

        UserCouponModel userCouponModel = userCouponMapper.lockById(userCouponId);

        if (userCouponModel == null) {
            logger.error(MessageFormat.format("[Experience Invest] user({0}) is using a nonexistent user coupon({1}) ",
                    investDto.getLoginName(), String.valueOf(userCouponId)));
            return dto;
        }

        if (!isUserCouponAvailable(investDto, userCouponModel)) {
            return dto;
        }

        CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
        couponModel.setUsedCount(couponModel.getUsedCount() + 1);
        couponMapper.updateCoupon(couponModel);

        InvestModel investModel = this.generateInvest(investDto, couponModel);

        userCouponModel.setLoanId(Long.parseLong(investDto.getLoanId()));
        userCouponModel.setInvestId(investModel.getId());
        userCouponModel.setUsedTime(new Date());
        userCouponModel.setStatus(InvestStatus.SUCCESS);
        userCouponMapper.update(userCouponModel);

        dataDto.setStatus(true);

        return dto;
    }

    private InvestModel generateInvest(InvestDto investDto, CouponModel couponModel) {
        LoanModel loanModel = loanMapper.findById(Long.parseLong(investDto.getLoanId()));
        long amount = Long.parseLong(investDto.getAmount());

        InvestModel investModel = new InvestModel(idGenerator.generate(), Long.parseLong(investDto.getLoanId()), null, amount, investDto.getLoginName(), new Date(), investDto.getSource(), investDto.getChannel(), defaultFee);
        investModel.setStatus(InvestStatus.SUCCESS);
        investModel.setTransferStatus(TransferStatus.NONTRANSFERABLE);
        investMapper.create(investModel);
        Date repayDate = new DateTime().plusDays(loanModel.getDuration()).withTimeAtStartOfDay().minusSeconds(1).toDate();
        long expectedInterest = InterestCalculator.estimateCouponExpectedInterest(amount, loanModel, couponModel);
        long expectedFee = InterestCalculator.estimateCouponExpectedFee(loanModel, couponModel, amount, defaultFee);

        InvestRepayModel investRepayModel = new InvestRepayModel(idGenerator.generate(), investModel.getId(), 1, amount, expectedInterest, expectedFee, repayDate, RepayStatus.REPAYING);
        investRepayMapper.create(Lists.newArrayList(investRepayModel));
        return investModel;
    }

    private boolean isUserCouponRequired(InvestDto investDto) {
        if (StringUtils.isEmpty(investDto.getLoginName())) {
            logger.error("[Experience Invest] the login name is null");
            return false;
        }

        LoanModel loanModel = loanMapper.findById(Long.parseLong(investDto.getLoanId()));

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

        long investAmount = Long.parseLong(investDto.getAmount());
        if (investAmount != 0) {
            logger.error(MessageFormat.format("[Experience Invest] user({0}) invest amount({1}) is not 0",
                    investDto.getLoginName(), investDto.getAmount()));
            return false;
        }

        if (CollectionUtils.isEmpty(investDto.getUserCouponIds())) {
            logger.error(MessageFormat.format("[Experience Invest] user({0}) is no coupons ", investDto.getLoginName()));
            return false;
        }

        if (investDto.getUserCouponIds().size() != 1) {
            logger.error(MessageFormat.format("[Experience Invest] user({0}) is using more than one coupons({1}) ",
                    investDto.getLoginName(), String.valueOf(investDto.getUserCouponIds().size())));
            return false;
        }
        return true;
    }

    private boolean isUserCouponAvailable(InvestDto investDto, UserCouponModel userCouponModel) {
        long investAmount = Long.parseLong(investDto.getAmount());
        LoanModel loanModel = loanMapper.findById(Long.parseLong(investDto.getLoanId()));

        if (userCouponModel.getStatus() == InvestStatus.SUCCESS) {
            logger.error(MessageFormat.format("[Experience Invest] user({0}) is using a used user coupon({1}) ",
                    investDto.getLoginName(), String.valueOf(userCouponModel.getId())));
            return false;
        }

        CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
        if (couponModel.getProductTypes().contains(loanModel.getProductType()) && investAmount < couponModel.getInvestLowerLimit()) {
            logger.error(MessageFormat.format("[Experience Invest] user({0}) invest amount({1}) with a using invalid({2}, {3}) user coupon({4})",
                    investDto.getLoginName(),
                    String.valueOf(investAmount),
                    Joiner.on(",").join(couponModel.getProductTypes()),
                    String.valueOf(couponModel.getInvestLowerLimit()),
                    String.valueOf(userCouponModel.getId())));
            return false;
        }

        if (CollectionUtils.isNotEmpty(investMapper.findByLoanIdAndLoginName(loanModel.getId(), investDto.getLoginName()))) {
            logger.error(MessageFormat.format("[Experience Invest] user({0}) has already invested the loan({1})",
                    investDto.getLoginName(), String.valueOf(loanModel.getId())));
            return false;
        }

        return true;
    }
}
