package com.tuotiansudai.service;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.RechargeStatus;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.fudian.message.BankRechargeMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.BankRechargeMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.BankRechargeModel;
import com.tuotiansudai.repository.model.Source;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by qduljs2011 on 2018/8/28.
 */
@ActiveProfiles("test")
public class BankRechargeServiceTest {
    @InjectMocks
    private BankRechargeService bankRechargeService;

    @Mock
    private BankRechargeMapper bankRechargeMapper;

    @Mock
    private BankAccountMapper bankAccountMapper;

    @Mock
    private MQWrapperClient mqWrapperClient;

    @Mock
    private BankWrapperClient bankWrapperClient;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        Field bankWrapperClientField = BankRechargeService.class.getDeclaredField("bankWrapperClient");
        bankWrapperClientField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(bankWrapperClientField, bankWrapperClientField.getModifiers() & ~Modifier.FINAL);
        bankWrapperClientField.set(this.bankRechargeService, this.bankWrapperClient);
    }

    @Test
    public void processRechargeSuccess() {
        BankRechargeModel bankRechargeModel = new BankRechargeModel();
        bankRechargeModel.setStatus(RechargeStatus.WAIT_PAY);
        BankRechargeMessage bankRechargeMessage = new BankRechargeMessage();
        bankRechargeMessage.setStatus(true);
        //
        when(bankRechargeMapper.findById(anyLong())).thenReturn(bankRechargeModel);
        //
        bankRechargeService.processRecharge(bankRechargeMessage);
        //
        verify(mqWrapperClient, times(1)).sendMessage(any(MessageQueue.AmountTransfer.getClass()), any(List.class));
        verify(bankRechargeMapper, times(1)).update(any(BankRechargeModel.class));

    }

    @Test
    public void processRechargeFalse() {
        BankRechargeModel bankRechargeModel = new BankRechargeModel();
        bankRechargeModel.setStatus(RechargeStatus.WAIT_PAY);
        BankRechargeMessage bankRechargeMessage = new BankRechargeMessage();
        bankRechargeMessage.setStatus(false);
        //
        when(bankRechargeMapper.findById(anyLong())).thenReturn(bankRechargeModel);
        //
        bankRechargeService.processRecharge(bankRechargeMessage);
        //
        verify(bankRechargeMapper, times(1)).update(any(BankRechargeModel.class));
        verify(mqWrapperClient, times(0)).sendMessage(any(MessageQueue.AmountTransfer.getClass()), any());

    }

    @Test
    public void processRechargeFalseStatus() {
        BankRechargeModel bankRechargeModel = new BankRechargeModel();
        BankRechargeMessage bankRechargeMessage = new BankRechargeMessage();
        //
        when(bankRechargeMapper.findById(anyLong())).thenReturn(bankRechargeModel);
        //
        bankRechargeService.processRecharge(bankRechargeMessage);
        //
        verify(bankRechargeMapper, times(0)).update(any(BankRechargeModel.class));
        verify(mqWrapperClient, times(0)).sendMessage(any(MessageQueue.class), any());

    }

    @Test
    public void rechargeSuccessLoaner() {
        BankAccountModel bankAccountMode = new BankAccountModel();
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), any(Role.class))).thenReturn(bankAccountMode);
        //
        bankRechargeService.recharge(Source.WEB, "loginName", "mobile", 1l, "payType", "channel", Role.LOANER);
        //
        verify(bankRechargeMapper, times(1)).createLoaner(any(BankRechargeModel.class));
        verify(bankRechargeMapper, times(0)).createInvestor(any(BankRechargeModel.class));
        verify(bankWrapperClient, times(1)).recharge(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString());
    }

    @Test
    public void rechargeSuccessInvestor() {
        BankAccountModel bankAccountMode = new BankAccountModel();
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), any(Role.class))).thenReturn(bankAccountMode);
        //
        bankRechargeService.recharge(Source.WEB, "loginName", "mobile", 1l, "payType", "channel", Role.INVESTOR);
        //
        verify(bankRechargeMapper, times(0)).createLoaner(any(BankRechargeModel.class));
        verify(bankRechargeMapper, times(1)).createInvestor(any(BankRechargeModel.class));
        verify(bankWrapperClient, times(1)).recharge(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString());
    }

    @Test
    public void rechargeFail() {
        bankRechargeService.recharge(Source.WEB, "loginName", "mobile", 1l, "payType", "channel", null);
        //
        verify(bankRechargeMapper, times(0)).createLoaner(any(BankRechargeModel.class));
        verify(bankRechargeMapper, times(0)).createInvestor(any(BankRechargeModel.class));
        verify(bankWrapperClient, times(0)).recharge(anyLong(), any(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyString());
    }
}
