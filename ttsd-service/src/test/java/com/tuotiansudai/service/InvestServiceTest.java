package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
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
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class InvestServiceTest {
    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestService investService;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private MembershipMapper membershipMapper;

    static private long LOAN_ID = 0;

    private void createLoanByUserId(String userId, long loanId) {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName(userId);
        loanDto.setLoanerUserName("借款人");
        loanDto.setLoanerIdentityNumber("111111111111111111");
        loanDto.setAgentLoginName(userId);
        loanDto.setBasicRate("16.00");
        loanDto.setId(loanId);
        loanDto.setProjectName("店铺资金周转");
        loanDto.setActivityRate("12");
        loanDto.setShowOnHome(true);
        loanDto.setPeriods(30);
        loanDto.setActivityType(ActivityType.NORMAL);
        loanDto.setContractId(123);
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("10000");
        loanDto.setType(LoanType.INVEST_INTEREST_LUMP_SUM_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.WAITING_VERIFY);
        loanDto.setProductType(ProductType._30);
        loanDto.setPledgeType(PledgeType.HOUSE);
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setDeadline(new DateTime().plusDays(10).toDate());
        loanMapper.create(loanModel);
    }

    private void createUserByUserId(String userId) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(userId);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("1" + RandomStringUtils.randomNumeric(10));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
    }

    private void createInvests(String loginName, long loanId) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, -98);
        for (int i = 10000000; i < 10099000; i += 1000) {
            cal.add(Calendar.SECOND, 1);
            InvestModel model = new InvestModel(IdGenerator.generate(), loanId, null, loginName, 1, 0.1, false, new Date(), Source.WEB, null);
            model.setStatus(InvestStatus.SUCCESS);
            investMapper.create(model);
        }
    }

    @Before
    public void setup() throws Exception {
        long loanId = IdGenerator.generate();
        LOAN_ID = loanId;
        createUserByUserId("testuser");
        createLoanByUserId("testuser", loanId);
        createInvests("testuser", loanId);
    }

    @Test
    public void shouldEstimateInvestIncomeIsOk() {
        String loginName = "testExtraRate";
        long loanId = IdGenerator.generate();
        createUserByUserId(loginName);
        createLoanByUserId(loginName, loanId);
        List<ExtraLoanRateModel> extraLoanRateModels = createExtraLoanRate(loanId);
        extraLoanRateMapper.create(extraLoanRateModels);

        loanDetailsMapper.create(createLoanDetails(loanId));

        long amount = investService.estimateInvestIncome(loanId, 0.1, loginName, 100000, new Date());
        assertNotNull(amount);
        amount = investService.estimateInvestIncome(loanId, 0.1, loginName, 1000000, new Date());
        assertNotNull(amount);
        amount = investService.estimateInvestIncome(loanId, 0.1, loginName, 5000000, new Date());
        assertNotNull(amount);
    }

    private List<ExtraLoanRateModel> createExtraLoanRate(long loanId) {
        ExtraLoanRateModel model = new ExtraLoanRateModel();
        model.setLoanId(loanId);
        model.setExtraRateRuleId(100001);
        model.setCreatedTime(new Date());
        model.setMinInvestAmount(100000);
        model.setMaxInvestAmount(1000000);
        model.setRate(0.1);
        ExtraLoanRateModel model2 = new ExtraLoanRateModel();
        model2.setLoanId(loanId);
        model2.setExtraRateRuleId(100001);
        model2.setCreatedTime(new Date());
        model2.setMinInvestAmount(1000000);
        model2.setMaxInvestAmount(5000000);
        model2.setRate(0.3);
        ExtraLoanRateModel model3 = new ExtraLoanRateModel();
        model3.setLoanId(loanId);
        model3.setExtraRateRuleId(100001);
        model3.setCreatedTime(new Date());
        model3.setMinInvestAmount(5000000);
        model3.setMaxInvestAmount(0);
        model3.setRate(0.5);
        List<ExtraLoanRateModel> list = Lists.newArrayList();
        list.add(model);
        list.add(model2);
        list.add(model3);
        return list;
    }

    private LoanDetailsModel createLoanDetails(long loanId) {
        LoanDetailsModel loanDetailsModel = new LoanDetailsModel();
        loanDetailsModel.setId(IdGenerator.generate());
        loanDetailsModel.setDeclaration("声明材料");
        loanDetailsModel.setExtraSource(Lists.newArrayList(Source.WEB));
        loanDetailsModel.setLoanId(loanId);
        return loanDetailsModel;
    }

    @Test
    public void testCalculateMembershipPreference() throws Exception {
        UserMembershipModel userMembershipModel0 = new UserMembershipModel("testUser", membershipMapper.findByLevel(0).getId(), DateTime.parse("2099-06-30T01:20").toDate(), UserMembershipType.GIVEN);
        userMembershipModel0.setCreatedTime(new DateTime().minusDays(1).toDate());
        userMembershipMapper.create(userMembershipModel0);
        assertEquals(0, investService.calculateMembershipPreference("testUser", LOAN_ID, Lists.newArrayList(10000L), 10000L, Source.WEB));

        UserMembershipModel userMembershipModel1 = new UserMembershipModel("testUser", membershipMapper.findByLevel(1).getId(), DateTime.parse("2099-06-30T01:20").toDate(), UserMembershipType.GIVEN);
        userMembershipModel1.setCreatedTime(new DateTime().minusDays(1).toDate());
        userMembershipMapper.create(userMembershipModel1);
        assertEquals(0, investService.calculateMembershipPreference("testUser", LOAN_ID, Lists.newArrayList(10000L), 10000L, Source.WEB));

        UserMembershipModel userMembershipModel2 = new UserMembershipModel("testUser", membershipMapper.findByLevel(2).getId(), DateTime.parse("2099-06-30T01:20").toDate(), UserMembershipType.GIVEN);
        userMembershipModel2.setCreatedTime(new DateTime().minusDays(1).toDate());
        userMembershipMapper.create(userMembershipModel2);
        assertEquals(1, investService.calculateMembershipPreference("testUser", LOAN_ID, Lists.newArrayList(10000L), 10000L, Source.WEB));

        UserMembershipModel userMembershipModel3 = new UserMembershipModel("testUser", membershipMapper.findByLevel(3).getId(), DateTime.parse("2099-06-30T01:20").toDate(), UserMembershipType.GIVEN);
        userMembershipModel3.setCreatedTime(new DateTime().minusDays(1).toDate());
        userMembershipMapper.create(userMembershipModel3);
        assertEquals(2, investService.calculateMembershipPreference("testUser", LOAN_ID, Lists.newArrayList(10000L), 10000L, Source.WEB));

        UserMembershipModel userMembershipModel4 = new UserMembershipModel("testUser", membershipMapper.findByLevel(4).getId(), DateTime.parse("2099-06-30T01:20").toDate(), UserMembershipType.GIVEN);
        userMembershipModel4.setCreatedTime(new DateTime().minusDays(1).toDate());
        userMembershipMapper.create(userMembershipModel4);
        assertEquals(2, investService.calculateMembershipPreference("testUser", LOAN_ID, Lists.newArrayList(10000L), 10000L, Source.WEB));

        UserMembershipModel userMembershipModel5 = new UserMembershipModel("testUser", membershipMapper.findByLevel(5).getId(), DateTime.parse("2099-06-30T01:20").toDate(), UserMembershipType.GIVEN);
        userMembershipModel5.setCreatedTime(new DateTime().minusDays(1).toDate());
        userMembershipMapper.create(userMembershipModel5);
        extraLoanRateMapper.create(createExtraLoanRate(LOAN_ID));
        loanDetailsMapper.create(createLoanDetails(LOAN_ID));
        long expectedInterest = investService.calculateMembershipPreference("testUser", LOAN_ID, Lists.newArrayList(10000L), 1000000L, Source.WEB);
        assertEquals(505, expectedInterest);
    }
}
