package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NoPasswordInvestTurnOnRequestDto;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppNoPasswordInvestTurnOnServiceImpl;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
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
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})public class MobileAppNoPasswordInvestServiceTurnOnTest extends ServiceTestBase {
    @InjectMocks
    private MobileAppNoPasswordInvestTurnOnServiceImpl mobileAppNoPasswordInvestTurnOnService;

    @Mock
    private BankAccountMapper bankAccountMapper;

    @Ignore
    @Test
    public void shouldNoPasswordInvestTurnOnIsOk() {
        BankAccountModel bankAccountModel = new BankAccountModel("loginName", "payUserId", "payAccountId", "111", "111","");
        bankAccountModel.setAutoInvest(true);
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), any())).thenReturn(bankAccountModel);
        NoPasswordInvestTurnOnRequestDto noPasswordInvestTurnOnRequestDto = new NoPasswordInvestTurnOnRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("loginName");
        noPasswordInvestTurnOnRequestDto.setBaseParam(baseParam);
        BaseResponseDto baseResponseDto = mobileAppNoPasswordInvestTurnOnService.noPasswordInvestTurnOn(noPasswordInvestTurnOnRequestDto, "127.0.0.1");

        assertEquals("0000", baseResponseDto.getCode());
        assertEquals("", baseResponseDto.getMessage());

    }

}
