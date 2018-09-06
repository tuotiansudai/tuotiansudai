package com.tuotiansudai.mq.consumer.amount.service;

import com.tuotiansudai.enums.BillOperationType;
import com.tuotiansudai.enums.SystemBillBusinessType;
import com.tuotiansudai.message.BankSystemBillMessage;
import com.tuotiansudai.repository.mapper.BankSystemBillMapper;
import com.tuotiansudai.repository.model.BankSystemBillModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
public class BankSystemBillServiceTest {

    @InjectMocks
    private BankSystemBillService bankSystemBillService;

    @Mock
    private BankSystemBillMapper bankSystemBillMapper;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void systemBillProcess() {
        ArgumentCaptor<BankSystemBillModel> bankSystemBillModelCaptor = ArgumentCaptor.forClass(BankSystemBillModel.class);
        bankSystemBillService.systemBillProcess(mockBankSystemBillMessage());
        verify(bankSystemBillMapper, times(1)).create(bankSystemBillModelCaptor.capture());
        assertThat(bankSystemBillModelCaptor.getValue().getAmount(), is(10L));
        assertThat(bankSystemBillModelCaptor.getValue().getBusinessType(), is(SystemBillBusinessType.INVEST_FEE));
    }

    private BankSystemBillMessage mockBankSystemBillMessage() {
        return new BankSystemBillMessage(
                BillOperationType.IN,
                1,
                "111111",
                "20180810",
                10L,
                SystemBillBusinessType.INVEST_FEE,
                null);
    }
}
