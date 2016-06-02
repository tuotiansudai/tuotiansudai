package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.repository.mapper.BaseSyncMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.MerRegisterPersonRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.BaseSyncResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.MerRegisterPersonResponseModel;
import com.tuotiansudai.paywrapper.service.impl.RegisterServiceImpl;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
@Transactional
public class RegisterServiceTest {
    @InjectMocks
    private RegisterServiceImpl registerService;

    @Mock
    private PaySyncClient paySyncClient;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private UserRoleMapper userRoleMapper;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void shouldCreateAccount() throws Exception {
        MerRegisterPersonResponseModel responseModel = new MerRegisterPersonResponseModel();
        responseModel.setRetCode("0000");
        responseModel.setUserId("payUserId");
        responseModel.setAccountId("payAccountId");
        UserModel userModel = new UserModel();
        userModel.setLoginName("loginName");
        when(userMapper.findByLoginName("loginName")).thenReturn(userModel);
        when(paySyncClient.send(Matchers.<Class<? extends BaseSyncMapper>>any(), any(MerRegisterPersonRequestModel.class), Matchers.<Class<BaseSyncResponseModel>>any()))
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

        ArgumentCaptor<ArrayList<UserRoleModel>> userRoleModelArgumentCaptor = ArgumentCaptor.forClass((Class<ArrayList<UserRoleModel>>) new ArrayList<UserRoleModel>().getClass());
        verify(userRoleMapper, times(1)).create(userRoleModelArgumentCaptor.capture());
        assertThat(userRoleModelArgumentCaptor.getValue().get(0).getRole().name(), is(Role.INVESTOR.name()));
    }
}
