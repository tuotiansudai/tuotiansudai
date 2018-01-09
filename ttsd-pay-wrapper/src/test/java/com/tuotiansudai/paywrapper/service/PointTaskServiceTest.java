package com.tuotiansudai.paywrapper.service;

import com.google.common.base.Strings;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.point.repository.mapper.PointTaskMapper;
import com.tuotiansudai.point.repository.mapper.UserPointTaskMapper;
import com.tuotiansudai.point.repository.model.PointBillModel;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.point.repository.model.UserPointTaskModel;
import com.tuotiansudai.point.service.PointTaskService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:dispatcher-servlet.xml"})
@Transactional
public class PointTaskServiceTest {

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PointTaskService pointTaskService;

    @Autowired
    private UserPointTaskMapper userPointTaskMapper;

    @Autowired
    private PointTaskMapper pointTaskMapper;

    @Autowired
    private PointBillMapper pointBillMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Test
    public void shouldCompletedEachSumInvestTask() {
        String loginName = "eachSumInvestor";
        this.createFakeUser(loginName, null);
        LoanModel fakeLoan = this.createFakeLoan(ProductType._30);

        this.createFakeInvest(loginName, fakeLoan.getId(), 1);
        pointTaskService.completeAdvancedTask(PointTask.EACH_SUM_INVEST, loginName);

        long maxTaskLevel = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, PointTask.EACH_SUM_INVEST);
        assertThat(maxTaskLevel, is(0L));

        this.createFakeInvest(loginName, fakeLoan.getId(), 100000000);

        pointTaskService.completeAdvancedTask(PointTask.EACH_SUM_INVEST, loginName);

        maxTaskLevel = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, PointTask.EACH_SUM_INVEST);
        assertThat(maxTaskLevel, is(1L));

        List<PointBillModel> pointBillModels = pointBillMapper.findByLoginName(loginName);
        assertThat(pointBillModels.size(), is(1));

        assertThat(pointBillModels.get(0).getBusinessType(), is(PointBusinessType.TASK));
        assertThat(pointBillModels.get(0).getPoint(), is(100L));
    }

    @Test
    public void shouldCompletedEachSingleInvestTask() {
        String loginName = "eachSingleInvestor";
        this.createFakeUser(loginName, null);
        LoanModel fakeLoan = this.createFakeLoan(ProductType._30);
        this.createFakeInvest(loginName, fakeLoan.getId(), 50000000L);

        pointTaskService.completeAdvancedTask(PointTask.FIRST_SINGLE_INVEST, loginName);

        this.createFakeInvest(loginName, fakeLoan.getId(), 50000000L);

        pointTaskService.completeAdvancedTask(PointTask.FIRST_SINGLE_INVEST, loginName);

        long maxTaskLevel = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, PointTask.FIRST_SINGLE_INVEST);
        assertThat(maxTaskLevel, is(1L));

        List<PointBillModel> pointBillModels = pointBillMapper.findByLoginName(loginName);
        assertThat(pointBillModels.size(), is(1));

        assertThat(pointBillModels.get(0).getBusinessType(), is(PointBusinessType.TASK));
        assertThat(pointBillModels.get(0).getPoint(), is(200L));
    }

    @Test
    public void shouldCompletedEachRecommendTask() throws Exception {
        UserModel referrer = this.createFakeUser("eachRecommendReferrer", null);
        UserModel newbie1 = this.createFakeUser("newbie1", referrer.getLoginName());


        pointTaskService.completeAdvancedTask(PointTask.EACH_RECOMMEND_REGISTER, newbie1.getLoginName());

        assertThat(userPointTaskMapper.findMaxTaskLevelByLoginName(referrer.getLoginName(), PointTask.EACH_RECOMMEND_REGISTER), is(1L));

        UserModel newbie2 = this.createFakeUser("newbie2", referrer.getLoginName());

        pointTaskService.completeAdvancedTask(PointTask.EACH_RECOMMEND_REGISTER, newbie2.getLoginName());

        assertThat(userPointTaskMapper.findMaxTaskLevelByLoginName(referrer.getLoginName(), PointTask.EACH_RECOMMEND_REGISTER), is(2L));
    }

    @Test
    public void shouldCompletedFirstReferrerInvestTask() throws Exception {
        LoanModel fakeLoan = this.createFakeLoan(ProductType._30);
        UserModel referrer = this.createFakeUser("referrer", null);
        UserModel newbie1 = this.createFakeUser("newbie1", referrer.getLoginName());
        this.createFakeInvest(newbie1.getLoginName(), fakeLoan.getId(), 1L);

        pointTaskService.completeAdvancedTask(PointTask.FIRST_REFERRER_INVEST, newbie1.getLoginName());

        assertThat(userPointTaskMapper.findMaxTaskLevelByLoginName(referrer.getLoginName(), PointTask.FIRST_REFERRER_INVEST), is(1L));

        UserModel newbie2 = this.createFakeUser("newbie2", referrer.getLoginName());
        this.createFakeInvest(newbie2.getLoginName(), fakeLoan.getId(), 2L);

        pointTaskService.completeAdvancedTask(PointTask.FIRST_REFERRER_INVEST, newbie2.getLoginName());

        assertThat(userPointTaskMapper.findMaxTaskLevelByLoginName(referrer.getLoginName(), PointTask.FIRST_REFERRER_INVEST), is(1L));
    }

    @Test
    public void shouldCompletedFirstInvest180InvestTask() {
        String loginName = "firstInvest180Investor";
        this.createFakeUser(loginName, null);
        LoanModel fakeLoan = this.createFakeLoan(ProductType._180);
        this.createFakeInvest(loginName, fakeLoan.getId(), 1L);

        pointTaskService.completeAdvancedTask(PointTask.FIRST_INVEST_180, loginName);

        assertThat(userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, PointTask.FIRST_INVEST_180), is(1L));

        this.createFakeInvest(loginName, fakeLoan.getId(), 1L);

        assertThat(userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, PointTask.FIRST_INVEST_180), is(1L));

        List<PointBillModel> pointBillModels = pointBillMapper.findByLoginName(loginName);
        assertThat(pointBillModels.size(), is(1));
        assertThat(pointBillModels.get(0).getBusinessType(), is(PointBusinessType.TASK));
        assertThat(pointBillModels.get(0).getPoint(), is(50L));
    }

    @Test
    public void shouldCompletedFirstInvest360InvestTask() {
        String loginName = "firstInvest360investor";
        this.createFakeUser(loginName, null);
        LoanModel fakeLoan = this.createFakeLoan(ProductType._360);
        this.createFakeInvest(loginName, fakeLoan.getId(), 1L);

        pointTaskService.completeAdvancedTask(PointTask.FIRST_INVEST_360, loginName);

        assertThat(userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, PointTask.FIRST_INVEST_360), is(1L));

        this.createFakeInvest(loginName, fakeLoan.getId(), 1L);

        assertThat(userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, PointTask.FIRST_INVEST_360), is(1L));

        List<PointBillModel> pointBillModels = pointBillMapper.findByLoginName(loginName);
        assertThat(pointBillModels.size(), is(1));
        assertThat(pointBillModels.get(0).getBusinessType(), is(PointBusinessType.TASK));
        assertThat(pointBillModels.get(0).getPoint(), is(200L));
    }

    @Test
    public void shouldEachRecommendRegisterIsOk() {
        UserModel referrerUserModel = createFakeUser("referrerLoginName", null);
        UserModel testName = createFakeUser("testName", referrerUserModel.getLoginName());

        pointTaskService.completeAdvancedTask(PointTask.EACH_RECOMMEND_REGISTER, testName.getLoginName());

        List<UserPointTaskModel> userPointTaskModels = userPointTaskMapper.findByLoginName(referrerUserModel.getLoginName());
        Optional<UserPointTaskModel> completeTask = userPointTaskModels.stream().findFirst().filter(userPointTaskModel -> pointTaskMapper.findById(userPointTaskModel.getPointTaskId()).getName().equals(PointTask.EACH_RECOMMEND_REGISTER));
        assertTrue(completeTask.isPresent());
        assertEquals(completeTask.get().getPoint(), 100l);

        pointTaskService.completeAdvancedTask(PointTask.EACH_RECOMMEND_REGISTER, testName.getLoginName());
        userPointTaskModels = userPointTaskMapper.findByLoginName(referrerUserModel.getLoginName());
        completeTask = userPointTaskModels.stream().findFirst().filter(userPointTaskModel -> pointTaskMapper.findById(userPointTaskModel.getPointTaskId()).getName().equals(PointTask.EACH_RECOMMEND_REGISTER));
        assertTrue(completeTask.isPresent());
        assertEquals(completeTask.get().getPoint(), 100l);
    }

    @Test
    public void shouldFirstInvestReferrerCompleteEachRecommendInvestRewardIsOk() {
        UserModel referrerUserModel = createFakeUser("referrerLoginName", null);
        UserModel testName = createFakeUser("testName", referrerUserModel.getLoginName());
        LoanModel loanModel = createFakeLoan(ProductType._180);
        createFakeInvest(testName.getLoginName(), loanModel.getId(), 10l);

        pointTaskService.completeAdvancedTask(PointTask.EACH_RECOMMEND_INVEST, testName.getLoginName());

        List<UserPointTaskModel> userPointTaskModels = userPointTaskMapper.findByLoginName(referrerUserModel.getLoginName());
        Optional<UserPointTaskModel> completeTask = userPointTaskModels.stream().findFirst().filter(userPointTaskModel -> pointTaskMapper.findById(userPointTaskModel.getPointTaskId()).getName().equals(PointTask.EACH_RECOMMEND_INVEST));
        assertTrue(completeTask.isPresent());
        assertEquals(completeTask.get().getPoint(), 200l);
    }

    @Test
    public void shouldSecondInvestReferrerCompleteEachRecommendInvestRewardIsOk() {
        UserModel referrerUserModel = createFakeUser("referrerLoginName", null);
        UserModel testName = createFakeUser("testName", referrerUserModel.getLoginName());
        LoanModel loanModel = createFakeLoan(ProductType._180);
        createFakeInvest(testName.getLoginName(), loanModel.getId(), 10l);
        createFakeInvest(testName.getLoginName(), loanModel.getId(), 110l);

        pointTaskService.completeAdvancedTask(PointTask.EACH_RECOMMEND_INVEST, testName.getLoginName());

        List<UserPointTaskModel> userPointTaskModels = userPointTaskMapper.findByLoginName(referrerUserModel.getLoginName());
        Optional<UserPointTaskModel> completeTask = userPointTaskModels.stream().findFirst().filter(userPointTaskModel -> pointTaskMapper.findById(userPointTaskModel.getPointTaskId()).getName().equals(PointTask.EACH_RECOMMEND_INVEST));
        assertTrue(!completeTask.isPresent());
    }

    @Test
    public void shouldReferrerIsNullCompleteTaskIsOk() {
        UserModel referrerUserModel = createFakeUser("referrerLoginName", null);
        UserModel testName = createFakeUser("testName", null);
        LoanModel loanModel = createFakeLoan(ProductType._180);
        createFakeInvest(testName.getLoginName(), loanModel.getId(), 10l);

        pointTaskService.completeAdvancedTask(PointTask.EACH_RECOMMEND_INVEST, testName.getLoginName());
        pointTaskService.completeAdvancedTask(PointTask.EACH_RECOMMEND, testName.getLoginName());
        pointTaskService.completeAdvancedTask(PointTask.EACH_RECOMMEND_REGISTER, testName.getLoginName());
        pointTaskService.completeAdvancedTask(PointTask.EACH_RECOMMEND_BANK_CARD, testName.getLoginName());
        pointTaskService.completeAdvancedTask(PointTask.FIRST_REFERRER_INVEST, testName.getLoginName());

        List<UserPointTaskModel> userPointTaskModels = userPointTaskMapper.findByLoginName(referrerUserModel.getLoginName());
        Optional<UserPointTaskModel> completeTask = userPointTaskModels.stream().findFirst().filter(userPointTaskModel -> pointTaskMapper.findById(userPointTaskModel.getPointTaskId()).getName().equals(PointTask.EACH_RECOMMEND_INVEST));
        assertTrue(!completeTask.isPresent());
        completeTask = userPointTaskModels.stream().findFirst().filter(userPointTaskModel -> pointTaskMapper.findById(userPointTaskModel.getPointTaskId()).getName().equals(PointTask.EACH_RECOMMEND));
        assertTrue(!completeTask.isPresent());
        completeTask = userPointTaskModels.stream().findFirst().filter(userPointTaskModel -> pointTaskMapper.findById(userPointTaskModel.getPointTaskId()).getName().equals(PointTask.EACH_RECOMMEND_BANK_CARD));
        assertTrue(!completeTask.isPresent());
        completeTask = userPointTaskModels.stream().findFirst().filter(userPointTaskModel -> pointTaskMapper.findById(userPointTaskModel.getPointTaskId()).getName().equals(PointTask.FIRST_REFERRER_INVEST));
        assertTrue(!completeTask.isPresent());

    }

    @Test
    public void shouldEachRecommendReferrerBankCardIsOk() {
        UserModel referrerUserModel = createFakeUser("referrerCardLoginName", null);
        UserModel testName = createFakeUser("testCardLoginName", referrerUserModel.getLoginName());

        BankCardModel bankCardModel = new BankCardModel();
        bankCardModel.setBankCode("ICBC");
        bankCardModel.setStatus(BankCardStatus.PASSED);
        bankCardModel.setCreatedTime(new Date());
        bankCardModel.setLoginName(testName.getLoginName());
        bankCardModel.setIsFastPayOn(true);
        bankCardModel.setCardNumber("1234567890");
        bankCardMapper.create(bankCardModel);

        pointTaskService.completeNewbieTask(PointTask.BIND_BANK_CARD, testName.getLoginName());
        List<UserPointTaskModel> userPointTaskModels = userPointTaskMapper.findByLoginName(referrerUserModel.getLoginName());
        Optional<UserPointTaskModel> completeTask = userPointTaskModels.stream().findFirst().filter(userPointTaskModel -> pointTaskMapper.findById(userPointTaskModel.getPointTaskId()).getName().equals(PointTask.EACH_RECOMMEND_BANK_CARD));
        assertTrue(completeTask.isPresent());
        assertEquals(completeTask.get().getPoint(), 100l);
    }

    @Test
    public void shouldEachRecommendReferrerBankCardReferrerIsNullIsOk() {
        UserModel referrerUserModel = createFakeUser("referrerBankCardLoginName", null);
        UserModel testName = createFakeUser("testBankCardLoginName", null);

        BankCardModel bankCardModel = new BankCardModel();
        bankCardModel.setBankCode("ICBC");
        bankCardModel.setStatus(BankCardStatus.PASSED);
        bankCardModel.setCreatedTime(new Date());
        bankCardModel.setLoginName(testName.getLoginName());
        bankCardModel.setIsFastPayOn(true);
        bankCardModel.setCardNumber("1234567890");
        bankCardMapper.create(bankCardModel);

        pointTaskService.completeNewbieTask(PointTask.BIND_BANK_CARD, testName.getLoginName());
        List<UserPointTaskModel> userPointTaskModels = userPointTaskMapper.findByLoginName(referrerUserModel.getLoginName());
        Optional<UserPointTaskModel> completeTask = userPointTaskModels.stream().findFirst().filter(userPointTaskModel -> pointTaskMapper.findById(userPointTaskModel.getPointTaskId()).getName().equals(PointTask.EACH_RECOMMEND_BANK_CARD));
        assertTrue(!completeTask.isPresent());
    }


    private InvestModel createFakeInvest(String loginName, long loanId, long amount) {
        InvestModel investModel = new InvestModel();
        investModel.setId(IdGenerator.generate());
        investModel.setLoanId(loanId);
        investModel.setLoginName(loginName);
        investModel.setAmount(amount);
        investModel.setStatus(InvestStatus.SUCCESS);
        investModel.setTransferStatus(TransferStatus.TRANSFERABLE);
        investModel.setSource(Source.WEB);
        investMapper.create(investModel);
        return investModel;
    }

    private UserModel createFakeUser(String loginName, String referrer) {
        UserModel fakeUser = new UserModel();
        fakeUser.setLoginName(loginName);
        fakeUser.setPassword("123abc");
        fakeUser.setEmail("12345@abc.com");
        fakeUser.setMobile(RandomStringUtils.randomNumeric(11));
        fakeUser.setRegisterTime(new Date());
        fakeUser.setUserName("userName1");
        if (!Strings.isNullOrEmpty(referrer)) {
            fakeUser.setReferrer(referrer);
        }
        fakeUser.setStatus(UserStatus.ACTIVE);
        fakeUser.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(fakeUser);
        AccountModel accountModel = new AccountModel(fakeUser.getLoginName(), "", "", new Date());
        accountMapper.create(accountModel);
        return fakeUser;
    }

    private LoanModel createFakeLoan(ProductType productType) {
        UserModel loaner = this.createFakeUser("loaner", null);
        LoanDto loanDto = new LoanDto();
        loanDto.setId(IdGenerator.generate());
        loanDto.setLoanerLoginName("loaner");
        loanDto.setLoanerUserName("借款人");
        loanDto.setLoanerIdentityNumber("id");
        loanDto.setAgentLoginName("loaner");
        loanDto.setBasicRate("16.00");
        loanDto.setProjectName("店铺资金周转");
        loanDto.setActivityRate("12");
        loanDto.setShowOnHome(true);
        loanDto.setPeriods(1);
        loanDto.setActivityType(ActivityType.NORMAL);
        loanDto.setContractId(123);
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("1000");
        loanDto.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.RAISING);
        loanDto.setProductType(productType);
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setAgentLoginName(loaner.getLoginName());
        loanModel.setPledgeType(PledgeType.HOUSE);
        loanMapper.create(loanModel);
        return loanModel;
    }

}
