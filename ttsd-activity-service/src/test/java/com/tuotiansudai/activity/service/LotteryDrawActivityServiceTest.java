package com.tuotiansudai.activity.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.LotteryPrize;
import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
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

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
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
        List<UserModel> userModels = Lists.newArrayList(referrerUserModel1, referrerUserModel2);
        AccountModel accountModel = new AccountModel("test", "1", "1", DateTime.now().toDate());

        ReflectionTestUtils.setField(lotteryDrawActivityService, "annualTime", Lists.newArrayList(DateTime.now().plusDays(-1).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")), DateTime.now().plusDays(1).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))));
        when(userMapper.findByMobile(anyString())).thenReturn(userModel);
        when(userMapper.findUsersByRegisterTimeOrReferrer(any(Date.class), any(Date.class), anyString())).thenReturn(userModels);
        when(pointBillMapper.findCountPointBillPagination(anyString(), anyString(), any(Date.class), any(Date.class), anyList())).thenReturn(1l);
        when(accountMapper.findByLoginName(anyString())).thenReturn(accountModel);
        when(investMapper.countInvestorSuccessInvestByInvestTime(anyString(), any(Date.class), any(Date.class))).thenReturn(1l);
        when(investMapper.sumSuccessActivityInvestAmount(anyString(), anyString(), any(Date.class), any(Date.class))).thenReturn(1000000000l);
        when(userLotteryPrizeMapper.findUserLotteryPrizeCountViews(anyString(), any(LotteryPrize.class), any(ActivityCategory.class), any(Date.class), any(Date.class))).thenReturn(0);

        int time = lotteryDrawActivityService.countDrawLotteryTime(mobile, ActivityCategory.ANNUAL_ACTIVITY);
        assertEquals(time, 21);
    }

}
