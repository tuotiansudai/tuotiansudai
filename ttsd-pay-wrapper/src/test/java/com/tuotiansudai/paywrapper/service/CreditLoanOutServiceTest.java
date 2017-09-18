package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.CreditLoanBillMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.RedisWrapperClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class CreditLoanOutServiceTest {

    @Autowired
    private CreditLoanBillMapper creditLoanBillMapper;

    @Autowired
    private CreditLoanOutService creditLoanOutService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final static String CREDIT_LOAN_OUT_REPAY_AGENT_SUM_AMOUNT = "CREDIT_LOAN_OUT_REPAY_AGENT_SUM_AMOUNT";

    private final static String CREDIT_LOAN_OUT_REPAY_AGENT_AMOUNT = "CREDIT_LOAN_OUT_REPAY_AGENT_AMOUNT";

    @Test
    public void shouldCreditLoanOutIsSuccess(){
        createCreditCreditLoanBill();
        UserModel userModel = getFakeUserModel();
        deleteRedis(userModel.getLoginName());
        userMapper.create(userModel);

        AccountModel accountModel = new AccountModel("loginName","payUserId","payAccountId",new Date());
        accountModel.setBalance(1000l);
        accountMapper.create(accountModel);

        creditLoanOutService.creditLoanOut();
        assertFalse(redisWrapperClient.hexists(CREDIT_LOAN_OUT_REPAY_AGENT_SUM_AMOUNT, userModel.getLoginName()));
        assertEquals(600l,Long.parseLong(redisWrapperClient.hget(CREDIT_LOAN_OUT_REPAY_AGENT_AMOUNT, userModel.getLoginName())));

        redisWrapperClient.hset(CREDIT_LOAN_OUT_REPAY_AGENT_SUM_AMOUNT, userModel.getLoginName(), "600");
        createCreditCreditLoanBill();
        creditLoanOutService.creditLoanOut();
        assertEquals(600l, Long.parseLong(redisWrapperClient.hget(CREDIT_LOAN_OUT_REPAY_AGENT_SUM_AMOUNT, userModel.getLoginName())));
        assertEquals(600l,Long.parseLong(redisWrapperClient.hget(CREDIT_LOAN_OUT_REPAY_AGENT_AMOUNT, userModel.getLoginName())));
        deleteRedis(userModel.getLoginName());

    }

    private void createCreditCreditLoanBill(){
        CreditLoanBillModel creditLoanBillModel = new CreditLoanBillModel();
        creditLoanBillModel.setCreatedTime(new Date());
        creditLoanBillModel.setDetail("detail");
        creditLoanBillModel.setAmount(100);
        creditLoanBillModel.setBusinessType(CreditLoanBillBusinessType.XYD_USER_REPAY);
        creditLoanBillModel.setOrderId(11111111111111L);
        creditLoanBillModel.setOperationType(SystemBillOperationType.IN);
        creditLoanBillModel.setLoginName("loginName");
        creditLoanBillMapper.create(creditLoanBillModel);
        creditLoanBillModel.setAmount(200);
        creditLoanBillMapper.create(creditLoanBillModel);
        creditLoanBillModel.setAmount(300);
        creditLoanBillMapper.create(creditLoanBillModel);
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

    public void deleteRedis(String loginName){
        redisWrapperClient.hdel(CREDIT_LOAN_OUT_REPAY_AGENT_AMOUNT,loginName);
        redisWrapperClient.hdel(CREDIT_LOAN_OUT_REPAY_AGENT_SUM_AMOUNT,loginName);
    }


}
