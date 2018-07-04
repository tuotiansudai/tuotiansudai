package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NoPasswordInvestTurnOffRequestDto;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppNoPasswordInvestTurnOffServiceImpl;
import com.tuotiansudai.enums.SmsCaptchaType;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.SmsCaptchaService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})public class MobileAppNoPasswordInvestTurnOffServiceTest extends ServiceTestBase {
    @InjectMocks
    private MobileAppNoPasswordInvestTurnOffServiceImpl mobileAppNoPasswordInvestTurnOffService;

    @Mock
    private BankAccountMapper bankAccountMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SmsCaptchaService smsCaptchaService;

    @Ignore
    @Test
    public void shouldNoPasswordInvestTurnOffIsOk() {
        BankAccountModel bankAccountModel = new BankAccountModel("loginName", "payUserId", "payAccountId", "111", "111");
        UserModel userModel = new UserModel();
        userModel.setMobile("13688888888");
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        when(userMapper.findByLoginName(anyString())).thenReturn(userModel);
        when(!smsCaptchaService.verifyMobileCaptcha(anyString(), anyString(), any(SmsCaptchaType.class))).thenReturn(true);

        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("shenjiaojiao");

        NoPasswordInvestTurnOffRequestDto noPasswordInvestTurnOffRequestDto = new NoPasswordInvestTurnOffRequestDto();
        noPasswordInvestTurnOffRequestDto.setBaseParam(baseParam);
        noPasswordInvestTurnOffRequestDto.setCaptcha("123456");
        when(bankAccountMapper.findInvestorByLoginName(anyString())).thenReturn(bankAccountModel);
        baseResponseDto = mobileAppNoPasswordInvestTurnOffService.noPasswordInvestTurnOff(noPasswordInvestTurnOffRequestDto, "127.0.0.1");

        assertEquals("0000", baseResponseDto.getCode());
        assertEquals("", baseResponseDto.getMessage());
    }

}
