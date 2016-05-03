package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NoPasswordInvestResponseDataDto;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppNoPasswordInvestServiceImpl;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MobileAppNoPasswordInvestServiceTest extends ServiceTestBase{
    @InjectMocks
    private MobileAppNoPasswordInvestServiceImpl mobileAppNoPasswordInvestService;

    @Mock
    private AccountMapper accountMapper;

    @Test
    public void shouldGetNoPasswordInvestDataIsSuccess(){
        AccountModel accountModel = new AccountModel("loginName", "userName", "identityNumber", "payUserId", "payAccountId", new Date());
        accountModel.setNoPasswordInvest(true);
        accountModel.setAutoInvest(true);
        when(accountMapper.findByLoginName(anyString())).thenReturn(accountModel);
        BaseParamDto baseParamDto = new BaseParamDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("loginName");
        baseParamDto.setBaseParam(baseParam);
        BaseResponseDto<NoPasswordInvestResponseDataDto> baseResponseDto = mobileAppNoPasswordInvestService.getNoPasswordInvestData(baseParamDto);

        assertTrue(baseResponseDto.getData().isAutoInvest());
        assertTrue(baseResponseDto.getData().isNoPasswordInvest());

    }



}
