package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.service.impl.MobileAppCertificationServiceImpl;
import com.tuotiansudai.api.service.impl.MobileAppFundManagementServiceImpl;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.InvestRepayService;
import com.tuotiansudai.service.RechargeService;
import com.tuotiansudai.service.WithdrawService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class MobileAppMobileAppFundManagementServiceTest extends ServiceTestBase{
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

        BaseResponseDto baseResponseDto = mobileAppFundManagementService.queryFundByUserId("admin");





    }

    private AccountModel fakeUserModel(){
        AccountModel accountModel = new AccountModel();
        accountModel.setBalance(1000);
        accountModel.setFreeze(2000);
        return accountModel;
    }

}
