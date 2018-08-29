package com.tuotiansudai.service;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.fudian.message.BankRegisterMessage;
import com.tuotiansudai.log.service.UserOpLogService;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.UserBankCardMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserBankCardModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
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
        BankAsyncMessage message = new BankAsyncMessage();
        message.setStatus(true);
        when(bankWrapperClient.registerInvestor(any(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(message);
        bankAccountService.registerInvestorAccount(mockRegisterAccountDto(), Source.WEB, "token", "ip", "deviceld");
        verify(userOpLogService, times(1)).sendUserOpLogMQ(loginNameCaptor.capture(), anyString(), anyString(), anyString(), any(), anyString());
        verify(bankWrapperClient, times(1)).registerInvestor(any(), loginNameCaptor.capture(), anyString(), anyString(), anyString(), anyString());
        assertThat(loginNameCaptor.getValue(), is("loginName"));
        assertTrue(message.isStatus());
    }

    @Test
    public void registerInvestorAccountFail(){
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), any())).thenReturn(new BankAccountModel());
        BankAsyncMessage message = bankAccountService.registerInvestorAccount(mockRegisterAccountDto(), Source.WEB, "token", "ip", "deviceld");
        verify(userOpLogService, times(0)).sendUserOpLogMQ(anyString(), anyString(), anyString(), anyString(), any(), anyString());
        verify(bankWrapperClient, times(0)).registerInvestor(any(), anyString(), anyString(), anyString(), anyString(), anyString());
        assertFalse(message.isStatus());
        assertThat(message.getMessage(), is("已实名认证"));
    }

    @Test
    public void registerLoanerAccountSuccess(){
        ArgumentCaptor<String> loginNameCaptor = ArgumentCaptor.forClass(String.class);
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.INVESTOR))).thenReturn(mockBankAccountModel());
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.LOANER))).thenReturn(null);
        when(userMapper.findByLoginName("loginName")).thenReturn(mockUserModel());
        BankAsyncMessage message = new BankAsyncMessage();
        message.setStatus(true);
        when(bankWrapperClient.registerInvestor(any(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(message);
        bankAccountService.registerLoanerAccount("loginName","token", "ip", "deviceld");
        verify(userOpLogService, times(1)).sendUserOpLogMQ(loginNameCaptor.capture(), anyString(), anyString(), anyString(), any(), anyString());
        verify(bankWrapperClient, times(1)).registerLoaner(any(), loginNameCaptor.capture(), anyString(), anyString(), anyString(), anyString());
        assertThat(loginNameCaptor.getValue(), is("loginName"));
    }

    @Test
    public void registerLoanerAccountFail(){
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.INVESTOR))).thenReturn(null);
        BankAsyncMessage message = bankAccountService.registerLoanerAccount("loginName","token", "ip", "deviceld");
        verify(userOpLogService, times(0)).sendUserOpLogMQ(anyString(), anyString(), anyString(), anyString(), any(), anyString());
        verify(bankWrapperClient, times(0)).registerInvestor(any(), anyString(), anyString(), anyString(), anyString(), anyString());
        assertFalse(message.isStatus());
        assertThat(message.getMessage(), is("未完成出借人实名认证"));

        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.INVESTOR))).thenReturn(mockBankAccountModel());
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.LOANER))).thenReturn(mockBankAccountModel());
        BankAsyncMessage message1 = bankAccountService.registerLoanerAccount("loginName","token", "ip", "deviceld");
        verify(userOpLogService, times(0)).sendUserOpLogMQ(anyString(), anyString(), anyString(), anyString(), any(), anyString());
        verify(bankWrapperClient, times(0)).registerInvestor(any(), anyString(), anyString(), anyString(), anyString(), anyString());
        assertFalse(message1.isStatus());
        assertThat(message1.getMessage(), is("已实名认证"));
    }

    private RegisterAccountDto mockRegisterAccountDto(){
        return new RegisterAccountDto("loginName", "mobile", "userName", "identityNumber", Source.WEB);
    }

    private BankAccountModel mockBankAccountModel(){
        return new BankAccountModel("loginName", "bankUserName", "bankAccountNo", "1111111", "20180810");
    }

    private UserModel mockUserModel(){
        UserModel userModel = new UserModel();
        userModel.setLoginName("loginName");
        userModel.setMobile("11111111111");
        userModel.setUserName("userName");
        userModel.setIdentityNumber("340322100000000000");
        return userModel;
    }

    @Test
    public void createInvestorBankAccountSuccess(){
        BankRegisterMessage message = mockBankRegisterMessage(true);

        ArgumentCaptor<BankAccountModel> bankAccountModelCaptor = ArgumentCaptor.forClass(BankAccountModel.class);
        ArgumentCaptor<UserBankCardModel> userBankCardModelCaptor = ArgumentCaptor.forClass(UserBankCardModel.class);

        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.INVESTOR))).thenReturn(null);
        doNothing().when(userMapper).updateUserNameAndIdentityNumber(anyString(), anyString(), anyString());
        doNothing().when(userRoleMapper).deleteByLoginNameAndRole(anyString(), eq(Role.INVESTOR));
        when(userRoleMapper.create(any())).thenReturn(1);
        doNothing().when(bankAccountMapper).createInvestor(any(BankAccountModel.class));
        doNothing().when(userBankCardMapper).createInvestor(any(UserBankCardModel.class));

        bankAccountService.processBankAccount(message);

        verify(userMapper, times(1)).updateUserNameAndIdentityNumber(anyString(), anyString(), anyString());
        verify(userRoleMapper, times(1)).deleteByLoginNameAndRole(anyString(), eq(Role.INVESTOR));
        verify(userRoleMapper, times(1)).create(any());
        verify(bankAccountMapper, times(1)).createInvestor(bankAccountModelCaptor.capture());
        verify(userBankCardMapper, times(1)).createInvestor(userBankCardModelCaptor.capture());
        verify(mqWrapperClient, times(1)).sendMessage(eq(MessageQueue.EventMessage), any(EventMessage.class));
        verify(mqWrapperClient, times(1)).sendMessage(eq(MessageQueue.PushMessage), any(PushMessage.class));

        assertThat(bankAccountModelCaptor.getValue().getLoginName(), is("loginName"));
        assertThat(bankAccountModelCaptor.getValue().getBankUserName(), is("UU02683949835091001"));
        assertThat(bankAccountModelCaptor.getValue().getBankAccountNo(), is("UA02683949835131001"));
        assertThat(userBankCardModelCaptor.getValue().getCardNumber(), is("6228483000000000000"));

    }

    @Test
    public void createInvestorBankAccountFail(){
        BankRegisterMessage message = mockBankRegisterMessage(true);

        ArgumentCaptor<BankAccountModel> bankAccountModelCaptor = ArgumentCaptor.forClass(BankAccountModel.class);
        ArgumentCaptor<UserBankCardModel> userBankCardModelCaptor = ArgumentCaptor.forClass(UserBankCardModel.class);

        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.INVESTOR))).thenReturn(mockBankAccountModel());

        bankAccountService.processBankAccount(message);

        verify(userMapper, times(0)).updateUserNameAndIdentityNumber(anyString(), anyString(), anyString());
        verify(userRoleMapper, times(0)).deleteByLoginNameAndRole(anyString(), eq(Role.INVESTOR));
        verify(userRoleMapper, times(0)).create(any());
        verify(bankAccountMapper, times(0)).createInvestor(bankAccountModelCaptor.capture());
        verify(userBankCardMapper, times(0)).createInvestor(userBankCardModelCaptor.capture());
        verify(mqWrapperClient, times(0)).sendMessage(eq(MessageQueue.EventMessage), any(EventMessage.class));
        verify(mqWrapperClient, times(0)).sendMessage(eq(MessageQueue.PushMessage), any(PushMessage.class));
    }

    @Test
    public void createLoanerBankAccountSuccess(){
        BankRegisterMessage message = mockBankRegisterMessage(false);

        ArgumentCaptor<BankAccountModel> bankAccountModelCaptor = ArgumentCaptor.forClass(BankAccountModel.class);
        ArgumentCaptor<UserBankCardModel> userBankCardModelCaptor = ArgumentCaptor.forClass(UserBankCardModel.class);

        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.LOANER))).thenReturn(null);
        doNothing().when(userRoleMapper).deleteByLoginNameAndRole(anyString(), eq(Role.LOANER));
        when(userRoleMapper.create(any())).thenReturn(1);
        doNothing().when(bankAccountMapper).createLoaner(any(BankAccountModel.class));
        doNothing().when(userBankCardMapper).createLoaner(any(UserBankCardModel.class));

        bankAccountService.processBankAccount(message);

        verify(userRoleMapper, times(1)).deleteByLoginNameAndRole(anyString(), eq(Role.LOANER));
        verify(userRoleMapper, times(1)).create(any());
        verify(bankAccountMapper, times(1)).createLoaner(bankAccountModelCaptor.capture());
        verify(userBankCardMapper, times(1)).createLoaner(userBankCardModelCaptor.capture());
        verify(mqWrapperClient, times(1)).sendMessage(eq(MessageQueue.EventMessage), any(EventMessage.class));
        verify(mqWrapperClient, times(1)).sendMessage(eq(MessageQueue.PushMessage), any(PushMessage.class));

        assertThat(bankAccountModelCaptor.getValue().getLoginName(), is("loginName"));
        assertThat(bankAccountModelCaptor.getValue().getBankUserName(), is("UU02683949835091001"));
        assertThat(bankAccountModelCaptor.getValue().getBankAccountNo(), is("UA02683949835131001"));
        assertThat(userBankCardModelCaptor.getValue().getCardNumber(), is("6228483000000000000"));

    }

    @Test
    public void createLoanerBankAccountFail(){
        BankRegisterMessage message = mockBankRegisterMessage(false);

        ArgumentCaptor<BankAccountModel> bankAccountModelCaptor = ArgumentCaptor.forClass(BankAccountModel.class);
        ArgumentCaptor<UserBankCardModel> userBankCardModelCaptor = ArgumentCaptor.forClass(UserBankCardModel.class);

        when(bankAccountMapper.findByLoginNameAndRole(anyString(), eq(Role.LOANER))).thenReturn(mockBankAccountModel());

        bankAccountService.processBankAccount(message);

        verify(userMapper, times(0)).updateUserNameAndIdentityNumber(anyString(), anyString(), anyString());
        verify(userRoleMapper, times(0)).deleteByLoginNameAndRole(anyString(), eq(Role.LOANER));
        verify(userRoleMapper, times(0)).create(any());
        verify(bankAccountMapper, times(0)).createLoaner(bankAccountModelCaptor.capture());
        verify(userBankCardMapper, times(0)).createLoaner(userBankCardModelCaptor.capture());
        verify(mqWrapperClient, times(0)).sendMessage(eq(MessageQueue.EventMessage), any(EventMessage.class));
        verify(mqWrapperClient, times(0)).sendMessage(eq(MessageQueue.PushMessage), any(PushMessage.class));
    }

    private BankRegisterMessage mockBankRegisterMessage(boolean isInvestor){
        BankRegisterMessage message = new BankRegisterMessage("loginName", "18895730992", "3403221000000000000", "realName", "token", "UU02683949835091001", "UA02683949835131001", "20180810000000000001", "20180810", isInvestor);
        message.setBankCardNo("6228483000000000000");
        return message;
    }
}
