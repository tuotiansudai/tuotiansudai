package com.tuotiansudai.console.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.LoanDto;
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
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class InvestAchievementServiceTest {

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private InvestAchievementService investAchievementService;

    private InvestModel createInvest(String loginName, long loanId) {
        InvestModel model = new InvestModel();
        model.setAmount(10);
        model.setCreatedTime(new Date());
        model.setTradingTime(new Date());
        model.setId(idGenerator.generate());
        model.setIsAutoInvest(false);
        model.setLoginName(loginName);
        model.setLoanId(loanId);
        model.setSource(Source.ANDROID);
        model.setStatus(InvestStatus.SUCCESS);
        model.setTransferStatus(TransferStatus.TRANSFERABLE);
        investMapper.create(model);
        return model;
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
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setStatus(LoanStatus.REPAYING);
        loanMapper.create(loanModel);
        return loanModel;
    }

    @Test
    public void shouldGetInvestAchievement() {
        UserModel userModelAgent = createUserByUserId("agent");
        UserModel userModelFirstMan = createUserByUserId("firstMan");
        UserModel userModelLastMan = createUserByUserId("lastMan");
        UserModel userModelMaxMan = createUserByUserId("maxMan");

        long loanId = idGenerator.generate();
        LoanModel loanModel = createLoanByUserId(userModelAgent.getLoginName(), loanId);

        InvestModel investModelFirst = createInvest(userModelFirstMan.getLoginName(), loanId);
        investModelFirst.setAchievements(Lists.newArrayList(InvestAchievement.FIRST_INVEST));
        investMapper.update(investModelFirst);

        InvestModel investModelLast = createInvest(userModelLastMan.getLoginName(), loanId);
        investModelLast.setAchievements(Lists.newArrayList(InvestAchievement.LAST_INVEST));
        investMapper.update(investModelLast);

        InvestModel investModelMax = createInvest(userModelMaxMan.getLoginName(), loanId);
        investModelMax.setAchievements(Lists.newArrayList(InvestAchievement.MAX_AMOUNT));
        investMapper.update(investModelMax);

        loanModel.setFirstInvestAchievementId(investModelFirst.getId());
        loanModel.setLastInvestAchievementId(investModelLast.getId());
        loanModel.setMaxAmountAchievementId(investModelMax.getId());

        loanMapper.update(loanModel);

        List<LoanAchievementView> investAchievementDtosFirst = investAchievementService.findInvestAchievement(1, 10, userModelFirstMan.getLoginName());
        assertThat(investAchievementDtosFirst.get(0).getFirstInvestLoginName(), is(userModelFirstMan.getLoginName()));

        List<LoanAchievementView> investAchievementDtosLast = investAchievementService.findInvestAchievement(1, 10, userModelLastMan.getLoginName());
        assertThat(investAchievementDtosLast.get(0).getLastInvestLoginName(), is(userModelLastMan.getLoginName()));

        List<LoanAchievementView> investAchievementDtosMax = investAchievementService.findInvestAchievement(1, 10, userModelMaxMan.getLoginName());
        assertThat(investAchievementDtosMax.get(0).getMaxAmountLoginName(), is(userModelMaxMan.getLoginName()));

    }

}
