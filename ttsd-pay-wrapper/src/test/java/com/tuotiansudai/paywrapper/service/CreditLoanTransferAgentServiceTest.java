package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.paywrapper.service.impl.CreditLoanTransferAgentServiceImpl;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.CreditLoanBillMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.RedisWrapperClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class CreditLoanTransferAgentServiceTest {

    @InjectMocks
    private CreditLoanTransferAgentServiceImpl creditLoanTransferAgentService;

    @Mock
    private CreditLoanBillMapper creditLoanBillMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private PaySyncClient paySyncClient;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final static String CREDIT_LOAN_TRANSFER_AGENT_SUM_AMOUNT = "CREDIT_LOAN_TRANSFER_AGENT_SUM_AMOUNT";

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCreditLoanOutIsSuccess() throws Exception{
        UserModel userModel = getFakeUserModel();
        deleteRedis(userModel.getLoginName());

        AccountModel accountModel = new AccountModel("loginName","payUserId","payAccountId",new Date());
        accountModel.setBalance(1000l);

        when(creditLoanBillMapper.findSumAmountByIncome(any(),any())).thenReturn(600l);
        when(userMapper.findByMobile(anyString())).thenReturn(userModel);
        when(accountMapper.findByLoginName(anyString())).thenReturn(accountModel);

        ProjectTransferResponseModel responseModel = new ProjectTransferResponseModel();
        Map<String, String> resData = new HashMap<>();
        resData.put("ret_code", "0000");
        responseModel.initializeModel(resData);
        when(paySyncClient.send(any(), any(), any())).thenReturn(responseModel);

        creditLoanTransferAgentService.creditLoanTransferAgent();
        assertFalse(redisWrapperClient.hexists(CREDIT_LOAN_TRANSFER_AGENT_SUM_AMOUNT, userModel.getLoginName()));

        redisWrapperClient.hset(CREDIT_LOAN_TRANSFER_AGENT_SUM_AMOUNT, userModel.getLoginName(), "600");
        when(creditLoanBillMapper.findSumAmountByIncome(any(),any())).thenReturn(1200l);
        creditLoanTransferAgentService.creditLoanTransferAgent();
        assertEquals(600l, Long.parseLong(redisWrapperClient.hget(CREDIT_LOAN_TRANSFER_AGENT_SUM_AMOUNT, userModel.getLoginName())));
        deleteRedis(userModel.getLoginName());

    }

    public UserModel getFakeUserModel() {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("loginName");
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("00000000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }

    public void deleteRedis(String agentMobile){
        redisWrapperClient.hdel(CREDIT_LOAN_TRANSFER_AGENT_SUM_AMOUNT,agentMobile);
    }
}
