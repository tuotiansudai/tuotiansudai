package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NoPasswordInvestTurnOffRequestDto;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppNoPasswordInvestTurnOffServiceImpl;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.SmsCaptchaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MobileAppNoPasswordInvestTurnOffServiceTest extends ServiceTestBase{
    @InjectMocks
    private MobileAppNoPasswordInvestTurnOffServiceImpl mobileAppNoPasswordInvestTurnOffService;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SmsCaptchaService smsCaptchaService;

    @Test
    public void shouldNoPasswordInvestTurnOffIsOk(){
        AccountModel accountModel = new AccountModel("loginName", "userName", "identityNumber", "payUserId", "payAccountId", new Date());
        UserModel userModel = new UserModel();
        userModel.setMobile("13688888888");
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        when(userMapper.findByLoginName(anyString())).thenReturn(userModel);
        when(!smsCaptchaService.verifyMobileCaptcha(anyString(),anyString(), any(CaptchaType.class))).thenReturn(true);

        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("shenjiaojiao");

        NoPasswordInvestTurnOffRequestDto noPasswordInvestTurnOffRequestDto = new NoPasswordInvestTurnOffRequestDto();
        noPasswordInvestTurnOffRequestDto.setBaseParam(baseParam);
        noPasswordInvestTurnOffRequestDto.setCaptcha("123456");
        when(accountMapper.findByLoginName(anyString())).thenReturn(accountModel);
        baseResponseDto = mobileAppNoPasswordInvestTurnOffService.noPasswordInvestTurnOff(noPasswordInvestTurnOffRequestDto);

        assertEquals("0000",baseResponseDto.getCode());
        assertEquals("",baseResponseDto.getMessage());
    }

}
