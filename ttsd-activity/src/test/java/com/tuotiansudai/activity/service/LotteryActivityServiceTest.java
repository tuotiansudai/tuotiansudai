package com.tuotiansudai.activity.service;


import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static sun.nio.cs.Surrogate.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class LotteryActivityServiceTest {

    @Autowired
    private LotteryActivityService lotteryActivityService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private InvestMapper investMapper;
    @Autowired
    private LoanMapper loanMapper;

    @Test
    public void shouldGetDrawPrizeTimeIsOk(){
        String loginName = "testDrawPrize";
        String referrerName = "testReferrerName";
        String mobile = "15510001234";
        String referrerMobile = "15510001235";
        Date activityAutumnStartTime = DateUtils.addMonths(DateTime.now().toDate(),-1);
        Date activityAutumnEndTime = DateUtils.addMonths(DateTime.now().toDate(), 1);
        ReflectionTestUtils.setField(lotteryActivityService, "activityAutumnStartTime", activityAutumnStartTime);
        ReflectionTestUtils.setField(lotteryActivityService, "activityAutumnEndTime", activityAutumnEndTime);
        UserModel userModel = getFakeUser(referrerName,referrerMobile);
        getFakeUser(loginName,mobile);
        getReferrer(referrerName, loginName);
        LoanModel loanModel = getFakeLoan(loginName, referrerName);
        getFakeInvestModel(loanModel.getId(),loginName);
        int time = lotteryActivityService.getDrawPrizeTime(userModel.getMobile());
        assertEquals(time,3);
    }

    private InvestModel getFakeInvestModel(long loanId,String loginName) {
        InvestModel model = new InvestModel(idGenerator.generate(), loanId, null, 1000000L, loginName, DateTime.now().toDate(), Source.WEB, null, 0.1);
        model.setStatus(InvestStatus.SUCCESS);
        investMapper.create(model);
        return model;
    }

    private LoanModel getFakeLoan(String loanerLoginName, String agentLoginName) {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(idGenerator.generate());
        fakeLoanModel.setName("loanName");
        fakeLoanModel.setLoanerLoginName(loanerLoginName);
        fakeLoanModel.setLoanerUserName("借款人");
        fakeLoanModel.setLoanerIdentityNumber("111111111111111111");
        fakeLoanModel.setAgentLoginName(agentLoginName);
        fakeLoanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        fakeLoanModel.setPeriods(3);
        fakeLoanModel.setStatus(LoanStatus.REPAYING);
        fakeLoanModel.setActivityType(ActivityType.NORMAL);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setPledgeType(PledgeType.HOUSE);
        fakeLoanModel.setCreatedTime(new Date());
        fakeLoanModel.setProductType(ProductType._180);
        loanMapper.create(fakeLoanModel);
        return fakeLoanModel;
    }

    private ReferrerRelationModel getReferrer(String referrerName,String loginName){
        ReferrerRelationModel referrerRelationModel = new ReferrerRelationModel();
        referrerRelationModel.setReferrerLoginName(referrerName);
        referrerRelationModel.setLoginName(loginName);
        referrerRelationModel.setLevel(1);
        referrerRelationMapper.create(referrerRelationModel);
        return referrerRelationModel;
    }

    private UserModel getFakeUser(String loginName,String mobile) {
        UserModel fakeUser = new UserModel();
        fakeUser.setLoginName(loginName);
        fakeUser.setPassword("password");
        fakeUser.setEmail("fakeUsr@tuotiansudai.com");
        fakeUser.setMobile(mobile);
        fakeUser.setRegisterTime(new Date());
        fakeUser.setStatus(UserStatus.ACTIVE);
        fakeUser.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(fakeUser);
        return fakeUser;
    }
}
