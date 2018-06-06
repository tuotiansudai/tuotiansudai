package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.PayrollDetailMapper;
import com.tuotiansudai.repository.mapper.PayrollMapper;
import com.tuotiansudai.repository.model.*;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:dispatcher-servlet.xml"})
@Transactional
public class PayrollServiceTest {

    @Autowired
    private PayrollMapper payrollMapper;

    @Autowired
    private PayrollDetailMapper payrollDetailMapper;

    @Mock
    private PaySyncClient paySyncClient;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    @Autowired
    private PayrollService payrollService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldPay() throws Exception {
        PayrollModel payrollModel = mockPayroll();
        mockPayrollDetail(payrollModel);

        TransferResponseModel mockResponse = new TransferResponseModel();
        mockResponse.setRetCode("0000");
        AccountModel mockAccount = new AccountModel("test", "test", "test", new Date());

        when(accountMapper.findByLoginName(anyString())).thenReturn(mockAccount);
        when(paySyncClient.send(any(), any(), any())).thenReturn(mockResponse);

        payrollService.pay(payrollModel.getId());

        PayrollModel payroll = payrollMapper.findById(payrollModel.getId());
        List<PayrollDetailModel> payrollDetailModels = payrollDetailMapper.findByPayrollId(payrollModel.getId());

        assertEquals(PayrollStatusType.SUCCESS, payroll.getStatus());
        assertTrue(payrollDetailModels.stream().allMatch(m -> m.getStatus() == PayrollPayStatus.SUCCESS));
    }

    private PayrollModel mockPayroll() {
        PayrollModel payrollModel = new PayrollModel("title", 900000L, 5);
        payrollModel.setStatus(PayrollStatusType.AUDITED);
        payrollModel.setCreatedBy("loginName");
        payrollModel.setCreatedTime(new Date());
        payrollMapper.create(payrollModel);
        return payrollModel;
    }

    private void mockPayrollDetail(PayrollModel payrollModel) {
        long totalAmount = payrollModel.getTotalAmount();
        long count = payrollModel.getHeadCount();
        for (int i = 0; i < count; i++) {
            long amount = totalAmount / (count - i);
            PayrollDetailModel detailModel = new PayrollDetailModel(
                    RandomStringUtils.randomAlphabetic(5),
                    RandomStringUtils.randomAlphabetic(3),
                    "138" + RandomStringUtils.randomNumeric(8), amount);
            detailModel.setLoginName(RandomStringUtils.randomAlphabetic(8));
            detailModel.setStatus(PayrollPayStatus.WAITING);
            detailModel.setPayrollId(payrollModel.getId());
            payrollDetailMapper.create(Lists.newArrayList(detailModel));
            totalAmount -= amount;
        }
    }
}
