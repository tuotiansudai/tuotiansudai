package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.paywrapper.client.PayClient;
import com.tuotiansudai.paywrapper.repository.mapper.BaseMapper;
import com.tuotiansudai.paywrapper.repository.model.request.MerRegisterPersonRequestModel;
import com.tuotiansudai.paywrapper.repository.model.response.BaseResponseModel;
import com.tuotiansudai.paywrapper.repository.model.response.MerRegisterPersonResponseModel;
import com.tuotiansudai.paywrapper.service.impl.RegisterServiceImpl;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
@TransactionConfiguration
@Transactional
public class RegisterServiceTest {
    @InjectMocks
    private RegisterServiceImpl registerService;

    @Mock
    private PayClient payClient;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AccountMapper accountMapper;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void shouldCreateAccount() throws Exception {
        MerRegisterPersonResponseModel responseModel = new MerRegisterPersonResponseModel();
        responseModel.setReturnCode("0000");
        responseModel.setUmpUserId("payUserId");
        responseModel.setUmpAccountId("payAccountId");
        UserModel userModel = new UserModel();
        userModel.setLoginName("loginName");
        when(userMapper.findByLoginName("loginName")).thenReturn(userModel);
        when(payClient.send(Matchers.<Class<? extends BaseMapper>>any(), any(MerRegisterPersonRequestModel.class), Matchers.<Class<BaseResponseModel>>any()))
                .thenReturn(responseModel);
        RegisterAccountDto dto = new RegisterAccountDto();
        dto.setLoginName("loginName");
        dto.setUserName("userName");
        dto.setIdentityNumber("identityNumber");
        dto.setMobile("13900000000");
        BaseDto<PayDataDto> baseDto = registerService.register(dto);

        ArgumentCaptor<AccountModel> accountModelArgumentCaptor = ArgumentCaptor.forClass(AccountModel.class);
        verify(accountMapper, times(1)).create(accountModelArgumentCaptor.capture());
        AccountModel accountModel = accountModelArgumentCaptor.getValue();
        assertThat(accountModel.getLoginName(), is("loginName"));
        assertThat(accountModel.getUserName(), is("userName"));
        assertThat(accountModel.getIdentityNumber(), is("identityNumber"));
        assertThat(accountModel.getPayUserId(), is("payUserId"));
        assertThat(accountModel.getPayAccountId(), is("payAccountId"));
        assertTrue(baseDto.getData().getStatus());
    }
}
