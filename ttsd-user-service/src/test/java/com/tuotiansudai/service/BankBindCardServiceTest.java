package com.tuotiansudai.service;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.fudian.message.BankBindCardMessage;
import com.tuotiansudai.log.service.UserOpLogService;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.UserBankCardMapper;
import com.tuotiansudai.repository.model.*;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class BankBindCardServiceTest {

    @InjectMocks
    private BankBindCardService bankBindCardService;

    @Mock
    private BankWrapperClient bankWrapperClient;

    @Mock
    private UserBankCardMapper userBankCardMapper;

    @Mock
    private UserOpLogService userOpLogService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BankAccountMapper bankAccountMapper;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        Field bankWrapperClientField = this.bankBindCardService.getClass().getDeclaredField("bankWrapperClient");
        bankWrapperClientField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(bankWrapperClientField, bankWrapperClientField.getModifiers() & ~Modifier.FINAL);
        bankWrapperClientField.set(this.bankBindCardService, this.bankWrapperClient);
    }

    @Test
    public void bindSuccess(){
        ArgumentCaptor<String> loginNameCaptor = ArgumentCaptor.forClass(String.class);
        when(userBankCardMapper.findByLoginNameAndRole(anyString(), any())).thenReturn(null);
        when(userMapper.findByLoginName(anyString())).thenReturn(mockUserModel());
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), any())).thenReturn(mockBankAccountModel());
        bankBindCardService.bind("loginName", Source.WEB, "ip", "deviceId", Role.INVESTOR);
        verify(bankWrapperClient, times(1)).investorBindBankCard(any(), loginNameCaptor.capture(), anyString(), anyString(), anyString());
        verify(bankWrapperClient, times(0)).loanerBindBankCard(any(), anyString(), anyString(), anyString(), anyString());
        assertThat(loginNameCaptor.getValue(), is("loginName"));

    }

    @Test
    public void bindFail(){
        when(userBankCardMapper.findByLoginNameAndRole(anyString(), any())).thenReturn(new UserBankCardModel());
        when(userMapper.findByLoginName(anyString())).thenReturn(mockUserModel());
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), any())).thenReturn(mockBankAccountModel());
        BankAsyncMessage message = bankBindCardService.bind("loginName", Source.WEB, "ip", "deviceId", Role.INVESTOR);
        verify(bankWrapperClient, times(0)).investorBindBankCard(any(), anyString(), anyString(), anyString(), anyString());
        verify(bankWrapperClient, times(0)).loanerBindBankCard(any(), anyString(), anyString(), anyString(), anyString());
        assertFalse(message.isStatus());
        assertThat(message.getMessage(), is("已绑定银行卡"));
    }

    @Test
    public void processBindSuccess(){
        ArgumentCaptor<UserBankCardModel> userBankCardModelCaptor = ArgumentCaptor.forClass(UserBankCardModel.class);
        when(userBankCardMapper.findByLoginNameAndRole(anyString(), any())).thenReturn(null);
        bankBindCardService.processBind(mockBankBindCardMessage());
        verify(userBankCardMapper, times(1)).createInvestor(userBankCardModelCaptor.capture());
        verify(userBankCardMapper, times(0)).createLoaner(userBankCardModelCaptor.capture());
        assertThat(userBankCardModelCaptor.getValue().getLoginName(), is("loginName"));
        assertThat(userBankCardModelCaptor.getValue().getCardNumber(), is("111111111111111111"));
        assertThat(userBankCardModelCaptor.getValue().getBankOrderNo(), is("111111"));
    }

    @Test
    public void processBindFail(){
        when(userBankCardMapper.findByLoginNameAndRole(anyString(), any())).thenReturn(new UserBankCardModel());
        bankBindCardService.processBind(mockBankBindCardMessage());
        verify(userBankCardMapper, times(0)).createInvestor(any(UserBankCardModel.class));
        verify(userBankCardMapper, times(0)).createLoaner(any(UserBankCardModel.class));
    }

    @Test
    public void unbindSuccess(){
        ArgumentCaptor<String> loginNameCaptor = ArgumentCaptor.forClass(String.class);
        when(userBankCardMapper.findByLoginNameAndRole(anyString(), any())).thenReturn(mockUserBankCardModel());
        when(userMapper.findByLoginName(anyString())).thenReturn(mockUserModel());
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), any())).thenReturn(mockBankAccountModel());
        bankBindCardService.unbind("loginName", Source.WEB, "ip", "deviceId", Role.INVESTOR);
        verify(bankWrapperClient, times(1)).investorUnbindBankCard(any(), loginNameCaptor.capture(), anyString(), anyString(), anyString());
        verify(bankWrapperClient, times(0)).loanerUnbindBankCard(any(), anyString(), anyString(), anyString(), anyString());
        assertThat(loginNameCaptor.getValue(), is("loginName"));
    }

    @Test
    public void unbindFail(){
        when(userBankCardMapper.findByLoginNameAndRole(anyString(), any())).thenReturn(null);
        BankAsyncMessage message = bankBindCardService.unbind("loginName", Source.WEB, "ip", "deviceId", Role.INVESTOR);
        verify(bankWrapperClient, times(0)).investorUnbindBankCard(any(), anyString(), anyString(), anyString(), anyString());
        verify(bankWrapperClient, times(0)).loanerUnbindBankCard(any(), anyString(), anyString(), anyString(), anyString());
        assertFalse(message.isStatus());
        assertThat(message.getMessage(), is("未绑定银行卡"));
    }

    @Test
    public void processUnbindSuccess(){
        when(userBankCardMapper.findByLoginNameAndRole(anyString(), any())).thenReturn(mockUserBankCardModel());
        bankBindCardService.processUnbind(mockBankBindCardMessage());
        verify(userBankCardMapper, times(1)).updateStatus(anyLong(), eq(UserBankCardStatus.UNBOUND));
    }

    @Test
    public void processUnbindFail(){
        when(userBankCardMapper.findByLoginNameAndRole(anyString(), any())).thenReturn(null);
        bankBindCardService.processUnbind(mockBankBindCardMessage());
        verify(userBankCardMapper, times(0)).updateStatus(anyLong(), eq(UserBankCardStatus.UNBOUND));
    }

    private BankAccountModel mockBankAccountModel(){
        return new BankAccountModel("loginName", "bankUserName", "bankAccountNo", "1111111", "20180810", "22222222222");
    }

    private UserBankCardModel mockUserBankCardModel(){
        return new UserBankCardModel("loginName", "bank", "code", "111111111111111111", "111111", "20180810", UserBankCardStatus.BOUND);
    }

    private UserModel mockUserModel(){
        UserModel userModel = new UserModel();
        userModel.setLoginName("loginName");
        userModel.setMobile("11111111111");
        return userModel;
    }

    private BankBindCardMessage mockBankBindCardMessage(){
        BankBindCardMessage message = new BankBindCardMessage("loginName", "11111111111", "bankUserName", "bankAccountNo", "111111", "20180810", true);
        message.setCardNumber("111111111111111111");
        return message;
    }
}
