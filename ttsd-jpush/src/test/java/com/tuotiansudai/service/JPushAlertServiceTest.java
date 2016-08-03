package com.tuotiansudai.service;

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
import com.tuotiansudai.jpush.service.JPushScheduleAlertService;
import com.tuotiansudai.jpush.service.impl.JPushAlertServiceImpl;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class JPushAlertServiceTest {
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

    @Autowired
    private JPushScheduleAlertService jPushScheduleAlertService ;

    private static final long loanId = 10000000001L;
    private static final long loanRepayId = 100000003L;
    private static final long investId = 3939399323434L;

    private static final long loanId2 = 20000000001L;
    private static final long loanRepayId2 = 200000003L;
    private static final long investId2 = 39393993235555L;

    private static final long loanId3 = 30000000001L;
    private static final long loanRepayId3 = 300000003L;
    private static final long investId3 = 49393993235555L;

    private InvestRepayModel createInvestRepayHasDefaultInterest(long investId, RepayStatus repayStatus, int period) {
        InvestRepayModel investRepayModel = new InvestRepayModel();
        investRepayModel.setId(idGenerator.generate());
        investRepayModel.setInvestId(investId);
        investRepayModel.setStatus(repayStatus);
        investRepayModel.setCorpus(100);
        investRepayModel.setExpectedInterest(100);
        investRepayModel.setDefaultInterest(100);
        investRepayModel.setActualInterest(100);
        investRepayModel.setExpectedFee(0);
        investRepayModel.setActualFee(0);
        investRepayModel.setPeriod(period);
        investRepayModel.setRepayDate(new Date());
        investRepayModel.setActualRepayDate(new Date());
        investRepayModel.setCreatedTime(new Date());
        return investRepayModel;
    }

    private InvestRepayModel createInvestRepayNoDefaultInterest(long investId, RepayStatus repayStatus, int period) {
        InvestRepayModel investRepayModel = new InvestRepayModel();
        investRepayModel.setId(idGenerator.generate());
        investRepayModel.setInvestId(investId);
        investRepayModel.setStatus(repayStatus);
        investRepayModel.setCorpus(100);
        investRepayModel.setExpectedInterest(100);
        investRepayModel.setActualInterest(100);
        investRepayModel.setDefaultInterest(0);
        investRepayModel.setExpectedInterest(100);
        investRepayModel.setExpectedFee(100);
        investRepayModel.setActualFee(100);
        investRepayModel.setPeriod(period);
        investRepayModel.setRepayAmount(100);
        investRepayModel.setRepayDate(new Date());
        investRepayModel.setActualRepayDate(new Date());
        investRepayModel.setCreatedTime(new Date());
        return investRepayModel;
    }

    private JPushAlertModel createJPushAlert() {
        JPushAlertModel jPushAlertModel = new JPushAlertModel();
        jPushAlertModel.setId(1005);
        jPushAlertModel.setName("用户资金变动推送");
        jPushAlertModel.setPushType(PushType.COUPON_INCOME_ALERT);
        jPushAlertModel.setPushSource(PushSource.ALL);
        jPushAlertModel.setContent("【拓天速贷】亲爱的天宝，您刚刚收到{0}的收益{1}元，请点击查看");
        jPushAlertModel.setIsAutomatic(true);
        jPushAlertModel.setCreatedTime(new Date());
        jPushAlertModel.setJumpTo(JumpTo.MY_WEALTH);
        jPushAlertModel.setStatus(PushStatus.ENABLED);
        return jPushAlertModel;
    }

    private List<InvestModel> createInvestSuccessList(long loanId) {

        List<InvestModel> investModelList = new ArrayList<InvestModel>();
        InvestModel investModel_1 = new InvestModel();
        investModel_1.setId(10001);
        investModel_1.setLoanId(loanId);
        investModel_1.setStatus(InvestStatus.SUCCESS);
        investModel_1.setLoginName("test1");

        InvestModel investModel_2 = new InvestModel();
        investModel_2.setId(10002);
        investModel_2.setLoanId(loanId);
        investModel_2.setStatus(InvestStatus.SUCCESS);
        investModel_2.setLoginName("test2");

        InvestModel investModel_3 = new InvestModel();
        investModel_3.setId(10003);
        investModel_3.setLoanId(loanId);
        investModel_3.setStatus(InvestStatus.SUCCESS);
        investModel_3.setLoginName("test3");
        investModelList.add(investModel_1);
        investModelList.add(investModel_2);
        investModelList.add(investModel_3);

        return investModelList;
    }

    private List<InvestReferrerRewardModel> createInvestReferrerRewardModelList(long investId) {
        List<InvestReferrerRewardModel> investReferrerRewardModelList = new ArrayList<InvestReferrerRewardModel>();
        InvestReferrerRewardModel investReferrerRewardModel_1 = new InvestReferrerRewardModel();
        investReferrerRewardModel_1.setInvestId(investId);
        investReferrerRewardModel_1.setAmount(10);
        investReferrerRewardModel_1.setReferrerLoginName("testreffer");
        investReferrerRewardModel_1.setStatus(ReferrerRewardStatus.FAILURE);

        InvestReferrerRewardModel investReferrerRewardModel_2 = new InvestReferrerRewardModel();
        investReferrerRewardModel_2.setInvestId(investId);
        investReferrerRewardModel_2.setAmount(10);
        investReferrerRewardModel_2.setReferrerLoginName("testreffer2");
        investReferrerRewardModel_2.setStatus(ReferrerRewardStatus.SUCCESS);

        investReferrerRewardModelList.add(investReferrerRewardModel_1);
        investReferrerRewardModelList.add(investReferrerRewardModel_2);

        return investReferrerRewardModelList;
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

    private ExchangeCouponDto fakeCouponDto(CouponType couponType, UserGroup userGroup) {
        ExchangeCouponDto exchangeCouponDto = new ExchangeCouponDto();
        exchangeCouponDto.setId(1001L);
        exchangeCouponDto.setAmount("1000.00");
        exchangeCouponDto.setTotalCount(1000L);
        exchangeCouponDto.setEndTime(new Date());
        exchangeCouponDto.setStartTime(new Date());
        exchangeCouponDto.setInvestLowerLimit("1000.00");
        exchangeCouponDto.setCouponType(couponType);
        List<ProductType> productTypes = Lists.newArrayList();
        productTypes.add(ProductType._180);
        exchangeCouponDto.setProductTypes(productTypes);
        exchangeCouponDto.setInvestLowerLimit("1000.00");
        exchangeCouponDto.setUserGroup(userGroup);
        return exchangeCouponDto;
    }

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        createJPushAlert();
    }

    private void publicMockMethod(long loanId1, int currentPeriod, String loginName, long investId, String registrationIds, InvestRepayModel investRepayModel) {
        LoanRepayModel loanRepayModel = new LoanRepayModel();
        loanRepayModel.setLoanId(loanId1);
        loanRepayModel.setPeriod(currentPeriod);
        when(loanRepayMapper.findById(anyLong())).thenReturn(loanRepayModel);

        List<InvestModel> investModels = new ArrayList<InvestModel>();
        InvestModel investModel = new InvestModel();
        investModel.setId(investId);
        investModel.setLoginName(loginName);
        investModels.add(investModel);

        AccountModel accountModel = new AccountModel(loginName, "test", "32424234", "test", "1233", new Date());
        accountModel.setBalance(10);

        LoanModel loanModel = new LoanModel();
        loanModel.setStatus(LoanStatus.COMPLETE);
        loanModel.setId(loanId);

        when(loanMapper.findById(anyLong())).thenReturn(loanModel);

        when(investMapper.findSuccessInvestsByLoanId(anyLong())).thenReturn(investModels);

        when(jPushAlertMapper.findJPushAlertByPushType(any(PushType.class))).thenReturn(createJPushAlert());

        when(investRepayMapper.findByInvestIdAndPeriod(anyInt(), anyInt())).thenReturn(investRepayModel);

        when(mobileAppJPushClient.sendPushAlertByRegistrationIds(anyString(), anyList(), anyString(), anyMap(), any(PushSource.class))).thenReturn(true);

        when(investMapper.findSuccessInvestsByLoanId(anyLong())).thenReturn(createInvestSuccessList(loanId1));

        when(investReferrerRewardMapper.findByInvestId(anyLong())).thenReturn(createInvestReferrerRewardModelList(investId));

        when(accountMapper.findByLoginName(anyString())).thenReturn(accountModel);

        when(redisWrapperClient.hexists(anyString(), anyString())).thenReturn(true);
        when(redisWrapperClient.hget(anyString(), anyString())).thenReturn(registrationIds);
    }

    @Test
    public void shouldGetDefaultInterestWhenHasDefaultInterest() {
        List<InvestRepayModel> investRepayModels = Lists.newArrayList();

        investRepayModels.add(createInvestRepayNoDefaultInterest(investId, RepayStatus.COMPLETE, 1));
        investRepayModels.add(createInvestRepayNoDefaultInterest(investId, RepayStatus.OVERDUE, 2));
        investRepayModels.add(createInvestRepayNoDefaultInterest(investId, RepayStatus.REPAYING, 3));

        publicMockMethod(loanId, 2, "testuser123", investId, "abdisierieruis123", investRepayModels.get(1));

        jPushAlertService.autoJPushRepayAlert(loanRepayId2, false);

        ArgumentCaptor argumentJPushAlertId = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor argumentAlert = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<PushSource> argumentPushSource = ArgumentCaptor.forClass(PushSource.class);
        ArgumentCaptor<ArrayList<String>> argumentRegistrationIds = ArgumentCaptor.forClass((Class<ArrayList<String>>) new ArrayList<String>().getClass());
        ArgumentCaptor<HashMap> argumentCaptorExtras = ArgumentCaptor.forClass(HashMap.class);

        verify(mobileAppJPushClient, times(3)).sendPushAlertByRegistrationIds((String) argumentJPushAlertId.capture(), argumentRegistrationIds.capture(), (String) argumentAlert.capture(), argumentCaptorExtras.capture(), argumentPushSource.capture());

        assertEquals(String.valueOf(createJPushAlert().getId()), argumentJPushAlertId.getValue());
        assertEquals(createJPushAlert().getContent().replace("{0}", "1.00"), argumentAlert.getValue());
    }

    @Test
    public void shouldGetDefaultInterestWhenNoDefaultInterest() {
        List<InvestRepayModel> investRepayModels = Lists.newArrayList();
        investRepayModels.add(createInvestRepayNoDefaultInterest(investId2, RepayStatus.COMPLETE, 1));
        investRepayModels.add(createInvestRepayNoDefaultInterest(investId2, RepayStatus.COMPLETE, 2));
        investRepayModels.add(createInvestRepayNoDefaultInterest(investId2, RepayStatus.REPAYING, 3));

        publicMockMethod(loanId2, 3, "testuser1234", investId2, "abdisierieruis1234", investRepayModels.get(2));

        jPushAlertService.autoJPushRepayAlert(loanRepayId2, false);

        ArgumentCaptor argumentJPushAlertId = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor argumentAlert = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<PushSource> argumentPushSource = ArgumentCaptor.forClass(PushSource.class);
        ArgumentCaptor<ArrayList<String>> argumentRegistrationIds = ArgumentCaptor.forClass((Class<ArrayList<String>>) new ArrayList<String>().getClass());
        ArgumentCaptor<HashMap> argumentCaptorExtras = ArgumentCaptor.forClass(HashMap.class);

        verify(mobileAppJPushClient, times(3)).sendPushAlertByRegistrationIds((String) argumentJPushAlertId.capture(), argumentRegistrationIds.capture(), (String) argumentAlert.capture(), argumentCaptorExtras.capture(), argumentPushSource.capture());

        assertEquals(String.valueOf(createJPushAlert().getId()), argumentJPushAlertId.getValue());
        assertEquals(createJPushAlert().getContent().replace("{0}", "1.00"), argumentAlert.getValue());

    }

    @Test
    public void shouldGetDefaultInterestWhenGapDefaultInterest() {
        List<InvestRepayModel> investRepayModels = Lists.newArrayList();
        investRepayModels.add(createInvestRepayNoDefaultInterest(investId3, RepayStatus.COMPLETE, 1));
        investRepayModels.add(createInvestRepayHasDefaultInterest(investId3, RepayStatus.OVERDUE, 2));
        investRepayModels.add(createInvestRepayNoDefaultInterest(investId3, RepayStatus.OVERDUE, 3));
        investRepayModels.add(createInvestRepayNoDefaultInterest(investId3, RepayStatus.REPAYING, 4));
        investRepayModels.add(createInvestRepayNoDefaultInterest(investId3, RepayStatus.REPAYING, 5));

        publicMockMethod(loanId3, 3, "testuser12345", investId3, "abdisierieruis12345", investRepayModels.get(2));

        jPushAlertService.autoJPushRepayAlert(loanRepayId3, false);

        ArgumentCaptor argumentJPushAlertId = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor argumentAlert = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<PushSource> argumentPushSource = ArgumentCaptor.forClass(PushSource.class);
        ArgumentCaptor<ArrayList<String>> argumentRegistrationIds = ArgumentCaptor.forClass((Class<ArrayList<String>>) new ArrayList<String>().getClass());
        ArgumentCaptor<HashMap> argumentCaptorExtras = ArgumentCaptor.forClass(HashMap.class);

        verify(mobileAppJPushClient, times(3)).sendPushAlertByRegistrationIds((String) argumentJPushAlertId.capture(), argumentRegistrationIds.capture(), (String) argumentAlert.capture(), argumentCaptorExtras.capture(), argumentPushSource.capture());

        assertEquals(String.valueOf(createJPushAlert().getId()), argumentJPushAlertId.getValue());
        assertEquals(createJPushAlert().getContent().replace("{0}", "1.00"), argumentAlert.getValue());
    }

    @Test
    public void shouldAutoJPushReferrerRewardAlert() {

        publicMockMethod(loanId, 2, "testuser123", investId, "abdisierieruis123", null);

        jPushAlertService.autoJPushReferrerRewardAlert(10001);

        ArgumentCaptor argumentJPushAlertId = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor argumentAlert = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<PushSource> argumentPushSource = ArgumentCaptor.forClass(PushSource.class);
        ArgumentCaptor<ArrayList<String>> argumentRegistrationIds = ArgumentCaptor.forClass((Class<ArrayList<String>>) new ArrayList<String>().getClass());
        ArgumentCaptor<HashMap> argumentCaptorExtras = ArgumentCaptor.forClass(HashMap.class);
        verify(mobileAppJPushClient, times(3)).sendPushAlertByRegistrationIds((String) argumentJPushAlertId.capture(), argumentRegistrationIds.capture(), (String) argumentAlert.capture(), argumentCaptorExtras.capture(), argumentPushSource.capture());

    }

    @Test
    public void shouldAutoJPushCouponIncomeAlert() {

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

        CouponModel couponModel = new CouponModel(fakeCouponDto(CouponType.INVEST_COUPON, UserGroup.ALL_USER));

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

        jPushAlertService.autoJPushCouponIncomeAlert(currentLoanRepayModel.getId());

        ArgumentCaptor argumentJPushAlertId = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor argumentAlert = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<PushSource> argumentPushSource = ArgumentCaptor.forClass(PushSource.class);
        ArgumentCaptor<ArrayList<String>> argumentRegistrationIds = ArgumentCaptor.forClass((Class<ArrayList<String>>) new ArrayList<String>().getClass());
        ArgumentCaptor<HashMap> argumentCaptorExtras = ArgumentCaptor.forClass(HashMap.class);

        verify(mobileAppJPushClient, times(2)).sendPushAlertByRegistrationIds((String) argumentJPushAlertId.capture(), argumentRegistrationIds.capture(), (String) argumentAlert.capture(), argumentCaptorExtras.capture(), argumentPushSource.capture());

        assertEquals(String.valueOf(createJPushAlert().getId()), argumentJPushAlertId.getValue());
        assertEquals(createJPushAlert().getContent().replace("{0}", "投资体验券").replace("{1}", "69.04"), argumentAlert.getValue());
    }

    @Test
    public void shouldAutoJPushRedEnvelopeAlert() {
        LoanModel loanModel = fakeLoanModel("test123");
        LoanRepayModel currentLoanRepayModel = new LoanRepayModel();
        currentLoanRepayModel.setId(1000101);
        currentLoanRepayModel.setLoanId(loanModel.getId());
        currentLoanRepayModel.setPeriod(1);
        currentLoanRepayModel.setStatus(RepayStatus.COMPLETE);
        List<LoanRepayModel> loanRepayModels = new ArrayList<>();

        for (int i = 1; i < 4; i++) {
            LoanRepayModel repayModel = new LoanRepayModel();
            repayModel.setLoanId(loanModel.getId());
            repayModel.setId(Long.parseLong(10001 + "0" + i));
            repayModel.setPeriod(i);
            loanRepayModels.add(repayModel);
        }
        CouponModel couponModel = new CouponModel(fakeCouponDto(CouponType.RED_ENVELOPE, UserGroup.ALL_USER));
        InvestModel investModel = new InvestModel(1001, loanModel.getId(), null, 100, "test123", null, Source.WEB, null, 0.1);
        List<UserCouponModel> userCouponModels = new ArrayList<>();
        UserCouponModel userCouponModel = new UserCouponModel();
        userCouponModel.setId(idGenerator.generate());
        userCouponModel.setInvestId(1001L);
        userCouponModel.setActualInterest(10);
        userCouponModel.setLoginName("zhangyong");
        userCouponModel.setCouponId(couponModel.getId());
        userCouponModel.setLoanId(loanModel.getId());
        userCouponModel.setStatus(InvestStatus.SUCCESS);
        userCouponModel.setActualInterest(1000);
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
        ArgumentCaptor<HashMap> argumentCaptorExtras = ArgumentCaptor.forClass(HashMap.class);
        ArgumentCaptor<PushSource> argumentPushSource = ArgumentCaptor.forClass(PushSource.class);
        ArgumentCaptor<ArrayList<String>> argumentRegistrationIds = ArgumentCaptor.forClass((Class<ArrayList<String>>) new ArrayList<String>().getClass());

        jPushAlertService.autoJPushRedEnvelopeAlert(currentLoanRepayModel.getId());

        verify(mobileAppJPushClient, times(1)).sendPushAlertByRegistrationIds((String) argumentJPushAlertId.capture(),
                argumentRegistrationIds.capture(), (String) argumentAlert.capture(), argumentCaptorExtras.capture(), argumentPushSource.capture());

    }

    @Test
    public void shouldSendPushAlertManyExtrasByRegistrationIdsIsOk() {
        List<InvestRepayModel> investRepayModels = Lists.newArrayList();

        investRepayModels.add(createInvestRepayNoDefaultInterest(investId, RepayStatus.COMPLETE, 1));
        investRepayModels.add(createInvestRepayNoDefaultInterest(investId, RepayStatus.OVERDUE, 2));
        investRepayModels.add(createInvestRepayNoDefaultInterest(investId, RepayStatus.REPAYING, 3));

        publicMockMethod(loanId, 2, "testuser123", investId, "abdisierieruis123", investRepayModels.get(1));
        LoanModel loanModel = new LoanModel();
        loanModel.setStatus(LoanStatus.COMPLETE);
        loanModel.setId(loanId);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        JPushAlertModel jPushAlertModel = createJPushAlert();
        jPushAlertModel.setJumpTo(JumpTo.INVEST_REPAY);
        when(jPushAlertMapper.findJPushAlertByPushType(any(PushType.class))).thenReturn(jPushAlertModel);

        jPushAlertService.autoJPushRepayAlert(loanRepayId2, false);

        ArgumentCaptor argumentJPushAlertId = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor argumentAlert = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<PushSource> argumentPushSource = ArgumentCaptor.forClass(PushSource.class);
        ArgumentCaptor<HashMap> argumentCaptorExtras = ArgumentCaptor.forClass(HashMap.class);
        ArgumentCaptor<ArrayList<String>> argumentRegistrationIds = ArgumentCaptor.forClass((Class<ArrayList<String>>) new ArrayList<String>().getClass());

        verify(mobileAppJPushClient, times(3)).sendPushAlertByRegistrationIds((String) argumentJPushAlertId.capture(), argumentRegistrationIds.capture(), (String) argumentAlert.capture(), argumentCaptorExtras.capture(), argumentPushSource.capture());
        assertEquals(String.valueOf(createJPushAlert().getId()), argumentJPushAlertId.getValue());
        assertEquals(createJPushAlert().getContent().replace("{0}", "1.00"), argumentAlert.getValue());
        assertEquals(String.valueOf(1),argumentCaptorExtras.getAllValues().get(0).get("isCompleted"));
        assertEquals(String.valueOf(10003),argumentCaptorExtras.getAllValues().get(0).get("investId"));
        assertEquals(String.valueOf(loanModel.getId()),argumentCaptorExtras.getAllValues().get(0).get("loanId"));
    }
}
