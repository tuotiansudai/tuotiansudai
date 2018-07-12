package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NoPasswordInvestResponseDataDto;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppNoPasswordInvestServiceImpl;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})public class MobileAppNoPasswordInvestServiceTest extends ServiceTestBase{
    @InjectMocks
    private MobileAppNoPasswordInvestServiceImpl mobileAppNoPasswordInvestService;

    @Mock
    private BankAccountMapper bankAccountMapper;

    @Test
    public void shouldGetNoPasswordInvestDataIsSuccess(){
        BankAccountModel bankAccountModel = new BankAccountModel("loginName", "payUserId", "payAccountId", "111", "111");
        bankAccountModel.setAutoInvest(true);
        bankAccountModel.setAuthorization(true);
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), any())).thenReturn(bankAccountModel);
        BaseParamDto baseParamDto = new BaseParamDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("loginName");
        baseParamDto.setBaseParam(baseParam);
        BaseResponseDto<NoPasswordInvestResponseDataDto> baseResponseDto = mobileAppNoPasswordInvestService.getNoPasswordInvestData(baseParamDto);

        assertTrue(baseResponseDto.getData().isAutoInvest());
        assertTrue(baseResponseDto.getData().isNoPasswordInvest());

    }



}
