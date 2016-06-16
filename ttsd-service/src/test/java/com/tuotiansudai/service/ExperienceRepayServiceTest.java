package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class ExperienceRepayServiceTest {
    @Autowired
    ExperienceRepayService experienceRepayService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    LoanMapper loanMapper;

    @Autowired
    InvestMapper investMapper;

    @Autowired
    InvestRepayMapper investRepayMapper;

    @Autowired
    private IdGenerator idGenerator;

    private long loanId;

    private long investId;

    private long investRepayId;

    @Before
    public void setUp() throws Exception {
        loanId = idGenerator.generate();
        investId = idGenerator.generate();
        investRepayId = idGenerator.generate();
    }

    private UserModel createUserByLoginName(String loginName) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("1" + RandomStringUtils.randomNumeric(10));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
        return userModelTest;
    }

    private LoanModel createLoanByLoginName(String loginName, long loanId, ProductType productType) {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName(loginName);
        loanDto.setLoanerUserName("借款人");
        loanDto.setLoanerIdentityNumber("111111111111111111");
        loanDto.setAgentLoginName(loginName);
        loanDto.setBasicRate("16.00");
        loanDto.setId(loanId);
        loanDto.setProjectName("店铺资金周转");
        loanDto.setActivityRate("12");
        loanDto.setShowOnHome(true);
        loanDto.setPeriods(31);
        loanDto.setActivityType(ActivityType.NEWBIE);
        loanDto.setContractId(123);
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("5888000");
        loanDto.setProductType(productType);
        loanDto.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.WAITING_VERIFY);
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setStatus(LoanStatus.RAISING);
        loanModel.setUpdateTime(new Date());
        loanMapper.create(loanModel);
        return loanModel;
    }

    private InvestModel createInvest(long id, String loginName, long loanId, long amount) {
        InvestModel model = new InvestModel();
        model.setAmount(amount);
        model.setInvestTime(new Date());
        model.setId(id);
        model.setTransferStatus(TransferStatus.TRANSFERABLE);
        model.setIsAutoInvest(false);
        model.setLoginName(loginName);
        model.setLoanId(loanId);
        model.setSource(Source.ANDROID);
        model.setStatus(InvestStatus.SUCCESS);
        investMapper.create(model);
        return model;
    }

    private InvestRepayModel createInvestRepay(long id, long investId, RepayStatus repayStatus, Date repayDate) {
        InvestRepayModel investRepayModel = new InvestRepayModel();
        investRepayModel.setId(id);
        investRepayModel.setInvestId(investId);
        investRepayModel.setStatus(repayStatus);
        investRepayModel.setCreatedTime(new Date());
        investRepayModel.setExpectedFee(5);
        investRepayModel.setExpectedInterest(10);
        investRepayModel.setRepayDate(repayDate);
        investRepayModel.setPeriod(1);
        investRepayMapper.create(Lists.newArrayList(investRepayModel));
        return investRepayModel;
    }

    private void prepareData() {
        createUserByLoginName("testLoanUser");
        createUserByLoginName("testInvestUser");

        createLoanByLoginName("testLoanUser", loanId, ProductType.EXPERIENCE);

        createInvest(investId, "testInvestUser", loanId, 5888);

        createInvestRepay(investRepayId, investId, RepayStatus.REPAYING, new Date());
    }

    @Test
    public void testNewbieExperienceService() {
        prepareData();

        Date repayDate = new Date();
        experienceRepayService.repay(repayDate);

        InvestRepayModel investRepayModel = investRepayMapper.findById(investRepayId);
        assertEquals(RepayStatus.COMPLETE, investRepayModel.getStatus());
        assertEquals(investRepayModel.getExpectedFee(), investRepayModel.getActualFee());
        assertEquals(investRepayModel.getExpectedInterest(), investRepayModel.getActualInterest());
        assertEquals(investRepayModel.getRepayAmount(), investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee());
    }
}
