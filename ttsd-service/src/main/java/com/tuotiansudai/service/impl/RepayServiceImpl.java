package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.RepayService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

@Service
public class RepayServiceImpl implements RepayService {

    private static Logger logger = Logger.getLogger(RepayServiceImpl.class);

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

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private CouponRepayMapper couponRepayMapper;

    @Autowired
    private InvestExtraRateMapper investExtraRateMapper;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    private final static String INVEST_COUPON_MESSAGE = "您使用了{0}元体验券";

    private final static String INTEREST_COUPON_MESSAGE = "您使用了{0}%加息券";

    private final static String BIRTHDAY_COUPON_MESSAGE = "您已享受生日福利";

    private final static Map<Integer, String> membershipMessage = Maps.newHashMap(ImmutableMap.<Integer, String>builder()
            .put(0, "平台收取收益和奖励的10%作为服务费")
            .put(1, "平台收取收益和奖励的10%作为服务费")
            .put(2, "平台收取收益和奖励的10%作为服务费,v2会员享受服务费9折优惠")
            .put(3, "平台收取收益和奖励的10%作为服务费,v3会员享受服务费8折优惠")
            .put(4, "平台收取收益和奖励的10%作为服务费,v4会员享受服务费8折优惠")
            .put(5, "平台收取收益和奖励的10%作为服务费,v5会员享受服务费7折优惠")
            .build());

    @Override
    public BaseDto<PayFormDataDto> repay(RepayDto repayDto) {
        return payWrapperClient.repay(repayDto);
    }

    @Override
    public BaseDto<LoanerLoanRepayDataDto> getLoanRepay(String loginName, long loanId) {
        BaseDto<LoanerLoanRepayDataDto> baseDto = new BaseDto<>();
        LoanerLoanRepayDataDto dataDto = new LoanerLoanRepayDataDto();
        baseDto.setData(dataDto);

        LoanModel loanModel = loanMapper.findById(loanId);
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByAgentAndLoanId(loginName, loanId);
        if (loanRepayModels.isEmpty()) {
            logger.error(MessageFormat.format("login user({0}) is not agent({1}) of loan({2})",
                    loginName, loanModel.getAgentLoginName(), String.valueOf(loanId)));
            return baseDto;
        }

        final LoanRepayModel enabledLoanRepayModel = loanRepayMapper.findEnabledLoanRepayByLoanId(loanId);
        boolean isWaitPayLoanRepayExist = Iterators.any(loanRepayModels.iterator(), new Predicate<LoanRepayModel>() {
            @Override
            public boolean apply(LoanRepayModel input) {
                return input.getStatus() == RepayStatus.WAIT_PAY;
            }
        });

        boolean isRepayingLoanRepayExist = Iterators.any(loanRepayModels.iterator(), new Predicate<LoanRepayModel>() {
            @Override
            public boolean apply(LoanRepayModel input) {
                return input.getStatus() == RepayStatus.REPAYING;
            }
        });

        dataDto.setLoanId(loanId);
        dataDto.setLoanerBalance(AmountConverter.convertCentToString(accountMapper.findByLoginName(loginName).getBalance()));

        if (enabledLoanRepayModel != null && !isWaitPayLoanRepayExist) {
            dataDto.setNormalRepayEnabled(true);
            long defaultInterest = 0;
            for (LoanRepayModel loanRepayModel : loanRepayModels) {
                defaultInterest += (loanRepayModel.getDefaultInterest()+loanRepayModel.getOverdueInterest());
            }
            dataDto.setNormalRepayAmount(AmountConverter.convertCentToString(enabledLoanRepayModel.getCorpus() + enabledLoanRepayModel.getExpectedInterest() + defaultInterest));
        }

        if (loanModel.getStatus() != LoanStatus.OVERDUE && isRepayingLoanRepayExist && !isWaitPayLoanRepayExist) {
            dataDto.setAdvanceRepayEnabled(true);
            DateTime now = new DateTime();
            DateTime lastSuccessRepayDate = InterestCalculator.getLastSuccessRepayDate(loanModel, loanRepayModels);
            List<InvestModel> successInvests = investMapper.findSuccessInvestsByLoanId(loanId);
            long advanceRepayInterest = InterestCalculator.calculateLoanRepayInterest(loanModel, successInvests, lastSuccessRepayDate, now);
            long corpus = loanRepayMapper.findLastLoanRepay(loanId).getCorpus();
            dataDto.setAdvanceRepayAmount(AmountConverter.convertCentToString(corpus + advanceRepayInterest));
        }

        List<LoanerLoanRepayDataItemDto> records = Lists.transform(loanRepayModels, new Function<LoanRepayModel, LoanerLoanRepayDataItemDto>() {
            @Override
            public LoanerLoanRepayDataItemDto apply(LoanRepayModel loanRepayModel) {
                boolean isEnabledLoanRepay = enabledLoanRepayModel != null && loanRepayModel.getId() == enabledLoanRepayModel.getId();
                return new LoanerLoanRepayDataItemDto(loanRepayModel, isEnabledLoanRepay);
            }
        });

        dataDto.setStatus(true);
        dataDto.setRecords(records);
        return baseDto;
    }

    @Override
    @Transactional
    public void resetPayExpiredLoanRepay(long loanId) {
        LoanRepayModel enabledLoanRepay = loanRepayMapper.findEnabledLoanRepayByLoanId(loanId);
        if (enabledLoanRepay != null && enabledLoanRepay.getStatus() == RepayStatus.WAIT_PAY &&
                new DateTime(enabledLoanRepay.getActualRepayDate()).plusMinutes(30).isBefore(new DateTime())) {
            enabledLoanRepay.setActualInterest(0);
            enabledLoanRepay.setRepayAmount(0);
            enabledLoanRepay.setStatus(enabledLoanRepay.getActualRepayDate().before(enabledLoanRepay.getRepayDate()) ? RepayStatus.REPAYING : RepayStatus.OVERDUE);
            enabledLoanRepay.setActualRepayDate(null);
            loanRepayMapper.update(enabledLoanRepay);
        }
    }

    @Override
    public BaseDto<InvestRepayDataDto> findInvestorInvestRepay(String loginName, long investId) {
        InvestRepayDataDto dataDto = new InvestRepayDataDto();
        dataDto.setStatus(true);
        BaseDto<InvestRepayDataDto> baseDto = new BaseDto<>(dataDto);

        final InvestModel investModel = investMapper.findById(investId);
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());

        final List<InvestRepayModel> investRepayModels = investRepayMapper.findByLoginNameAndInvestId(loginName, investId);

        int lastPeriod = investRepayModels.size();
        List<InvestRepayDataItemDto> records = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(investRepayModels)) {
            long sumActualInterest = 0L; //已收回款总额
            long sumExpectedInterest = 0L; //待收回款总额

            for (InvestRepayModel investRepayModel : investRepayModels) {
                InvestRepayDataItemDto investRepayDataItemDto = new InvestRepayDataItemDto(investRepayModel);

                long expectedTotalAmount = investRepayModel.getCorpus() + investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee() ; //当期应收回款
                long interestTotalAmount = investRepayModel.getDefaultInterest() + investRepayModel.getOverdueInterest() - investRepayModel.getDefaultFee() - investRepayModel.getOverdueFee();
                long expectedTotalFee = investRepayModel.getExpectedFee(); // 当期应缴服务费
                long actualTotalAmount = investRepayModel.getRepayAmount(); // 当期实收回款
                long actualTotalFee = investRepayModel.getActualFee(); // 当期实缴服务费
                long couponExpectedInterest = 0L; // 当期应收奖励
                long couponActualInterest = 0L; // 当期实收奖励

                CouponRepayModel couponRepayModel = couponRepayMapper.findByUserCouponByInvestIdAndPeriod(investRepayDataItemDto.getInvestId(), investRepayDataItemDto.getPeriod());

                if (couponRepayModel != null) {
                    expectedTotalAmount += couponRepayModel.getExpectedInterest() - couponRepayModel.getExpectedFee();
                    expectedTotalFee += couponRepayModel.getExpectedFee();
                    actualTotalAmount += couponRepayModel.getRepayAmount();
                    actualTotalFee += couponRepayModel.getActualFee();
                    couponExpectedInterest += couponRepayModel.getExpectedInterest();
                    couponActualInterest += couponRepayModel.getActualInterest();
                }

                if (investRepayModel.getPeriod() == lastPeriod) {
                    InvestExtraRateModel investExtraRateModel = investExtraRateMapper.findByInvestId(investRepayModel.getInvestId());
                    if (investExtraRateModel != null && !investExtraRateModel.isTransfer()) {
                        expectedTotalAmount += investExtraRateModel.getExpectedInterest() - investExtraRateModel.getExpectedFee();
                        expectedTotalFee += investExtraRateModel.getExpectedFee();
                        actualTotalAmount += investExtraRateModel.getRepayAmount();
                        actualTotalFee += investExtraRateModel.getActualFee();
                        couponExpectedInterest += investExtraRateModel.getExpectedInterest();
                        couponActualInterest += investExtraRateModel.getActualInterest();
                    }
                }
                investRepayDataItemDto.setAmount(AmountConverter.convertCentToString(expectedTotalAmount));
                investRepayDataItemDto.setExpectedFee(AmountConverter.convertCentToString(expectedTotalFee));
                investRepayDataItemDto.setCouponExpectedInterest(AmountConverter.convertCentToString(couponExpectedInterest));

                if (RepayStatus.COMPLETE == investRepayModel.getStatus()) {
                    sumActualInterest += actualTotalAmount;
                    investRepayDataItemDto.setActualAmount(AmountConverter.convertCentToString(actualTotalAmount));
                    investRepayDataItemDto.setActualFee(AmountConverter.convertCentToString(actualTotalFee));
                    investRepayDataItemDto.setCouponActualInterest(AmountConverter.convertCentToString(couponActualInterest));
                } else {
                    sumExpectedInterest += expectedTotalAmount + interestTotalAmount;
                }

                if (loanModel.getProductType() == ProductType.EXPERIENCE) {
                    investRepayDataItemDto.setLoan(loanModel);
                    investRepayDataItemDto.setInvestExperienceAmount(AmountConverter.convertCentToString(investModel.getAmount()));
                }
                records.add(investRepayDataItemDto);
            }
            dataDto.setSumActualInterest(AmountConverter.convertCentToString(sumActualInterest));
            dataDto.setSumExpectedInterest(AmountConverter.convertCentToString(sumExpectedInterest));
            dataDto.setRecords(records);
        }

        List<UserCouponModel> userCouponModels = userCouponMapper.findUserCouponSuccessAndCouponTypeByInvestId(investId, Lists.newArrayList(CouponType.RED_ENVELOPE));
        dataDto.setRedInterest(AmountConverter.convertCentToString(CollectionUtils.isNotEmpty(userCouponModels) ?
                userCouponModels.stream().mapToLong(UserCouponModel::getActualInterest).sum() : 0L));

        userCouponModels = userCouponMapper.findUserCouponSuccessAndCouponTypeByInvestId(investId, Lists.newArrayList(CouponType.INTEREST_COUPON, CouponType.INVEST_COUPON, CouponType.BIRTHDAY_COUPON));
        for (UserCouponModel userCouponModel : userCouponModels) {
            CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
            switch (couponModel.getCouponType()) {
                case INVEST_COUPON:
                    dataDto.setCouponMessage(MessageFormat.format(INVEST_COUPON_MESSAGE, AmountConverter.convertCentToString(couponModel.getAmount())));
                    break;
                case INTEREST_COUPON:
                    dataDto.setCouponMessage(MessageFormat.format(INTEREST_COUPON_MESSAGE, covertRate(String.format("%.2f", couponModel.getRate() * 100))));
                    break;
                case BIRTHDAY_COUPON:
                    dataDto.setCouponMessage(BIRTHDAY_COUPON_MESSAGE);
                    break;
            }
        }
        MembershipModel membershipModel = userMembershipEvaluator.evaluateSpecifiedDate(investModel.getLoginName(), investModel.getCreatedTime());
        dataDto.setLevelMessage(membershipMessage.get(membershipModel.getLevel()));
        return baseDto;
    }

    private static String covertRate(String rate) {
        return rate.contains(".00") ? rate.replaceAll("\\.00", "") : String.valueOf(Double.parseDouble(rate));
    }
}
