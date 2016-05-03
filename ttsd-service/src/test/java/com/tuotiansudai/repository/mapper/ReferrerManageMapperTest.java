package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.ReferrerManageView;
import com.tuotiansudai.repository.model.ReferrerRelationView;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class ReferrerManageMapperTest {

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestReferrerRewardMapper investReferrerRewardMapper;

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    @Autowired
    private ReferrerManageMapper referrerManageMapper;

    @Test
    public void shouldGetSomeReferRelationship() throws Exception {
        UserModel user1 = new UserModel();
        user1.setId(idGenerator.generate());
        user1.setLoginName("test1");
        user1.setPassword("123");
        user1.setMobile("13900000000");
        user1.setRegisterTime(new Date());
        user1.setLastModifiedTime(new Date());
        user1.setStatus(UserStatus.ACTIVE);
        user1.setSalt("123");
        userMapper.create(user1);

        UserModel  user2 = new UserModel();
        user2.setId(idGenerator.generate());
        user2.setLoginName("test2");
        user2.setPassword("123");
        user2.setMobile("13900000001");
        user2.setRegisterTime(new Date());
        user2.setLastModifiedTime(new Date());
        user2.setStatus(UserStatus.ACTIVE);
        user2.setSalt("123");
        userMapper.create(user2);

        ReferrerRelationModel referrerRelationModel = new ReferrerRelationModel();
        referrerRelationModel.setReferrerLoginName("test1");
        referrerRelationModel.setLoginName("test2");
        referrerRelationModel.setLevel(1);
        referrerRelationMapper.create(referrerRelationModel);

        Calendar start =  Calendar.getInstance();
        start.add(Calendar.DATE, -300);

        Calendar end =  Calendar.getInstance();
        end.add(Calendar.DATE, 1);
        List<ReferrerRelationView> referRelationList = referrerManageMapper.findReferRelationList("test1", "test2", start.getTime(), end.getTime(), null, 0, 10);
        assertNotNull(referRelationList.get(0));

        int referRelationCount = referrerManageMapper.findReferRelationCount("test1", null, start.getTime(), end.getTime(), null);
        assert(referRelationCount>0);
    }

    @Test
    public void shouldGetSomeReferInvest() throws Exception {

        UserModel user1 = new UserModel();
        user1.setId(idGenerator.generate());
        user1.setLoginName("test1");
        user1.setPassword("123");
        user1.setMobile("13900000000");
        user1.setRegisterTime(new Date());
        user1.setLastModifiedTime(new Date());
        user1.setStatus(UserStatus.ACTIVE);
        user1.setSalt("123");
        userMapper.create(user1);

        UserModel  user2 = new UserModel();
        user2.setId(idGenerator.generate());
        user2.setLoginName("test2");
        user2.setPassword("123");
        user2.setMobile("13900000001");
        user2.setRegisterTime(new Date());
        user2.setLastModifiedTime(new Date());
        user2.setStatus(UserStatus.ACTIVE);
        user2.setSalt("123");
        userMapper.create(user2);

        ReferrerRelationModel referrerRelationModel = new ReferrerRelationModel();
        referrerRelationModel.setReferrerLoginName("test1");
        referrerRelationModel.setLoginName("test2");
        referrerRelationModel.setLevel(1);
        referrerRelationMapper.create(referrerRelationModel);

        LoanModel loanModel = createMockLoan(user2.getLoginName());
        InvestModel investModel = createMockInvest(user2.getLoginName(), loanModel.getId());

        long id = idGenerator.generate();
        InvestReferrerRewardModel investReferrerRewardModel = new InvestReferrerRewardModel(id, investModel.getId(), 100, user1.getLoginName(), Role.INVESTOR);
        investReferrerRewardMapper.create(investReferrerRewardModel);

        Calendar start =  Calendar.getInstance();
        start.add(Calendar.DATE, -300);

        Calendar end =  Calendar.getInstance();
        end.add(Calendar.DATE, 1);
        List<ReferrerManageView> referManageList = referrerManageMapper.findReferInvestList("test1", null, start.getTime(), end.getTime(), null, 0, 10);

        assertNotNull(referManageList.get(0));

        int referInvestCount = referrerManageMapper.findReferInvestCount("test1", "test2", null, end.getTime(), null);

        assert(referInvestCount>0);
    }

    private InvestModel createMockInvest(String loginName, long loanId) {
        InvestModel model = new InvestModel(idGenerator.generate(), loanId, null, 1, loginName, Source.WEB, null);
        model.setCreatedTime(new DateTime().withTimeAtStartOfDay().toDate());
        model.setStatus(InvestStatus.SUCCESS);
        investMapper.create(model);
        return model;
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
        fakeLoanModel.setLoanerUserName("借款人");
        fakeLoanModel.setLoanerIdentityNumber("111111111111111111");
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

    @Test
    public void findReferInvestSumAmount() throws Exception {

        readyDate("testReferInvest");
        List<ReferrerManageView> referrerManageViews = referrerManageMapper.findReferInvestSumAmount("testReferInvest");
        assertNotNull(referrerManageViews.get(0).getInvestAmount());

    }

    private void readyDate(String loginName){
        ReferrerRelationModel referrerRelationModel = new ReferrerRelationModel();
        referrerRelationModel.setReferrerLoginName(loginName);
        referrerRelationModel.setLoginName(loginName);
        referrerRelationModel.setLevel(1);
        UserModel  user = new UserModel();
        user.setId(idGenerator.generate());
        user.setLoginName(loginName);
        user.setPassword("123");
        user.setMobile("13900000000");
        user.setRegisterTime(new Date());
        user.setLastModifiedTime(new Date());
        user.setStatus(UserStatus.ACTIVE);
        user.setSalt("123");
        createMockUser(loginName);
        createMockInvest(loginName, createMockLoan(loginName).getId());
        referrerRelationMapper.create(referrerRelationModel);
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


}
