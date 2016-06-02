package com.tuotiansudai.service;

import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import org.apache.commons.lang3.RandomStringUtils;
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
        investRepayModel.setActualFee(0);
        investRepayModel.setExpectedInterest(10);
        investRepayModel.setActualInterest(0);
        investRepayModel.setRepayDate(repayDate);
        investRepayModel.setActualRepayDate(new Date());
        investRepayModel.setPeriod(1);
        List<InvestRepayModel> investRepayModels = new ArrayList<>();
        investRepayModels.add(investRepayModel);
        investRepayMapper.create(investRepayModels);
        return investRepayModel;
    }

    private void prepareData() {
        createUserByLoginName("testLoanUser");
        createUserByLoginName("testInvestUser");

        createInvest(11, "testInvestUser", 1, 5888);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, Calendar.MAY, 22, 0, 0, 0);
        createInvestRepay(111, 11, RepayStatus.REPAYING, calendar.getTime());
    }

    @Test
    public void testNewbieExperienceService() {
        prepareData();

        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, Calendar.MAY, 22, 0, 0, 0);
        Date compareDate = calendar.getTime();
        calendar.set(Calendar.HOUR, 16);
        Date actualRepayDate = calendar.getTime();

        experienceRepayService.repay(compareDate, actualRepayDate);

        calendar.set(Calendar.MILLISECOND, 0);
        actualRepayDate = calendar.getTime();

        InvestRepayModel investRepayModel = investRepayMapper.findById(111);
        assertEquals(RepayStatus.COMPLETE, investRepayModel.getStatus());
        assertEquals(investRepayModel.getExpectedFee(), investRepayModel.getActualFee());
        assertEquals(investRepayModel.getExpectedInterest(), investRepayModel.getActualInterest());
        assertEquals(actualRepayDate, investRepayModel.getActualRepayDate());

    }
}
