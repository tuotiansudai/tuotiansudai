package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.FundManagementResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppFundManagementServiceImpl;
import com.tuotiansudai.coupon.repository.model.UserCouponView;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.point.service.PointService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.ReferrerManageMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.service.InvestRepayService;
import com.tuotiansudai.service.RechargeService;
import com.tuotiansudai.service.UserBillService;
import com.tuotiansudai.service.WithdrawService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class MobileAppFundManagementServiceTest extends ServiceTestBase{
    @InjectMocks
    private MobileAppFundManagementServiceImpl mobileAppFundManagementService;
    @Mock
    private AccountMapper accountMapper;
    @Mock
    private RechargeService rechargeService;
    @Mock
    private WithdrawService withdrawService;
    @Mock
    private InvestRepayService investRepayService;
    @Mock
    private UserBillService userBillService;
    @Mock
    private PointService pointService;
    @Mock
    private UserCouponService userCouponService;
    @Mock
    private ReferrerManageMapper referrerManageMapper;
    @Test
    public void shouldQueryFundByUserIdIsOk(){
        AccountModel accountModel = fakeUserModel();
        when(accountMapper.findByLoginName(anyString())).thenReturn(accountModel);
        when(rechargeService.sumSuccessRechargeAmount(anyString())).thenReturn(1100l);
        when(withdrawService.sumSuccessWithdrawAmount(anyString())).thenReturn(1200l);
        when(investRepayService.findSumRepayingCorpusByLoginName(anyString())).thenReturn(1300l);
        when(investRepayService.findSumRepayingInterestByLoginName(anyString())).thenReturn(1400l);
        when(investRepayService.findSumRepaidInterestByLoginName(anyString())).thenReturn(1500l);
        when(investRepayService.findSumRepaidCorpusByLoginName(anyString())).thenReturn(1600l);
        when(userBillService.findSumRewardByLoginName(anyString())).thenReturn(1700l);
        when(pointService.getAvailablePoint(anyString())).thenReturn(1700l);
        when(referrerManageMapper.findReferInvestTotalAmount("", null, null, null, null)).thenReturn(1700l);
        List<UserCouponView> userCouponViews = new ArrayList<>();
        userCouponViews.add(new UserCouponView());
        when(userCouponService.getUnusedUserCoupons(anyString())).thenReturn(userCouponViews);


        BaseResponseDto<FundManagementResponseDataDto> baseResponseDto = mobileAppFundManagementService.queryFundByUserId("admin");

        assertEquals(ReturnMessage.SUCCESS.getCode(),baseResponseDto.getCode());
        assertEquals(baseResponseDto.getData().getAccountBalance(), "10.00");
        assertEquals(baseResponseDto.getData().getFrozenMoney(), "20.00");
        assertEquals(baseResponseDto.getData().getAvailableMoney(), "10.00");
        assertEquals(baseResponseDto.getData().getPaidRechargeMoney(), "11.00");
        assertEquals(baseResponseDto.getData().getSuccessWithdrawMoney(), "12.00");
        assertEquals(baseResponseDto.getData().getTotalAssets(), "57.00");
        assertEquals(baseResponseDto.getData().getTotalInvestment(), "29.00");
        assertEquals(baseResponseDto.getData().getExpectedTotalInterest(), "32.00");
        assertEquals(baseResponseDto.getData().getReceivedInterest(), "15.00");
        assertEquals(baseResponseDto.getData().getReceivedCorpus(), "16.00");
        assertEquals(baseResponseDto.getData().getReceivableCorpus(), "13.00");
        assertEquals(baseResponseDto.getData().getReceivableInterest(), "14.00");
        assertEquals(baseResponseDto.getData().getReceivableCorpusInterest(), "27.00");
        assertEquals(baseResponseDto.getData().getPoint(), "1700");
        assertEquals(baseResponseDto.getData().getUsableUserCouponCount(), "1");
    }

    private AccountModel fakeUserModel(){
        AccountModel accountModel = new AccountModel();
        accountModel.setBalance(1000l);
        accountModel.setFreeze(2000l);
        return accountModel;
    }

}
