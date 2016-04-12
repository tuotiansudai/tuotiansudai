package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.FundManagementResponseDataDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.impl.MobileAppFundManagementServiceImpl;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.service.InvestRepayService;
import com.tuotiansudai.service.RechargeService;
import com.tuotiansudai.service.UserBillService;
import com.tuotiansudai.service.WithdrawService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

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
    }

    private AccountModel fakeUserModel(){
        AccountModel accountModel = new AccountModel();
        accountModel.setBalance(1000l);
        accountModel.setFreeze(2000l);
        return accountModel;
    }

}
