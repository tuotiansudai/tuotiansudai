package com.tuotiansudai.service;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.enums.WithdrawStatus;
import com.tuotiansudai.fudian.message.BankWithdrawMessage;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.message.WeChatMessageNotify;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.BankWithdrawMapper;
import com.tuotiansudai.repository.mapper.UserBankCardMapper;
import com.tuotiansudai.repository.mapper.WeChatUserMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.BankWithdrawModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserBankCardModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by qduljs2011 on 2018/8/29.
 */
@ActiveProfiles("test")
public class BankWithdrawServiceTest {
    @InjectMocks
    private BankWithdrawService bankWithdrawService;
    @Mock
    private BankWrapperClient bankWrapperClient;
    @Mock
    private MQWrapperClient mqWrapperClient;
    @Mock
    private BankWithdrawMapper bankWithdrawMapper;
    @Mock
    private BankAccountMapper bankAccountMapper;
    @Mock
    private WeChatUserMapper weChatUserMapper;
    @Mock
    private UserBankCardMapper userBankCardMapper;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        Field bankWrapperClientField = BankWithdrawService.class.getDeclaredField("bankWrapperClient");
        bankWrapperClientField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(bankWrapperClientField, bankWrapperClientField.getModifiers() & ~Modifier.FINAL);
        bankWrapperClientField.set(this.bankWithdrawService, this.bankWrapperClient);
    }

    @Test
    public void withdrawSuccessLoaner() {
        BankAccountModel bankAccountMode = new BankAccountModel();
        UserBankCardModel userBankCardModel = new UserBankCardModel();
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), any(Role.class))).thenReturn(bankAccountMode);
        when(userBankCardMapper.findByLoginNameAndRole(anyString(), any(Role.class))).thenReturn(userBankCardModel);
        //
        bankWithdrawService.withdraw(Source.WEB, "loginName", "mobile", 1l, Role.LOANER);
        //
        verify(bankWithdrawMapper, times(1)).createLoaner(any(BankWithdrawModel.class));
        verify(bankWithdrawMapper, times(0)).createInvestor(any(BankWithdrawModel.class));
        verify(bankWrapperClient, times(1)).withdraw(anyLong(), any(Source.class), anyString(), anyString(), anyString(), anyString(), anyLong(), anyBoolean(), anyString());
    }

    @Test
    public void withdrawSuccessInvestor() {
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), any(Role.class))).thenReturn(new BankAccountModel());
        when(userBankCardMapper.findByLoginNameAndRole(anyString(), any(Role.class))).thenReturn(new UserBankCardModel());
        //
        bankWithdrawService.withdraw(Source.WEB, "loginName", "mobile", 1l, Role.INVESTOR);
        //
        verify(bankWithdrawMapper, times(0)).createLoaner(any(BankWithdrawModel.class));
        verify(bankWithdrawMapper, times(1)).createInvestor(any(BankWithdrawModel.class));
        verify(bankWrapperClient, times(1)).withdraw(anyLong(), any(Source.class), anyString(), anyString(), anyString(), anyString(), anyLong(), anyBoolean(), anyString());
    }

    @Test
    public void withdrawFail() {
        bankWithdrawService.withdraw(Source.WEB, "loginName", "mobile", 1l, null);
        //
        verify(bankWithdrawMapper, times(0)).createLoaner(any(BankWithdrawModel.class));
        verify(bankWithdrawMapper, times(0)).createInvestor(any(BankWithdrawModel.class));
        verify(bankWrapperClient, times(0)).withdraw(anyLong(), any(Source.class), anyString(), anyString(), anyString(), anyString(), anyLong(), anyBoolean(), anyString());
    }

    @Test
    public void processWithdrawSuccess() {
        BankWithdrawModel bankWithdrawModel = new BankWithdrawModel();
        bankWithdrawModel.setStatus(WithdrawStatus.WAIT_PAY);
        BankWithdrawMessage bankWithdrawMessage = new BankWithdrawMessage();
        bankWithdrawMessage.setStatus(true);
        //
        when(bankWithdrawMapper.findById(anyLong())).thenReturn(bankWithdrawModel);
        //
        bankWithdrawService.processWithdraw(bankWithdrawMessage);
        //
        verify(mqWrapperClient, times(1)).sendMessage(eq(MessageQueue.AmountTransfer), any(List.class));
        verify(mqWrapperClient, times(1)).sendMessage(eq(MessageQueue.EventMessage), any(List.class));
        verify(mqWrapperClient, times(1)).sendMessage(eq(MessageQueue.PushMessage), any(PushMessage.class));
        verify(mqWrapperClient, times(1)).sendMessage(eq(MessageQueue.WeChatMessageNotify), any(WeChatMessageNotify.class));
        verify(bankWithdrawMapper, times(1)).update(any(BankWithdrawModel.class));

    }

    @Test
    public void processWithdrawFalse() {
        BankWithdrawModel bankWithdrawModel = new BankWithdrawModel();
        bankWithdrawModel.setStatus(WithdrawStatus.WAIT_PAY);
        BankWithdrawMessage bankWithdrawMessage = new BankWithdrawMessage();
        bankWithdrawMessage.setStatus(false);
        //
        when(bankWithdrawMapper.findById(anyLong())).thenReturn(bankWithdrawModel);
        //
        bankWithdrawService.processWithdraw(bankWithdrawMessage);
        //
        verify(bankWithdrawMapper, times(1)).update(any(BankWithdrawModel.class));
        verify(mqWrapperClient, times(0)).sendMessage(any(MessageQueue.class), any());
    }
}
