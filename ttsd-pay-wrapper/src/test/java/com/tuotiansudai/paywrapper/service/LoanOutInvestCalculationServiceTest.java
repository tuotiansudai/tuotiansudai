package com.tuotiansudai.paywrapper.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.jpush.client.MobileAppJPushClient;
import com.tuotiansudai.jpush.repository.mapper.JPushAlertMapper;
import com.tuotiansudai.jpush.repository.model.*;
import com.tuotiansudai.jpush.service.impl.JPushAlertServiceImpl;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
public class LoanOutInvestCalculationServiceTest {
    @InjectMocks
    private JPushAlertServiceImpl jPushAlertService;

    @Mock
    private InvestMapper investMapper;

    @Mock
    private InvestRepayMapper investRepayMapper;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private JPushAlertMapper jPushAlertMapper;

    @Mock
    private LoanRepayMapper loanRepayMapper;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private InvestReferrerRewardMapper investReferrerRewardMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserCouponMapper userCouponMapper;

    @Mock
    private CouponMapper couponMapper;

    @Mock
    private IdGenerator idGenerator;

    @Mock
    private MobileAppJPushClient mobileAppJPushClient;

    @Mock
    private RedisWrapperClient redisWrapperClient;


    @Test
    public void loanOutInvestCalculation() {
        LoanModel loanModel = fakeLoanModel("test123");
        LoanRepayModel currentLoanRepayModel = new LoanRepayModel();

        currentLoanRepayModel.setId(1000101);
        currentLoanRepayModel.setLoanId(loanModel.getId());
        currentLoanRepayModel.setPeriod(1);
        currentLoanRepayModel.setStatus(RepayStatus.COMPLETE);

        List<LoanRepayModel> loanRepayModels = new ArrayList<LoanRepayModel>();

        for (int i = 1; i < 4; i++) {
            LoanRepayModel loanRepayModel1 = new LoanRepayModel();
            loanRepayModel1.setLoanId(loanModel.getId());
            loanRepayModel1.setId(Long.parseLong(10001 + "0" + i));
            loanRepayModel1.setPeriod(i);
            loanRepayModels.add(loanRepayModel1);
        }

        CouponModel couponModel = new CouponModel(fakeCouponDto());

        InvestModel investModel = new InvestModel(1001, loanModel.getId(), null, 100, "test123", null, Source.WEB, null, 0.1);

        List<UserCouponModel> userCouponModels = new ArrayList<UserCouponModel>();

        UserCouponModel userCouponModel = new UserCouponModel();
        userCouponModel.setId(idGenerator.generate());
        userCouponModel.setInvestId(1001L);
        userCouponModel.setActualInterest(10);
        userCouponModel.setLoginName("test1");
        userCouponModel.setCouponId(couponModel.getId());
        userCouponModel.setLoanId(loanModel.getId());
        userCouponModel.setStatus(InvestStatus.SUCCESS);
        userCouponModels.add(userCouponModel);

        UserCouponModel userCouponModel2 = new UserCouponModel();
        userCouponModel2.setId(idGenerator.generate());
        userCouponModel2.setInvestId(1001L);
        userCouponModel2.setLoginName("test2");
        userCouponModel2.setCouponId(couponModel.getId());
        userCouponModel2.setLoanId(loanModel.getId());
        userCouponModel2.setStatus(InvestStatus.SUCCESS);
        userCouponModels.add(userCouponModel2);

        when(loanRepayMapper.findById(anyLong())).thenReturn(currentLoanRepayModel);

        when(loanMapper.findById(anyLong())).thenReturn(loanModel);

        when(loanRepayMapper.findByLoanIdOrderByPeriodAsc(anyLong())).thenReturn(loanRepayModels);

        when(userCouponMapper.findByLoanId(anyLong(), anyList())).thenReturn(userCouponModels);

        when(jPushAlertMapper.findJPushAlertByPushType(any(PushType.class))).thenReturn(createJPushAlert());

        when(couponMapper.findById(anyLong())).thenReturn(couponModel);

        when(redisWrapperClient.hexists(anyString(), anyString())).thenReturn(true);

        when(redisWrapperClient.hget(anyString(), anyString())).thenReturn("test123");

        when(investMapper.findById(anyLong())).thenReturn(investModel);

        ArgumentCaptor argumentJPushAlertId = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor argumentAlert = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor argumentextraKey = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor argumentextraValue = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<PushSource> argumentPushSource = ArgumentCaptor.forClass(PushSource.class);
        ArgumentCaptor<ArrayList<String>> argumentRegistrationIds = ArgumentCaptor.forClass((Class<ArrayList<String>>) new ArrayList<String>().getClass());

        verify(mobileAppJPushClient, times(1)).sendPushAlertByRegistrationIds((String) argumentJPushAlertId.capture(), argumentRegistrationIds.capture(), (String) argumentAlert.capture(), (String) argumentextraKey.capture(), (String) argumentextraValue.capture(), argumentPushSource.capture());

    }

    private LoanModel fakeLoanModel(String loginName) {
        LoanModel loanModel = new LoanModel();
        loanModel.setAgentLoginName(loginName);
        loanModel.setBaseRate(16.00);
        loanModel.setId(10);
        loanModel.setName("店铺资金周转");
        loanModel.setActivityRate(12);
        loanModel.setShowOnHome(true);
        loanModel.setPeriods(3);
        loanModel.setActivityType(ActivityType.EXCLUSIVE);
        loanModel.setContractId(123);
        loanModel.setDescriptionHtml("asdfasdf");
        loanModel.setDescriptionText("asdfasd");
        loanModel.setFundraisingEndTime(new Date());
        loanModel.setFundraisingStartTime(new Date());
        loanModel.setInvestIncreasingAmount(1);
        loanModel.setLoanAmount(10000);
        loanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanModel.setMaxInvestAmount(100000000000L);
        loanModel.setMinInvestAmount(0);
        loanModel.setCreatedTime(new Date());
        loanModel.setStatus(LoanStatus.RAISING);
        loanModel.setLoanerLoginName(loginName);
        loanModel.setLoanerUserName("借款人");
        loanModel.setLoanerIdentityNumber("111111111111111111");
        return loanModel;
    }

    private ExchangeCouponDto fakeCouponDto() {
        ExchangeCouponDto exchangeCouponDto = new ExchangeCouponDto();
        exchangeCouponDto.setId(1001L);
        exchangeCouponDto.setAmount("1000.00");
        exchangeCouponDto.setTotalCount(1000L);
        exchangeCouponDto.setEndTime(new Date());
        exchangeCouponDto.setStartTime(new Date());
        exchangeCouponDto.setInvestLowerLimit("1000.00");
        exchangeCouponDto.setCouponType(CouponType.INVEST_COUPON);
        List<ProductType> productTypes = Lists.newArrayList();
        productTypes.add(ProductType._180);
        exchangeCouponDto.setProductTypes(productTypes);
        exchangeCouponDto.setInvestLowerLimit("1000.00");
        exchangeCouponDto.setUserGroup(UserGroup.ALL_USER);
        return exchangeCouponDto;
    }

    private JPushAlertModel createJPushAlert() {
        JPushAlertModel jPushAlertModel = new JPushAlertModel();
        jPushAlertModel.setId(1005);
        jPushAlertModel.setName("用户资金变动推送-还款");
        jPushAlertModel.setPushType(PushType.REPAY_ALERT);
        jPushAlertModel.setPushSource(PushSource.ANDROID);
        jPushAlertModel.setContent("亲爱的天宝，您刚刚收到一笔{0}元的项目还款，请点击查看");
        jPushAlertModel.setIsAutomatic(true);
        jPushAlertModel.setCreatedTime(new Date());
        jPushAlertModel.setJumpTo(JumpTo.INVEST_RECEIVABLES);
        jPushAlertModel.setStatus(PushStatus.ENABLED);
        return jPushAlertModel;
    }
}
