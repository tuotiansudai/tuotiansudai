package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.FundManagementResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppFundManagementService;
import com.tuotiansudai.coupon.repository.model.UserCouponView;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.membership.service.UserMembershipService;
import com.tuotiansudai.point.service.PointService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.ReferrerManageMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.service.InvestRepayService;
import com.tuotiansudai.service.RechargeService;
import com.tuotiansudai.service.UserBillService;
import com.tuotiansudai.service.WithdrawService;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileAppFundManagementServiceImpl implements MobileAppFundManagementService {
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private RechargeService rechargeService;
    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    private InvestRepayService investRepayService;
    @Autowired
    private UserBillService userBillService;
    @Autowired
    private PointService pointService;
    @Autowired
    private UserCouponService userCouponService;
    @Autowired
    private ReferrerManageMapper referrerManageMapper;
    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;
    @Autowired
    private UserMembershipService userMembershipService;

    public BaseResponseDto queryFundByUserId(String userId) {
        AccountModel accountModel = accountMapper.findByLoginName(userId);
        long accountBalance = 0L;
        long frozenMoney = 0L;
        if(accountModel != null){
            accountBalance = accountModel.getBalance();
            frozenMoney = accountModel.getFreeze();
        }
        long paidRechargeMoney = rechargeService.sumSuccessRechargeAmount(userId);
        long successWithdrawMoney = withdrawService.sumSuccessWithdrawAmount(userId);
        long receivableCorpus = investRepayService.findSumRepayingCorpusByLoginName(userId);
        long receivableInterest = investRepayService.findSumRepayingInterestByLoginName(userId);
        long receivedInterest = investRepayService.findSumRepaidInterestByLoginName(userId);
        long receivedCorpus = investRepayService.findSumRepaidCorpusByLoginName(userId);
        long receivedReward = userBillService.findSumRewardByLoginName(userId);
        long myPoint = pointService.getAvailablePoint(userId);
        List<UserCouponView> unusedUserCoupons = userCouponService.getUnusedUserCoupons(userId);
        long rewardAmount = referrerManageMapper.findReferInvestTotalAmount(userId, null, null, null, null);
        //资产总额＝账户余额 ＋ 冻结金额 ＋ 应收本金 ＋ 应收利息
        long totalAssets = accountBalance + frozenMoney + receivableCorpus + receivableInterest;
        //累计投资额 = 已收本金 ＋ 应收本金
        long totalInvestment = receivableCorpus + receivedCorpus;
        //累计收益 = 已收奖励 ＋ 已收利息
        long expectedTotalInterest = receivedReward + receivedInterest;
        //待收本息 = 应收利息 ＋ 应收本金
        long receivableCorpusInterest = receivableInterest + receivableCorpus;
        int level = 0;
        MembershipModel membershipModel = userMembershipEvaluator.evaluate(userId);
        if(membershipModel != null){
            level = membershipModel.getLevel();
        }

        FundManagementResponseDataDto fundManagementResponseDataDto = new FundManagementResponseDataDto();
        fundManagementResponseDataDto.setAccountBalance(AmountConverter.convertCentToString(accountBalance));
        fundManagementResponseDataDto.setFrozenMoney(AmountConverter.convertCentToString(frozenMoney));
        fundManagementResponseDataDto.setAvailableMoney(AmountConverter.convertCentToString(accountBalance));
        fundManagementResponseDataDto.setPaidRechargeMoney(AmountConverter.convertCentToString(paidRechargeMoney));
        fundManagementResponseDataDto.setSuccessWithdrawMoney(AmountConverter.convertCentToString(successWithdrawMoney));
        fundManagementResponseDataDto.setTotalAssets(AmountConverter.convertCentToString(totalAssets));
        fundManagementResponseDataDto.setTotalInvestment(AmountConverter.convertCentToString(totalInvestment));
        fundManagementResponseDataDto.setExpectedTotalInterest(AmountConverter.convertCentToString(expectedTotalInterest));
        fundManagementResponseDataDto.setReceivedInterest(AmountConverter.convertCentToString(receivedInterest));
        fundManagementResponseDataDto.setReceivedCorpus(AmountConverter.convertCentToString(receivedCorpus));
        fundManagementResponseDataDto.setReceivableInterest(AmountConverter.convertCentToString(receivableInterest));
        fundManagementResponseDataDto.setReceivableCorpus(AmountConverter.convertCentToString(receivableCorpus));
        fundManagementResponseDataDto.setReceivableCorpusInterest(AmountConverter.convertCentToString(receivableCorpusInterest));
        fundManagementResponseDataDto.setUsableUserCouponCount(CollectionUtils.isNotEmpty(unusedUserCoupons) ? String.valueOf(unusedUserCoupons.size()) : "0");
        fundManagementResponseDataDto.setPoint(String.valueOf(myPoint));
        fundManagementResponseDataDto.setRewardAmount(AmountConverter.convertCentToString(rewardAmount));
        fundManagementResponseDataDto.setMembershipLevel(String.valueOf(level));
        fundManagementResponseDataDto.setMembershipPoint(String.valueOf(accountModel == null ? 0 : accountModel.getMembershipPoint()));
        fundManagementResponseDataDto.setAvailableMoneyCent(String.valueOf(accountBalance));
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setData(fundManagementResponseDataDto);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }
}
