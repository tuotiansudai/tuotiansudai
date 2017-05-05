package com.tuotiansudai.activity.service;


import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.repository.model.LotteryPrize;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeModel;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    private InvestMapper investMapper;
    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private RechargeMapper rechargeMapper;
    @Autowired
    private UserLotteryPrizeMapper userLotteryPrizeMapper;

    @Ignore
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
        getAccountModel(referrerName);
        getFakeInvestModel(loanModel.getId(), referrerName);
        getRechargeModel(userModel.getLoginName());
        int time = lotteryActivityService.getDrawPrizeTime(userModel.getMobile());
        assertEquals(time,6);
    }

    @Test
    public void shouldNoLoginNameGetDrawPrizeTimeIsOk(){
        int time = lotteryActivityService.getDrawPrizeTime("");
        assertEquals(time,0);
    }

    @Test
    public void shouldNoRegisterLoginNameGetDrawPrizeTimeIsOk(){
        UserModel fakeUser = new UserModel();
        fakeUser.setLoginName("testNoRegisterTime");
        fakeUser.setPassword("password");
        fakeUser.setEmail("fakeUsr@tuotiansudai.com");
        fakeUser.setMobile("testNoResisterTime");
        fakeUser.setRegisterTime(DateUtils.addMonths(DateTime.now().toDate(),-2));
        fakeUser.setStatus(UserStatus.ACTIVE);
        fakeUser.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        int time = lotteryActivityService.getDrawPrizeTime("");
        assertEquals(time,0);
    }

    @Ignore
    public void shouldFindDrawLotteryPrizeRecordByMobileIsOk(){
        UserModel userModel = getFakeUser("testDrawPrize11", "12345678902");
        getUserLotteryPrizeModel(userModel.getLoginName(), userModel.getMobile(), "testName");
        List<UserLotteryPrizeView> userLotteryPrizeViews = lotteryActivityService.findDrawLotteryPrizeRecordByMobile(userModel.getMobile(), null);
        assertTrue(CollectionUtils.isNotEmpty(userLotteryPrizeViews));
        userLotteryPrizeViews = lotteryActivityService.findDrawLotteryPrizeRecordByMobile("", null);
        assertEquals(userLotteryPrizeViews.size(), 0);
    }

    @Ignore
    public void shouldFindDrawLotteryPrizeRecordIsOk(){
        UserModel userModel = getFakeUser("testDrawPrize12", "12345678901");
        getUserLotteryPrizeModel(userModel.getLoginName(), userModel.getMobile(), "testName");
        List<UserLotteryPrizeView> userLotteryPrizeViews = lotteryActivityService.findDrawLotteryPrizeRecord(userModel.getMobile(), null);
        assertTrue(CollectionUtils.isNotEmpty(userLotteryPrizeViews));
    }

    @Test
    public void shouldDrawLotteryPrizeIsOk(){
        Date activityAutumnStartTime = DateUtils.addMonths(DateTime.now().toDate(),-1);
        Date activityAutumnEndTime = DateUtils.addMonths(DateTime.now().toDate(), 1);
        ReflectionTestUtils.setField(lotteryActivityService, "activityAutumnStartTime", activityAutumnStartTime);
        ReflectionTestUtils.setField(lotteryActivityService, "activityAutumnEndTime", activityAutumnEndTime);
        DrawLotteryResultDto drawLotteryResultDto = lotteryActivityService.drawLotteryPrize("");
        assertTrue(!drawLotteryResultDto.getStatus());
    }

    private UserLotteryPrizeModel getUserLotteryPrizeModel(String loginName,String mobile,String userName){
        UserLotteryPrizeModel userLotteryPrizeModel = new UserLotteryPrizeModel();
        userLotteryPrizeModel.setPrize(LotteryPrize.INTEREST_COUPON_2);
        userLotteryPrizeModel.setLotteryTime(DateTime.now().toDate());
        userLotteryPrizeModel.setLoginName(loginName);
        userLotteryPrizeModel.setMobile(mobile);
        userLotteryPrizeModel.setUserName(userName);
        userLotteryPrizeMapper.create(userLotteryPrizeModel);
        return userLotteryPrizeModel;
    }

    private RechargeModel getRechargeModel(String loginName){
        RechargeModel model = new RechargeModel();
        model.setId(IdGenerator.generate());
        model.setLoginName(loginName);
        model.setBankCode("bank");
        model.setCreatedTime(new Date());
        model.setSource(Source.WEB);
        model.setStatus(RechargeStatus.SUCCESS);
        model.setCreatedTime(DateTime.now().toDate());
        rechargeMapper.create(model);
        return model;
    }

    private AccountModel getAccountModel(String loginName){
        AccountModel model = new AccountModel(loginName, "payUserId", "payAccountId", new Date());
        accountMapper.create(model);
        return model;

    }

    private InvestModel getFakeInvestModel(long loanId,String loginName) {
        InvestModel model = new InvestModel(IdGenerator.generate(), loanId, null, 1000000L, loginName, DateTime.now().toDate(), Source.WEB, null, 0.1);
        model.setStatus(InvestStatus.SUCCESS);
        investMapper.create(model);
        return model;
    }

    private LoanModel getFakeLoan(String loanerLoginName, String agentLoginName) {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(IdGenerator.generate());
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
