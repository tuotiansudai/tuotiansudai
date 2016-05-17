package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseParam;
import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.NoPasswordInvestResponseDataDto;
import com.tuotiansudai.api.service.impl.MobileAppNoPasswordInvestTurnOnServiceImpl;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MobileAppNoPasswordInvestServiceTurnOnTest extends ServiceTestBase{
    @InjectMocks
    private MobileAppNoPasswordInvestTurnOnServiceImpl mobileAppNoPasswordInvestTurnOnService;

    @Mock
    private AccountMapper accountMapper;

    @Test
    public void shouldNoPasswordInvestTurnOnIsOk(){
        AccountModel accountModel = new AccountModel("loginName", "userName", "identityNumber", "payUserId", "payAccountId", new Date());
        accountModel.setNoPasswordInvest(true);
        when(accountMapper.findByLoginName(anyString())).thenReturn(accountModel);
        BaseParamDto baseParamDto = new BaseParamDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("loginName");
        baseParamDto.setBaseParam(baseParam);
        BaseResponseDto baseResponseDto = mobileAppNoPasswordInvestTurnOnService.noPasswordInvestTurnOn(baseParamDto, "127.0.0.1");

        assertEquals("0000",baseResponseDto.getCode());
        assertEquals("",baseResponseDto.getMessage());

    }



}
