package com.tuotiansudai.activity.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.LotteryPrize;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.RedisWrapperClient;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class LotteryDrawActivityServiceTest {

    @InjectMocks
    private LotteryDrawActivityService lotteryDrawActivityService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PointBillMapper pointBillMapper;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private InvestMapper investMapper;

    @Mock
    private UserLotteryPrizeMapper userLotteryPrizeMapper;

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();
    public final static String ACTIVITY_DOUBLE_ELEVEN_INVEST_KEY = "activity:double:eleven:invest";
    public final static String ACTIVITY_DOUBLE_ELEVEN_INCR_INVEST_KEY = "{0}:{1}";

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void cleanRedis() {
        redisWrapperClient.del(ACTIVITY_DOUBLE_ELEVEN_INVEST_KEY);
        redisWrapperClient.del(MessageFormat.format(ACTIVITY_DOUBLE_ELEVEN_INCR_INVEST_KEY, "testEleven", new DateTime(new Date()).withTimeAtStartOfDay().toString("yyyy-MM-dd")));
    }

    @Test
    public void shouldCountTimeIsZero() {
        String mobile = "152";
        when(userMapper.findByMobile(anyString())).thenReturn(null);
        int time = lotteryDrawActivityService.countDrawLotteryTime(mobile, ActivityCategory.ANNUAL_ACTIVITY);
        assertTrue(time == 0);
    }


    @Test
    public void shouldCountTimeIsOk() {
        String mobile = "152";
        UserModel userModel = new UserModel();
        userModel.setRegisterTime(DateTime.now().toDate());
        UserModel referrerUserModel1 = new UserModel();
        referrerUserModel1.setRegisterTime(DateTime.now().toDate());
        UserModel referrerUserModel2 = new UserModel();
        referrerUserModel2.setRegisterTime(DateTime.now().toDate());
        List<UserRegisterInfo> userModels = Lists.newArrayList(referrerUserModel1, referrerUserModel2);
        BasePaginationDataDto<UserRegisterInfo> pagingUserModels = new BasePaginationDataDto<>(1, 10, 2, userModels);
        AccountModel accountModel = new AccountModel("test", "1", "1", DateTime.now().toDate());

        ReflectionTestUtils.setField(lotteryDrawActivityService, "annualTime", Lists.newArrayList(DateTime.now().plusDays(-1).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")), DateTime.now().plusDays(1).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))));
        when(userMapper.findByMobile(anyString())).thenReturn(userModel);
        when(userMapper.findUsersByRegisterTimeAndReferrer(any(Date.class), any(Date.class), anyString(), anyInt(), anyInt())).thenReturn(pagingUserModels);
        when(pointBillMapper.findCountPointBillPagination(anyString(), anyString(), any(Date.class), any(Date.class), anyList())).thenReturn(1l);
        when(accountMapper.findByLoginName(anyString())).thenReturn(accountModel);
        when(investMapper.countInvestorSuccessInvestByInvestTime(anyString(), any(Date.class), any(Date.class))).thenReturn(1l);
        when(investMapper.sumSuccessActivityInvestAmount(anyString(), anyString(), any(Date.class), any(Date.class))).thenReturn(1000000000l);
        when(userLotteryPrizeMapper.findUserLotteryPrizeCountViews(anyString(), any(LotteryPrize.class), any(ActivityCategory.class), any(Date.class), any(Date.class))).thenReturn(0);

        int time = lotteryDrawActivityService.countDrawLotteryTime(mobile, ActivityCategory.ANNUAL_ACTIVITY);
        assertEquals(time, 11);

    }

    @Test
    public void shouldDoubleElevenCountTimesWhenMoreThanTenTimesOfDay() {
        String loginName = "testEleven";
        String mobile = "15233";
        UserModel userModel = createUserByLoginName(loginName, mobile);
        List<InvestModel> investModels = Lists.newArrayList();
        for (int i = 1; i < 50; i++) {
            InvestModel investModel = getFakeInvestModelByLoginName(loginName, new Date(), 111L);
            String incrKey = MessageFormat.format(ACTIVITY_DOUBLE_ELEVEN_INCR_INVEST_KEY, investModel.getLoginName(), new DateTime(investModel.getTradingTime()).withTimeAtStartOfDay().toString("yyyy-MM-dd"));
            if (i % 2 == 0 && Long.parseLong(!redisWrapperClient.exists(incrKey) ? "0" : redisWrapperClient.get(incrKey)) < 10) {
                String hkey = MessageFormat.format("{0}:{1}:{2}", investModel.getLoanId(), investModel.getId(), investModel.getLoginName());
                redisWrapperClient.hset(ACTIVITY_DOUBLE_ELEVEN_INVEST_KEY, hkey, "0");
            }
            investModels.add(investModel);
        }

        ReflectionTestUtils.setField(lotteryDrawActivityService, "activityDoubleElevenStartTime", DateTime.now().plusDays(-1).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
        ReflectionTestUtils.setField(lotteryDrawActivityService, "activityDoubleElevenEndTime", DateTime.now().plusDays(1).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
        when(userMapper.findByMobile(anyString())).thenReturn(userModel);
        when(investMapper.findSuccessDoubleElevenActivityByTime(anyLong(), any(Date.class), any(Date.class))).thenReturn(investModels);
        when(userLotteryPrizeMapper.findUserLotteryPrizeCountViews(anyString(), any(LotteryPrize.class), any(ActivityCategory.class), any(Date.class), any(Date.class))).thenReturn(0);

        int time = lotteryDrawActivityService.countDrawLotteryTime(mobile, ActivityCategory.DOUBLE_ELEVEN_ACTIVITY);
        assertEquals(time, 10);
    }

    @Test
    public void shouldDoubleElevenCountTimesByMultiLoan() {
        String loginName = "testEleven";
        String mobile = "15233";
        UserModel userModel = createUserByLoginName(loginName, mobile);
        List<InvestModel> investModels = Lists.newArrayList();
        InvestModel investModel = getFakeInvestModelByLoginName(loginName, new Date(), 111L);

        investModels.add(investModel);
        InvestModel investModel2 = getFakeInvestModelByLoginName(loginName, new Date(), 112L);
        investModels.add(investModel2);
        InvestModel investModel3 = getFakeInvestModelByLoginName(loginName, new Date(), 112L);
        String hkey1 = MessageFormat.format("{0}:{1}:{2}", investModel3.getLoanId(), investModel3.getId(), loginName);
        redisWrapperClient.hset(ACTIVITY_DOUBLE_ELEVEN_INVEST_KEY, hkey1, "0");
        investModels.add(investModel3);
        InvestModel investModel4 = getFakeInvestModelByLoginName(loginName, new Date(), 111L);
        String hkey2 = MessageFormat.format("{0}:{1}:{2}", investModel4.getLoanId(), investModel4.getId(), loginName);
        redisWrapperClient.hset(ACTIVITY_DOUBLE_ELEVEN_INVEST_KEY, hkey2, "0");
        investModels.add(investModel4);
        InvestModel investModel5 = getFakeInvestModelByLoginName(loginName, new Date(), 112L);
        investModels.add(investModel5);
        InvestModel investModel6 = getFakeInvestModelByLoginName(loginName, new Date(), 111L);
        investModels.add(investModel6);
        InvestModel investModel7 = getFakeInvestModelByLoginName(loginName, new Date(), 111L);
        String hkey3 = MessageFormat.format("{0}:{1}:{2}", investModel7.getLoanId(), investModel7.getId(), loginName);
        redisWrapperClient.hset(ACTIVITY_DOUBLE_ELEVEN_INVEST_KEY, hkey3, "0");
        investModels.add(investModel7);
        InvestModel investModel8 = getFakeInvestModelByLoginName(loginName, new Date(), 112L);
        investModels.add(investModel8);
        String hkey4 = MessageFormat.format("{0}:{1}:{2}", investModel8.getLoanId(), investModel8.getId(), loginName);
        redisWrapperClient.hset(ACTIVITY_DOUBLE_ELEVEN_INVEST_KEY, hkey4, "0");

        InvestModel investModel9 = getFakeInvestModelByLoginName(loginName, new Date(), 111L);
        investModels.add(investModel9);
        InvestModel investModel10 = getFakeInvestModelByLoginName(loginName, new Date(), 111L);
        investModels.add(investModel10);
        String hkey5 = MessageFormat.format("{0}:{1}:{2}", investModel10.getLoanId(), investModel10.getId(), loginName);
        redisWrapperClient.hset(ACTIVITY_DOUBLE_ELEVEN_INVEST_KEY, hkey5, "0");


        ReflectionTestUtils.setField(lotteryDrawActivityService, "activityDoubleElevenStartTime", DateTime.now().plusDays(-1).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
        ReflectionTestUtils.setField(lotteryDrawActivityService, "activityDoubleElevenEndTime", DateTime.now().plusDays(1).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
        when(userMapper.findByMobile(anyString())).thenReturn(userModel);
        when(investMapper.findSuccessDoubleElevenActivityByTime(anyLong(), any(Date.class), any(Date.class))).thenReturn(investModels);
        when(userLotteryPrizeMapper.findUserLotteryPrizeCountViews(anyString(), any(LotteryPrize.class), any(ActivityCategory.class), any(Date.class), any(Date.class))).thenReturn(0);

        int time = lotteryDrawActivityService.countDrawLotteryTime(mobile, ActivityCategory.DOUBLE_ELEVEN_ACTIVITY);
        assertEquals(time, 5);
    }


    @Test
    public void shouldDoubleElevenCountTimesByDiffDay() {
        String loginName = "testEleven";
        String mobile = "15233";
        UserModel userModel = createUserByLoginName(loginName, mobile);
        List<InvestModel> investModels = Lists.newArrayList();
        InvestModel investModel = getFakeInvestModelByLoginName(loginName, new Date(), 111L);
        investModels.add(investModel);

        InvestModel investModel2 = getFakeInvestModelByLoginName(loginName, new Date(), 111L);
        String hkey1 = MessageFormat.format("{0}:{1}:{2}", investModel2.getLoanId(), investModel2.getId(), loginName);
        redisWrapperClient.hset(ACTIVITY_DOUBLE_ELEVEN_INVEST_KEY, hkey1, "0");
        investModels.add(investModel2);

        InvestModel investModel3 = getFakeInvestModelByLoginName(loginName, new Date(), 111L);
        investModels.add(investModel3);

        InvestModel investModel4 = getFakeInvestModelByLoginName(loginName, new Date(), 111L);
        String hkey2 = MessageFormat.format("{0}:{1}:{2}", investModel4.getLoanId(), investModel4.getId(), loginName);
        redisWrapperClient.hset(ACTIVITY_DOUBLE_ELEVEN_INVEST_KEY, hkey2, "0");
        investModels.add(investModel4);

        investModels.add(getFakeInvestModelByLoginName(loginName, new Date(), 111L));

        InvestModel investModel6 = getFakeInvestModelByLoginName(loginName, new Date(), 111L);
        String hkey6 = MessageFormat.format("{0}:{1}:{2}", investModel6.getLoanId(), investModel6.getId(), loginName);
        redisWrapperClient.hset(ACTIVITY_DOUBLE_ELEVEN_INVEST_KEY, hkey6, "0");
        investModels.add(investModel4);

        investModels.add(getFakeInvestModelByLoginName(loginName, new Date(), 111L));


        InvestModel investModel8 = getFakeInvestModelByLoginName(loginName, new Date(), 111L);
        String hkey8 = MessageFormat.format("{0}:{1}:{2}", investModel8.getLoanId(), investModel8.getId(), loginName);
        redisWrapperClient.hset(ACTIVITY_DOUBLE_ELEVEN_INVEST_KEY, hkey8, "0");
        investModels.add(investModel8);
        investModels.add(getFakeInvestModelByLoginName(loginName, new Date(), 111L));

        InvestModel investModel10 = getFakeInvestModelByLoginName(loginName, new Date(), 111L);
        String hkey10 = MessageFormat.format("{0}:{1}:{2}", investModel10.getLoanId(), investModel10.getId(), loginName);
        redisWrapperClient.hset(ACTIVITY_DOUBLE_ELEVEN_INVEST_KEY, hkey10, "0");
        investModels.add(investModel10);
        investModels.add(getFakeInvestModelByLoginName(loginName, new Date(), 111L));

        InvestModel investModel12 = getFakeInvestModelByLoginName(loginName, new Date(), 111L);
        String hkey12 = MessageFormat.format("{0}:{1}:{2}", investModel12.getLoanId(), investModel12.getId(), loginName);
        redisWrapperClient.hset(ACTIVITY_DOUBLE_ELEVEN_INVEST_KEY, hkey12, "0");
        investModels.add(investModel12);
        investModels.add(getFakeInvestModelByLoginName(loginName, new Date(), 111L));

        InvestModel investModel14 = getFakeInvestModelByLoginName(loginName, new Date(), 111L);
        String hkey14 = MessageFormat.format("{0}:{1}:{2}", investModel14.getLoanId(), investModel14.getId(), loginName);
        redisWrapperClient.hset(ACTIVITY_DOUBLE_ELEVEN_INVEST_KEY, hkey14, "0");
        investModels.add(investModel14);
        investModels.add(getFakeInvestModelByLoginName(loginName, new Date(), 111L));

        InvestModel investModel16 = getFakeInvestModelByLoginName(loginName, new Date(), 111L);
        String hkey16 = MessageFormat.format("{0}:{1}:{2}", investModel16.getLoanId(), investModel16.getId(), loginName);
        redisWrapperClient.hset(ACTIVITY_DOUBLE_ELEVEN_INVEST_KEY, hkey16, "0");
        investModels.add(investModel16);
        investModels.add(getFakeInvestModelByLoginName(loginName, new Date(), 111L));

        InvestModel investModel18 = getFakeInvestModelByLoginName(loginName, new DateTime(new Date()).plusDays(1).toDate(), 111L);
        String hkey18 = MessageFormat.format("{0}:{1}:{2}", investModel18.getLoanId(), investModel18.getId(), loginName);
        redisWrapperClient.hset(ACTIVITY_DOUBLE_ELEVEN_INVEST_KEY, hkey18, "0");
        investModels.add(investModel18);
        investModels.add(getFakeInvestModelByLoginName(loginName, new DateTime(new Date()).plusDays(1).toDate(), 111L));


        InvestModel investModel20 = getFakeInvestModelByLoginName(loginName, new DateTime(new Date()).plusDays(1).toDate(), 111L);
        String hkey20 = MessageFormat.format("{0}:{1}:{2}", investModel20.getLoanId(), investModel20.getId(), loginName);
        redisWrapperClient.hset(ACTIVITY_DOUBLE_ELEVEN_INVEST_KEY, hkey20, "0");
        investModels.add(investModel20);
        investModels.add(getFakeInvestModelByLoginName(loginName, new DateTime(new Date()).plusDays(1).toDate(), 111L));

        InvestModel investModel22 = getFakeInvestModelByLoginName(loginName, new DateTime(new Date()).plusDays(1).toDate(), 111L);
        String hkey22 = MessageFormat.format("{0}:{1}:{2}", investModel22.getLoanId(), investModel22.getId(), loginName);
        redisWrapperClient.hset(ACTIVITY_DOUBLE_ELEVEN_INVEST_KEY, hkey22, "0");
        investModels.add(investModel22);
        investModels.add(getFakeInvestModelByLoginName(loginName, new DateTime(new Date()).plusDays(1).toDate(), 111L));


        ReflectionTestUtils.setField(lotteryDrawActivityService, "activityDoubleElevenStartTime", DateTime.now().plusDays(-2).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
        ReflectionTestUtils.setField(lotteryDrawActivityService, "activityDoubleElevenEndTime", DateTime.now().plusDays(2).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
        when(userMapper.findByMobile(anyString())).thenReturn(userModel);
        when(investMapper.findSuccessDoubleElevenActivityByTime(anyLong(), any(Date.class), any(Date.class))).thenReturn(investModels);
        when(userLotteryPrizeMapper.findUserLotteryPrizeCountViews(anyString(), any(LotteryPrize.class), any(ActivityCategory.class), any(Date.class), any(Date.class))).thenReturn(0);

        int time = lotteryDrawActivityService.countDrawLotteryTime(mobile, ActivityCategory.DOUBLE_ELEVEN_ACTIVITY);
        assertEquals(time, 11);
    }

    private UserModel createUserByLoginName(String loginName, String mobile) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile(mobile);
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        return userModelTest;
    }

    private InvestModel getFakeInvestModelByLoginName(String loginName, Date tradingTime, long loanId) {
        InvestModel model = new InvestModel(IdGenerator.generate(), loanId, null, 1000l, loginName, new DateTime().withTimeAtStartOfDay().toDate(), Source.WEB, null, 0.1);
        model.setStatus(InvestStatus.SUCCESS);
        model.setTradingTime(tradingTime);
        return model;
    }
}
