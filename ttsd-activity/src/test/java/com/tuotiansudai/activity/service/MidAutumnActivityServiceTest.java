package com.tuotiansudai.activity.service;


import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional(value = "activityTransactionManager")
public class MidAutumnActivityServiceTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    @Autowired
    private MidAutumnActivityService midAutumnActivityService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LotteryActivityService lotteryActivityService;

    @Test
    public void shouldNoLoginNameGetDrawPrizeTimeIsOk(){
        int time = lotteryActivityService.getDrawPrizeTime("");
        assertEquals(time,0);
    }
    @Ignore
    public void shouldGetMidAutumnHomeDataIsOk(){
        String loginName = "boss";
        Map autumnMap = midAutumnActivityService.getMidAutumnHomeData(loginName);
        assertTrue(autumnMap.size() > 0);
        assertEquals(autumnMap.get("myFamilyNum"), 2);
        assertTrue(CollectionUtils.isNotEmpty((Collection) autumnMap.get("myFamily")));
        assertEquals(autumnMap.get("todayInvestAmount"), "2.00");
        assertEquals(autumnMap.get("totalInvestAmount"), "10.00");
    }

    private InvestModel getFakeInvestModel(long loanId,String loginName) {
        InvestModel model = new InvestModel(idGenerator.generate(), loanId, null, 100l, loginName, DateTime.now().toDate(), Source.WEB, null, 0.1);
        model.setStatus(InvestStatus.SUCCESS);
        model.setCreatedTime(DateTime.parse("2016-09-16").toDate());
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
        fakeLoanModel.setCreatedTime(DateTime.parse("2016-09-16").toDate());
        fakeLoanModel.setProductType(ProductType._180);
        loanMapper.create(fakeLoanModel);
        return fakeLoanModel;
    }

    private void createDate(){
        UserModel boss = getUserModelTest("boss","15210006591");
        UserModel boss1 = getUserModelTest("boss1","15210006592");
        createReferrerRelationModel(boss.getLoginName(),boss1.getLoginName(),1);
        UserModel boss11 = getUserModelTest("boss11","15210006593");
        createReferrerRelationModel(boss1.getLoginName(),boss11.getLoginName(),1);
        createReferrerRelationModel(boss.getLoginName(),boss11.getLoginName(),2);
        UserModel boss12 = getUserModelTest("boss12","15210006594");
        createReferrerRelationModel(boss1.getLoginName(),boss12.getLoginName(),1);
        createReferrerRelationModel(boss.getLoginName(),boss12.getLoginName(),2);
        UserModel boss13 = getUserModelTest("boss13","15210006595");
        createReferrerRelationModel(boss1.getLoginName(),boss13.getLoginName(),1);
        UserModel boss131 = getUserModelTest("boss131","15210006596");
        createReferrerRelationModel(boss13.getLoginName(),boss131.getLoginName(),1);
        UserModel boss1311 = getUserModelTest("boss1311","15210006597");
        createReferrerRelationModel(boss131.getLoginName(),boss1311.getLoginName(),1);
        createReferrerRelationModel(boss13.getLoginName(),boss1311.getLoginName(),2);
        createReferrerRelationModel(boss1.getLoginName(),boss1311.getLoginName(),3);
        createReferrerRelationModel(boss.getLoginName(), boss1311.getLoginName(), 4);
        UserModel boss2 = getUserModelTest("boss2","15210006598");
        UserModel boss21 = getUserModelTest("boss21", "15210006599");
        createReferrerRelationModel(boss2.getLoginName(), boss21.getLoginName(), 1);
        LoanModel loanModel = getFakeLoan("boss","boss");
        getFakeInvestModel(loanModel.getId(),"boss1");
        getFakeInvestModel(loanModel.getId(), "boss2");
    }

    private ReferrerRelationModel createReferrerRelationModel(String referrerLoginName,String loginName,int level){
        ReferrerRelationModel referrerRelationModel = new ReferrerRelationModel();
        referrerRelationModel.setReferrerLoginName(referrerLoginName);
        referrerRelationModel.setLoginName(loginName);
        referrerRelationModel.setLevel(level);
        referrerRelationMapper.create(referrerRelationModel);
        return referrerRelationModel;
    }


    private UserModel getUserModelTest(String loginName,String mobile) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile(mobile);
        userModelTest.setRegisterTime(DateTime.parse("2016-09-16").toDate());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
        return userModelTest;
    }
}
