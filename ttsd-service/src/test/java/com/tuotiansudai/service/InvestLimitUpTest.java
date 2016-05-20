package com.tuotiansudai.service;

import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.exception.InvestExceptionType;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class InvestLimitUpTest {

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestService investService;

    @Autowired
    private AccountMapper accountMapper;

    private LoanModel createLoanByUserId(String userId, long loanId) {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName(userId);
        loanDto.setLoanerUserName("借款人");
        loanDto.setLoanerIdentityNumber("111111111111111111");
        loanDto.setAgentLoginName(userId);
        loanDto.setBasicRate("16.00");
        loanDto.setId(loanId);
        loanDto.setProjectName("店铺资金周转");
        loanDto.setActivityRate("12");
        loanDto.setShowOnHome(true);
        loanDto.setPeriods(30);
        loanDto.setActivityType(ActivityType.NEWBIE);
        loanDto.setContractId(123);
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        loanDto.setInvestFeeRate("15");
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("10000");
        loanDto.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.WAITING_VERIFY);
        loanDto.setProductType(ProductType._30);
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setStatus(LoanStatus.RAISING);
        loanMapper.create(loanModel);
        return loanModel;
    }

    private UserModel createUserByUserId(String userId) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(userId);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("1" + RandomStringUtils.randomNumeric(10));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
        return userModelTest;
    }

    private InvestModel createInvest(String loginName, long loanId) {
        InvestModel model = new InvestModel();
        model.setAmount(10);
        model.setInvestTime(new Date());
        model.setId(idGenerator.generate());
        model.setIsAutoInvest(false);
        model.setLoginName(loginName);
        model.setLoanId(loanId);
        model.setSource(Source.ANDROID);
        model.setStatus(InvestStatus.SUCCESS);
        model.setTransferStatus(TransferStatus.TRANSFERRING);
        investMapper.create(model);
        return model;
    }

    private AccountModel createAccountByUserId(String userId) {
        AccountModel accountModel = new AccountModel(userId, userId, "120101198810012010", "", "", new Date());
        accountModel.setBalance(10000);
        accountModel.setFreeze(10000);
        accountMapper.create(accountModel);
        accountModel.setAutoInvest(true);
        accountModel.setNoPasswordInvest(true);
        accountMapper.update(accountModel);
        return accountModel;
    }

    @Test
    public void shouldInvestNewbieFailed() {
        UserModel userModelInvest = createUserByUserId("testUserInvest");
        UserModel userModelLoaner = createUserByUserId("testUserLoaner");
        createAccountByUserId(userModelInvest.getLoginName());
        long loanId = idGenerator.generate();
        LoanModel loanModel = createLoanByUserId(userModelLoaner.getLoginName(), loanId);
        InvestModel investModel = createInvest(userModelInvest.getLoginName(), loanId);
        InvestDto investDto = new InvestDto();
        investDto.setLoginName(userModelInvest.getLoginName());
        investDto.setAmount("11");
        investDto.setNoPassword(false);
        investDto.setLoanId(String.valueOf(loanId));
        investDto.setSource(Source.WEB);
        try {
            investService.invest(investDto);
        } catch (InvestException e) {
            assertThat(e.getType(), is(InvestExceptionType.OUT_OF_NOVICE_INVEST_LIMIT));
        }
    }

    @Test
    public void shouldNoPasswordInvestNewbieFailed() {
        UserModel userModelInvest = createUserByUserId("testUserInvest");
        UserModel userModelLoaner = createUserByUserId("testUserLoaner");
        createAccountByUserId(userModelInvest.getLoginName());
        long loanId = idGenerator.generate();
        LoanModel loanModel = createLoanByUserId(userModelLoaner.getLoginName(), loanId);
        InvestModel investModel = createInvest(userModelInvest.getLoginName(), loanId);
        InvestDto investDto = new InvestDto();
        investDto.setLoginName(userModelInvest.getLoginName());
        investDto.setAmount("11");
        investDto.setNoPassword(true);
        investDto.setLoanId(String.valueOf(loanId));
        investDto.setSource(Source.WEB);
        try {
            investService.noPasswordInvest(investDto);
        } catch (InvestException e) {
            assertThat(e.getType(), is(InvestExceptionType.OUT_OF_NOVICE_INVEST_LIMIT));
        }
    }

}
