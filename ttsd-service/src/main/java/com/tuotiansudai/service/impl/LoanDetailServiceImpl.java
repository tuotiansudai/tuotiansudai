package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.LoanDetailService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.InterestCalculator;
import com.tuotiansudai.util.RandomUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class LoanDetailServiceImpl implements LoanDetailService {

    private final static String INVEST_NO_PASSWORD_REMIND_MAP = "invest_no_password_remind_map";

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestService investService;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private LoanTitleMapper loanTitleMapper;

    @Autowired
    private LoanTitleRelationMapper loanTitleRelationMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private RandomUtils randomUtils;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${invest.achievement.start.time}\")}")
    private Date achievementStartTime;

    @Override
    public LoanDetailDto getLoanDetail(String loginName, long loanId) {
        LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel == null) {
            return null;
        }

        LoanDetailDto loanDto = this.convertModelToDto(loanModel, loginName);
        loanDto.setStatus(true);
        return loanDto;
    }

    @Override
    public BaseDto<BasePaginationDataDto> getInvests(final String loginName, long loanId, int index, int pageSize) {
        long count = investMapper.findCountByStatus(loanId, InvestStatus.SUCCESS);
        List<InvestModel> investModels = investMapper.findByStatus(loanId, (index - 1) * pageSize, pageSize, InvestStatus.SUCCESS);
        List<LoanDetailInvestPaginationItemDto> records = Lists.newArrayList();

        if (CollectionUtils.isNotEmpty(investModels)) {
            records = Lists.transform(investModels, new Function<InvestModel, LoanDetailInvestPaginationItemDto>() {
                @Override
                public LoanDetailInvestPaginationItemDto apply(InvestModel input) {
                    LoanDetailInvestPaginationItemDto item = new LoanDetailInvestPaginationItemDto();
                    item.setAmount(AmountConverter.convertCentToString(input.getAmount()));
                    item.setSource(input.getSource());
                    item.setAutoInvest(input.isAutoInvest());
                    item.setMobile(randomUtils.encryptMobile(loginName, input.getLoginName(), Source.WEB));


                    long amount = 0;
                    List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(input.getId());
                    for (InvestRepayModel investRepayModel : investRepayModels) {
                        amount += investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee();
                    }

                    if (CollectionUtils.isEmpty(investRepayModels)) {
                        amount = investService.estimateInvestIncome(input.getLoanId(), loginName, input.getAmount());
                    }

                    item.setExpectedInterest(AmountConverter.convertCentToString(amount));
                    item.setCreatedTime(input.getTradingTime() == null ? input.getCreatedTime() : input.getTradingTime());
                    item.setAchievements(input.getAchievements());
                    return item;
                }
            });
        }
        BaseDto<BasePaginationDataDto> baseDto = new BaseDto<>();
        BasePaginationDataDto<LoanDetailInvestPaginationItemDto> dataDto = new BasePaginationDataDto<>(index, pageSize, count, records);

        // TODO:fake
        LoanModel loanModel = loanMapper.findById(loanId);
        if (loanId == 41650602422768L && loanModel.getStatus() == LoanStatus.REPAYING) {
            LoanDetailInvestPaginationItemDto fakeItem = new LoanDetailInvestPaginationItemDto();
            fakeItem.setMobile("186****9367");
            fakeItem.setAmount(AmountConverter.convertCentToString(loanModel.getLoanAmount()));
            fakeItem.setCreatedTime(new DateTime(2016, 7, 28, 0, 0, 0).toDate());
            fakeItem.setSource(Source.WEB);
            fakeItem.setAchievements(Lists.newArrayList(InvestAchievement.FIRST_INVEST, InvestAchievement.MAX_AMOUNT, InvestAchievement.LAST_INVEST));

            long fee = new BigDecimal(InterestCalculator.estimateExpectedInterest(loanModel, loanModel.getLoanAmount())).multiply(new BigDecimal(0.1)).setScale(0, BigDecimal.ROUND_DOWN).longValue();
            fakeItem.setExpectedInterest(AmountConverter.convertCentToString(InterestCalculator.estimateExpectedInterest(loanModel, loanModel.getLoanAmount()) - fee));
            dataDto = new BasePaginationDataDto<>(index, pageSize, 1, Lists.newArrayList(fakeItem));
        }

        baseDto.setData(dataDto);
        dataDto.setStatus(true);
        return baseDto;
    }

    private boolean isRemindNoPassword(String loginName) {
        return redisWrapperClient.hexists(INVEST_NO_PASSWORD_REMIND_MAP, loginName);
    }

    private LoanDetailDto convertModelToDto(LoanModel loanModel, String loginName) {
        long investedAmount = investMapper.sumSuccessInvestAmount(loanModel.getId());

        //TODO:fake
        if (loanModel.getId() == 41650602422768L) {
            if (loanModel.getStatus() == LoanStatus.REPAYING) {
                investedAmount = loanModel.getLoanAmount();
            }
        }

        LoanDetailDto loanDto = new LoanDetailDto(loanModel, investedAmount, loanTitleMapper.findAll(), loanTitleRelationMapper.findByLoanId(loanModel.getId()));

        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (accountModel != null) {
            loanDto.setHasRemindInvestNoPassword(this.isRemindNoPassword(loginName));
            loanDto.setAutoInvest(accountModel.isAutoInvest());
            loanDto.setInvestNoPassword(accountModel.isNoPasswordInvest());
            loanDto.setUserBalance(accountModel.getBalance());
            loanDto.setMaxAvailableInvestAmount(AmountConverter.convertCentToString(this.calculateMaxAvailableInvestAmount(loginName, loanModel, investedAmount)));
        }

        if (loanModel.getActivityType() == ActivityType.NEWBIE) {
            double newbieInterestCouponRate = 0;
            final List<CouponModel> allActiveCoupons = couponMapper.findAllActiveCoupons();
            for (CouponModel activeCoupon : allActiveCoupons) {
                if (activeCoupon.getCouponType() == CouponType.INTEREST_COUPON
                        && activeCoupon.getUserGroup() == UserGroup.NEW_REGISTERED_USER
                        && activeCoupon.getProductTypes().contains(ProductType._30)
                        && activeCoupon.getRate() > newbieInterestCouponRate) {
                    newbieInterestCouponRate = activeCoupon.getRate();
                }
            }
            loanDto.setNewbieInterestCouponRate(new BigDecimal(String.valueOf(newbieInterestCouponRate)).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
        }

        if (loanModel.getActivityType() != ActivityType.NEWBIE && loanModel.getFundraisingStartTime().after(achievementStartTime)) {
            LoanInvestAchievementDto achievementDto = new LoanInvestAchievementDto();
            if (loanModel.getFirstInvestAchievementId() != null) {
                InvestModel firstInvest = investMapper.findById(loanModel.getFirstInvestAchievementId());
                achievementDto.setFirstInvestAchievementDate(firstInvest.getTradingTime());
                achievementDto.setFirstInvestAchievementMobile(randomUtils.encryptMobile(loginName, firstInvest.getLoginName(), Source.WEB));
            }
            if (loanModel.getMaxAmountAchievementId() != null) {
                InvestModel maxInvest = investMapper.findById(loanModel.getMaxAmountAchievementId());
                long amount = investMapper.sumSuccessInvestAmountByLoginName(loanModel.getId(), maxInvest.getLoginName());
                achievementDto.setMaxAmountAchievementAmount(AmountConverter.convertCentToString(amount));
                achievementDto.setMaxAmountAchievementMobile(randomUtils.encryptMobile(loginName, maxInvest.getLoginName(), Source.WEB));
            }
            if (loanModel.getLastInvestAchievementId() != null) {
                InvestModel lastInvest = investMapper.findById(loanModel.getLastInvestAchievementId());
                achievementDto.setLastInvestAchievementDate(lastInvest.getTradingTime());
                achievementDto.setLastInvestAchievementMobile(randomUtils.encryptMobile(loginName, lastInvest.getLoginName(), Source.WEB));
            }
            achievementDto.setLoanRemainingAmount(AmountConverter.convertCentToString(loanModel.getLoanAmount() - investedAmount));

            loanDto.setAchievement(achievementDto);

            //TODO:fake
            if (loanModel.getId() == 41650602422768L && loanModel.getStatus() == LoanStatus.REPAYING) {
                achievementDto.setFirstInvestAchievementDate(new DateTime(2016, 7, 29, 12, 0, 0).toDate());
                achievementDto.setFirstInvestAchievementMobile("186****9367");
                achievementDto.setMaxAmountAchievementAmount(AmountConverter.convertCentToString(loanModel.getLoanAmount()));
                achievementDto.setMaxAmountAchievementMobile("186****9367");
                achievementDto.setLastInvestAchievementDate(new DateTime(2016, 7, 28, 0, 0, 0).toDate());
                achievementDto.setLastInvestAchievementMobile("186****9367");
                achievementDto.setLoanRemainingAmount("0");
            }
        }
        return loanDto;
    }

    private long calculateMaxAvailableInvestAmount(String loginName, LoanModel loanModel, long investedAmount) {
        long sumSuccessInvestAmount = investMapper.sumSuccessInvestAmountByLoginName(loanModel.getId(), loginName);
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        long maxAvailableInvestAmount = NumberUtils.min(accountModel.getBalance(), loanModel.getLoanAmount() - investedAmount, loanModel.getMaxInvestAmount() - sumSuccessInvestAmount);

        if (maxAvailableInvestAmount < loanModel.getMinInvestAmount()) {
            return 0L;
        }
        return maxAvailableInvestAmount - (maxAvailableInvestAmount - loanModel.getMinInvestAmount()) % loanModel.getInvestIncreasingAmount();
    }
}
