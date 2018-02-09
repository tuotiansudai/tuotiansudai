package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.coupon.service.ExchangeCodeService;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class CouponAssignmentServiceTest {

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private ExchangeCodeService exchangeCodeService;

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Mock
    private MQWrapperClient mqWrapperClient;


    @Test
    public void shouldExchangeCode() throws Exception {
        UserModel fakeUser = getFakeUser("fakeUser1");
        CouponModel fakeCoupon = getFakeCoupon(UserGroup.EXCHANGER_CODE, false);
        exchangeCodeService.generateExchangeCode(fakeCoupon.getId(), 1);
        List<String> exchangeCodes = exchangeCodeService.getExchangeCodes(fakeCoupon.getId());

        couponAssignmentService.assignUserCoupon(fakeUser.getLoginName(), exchangeCodes.get(0));

        List<UserCouponModel> userCouponModels = userCouponMapper.findByCouponId(fakeCoupon.getId());

        assertThat(userCouponModels.size(), is(1));
        assertThat(userCouponModels.get(0).getExchangeCode(), is(exchangeCodes.get(0)));
    }

    @Test
    public void shouldAssignCoupon() throws Exception {
        UserModel fakeUser = getFakeUser("fakeUser1");
        CouponModel fakeCoupon = getFakeCoupon(UserGroup.ALL_USER, false);

        couponAssignmentService.assignUserCoupon(fakeUser.getLoginName(), fakeCoupon.getId());
        couponAssignmentService.assignUserCoupon(fakeUser.getLoginName(), fakeCoupon.getId());

        List<UserCouponModel> userCouponModels = userCouponMapper.findByCouponId(fakeCoupon.getId());

        assertThat(userCouponModels.size(), is(1));
    }

    @Test
    public void shouldAssignMultipleCoupon() {
        UserModel fakeUser = getFakeUser("fakeUser1");
        CouponModel fakeCoupon = getFakeCoupon(UserGroup.ALL_USER, true);

        couponAssignmentService.assignUserCoupon(fakeUser.getLoginName(), fakeCoupon.getId());
        UserCouponModel userCouponModel = userCouponMapper.findByCouponId(fakeCoupon.getId()).get(0);
        userCouponModel.setStatus(InvestStatus.SUCCESS);
        userCouponMapper.update(userCouponModel);

        couponAssignmentService.assignUserCoupon(fakeUser.getLoginName(), fakeCoupon.getId());

        List<UserCouponModel> userCouponModels = userCouponMapper.findByCouponId(fakeCoupon.getId());
        assertThat(userCouponModels.size(), is(2));
    }

    @Test
    public void shouldAsyncAssignUserGroup() throws Exception {
        UserModel fakeUser = getFakeUser("fakeUser");
        this.createMockUser("fakeUser");
        UserMembershipModel userMembershipModel = new UserMembershipModel(fakeUser.getLoginName(), 6, new DateTime().plusDays(1).toDate(), UserMembershipType.UPGRADE);
        userMembershipModel.setCreatedTime(new DateTime().minusDays(1).toDate());
        userMembershipMapper.create(userMembershipModel);

        CouponModel fakeCoupon = getFakeCoupon(UserGroup.MEMBERSHIP_V5, false);

        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(couponAssignmentService, "mqWrapperClient", mqWrapperClient);

        ArgumentCaptor<MessageQueue> messageQueueCaptor = ArgumentCaptor.forClass(MessageQueue.class);
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);

        couponAssignmentService.asyncAssignUserCoupon(fakeUser.getLoginName(), Lists.newArrayList(UserGroup.MEMBERSHIP_V5));

        verify(mqWrapperClient, times(1)).sendMessage(messageQueueCaptor.capture(), stringCaptor.capture());
        assertEquals(MessageQueue.CouponAssigning, messageQueueCaptor.getValue());
        String queueMessage = fakeUser.getLoginName() + ":" + fakeCoupon.getId();
        assertEquals(queueMessage, stringCaptor.getValue());
    }

    @Test
    public void shouldAssign() throws Exception {
        UserModel fakeUser = getFakeUser("fakeUser1");
        CouponModel fakeCoupon = getFakeCoupon(UserGroup.ALL_USER, false);

        couponAssignmentService.assign(fakeUser.getLoginName(), fakeCoupon.getId(), "");
        couponAssignmentService.assign(fakeUser.getLoginName(), fakeCoupon.getId(), "");

        UserCouponModel userCouponModel = userCouponMapper.findByCouponId(fakeCoupon.getId()).get(0);
        userCouponModel.setStatus(InvestStatus.SUCCESS);
        userCouponMapper.update(userCouponModel);

        couponAssignmentService.assignUserCoupon(fakeUser.getLoginName(), fakeCoupon.getId());

        List<UserCouponModel> userCouponModels = userCouponMapper.findByCouponId(fakeCoupon.getId());
        assertThat(userCouponModels.size(), is(2));
    }

    @Test
    public void shouldAssignMultiple() throws Exception {
        UserModel fakeUser = getFakeUser("fakeUser1");
        CouponModel fakeCoupon = getFakeCoupon(UserGroup.ALL_USER, true);

        couponAssignmentService.assign(fakeUser.getLoginName(), fakeCoupon.getId(), "");
        couponAssignmentService.assign(fakeUser.getLoginName(), fakeCoupon.getId(), "");

        UserCouponModel userCouponModel = userCouponMapper.findByCouponId(fakeCoupon.getId()).get(0);
        userCouponModel.setStatus(InvestStatus.SUCCESS);
        userCouponMapper.update(userCouponModel);

        couponAssignmentService.assignUserCoupon(fakeUser.getLoginName(), fakeCoupon.getId());

        List<UserCouponModel> userCouponModels = userCouponMapper.findByCouponId(fakeCoupon.getId());
        assertThat(userCouponModels.size(), is(2));
    }

    private CouponModel getFakeCoupon(UserGroup userGroup, boolean isMultiple) {
        UserModel couponCreator = getFakeUser("fakeUser1");
        userMapper.create(couponCreator);
        return createCouponModel(userGroup, isMultiple, couponCreator.getLoginName());
    }

    public CouponModel createCouponModel(UserGroup userGroup, boolean isMultiple, String loginName) {
        CouponModel couponModel = new CouponModel();
        couponModel.setAmount(1);
        couponModel.setTotalCount(1L);
        couponModel.setStartTime(new DateTime().withTimeAtStartOfDay().toDate());
        couponModel.setEndTime(new DateTime().plusDays(1).toDate());
        couponModel.setDeadline(1);
        couponModel.setProductTypes(Lists.newArrayList(ProductType._30));
        couponModel.setCouponType(CouponType.INTEREST_COUPON);
        couponModel.setInvestLowerLimit(0);
        couponModel.setUserGroup(userGroup);
        couponModel.setCreatedBy(loginName);
        couponModel.setCreatedTime(new Date());
        couponModel.setActive(true);
        couponModel.setMultiple(isMultiple);
        couponModel.setCouponSource("couponSource");
        couponMapper.create(couponModel);
        return couponModel;
    }

    @Test
    public void shouldFirstInvestAchievementAssignUserCouponIsOk() {
        String loginName = "testAchievement";
        createMockUser(loginName);
        LoanModel loanModel = getFakeExperienceLoan(loginName);
        InvestModel firstInvestModel = createInvest(loginName, loanModel.getId());
        InvestModel maxInvestModel = createInvest(loginName, loanModel.getId());
        InvestModel lastInvestModel = createInvest(loginName, loanModel.getId());
        loanModel.setFirstInvestAchievementId(firstInvestModel.getId());
        loanModel.setMaxAmountAchievementId(maxInvestModel.getId());
        loanModel.setLastInvestAchievementId(lastInvestModel.getId());
        loanMapper.update(loanModel);
        getFakeUser(loginName);
        CouponModel couponModel = getFakeCoupon(UserGroup.FIRST_INVEST_ACHIEVEMENT, true);
        couponAssignmentService.assignInvestAchievementUserCoupon(loginName, 123456, couponModel.getId());
        List<UserCouponModel> userCouponModelList = userCouponMapper.findByLoginName(loginName, Lists.newArrayList(CouponType.INTEREST_COUPON));
        assertTrue(userCouponModelList.size() == 0);
        couponAssignmentService.assignInvestAchievementUserCoupon(loginName, loanModel.getId(), couponModel.getId());
        userCouponModelList = userCouponMapper.findByLoginName(loginName, Lists.newArrayList(CouponType.INTEREST_COUPON));
        assertTrue(userCouponModelList.size() == 1);
        couponAssignmentService.assignInvestAchievementUserCoupon(loginName, loanModel.getId(), couponModel.getId());
        userCouponModelList = userCouponMapper.findByLoginName(loginName, Lists.newArrayList(CouponType.INTEREST_COUPON));
        assertTrue(userCouponModelList.size() == 1);
        couponModel = createCouponModel(UserGroup.MAX_AMOUNT_ACHIEVEMENT, true, loginName);
        couponAssignmentService.assignInvestAchievementUserCoupon(loginName, loanModel.getId(), couponModel.getId());
        userCouponModelList = userCouponMapper.findByLoginName(loginName, Lists.newArrayList(CouponType.INTEREST_COUPON));
        assertTrue(userCouponModelList.size() == 2);
        couponModel = createCouponModel(UserGroup.LAST_INVEST_ACHIEVEMENT, true, loginName);
        couponAssignmentService.assignInvestAchievementUserCoupon(loginName, loanModel.getId(), couponModel.getId());
        userCouponModelList = userCouponMapper.findByLoginName(loginName, Lists.newArrayList(CouponType.INTEREST_COUPON));
        assertTrue(userCouponModelList.size() == 3);
    }

    @Test
    public void shouldMaxInvestAchievementAssignUserCouponIsOk() {
        String loginName = "testAchievement";
        createMockUser(loginName);
        LoanModel loanModel = getFakeExperienceLoan(loginName);
        InvestModel firstInvestModel = createInvest(loginName, loanModel.getId());
        InvestModel maxInvestModel = createInvest(loginName, loanModel.getId());
        InvestModel lastInvestModel = createInvest(loginName, loanModel.getId());
        loanModel.setFirstInvestAchievementId(firstInvestModel.getId());
        loanModel.setMaxAmountAchievementId(maxInvestModel.getId());
        loanModel.setLastInvestAchievementId(lastInvestModel.getId());
        loanMapper.update(loanModel);
        getFakeUser(loginName);
        CouponModel couponModel = getFakeCoupon(UserGroup.MAX_AMOUNT_ACHIEVEMENT, true);
        couponAssignmentService.assignInvestAchievementUserCoupon(loginName, 123456, couponModel.getId());
        List<UserCouponModel> userCouponModelList = userCouponMapper.findByLoginName(loginName, Lists.newArrayList(CouponType.INTEREST_COUPON));
        assertTrue(userCouponModelList.size() == 0);
        couponAssignmentService.assignInvestAchievementUserCoupon(loginName, loanModel.getId(), couponModel.getId());
        userCouponModelList = userCouponMapper.findByLoginName(loginName, Lists.newArrayList(CouponType.INTEREST_COUPON));
        assertTrue(userCouponModelList.size() == 1);
        couponAssignmentService.assignInvestAchievementUserCoupon(loginName, loanModel.getId(), couponModel.getId());
        userCouponModelList = userCouponMapper.findByLoginName(loginName, Lists.newArrayList(CouponType.INTEREST_COUPON));
        assertTrue(userCouponModelList.size() == 1);
        couponModel = createCouponModel(UserGroup.LAST_INVEST_ACHIEVEMENT, true, loginName);
        couponAssignmentService.assignInvestAchievementUserCoupon(loginName, loanModel.getId(), couponModel.getId());
        userCouponModelList = userCouponMapper.findByLoginName(loginName, Lists.newArrayList(CouponType.INTEREST_COUPON));
        assertTrue(userCouponModelList.size() == 2);
        couponAssignmentService.assignInvestAchievementUserCoupon(loginName, loanModel.getId(), couponModel.getId());
        userCouponModelList = userCouponMapper.findByLoginName(loginName, Lists.newArrayList(CouponType.INTEREST_COUPON));
        assertTrue(userCouponModelList.size() == 2);
    }

    @Test
    public void shouldLastInvestAchievementAssignUserCouponIsOk() {
        String loginName = "testAchievement";
        createMockUser(loginName);
        LoanModel loanModel = getFakeExperienceLoan(loginName);
        InvestModel firstInvestModel = createInvest(loginName, loanModel.getId());
        InvestModel maxInvestModel = createInvest(loginName, loanModel.getId());
        InvestModel lastInvestModel = createInvest(loginName, loanModel.getId());
        loanModel.setFirstInvestAchievementId(firstInvestModel.getId());
        loanModel.setMaxAmountAchievementId(maxInvestModel.getId());
        loanModel.setLastInvestAchievementId(lastInvestModel.getId());
        loanMapper.update(loanModel);
        getFakeUser(loginName);
        CouponModel couponModel = getFakeCoupon(UserGroup.LAST_INVEST_ACHIEVEMENT, true);
        couponAssignmentService.assignInvestAchievementUserCoupon(loginName, 123456, couponModel.getId());
        List<UserCouponModel> userCouponModelList = userCouponMapper.findByLoginName(loginName, Lists.newArrayList(CouponType.INTEREST_COUPON));
        assertTrue(userCouponModelList.size() == 0);
        couponAssignmentService.assignInvestAchievementUserCoupon(loginName, loanModel.getId(), couponModel.getId());
        userCouponModelList = userCouponMapper.findByLoginName(loginName, Lists.newArrayList(CouponType.INTEREST_COUPON));
        assertTrue(userCouponModelList.size() == 1);
        couponAssignmentService.assignInvestAchievementUserCoupon(loginName, loanModel.getId(), couponModel.getId());
        userCouponModelList = userCouponMapper.findByLoginName(loginName, Lists.newArrayList(CouponType.INTEREST_COUPON));
        assertTrue(userCouponModelList.size() == 1);
        couponModel = createCouponModel(UserGroup.MAX_AMOUNT_ACHIEVEMENT, true, loginName);
        couponAssignmentService.assignInvestAchievementUserCoupon(loginName, loanModel.getId(), couponModel.getId());
        userCouponModelList = userCouponMapper.findByLoginName(loginName, Lists.newArrayList(CouponType.INTEREST_COUPON));
        assertTrue(userCouponModelList.size() == 2);
    }

    @Test
    public void shouldManyLoanInvestAchievementAssignUserCouponIsOk() {
        String loginName = "testAchievement";
        createMockUser(loginName);
        LoanModel loanModel = getFakeExperienceLoan(loginName);
        InvestModel firstInvestModel = createInvest(loginName, loanModel.getId());
        InvestModel maxInvestModel = createInvest(loginName, loanModel.getId());
        InvestModel lastInvestModel = createInvest(loginName, loanModel.getId());
        loanModel.setFirstInvestAchievementId(firstInvestModel.getId());
        loanModel.setMaxAmountAchievementId(maxInvestModel.getId());
        loanModel.setLastInvestAchievementId(lastInvestModel.getId());
        loanMapper.update(loanModel);
        CouponModel couponModel = getFakeCoupon(UserGroup.LAST_INVEST_ACHIEVEMENT, true);
        couponAssignmentService.assignInvestAchievementUserCoupon(loginName, loanModel.getId(), couponModel.getId());
        List<UserCouponModel> userCouponModelList = userCouponMapper.findByLoginName(loginName, Lists.newArrayList(CouponType.INTEREST_COUPON));
        assertTrue(userCouponModelList.size() == 1);

        String loginNameTwo = "testAchievementTwo";
        createMockUser(loginNameTwo);
        loanModel = getFakeExperienceLoan(loginNameTwo);
        firstInvestModel = createInvest(loginNameTwo, loanModel.getId());
        maxInvestModel = createInvest(loginNameTwo, loanModel.getId());
        lastInvestModel = createInvest(loginNameTwo, loanModel.getId());
        loanModel.setFirstInvestAchievementId(firstInvestModel.getId());
        loanModel.setMaxAmountAchievementId(maxInvestModel.getId());
        loanModel.setLastInvestAchievementId(lastInvestModel.getId());
        loanMapper.update(loanModel);
        couponAssignmentService.assignInvestAchievementUserCoupon(loginName, loanModel.getId(), couponModel.getId());
        userCouponModelList = userCouponMapper.findByLoginName(loginName, Lists.newArrayList(CouponType.INTEREST_COUPON));
        assertTrue(userCouponModelList.size() == 1);
    }

    private InvestModel createInvest(String loginName, long loanId) {
        InvestModel model = new InvestModel(IdGenerator.generate(), loanId, null, 1, loginName, new Date(), Source.WEB, null, 0.1);
        investMapper.create(model);
        return model;
    }

    private LoanModel getFakeExperienceLoan(String loginName) {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(IdGenerator.generate());
        fakeLoanModel.setName(loginName);
        fakeLoanModel.setLoanAmount(10000L);
        fakeLoanModel.setLoanerLoginName(loginName);
        fakeLoanModel.setLoanerUserName("借款人");
        fakeLoanModel.setLoanerIdentityNumber("id");
        fakeLoanModel.setAgentLoginName(loginName);
        fakeLoanModel.setType(LoanType.LOAN_INTEREST_LUMP_SUM_REPAY);
        fakeLoanModel.setPeriods(1);
        fakeLoanModel.setStatus(LoanStatus.RAISING);
        fakeLoanModel.setActivityType(ActivityType.NEWBIE);
        fakeLoanModel.setProductType(ProductType.EXPERIENCE);
        fakeLoanModel.setBaseRate(0.15);
        fakeLoanModel.setActivityRate(0);
        fakeLoanModel.setDuration(3);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setRecheckTime(new Date());
        fakeLoanModel.setPledgeType(PledgeType.HOUSE);

        loanMapper.create(fakeLoanModel);
        return fakeLoanModel;
    }


    private void createMockUser(String loginName) {
        UserModel um = getFakeUser(loginName);
        userMapper.create(um);
    }

    public UserModel getFakeUser(String loginName) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setPassword("password");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13" + RandomStringUtils.randomNumeric(9));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }
}
