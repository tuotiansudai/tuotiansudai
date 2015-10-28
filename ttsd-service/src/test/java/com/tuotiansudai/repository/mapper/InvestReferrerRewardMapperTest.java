package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class InvestReferrerRewardMapperTest {

    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private InvestMapper investMapper;
    @Autowired
    private InvestReferrerRewardMapper investReferrerRewardMapper;

    @Test
    public void shouldCreateInvestReferrerReward(){
        UserModel userModelTest = createMockUser("helloworld");
        LoanModel loanModel = createMockLoan(userModelTest.getLoginName());
        InvestModel model = createMockInvest(userModelTest.getLoginName(), loanModel.getId());

        InvestReferrerRewardModel investReferrerRewardModel = new InvestReferrerRewardModel();
        long id = idGenerator.generate();
        investReferrerRewardModel.setId(id);
        investReferrerRewardModel.setStatus(ReferrerRewardStatus.SUCCESS);
        investReferrerRewardModel.setReferrerLoginName(userModelTest.getLoginName());
        investReferrerRewardModel.setTime(new Date());
        investReferrerRewardModel.setBonus(100);
        investReferrerRewardModel.setInvestId(model.getId());
        investReferrerRewardModel.setRoleName(Role.INVESTOR);
        investReferrerRewardMapper.create(investReferrerRewardModel);

        InvestReferrerRewardModel investReferrerRewardModel1 = investReferrerRewardMapper.findById(id);
        assertNotNull(investReferrerRewardModel1);
    }

    private InvestModel createMockInvest(String loginName, long loanId) {
        InvestModel model = new InvestModel();
        model.setAmount(1000000);
        // 舍弃毫秒数
        Date currentDate = new Date((new Date().getTime()/1000)*1000);
        model.setCreatedTime(currentDate);
        model.setId(idGenerator.generate());
        model.setIsAutoInvest(false);
        model.setLoginName(loginName);
        model.setLoanId(loanId);
        model.setSource(Source.ANDROID);
        model.setStatus(InvestStatus.SUCCESS);
        investMapper.create(model);
        return model;
    }

    private UserModel createMockUser(String loginName) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("139" + RandomStringUtils.randomNumeric(8));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
        return userModelTest;
    }


    private LoanModel createMockLoan(String loginName) {
        LoanModel fakeLoan = this.getFakeLoan(loginName, loginName, LoanStatus.PREHEAT);
        loanMapper.create(fakeLoan);
        return fakeLoan;
    }


    private LoanModel getFakeLoan(String loanerLoginName, String agentLoginName, LoanStatus loanStatus) {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(idGenerator.generate());
        fakeLoanModel.setName("loanName");
        fakeLoanModel.setLoanerLoginName(loanerLoginName);
        fakeLoanModel.setAgentLoginName(agentLoginName);
        fakeLoanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        fakeLoanModel.setPeriods(3);
        fakeLoanModel.setStatus(loanStatus);
        fakeLoanModel.setActivityType(ActivityType.NORMAL);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setCreatedTime(new Date());
        return fakeLoanModel;
    }
}
