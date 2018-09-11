package com.tuotiansudai.mq.consumer.amount.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.enums.BankUserBillBusinessType;
import com.tuotiansudai.enums.BillOperationType;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.BankUserBillMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.BankUserBillModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class AmountTransferServiceTest {

    @InjectMocks
    private AmountTransferService amountTransferService;

    @Mock
    private BankAccountMapper bankAccountMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BankUserBillMapper bankUserBillMapper;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void process(){

        ArgumentCaptor<Long> amountCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<BankUserBillModel> bankUserBillModelCaptor = ArgumentCaptor.forClass(BankUserBillModel.class);
        when(bankAccountMapper.findByLoginNameAndRole(eq("loginName"), eq(Role.INVESTOR))).thenReturn(mockBankAccountModel());
        when(userMapper.findByLoginName(eq("loginName"))).thenReturn(mockUserModel());

        amountTransferService.process(mockAmountTransferMessageList());

        verify(bankAccountMapper, times(2)).updateInvestorBalance(eq("loginName"), amountCaptor.capture());
        verify(bankUserBillMapper, times(2)).create(bankUserBillModelCaptor.capture());

        assertThat(amountCaptor.getAllValues().get(0), is(1000L));
        assertThat(amountCaptor.getAllValues().get(1), is(-10L));
        assertThat(bankUserBillModelCaptor.getAllValues().get(0).getAmount(), is(1000L));
        assertThat(bankUserBillModelCaptor.getAllValues().get(0).getBusinessType(), is(BankUserBillBusinessType.NORMAL_REPAY));
        assertThat(bankUserBillModelCaptor.getAllValues().get(1).getAmount(), is(10L));
        assertThat(bankUserBillModelCaptor.getAllValues().get(1).getBusinessType(), is(BankUserBillBusinessType.INVEST_FEE));
    }

    private List<AmountTransferMessage> mockAmountTransferMessageList(){
        return Lists.newArrayList(
                new AmountTransferMessage(1,
                        "loginName",
                        Role.INVESTOR,
                        1000L,
                        "111111",
                        "20180810",
                        BillOperationType.IN,
                        BankUserBillBusinessType.NORMAL_REPAY),
                new AmountTransferMessage(2,
                        "loginName",
                        Role.INVESTOR,
                        10L,
                        "111111",
                        "20180810",
                        BillOperationType.OUT,
                        BankUserBillBusinessType.INVEST_FEE));
    }

    private BankAccountModel mockBankAccountModel(){
        BankAccountModel bankAccountModel = new BankAccountModel();
        bankAccountModel.setBalance(0);
        bankAccountModel.setLoginName("loginName");
        return bankAccountModel;
    }

    private UserModel mockUserModel(){
        UserModel userModel = new UserModel();
        userModel.setMobile("11111111111");
        userModel.setLoginName("loginName");
        return userModel;
    }
}
