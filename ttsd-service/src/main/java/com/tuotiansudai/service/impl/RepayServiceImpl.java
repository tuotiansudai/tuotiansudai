package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
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

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.HashMap;
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
    private MembershipMapper membershipMapper;

    @Autowired
    private InvestExtraRateMapper investExtraRateMapper;

    private final static String INVEST_COUPON_MESSAGE = "您使用了{0}元体验券";

    private final static String INTEREST_COUPON_MESSAGE = "您使用了{0}%加息券";

    private final static String BIRTHDAY_COUPON_MESSAGE = "您使用了生日月福利";

    private final static Map<String,String> membershipMessage = new HashMap(){{
        put("0","平台收取收益和奖励的10%作为服务费");
        put("1","平台收取收益和奖励的10%作为服务费");
        put("2","平台收取收益和奖励的10%作为服务费,v2会员享受服务费9折优惠");
        put("3","平台收取收益和奖励的10%作为服务费,v3会员享受服务费8折优惠");
        put("4","平台收取收益和奖励的10%作为服务费,v4会员享受服务费8折优惠");
        put("5","平台收取收益和奖励的10%作为服务费,v5会员享受服务费7折优惠");
    }};

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
                defaultInterest += loanRepayModel.getDefaultInterest();
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
        BaseDto<InvestRepayDataDto> baseDto = new BaseDto<>();
        InvestRepayDataDto dataDto = new InvestRepayDataDto();
        dataDto.setStatus(true);
        dataDto.setRecords(Lists.<InvestRepayDataItemDto>newArrayList());
        baseDto.setData(dataDto);
        final List<InvestRepayModel> investRepayModels = investRepayMapper.findByLoginNameAndInvestId(loginName, investId);
        int lastPeriod = investRepayModels.size();
        List<InvestRepayDataItemDto> records = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(investRepayModels)) {
            long sumActualInterest = 0l;
            long sumExpectedInterest = 0l;
            for(InvestRepayModel investRepayModel : investRepayModels){
                long expectedAmount = investRepayModel.getCorpus() + investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee();
                long expectedFee = investRepayModel.getExpectedFee();
                long actualFee = investRepayModel.getActualFee();
                long repayAmount = investRepayModel.getRepayAmount();
                long couponExpectedInterest = 0l;
                InvestRepayDataItemDto investRepayDataItemDto = new InvestRepayDataItemDto(investRepayModel);
                CouponRepayModel couponRepayModel = couponRepayMapper.findByUserCouponByInvestIdAndPeriod(investRepayDataItemDto.getInvestId(), investRepayDataItemDto.getPeriod());
                if(couponRepayModel != null){
                    couponExpectedInterest = couponRepayModel.getExpectedInterest();
                    investRepayDataItemDto.setCouponExpectedInterest(AmountConverter.convertCentToString(couponExpectedInterest));
                    investRepayDataItemDto.setExpectedFee(AmountConverter.convertCentToString(expectedFee + couponRepayModel.getExpectedFee()));
                    expectedAmount += (couponExpectedInterest - couponRepayModel.getExpectedFee());
                    investRepayDataItemDto.setAmount(AmountConverter.convertCentToString(expectedAmount));
                    if (RepayStatus.COMPLETE.equals(investRepayModel.getStatus())) {
                        repayAmount += couponRepayModel.getRepayAmount();
                        investRepayDataItemDto.setActualAmount(AmountConverter.convertCentToString(repayAmount));
                        investRepayDataItemDto.setActualFee(AmountConverter.convertCentToString(actualFee + couponRepayModel.getActualFee()));
                    }
                }

                if (investRepayModel.getPeriod() == lastPeriod) {
                    InvestExtraRateModel investExtraRateModel = investExtraRateMapper.findByInvestId(investRepayModel.getInvestId());
                    if (investExtraRateModel != null && !investExtraRateModel.isTransfer()) {
                        repayAmount += investExtraRateModel.getRepayAmount();
                        investRepayDataItemDto.setCouponExpectedInterest(AmountConverter.convertCentToString(couponExpectedInterest + investExtraRateModel.getExpectedInterest()));
                        investRepayDataItemDto.setActualAmount(AmountConverter.convertCentToString(repayAmount));
                    }
                }
                sumActualInterest += repayAmount;
                if(!investRepayModel.getStatus().equals(RepayStatus.COMPLETE)){
                    sumExpectedInterest += expectedAmount;
                }
                records.add(investRepayDataItemDto);
            }
            dataDto.setSumActualInterest(AmountConverter.convertCentToString(sumActualInterest));
            dataDto.setSumExpectedInterest(AmountConverter.convertCentToString(sumExpectedInterest));
            dataDto.setRecords(records);
        }

        List<UserCouponModel> userCouponModels = userCouponMapper.findUserCouponSuccessAndCouponTypeByInvestId(investId, Lists.newArrayList(CouponType.RED_ENVELOPE));
        dataDto.setRedInterest(AmountConverter.convertCentToString(CollectionUtils.isNotEmpty(userCouponModels) ? userCouponModels.get(0).getExpectedInterest() : 0l));

        userCouponModels = userCouponMapper.findUserCouponSuccessAndCouponTypeByInvestId(investId, Lists.newArrayList(CouponType.INTEREST_COUPON,CouponType.INVEST_COUPON,CouponType.BIRTHDAY_COUPON));
        for(UserCouponModel userCouponModel : userCouponModels){
            CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
            switch (couponModel.getCouponType()){
                case INVEST_COUPON:
                    dataDto.setCouponMessage(MessageFormat.format(INVEST_COUPON_MESSAGE,AmountConverter.convertCentToString(couponModel.getAmount())));
                    break;
                case INTEREST_COUPON:
                    dataDto.setCouponMessage(MessageFormat.format(INTEREST_COUPON_MESSAGE,covertRate(String.format("%.2f",couponModel.getRate() * 100))));
                    break;
                case BIRTHDAY_COUPON:
                    dataDto.setCouponMessage(BIRTHDAY_COUPON_MESSAGE);
                    break;
            }
        }

        final InvestModel investModel = investMapper.findById(investId);
        List<MembershipModel> membershipModels =  membershipMapper.findAllMembership();
        Optional<MembershipModel> membershipModelOptional = Iterators.tryFind(membershipModels.iterator(), new Predicate<MembershipModel>() {
            @Override
            public boolean apply(MembershipModel input) {
                return input.getFee() == investModel.getInvestFeeRate() ;
            }
        });

        if(membershipModelOptional.isPresent()){
            dataDto.setLevelMessage(membershipMessage.get(String.valueOf(membershipModelOptional.get().getLevel())));
        }else{
            dataDto.setLevelMessage(membershipMessage.get(String.valueOf(0)));
        }
        return baseDto;
    }

    private static String covertRate(String rate){
        return rate.indexOf(".00") != -1 ? rate.replaceAll(".00","") : String.valueOf(Double.parseDouble(rate));
    }
}
