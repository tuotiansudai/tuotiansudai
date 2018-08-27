package com.tuotiansudai.service;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.log.service.UserOpLogService;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.UserBankCardMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class BankAccountServiceTest {

    @InjectMocks
    private BankAccountService bankAccountService;

    @Mock
    private BankWrapperClient bankWrapperClient;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BankAccountMapper bankAccountMapper;

    @Mock
    private MQWrapperClient mqWrapperClient;

    @Mock
    private UserOpLogService userOpLogService;

    @Mock
    private UserRoleMapper userRoleMapper;

    @Mock
    private UserBankCardMapper userBankCardMapper;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        Field bankWrapperClientField = this.bankAccountService.getClass().getDeclaredField("bankWrapperClient");
        bankWrapperClientField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(bankWrapperClientField, bankWrapperClientField.getModifiers() & ~Modifier.FINAL);
        bankWrapperClientField.set(this.bankAccountService, this.bankWrapperClient);
    }

    @Test
    public void registerInvestorAccountSuccess(){
        ArgumentCaptor<String> loginNameCaptor = ArgumentCaptor.forClass(String.class);
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), any())).thenReturn(null);

        bankAccountService.registerInvestorAccount(mockRegisterAccountDto(), Source.WEB, "token", "ip", "deviceld");

        verify(userOpLogService, times(1)).sendUserOpLogMQ(loginNameCaptor.capture(), anyString(), anyString(), anyString(), any(), anyString());
        verify(bankWrapperClient, times(1)).registerInvestor(any(), loginNameCaptor.capture(), anyString(), anyString(), anyString(), anyString());
        assertThat(loginNameCaptor.getValue(), is("loginName"));
    }

    private RegisterAccountDto mockRegisterAccountDto(){
        return new RegisterAccountDto("loginName", "mobile", "userName", "identityNumber", Source.WEB);
    }

    private BankAccountModel mockBankAccountModel(Role role){
        return new BankAccountModel("loginName", "bankUserName", "bankAccountNo", "bankAccountOrderNo", "bankAccountOrderDate");
    }

}
