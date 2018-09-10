package com.tuotiansudai.repository.mapper;


import com.google.common.collect.Lists;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class InvestMapperTest {
    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;
    @Autowired
    private LoanRepayMapper loanRepayMapper;
    @Autowired
    private TransferApplicationMapper transferApplicationMapper;
    @Autowired
    private BankAccountMapper bankAccountMapper;
    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Value("#{'${web.heroRanking.activity.period}'.split('\\~')}")
    private List<String> heroRankingActivityPeriod;

    private String User_ID = "helloworld1";
    private String User_ID2 = "fakeInvestUser1";
    private long Loan_ID = 200093023L;
    private long Loan_ID2 = 300093023L;
    private long Loan_ID3 = 400093023L;

    @Test
    public void shouldCreateInvest() throws Exception {
        InvestModel investModel = this.getFakeInvestModel();
        investMapper.create(investModel);

        InvestModel dbModel = investMapper.findById(investModel.getId());
        assertNotNull(dbModel);
    }

    @Test
    public void shouldUpdateInvestStatus() {
        InvestModel investModel = this.getFakeInvestModel();
        investModel.setStatus(InvestStatus.WAIT_PAY);
        investMapper.create(investModel);

        investModel.setStatus(InvestStatus.SUCCESS);
        investMapper.update(investModel);
        InvestModel investModel1 = investMapper.findById(investModel.getId());
        assertEquals(investModel1.getStatus(), InvestStatus.SUCCESS);

        investModel.setStatus(InvestStatus.FAIL);
        investMapper.update(investModel);
        InvestModel investModel2 = investMapper.findById(investModel.getId());
        assertEquals(investModel2.getStatus(), InvestStatus.FAIL);
    }

    private void createTestInvests() {
        for (int i = 0; i < 15; i++) {
            InvestModel investModel = this.getFakeInvestModel();
            if (i < 5) {
                investModel.setLoanId(Loan_ID2);
            } else if (i < 10) {
                investModel.setLoanId(Loan_ID3);
            }
            if (i % 2 == 1) {
                investModel.setLoginName(User_ID2);
            }
            investModel.setInvestTime(DateUtils.addHours(new Date(), -i));
            investMapper.create(investModel);
        }
    }

    private InvestModel getFakeInvestModel() {
        InvestModel model = new InvestModel(IdGenerator.generate(), Loan_ID, null, User_ID, 1000000L, 0.1, false, new DateTime().withTimeAtStartOfDay().toDate(), Source.WEB, null);
        model.setStatus(InvestStatus.SUCCESS);
        model.setTradingTime(new DateTime().withTimeAtStartOfDay().toDate());
        model.setCreatedTime(new DateTime().withTimeAtStartOfDay().toDate());
        return model;
    }

    private InvestModel getFakeInvestModelByLoginName(String loginName) {
        InvestModel model = new InvestModel(IdGenerator.generate(), Loan_ID, null, loginName, 1000000L, 0.1, false, new DateTime().withTimeAtStartOfDay().toDate(), Source.WEB, null);
        model.setStatus(InvestStatus.SUCCESS);
        model.setTradingTime(new DateTime().withTimeAtStartOfDay().toDate());
        return model;
    }

    @Before
    public void createLoan() {
        createLoan(User_ID, Loan_ID, ActivityType.NORMAL);
        createLoan(User_ID2, Loan_ID2, ActivityType.NORMAL);
        createLoan(User_ID2, Loan_ID3, ActivityType.NORMAL, LoanStatus.CANCEL);
        assertNotNull(loanMapper.findById(Loan_ID));
    }

    private void createLoan(String userId, long loanId, ActivityType activityType) {
        createLoan(userId, loanId, activityType, LoanStatus.WAITING_VERIFY);
    }

    private void createLoan(String userId, long loanId, ActivityType activityType, LoanStatus loanStatus) {
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
        loanDto.setActivityType(activityType);
        loanDto.setContractId(123);
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("10000");
        loanDto.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setProductType(ProductType._30);
        loanDto.setPledgeType(PledgeType.HOUSE);
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setStatus(loanStatus);
        loanMapper.create(loanModel);
    }

    @Before
    public void createUser() throws Exception {
        createUserByUserId(User_ID);
        createUserByUserId(User_ID2);
    }

    private UserModel createUserByUserId(String userId) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(userId);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("1" + RandomStringUtils.randomNumeric(10));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
        return userModelTest;
    }

    @Test
    public void shouldSumSuccessInvestAmount() {
        InvestModel investModel1 = getFakeInvestModel();
        investModel1.setStatus(InvestStatus.SUCCESS);
        InvestModel investModel2 = getFakeInvestModel();
        investModel2.setStatus(InvestStatus.FAIL);

        InvestModel investModel3 = getFakeInvestModel();
        investModel3.setStatus(InvestStatus.WAIT_PAY);

        investMapper.create(investModel1);
        investMapper.create(investModel2);
        investMapper.create(investModel3);

        long result = investMapper.sumSuccessInvestAmount(Loan_ID);

        assertEquals(1000000L, result);
    }


    @Test
    public void shouldSumSuccessInvestCount() {
        long newbieLoanId = IdGenerator.generate();
        createLoan(User_ID, newbieLoanId, ActivityType.NEWBIE);

        InvestModel investModel = this.getFakeInvestModel();
        investModel.setLoanId(newbieLoanId);
        investModel.setLoginName(User_ID2);
        investModel.setInvestTime(DateUtils.addHours(new Date(), -1));
        investMapper.create(investModel);

        InvestModel investModel2 = this.getFakeInvestModel();
        investModel2.setLoanId(Loan_ID2);
        investModel2.setLoginName(User_ID2);
        investModel2.setInvestTime(DateUtils.addHours(new Date(), -2));
        investMapper.create(investModel2);

        InvestModel investModel3 = this.getFakeInvestModel();
        investModel3.setLoanId(newbieLoanId);
        investModel3.setLoginName(User_ID2);
        investModel3.setInvestTime(DateUtils.addHours(new Date(), -3));
        investMapper.create(investModel3);

        int newbieInvestCount = investMapper.sumSuccessInvestCountByLoginName(User_ID2);
        assert newbieInvestCount == 3;
    }

    @Test
    public void shouldFindByLoginName() {
        createTestInvests();
        List<InvestModel> investModelList = investMapper.findPaginationByLoginName(User_ID2, 0, Integer.MAX_VALUE);
        assertEquals(4, investModelList.size());
        long investCount = investMapper.findCountByLoginName(User_ID2);
        assertEquals(4, investCount);
    }

    @Test
    public void shouldHasSuccessInvest() throws Exception {
        InvestModel fakeInvestModel = getFakeInvestModel();
        fakeInvestModel.setStatus(InvestStatus.SUCCESS);
        investMapper.create(fakeInvestModel);
        long amount = investMapper.sumSuccessInvestAmountByLoginName(null, User_ID, true);
        assertTrue(amount > 0);
    }

    @Test
    public void shouldHasNoSuccessInvest() throws Exception {
        long amount = investMapper.sumSuccessInvestAmountByLoginName(null, User_ID, true);
        assertTrue(amount == 0);
    }

    @Test
    public void shouldGetInvestDetail() throws Exception {
        List<InvestDataView> investDataViews = investMapper.getInvestDetail();
        assertTrue(investDataViews.size() >= 0);
    }

    @Test
    public void shouldNotFindNonTransferableWebTransferAbleApplicationPaginationByLoginNameIsSuccess() {
        long loanId = IdGenerator.generate();
        UserModel investorModel = createUser("investorModelRound5Test");
        UserModel loanerModel = createUser("loanerModelRound5Test");
        LoanModel loanModel = createLoanByUserId(loanerModel.getLoginName(), loanId, LoanStatus.REPAYING);
        createLoanDetailsByLoanId(loanModel);
        InvestModel investModel = createInvest(investorModel.getLoginName(), loanId, InvestStatus.SUCCESS, TransferStatus.TRANSFERABLE);
        LoanRepayModel loanRepayModel = getFakeLoanRepayModel(loanModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        loanRepayMapper.create(Lists.newArrayList(loanRepayModel));
        InvestRepayModel investRepayModel = getFakeInvestRepayModel(investModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        investRepayMapper.create(Lists.newArrayList(investRepayModel));
        List<TransferableInvestView> transferableInvestViews = investMapper.findWebTransferableApplicationPaginationByLoginName(investorModel.getLoginName(), 0, 10);
        assertEquals(1, transferableInvestViews.size());
        LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loanModel.getId());
        loanDetailsModel.setNonTransferable(true);
        loanDetailsModel.setDisableCoupon(true);
        loanDetailsMapper.updateByLoanId(loanDetailsModel);
        List<TransferableInvestView> transferableInvestViews1 = investMapper.findWebTransferableApplicationPaginationByLoginName(investorModel.getLoginName(), 0, 10);
        assertEquals(0, transferableInvestViews1.size());

    }

    @Test
    public void shouldFindWebTransferAbleApplicationPaginationByLoginNameIsSuccess() {
        long loanId = IdGenerator.generate();
        UserModel investorModel = createUser("investorModelRound5Test");
        UserModel loanerModel = createUser("loanerModelRound5Test");
        LoanModel loanModel = createLoanByUserId(loanerModel.getLoginName(), loanId, LoanStatus.REPAYING);
        createLoanDetailsByLoanId(loanModel);
        InvestModel investModel = createInvest(investorModel.getLoginName(), loanId, InvestStatus.SUCCESS, TransferStatus.TRANSFERABLE);
        LoanRepayModel loanRepayModel = getFakeLoanRepayModel(loanModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        loanRepayMapper.create(Lists.newArrayList(loanRepayModel));
        InvestRepayModel investRepayModel = getFakeInvestRepayModel(investModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        investRepayMapper.create(Lists.newArrayList(investRepayModel));
        List<TransferableInvestView> transferableInvestViews = investMapper.findWebTransferableApplicationPaginationByLoginName(investorModel.getLoginName(), 0, 10);
        assertEquals(1, transferableInvestViews.size());
        assertEquals(investModel.getId(), transferableInvestViews.get(0).getInvestId());
        assertEquals(investModel.getLoanId(), transferableInvestViews.get(0).getLoanId());
        assertEquals(loanModel.getName(), transferableInvestViews.get(0).getLoanName());
        assertEquals(investModel.getAmount(), transferableInvestViews.get(0).getAmount());
        assertEquals(investRepayModel.getExpectedInterest() + investRepayModel.getCorpus() - investRepayModel.getExpectedFee(), transferableInvestViews.get(0).getNextRepayAmount());
        assertEquals(new DateTime(loanRepayModel.getRepayDate()).withTimeAtStartOfDay(), new DateTime(transferableInvestViews.get(0).getNextRepayDate()).withTimeAtStartOfDay());

    }

    @Test
    public void shouldFindCountWebTransferableApplicationPaginationByLoginNameIsSuccess() {
        long loanId = IdGenerator.generate();
        UserModel investorModel = createUser("investorModelRound5Test");
        UserModel loanerModel = createUser("loanerModelRound5Test");
        LoanModel loanModel = createLoanByUserId(loanerModel.getLoginName(), loanId, LoanStatus.REPAYING);
        createLoanDetailsByLoanId(loanModel);
        InvestModel investModel = createInvest(investorModel.getLoginName(), loanId, InvestStatus.SUCCESS, TransferStatus.TRANSFERABLE);
        LoanRepayModel loanRepayModel = getFakeLoanRepayModel(loanModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        loanRepayMapper.create(Lists.newArrayList(loanRepayModel));
        InvestRepayModel investRepayModel = getFakeInvestRepayModel(investModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        investRepayMapper.create(Lists.newArrayList(investRepayModel));
        long count = investMapper.findWebCountTransferableApplicationPaginationByLoginName(investorModel.getLoginName());
        assertEquals(1, count);
    }

    @Test
    public void shouldFindWebTransferableApplicationPaginationByLoginNameOverdueIsFail() {
        long loanId = IdGenerator.generate();
        UserModel investorModel = createUser("investorModelRound5Test");
        UserModel loanerModel = createUser("loanerModelRound5Test");
        LoanModel loanModel = createLoanByUserId(loanerModel.getLoginName(), loanId, LoanStatus.OVERDUE);
        createLoanDetailsByLoanId(loanModel);
        InvestModel investModel = createInvest(investorModel.getLoginName(), loanId, InvestStatus.SUCCESS, TransferStatus.TRANSFERABLE);
        LoanRepayModel loanRepayModel = getFakeLoanRepayModel(loanModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        loanRepayMapper.create(Lists.newArrayList(loanRepayModel));
        InvestRepayModel investRepayModel = getFakeInvestRepayModel(investModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        investRepayMapper.create(Lists.newArrayList(investRepayModel));
        List<TransferableInvestView> transferableInvestViews = investMapper.findWebTransferableApplicationPaginationByLoginName(investorModel.getLoginName(), 0, 10);
        assertEquals(0, transferableInvestViews.size());
    }

    @Test
    public void shouldFindWebTransferableApplicationPaginationByLoginNameInvestTransferStatusCancelIsFail() {
        long loanId = IdGenerator.generate();
        UserModel investorModel = createUser("investorModelRound5Test");
        UserModel loanerModel = createUser("loanerModelRound5Test");
        LoanModel loanModel = createLoanByUserId(loanerModel.getLoginName(), loanId, LoanStatus.REPAYING);
        createLoanDetailsByLoanId(loanModel);
        InvestModel investModel = createInvest(investorModel.getLoginName(), loanId, InvestStatus.SUCCESS, TransferStatus.CANCEL);
        LoanRepayModel loanRepayModel = getFakeLoanRepayModel(loanModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        loanRepayMapper.create(Lists.newArrayList(loanRepayModel));
        InvestRepayModel investRepayModel = getFakeInvestRepayModel(investModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        investRepayMapper.create(Lists.newArrayList(investRepayModel));
        List<TransferableInvestView> transferableInvestViews = investMapper.findWebTransferableApplicationPaginationByLoginName(investorModel.getLoginName(), 0, 10);
        assertEquals(0, transferableInvestViews.size());
    }

    @Test
    public void shouldFindWebTransferableApplicationPaginationByLoginNameTransferStatusCancelDiffDayIsSuccess() {
        long loanId = IdGenerator.generate();
        UserModel investorModel = createUser("investorModelRound5Test");
        UserModel loanerModel = createUser("loanerModelRound5Test");
        LoanModel loanModel = createLoanByUserId(loanerModel.getLoginName(), loanId, LoanStatus.REPAYING);
        createLoanDetailsByLoanId(loanModel);
        InvestModel investModel = createInvest(investorModel.getLoginName(), loanId, InvestStatus.SUCCESS, TransferStatus.TRANSFERABLE);
        TransferApplicationModel transferApplicationModel = getFakeTransferApplicationModel(investorModel.getLoginName(), TransferStatus.CANCEL, investModel.getId(), loanModel.getId());
        transferApplicationModel.setApplicationTime(new DateTime().minusDays(1).toDate());
        transferApplicationMapper.create(transferApplicationModel);
        LoanRepayModel loanRepayModel = getFakeLoanRepayModel(loanModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        loanRepayMapper.create(Lists.newArrayList(loanRepayModel));
        InvestRepayModel investRepayModel = getFakeInvestRepayModel(investModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        investRepayMapper.create(Lists.newArrayList(investRepayModel));
        List<TransferableInvestView> transferableInvestViews = investMapper.findWebTransferableApplicationPaginationByLoginName(investorModel.getLoginName(), 0, 10);
        assertEquals(1, transferableInvestViews.size());
    }

    @Test
    public void shouldFindWebTransferableApplicationPaginationByLoginNameTransferStatusCancelSameDayIsSuccess() {
        long loanId = IdGenerator.generate();
        UserModel investorModel = createUser("investorModelRound5Test");
        UserModel loanerModel = createUser("loanerModelRound5Test");
        LoanModel loanModel = createLoanByUserId(loanerModel.getLoginName(), loanId, LoanStatus.REPAYING);
        createLoanDetailsByLoanId(loanModel);
        InvestModel investModel = createInvest(investorModel.getLoginName(), loanId, InvestStatus.SUCCESS, TransferStatus.TRANSFERABLE);
        investModel.setTransferStatus(TransferStatus.CANCEL);
        investMapper.update(investModel);
        TransferApplicationModel transferApplicationModel = getFakeTransferApplicationModel(investorModel.getLoginName(), TransferStatus.CANCEL, investModel.getId(), loanModel.getId());
        transferApplicationModel.setApplicationTime(new Date());
        transferApplicationMapper.create(transferApplicationModel);
        LoanRepayModel loanRepayModel = getFakeLoanRepayModel(loanModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        loanRepayMapper.create(Lists.newArrayList(loanRepayModel));
        InvestRepayModel investRepayModel = getFakeInvestRepayModel(investModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        investRepayMapper.create(Lists.newArrayList(investRepayModel));
        List<TransferableInvestView> transferableInvestViews = investMapper.findWebTransferableApplicationPaginationByLoginName(investorModel.getLoginName(), 0, 10);
        assertEquals(0, transferableInvestViews.size());
    }

    @Test
    public void shouldFindWebTransferableApplicationPaginationByLoginNameLessLimitDayIsFail() {
        long loanId = IdGenerator.generate();
        UserModel investorModel = createUser("investorModelRound5Test");
        UserModel loanerModel = createUser("loanerModelRound5Test");
        LoanModel loanModel = createLoanByUserId(loanerModel.getLoginName(), loanId, LoanStatus.REPAYING);
        createLoanDetailsByLoanId(loanModel);
        InvestModel investModel = createInvest(investorModel.getLoginName(), loanId, InvestStatus.SUCCESS, TransferStatus.TRANSFERABLE);
        LoanRepayModel loanRepayModel = getFakeLoanRepayModel(loanModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(1).toDate(), new DateTime().plusDays(1).toDate(), 1000l, 2000l, 3000l, 4000l);
        loanRepayMapper.create(Lists.newArrayList(loanRepayModel));
        InvestRepayModel investRepayModel = getFakeInvestRepayModel(investModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(1).toDate(), new DateTime().plusDays(1).toDate(), 1000l, 2000l, 3000l, 4000l);
        investRepayMapper.create(Lists.newArrayList(investRepayModel));
        List<TransferableInvestView> transferableInvestViews = investMapper.findWebTransferableApplicationPaginationByLoginName(investorModel.getLoginName(), 0, 10);
        assertEquals(0, transferableInvestViews.size());
    }

    @Test
    public void shouldFindTransferableApplicationPaginationByLoginNameIsSuccess() {
        long loanId = IdGenerator.generate();
        UserModel investorModel = createUser("investorModelRound5Test");
        UserModel loanerModel = createUser("loanerModelRound5Test");
        LoanModel loanModel = createLoanByUserId(loanerModel.getLoginName(), loanId, LoanStatus.REPAYING);
        createLoanDetailsByLoanId(loanModel);
        InvestModel investModel = createInvest(investorModel.getLoginName(), loanId, InvestStatus.SUCCESS, TransferStatus.TRANSFERABLE);
        LoanRepayModel loanRepayModel = getFakeLoanRepayModel(loanModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        loanRepayMapper.create(Lists.newArrayList(loanRepayModel));
        InvestRepayModel investRepayModel = getFakeInvestRepayModel(investModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        investRepayMapper.create(Lists.newArrayList(investRepayModel));
        List<InvestModel> investModels = investMapper.findTransferableApplicationPaginationByLoginName(investorModel.getLoginName(), 0, 10);
        assertEquals(1, investModels.size());
        assertEquals(investModel.getId(), investModels.get(0).getId());
        assertNull(investModels.get(0).getTransferInvestId());
        assertEquals(investModel.getLoanId(), investModels.get(0).getLoanId());
        assertEquals(investModel.getAmount(), investModels.get(0).getAmount());
        assertEquals(investModel.getStatus(), investModels.get(0).getStatus());
        assertEquals(investModel.getTransferStatus(), investModels.get(0).getTransferStatus());

    }

    @Test
    public void shouldFindCountTransferAbleApplicationPaginationByLoginNameIsSuccess() {
        long loanId = IdGenerator.generate();
        UserModel investorModel = createUser("investorModelRound5Test");
        UserModel loanerModel = createUser("loanerModelRound5Test");
        LoanModel loanModel = createLoanByUserId(loanerModel.getLoginName(), loanId, LoanStatus.REPAYING);
        createLoanDetailsByLoanId(loanModel);
        InvestModel investModel = createInvest(investorModel.getLoginName(), loanId, InvestStatus.SUCCESS, TransferStatus.TRANSFERABLE);
        LoanRepayModel loanRepayModel = getFakeLoanRepayModel(loanModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        loanRepayMapper.create(Lists.newArrayList(loanRepayModel));
        InvestRepayModel investRepayModel = getFakeInvestRepayModel(investModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        investRepayMapper.create(Lists.newArrayList(investRepayModel));
        long count = investMapper.findCountTransferableApplicationPaginationByLoginName(investorModel.getLoginName());
        assertEquals(1, count);
    }

    private LoanRepayModel getFakeLoanRepayModel(LoanModel fakeLoanModel,
                                                 int period,
                                                 RepayStatus repayStatus,
                                                 Date repayDate,
                                                 Date actualRepayDate,
                                                 long corpus,
                                                 long expectedInterest,
                                                 long actualInterest,
                                                 long defaultInterest
    ) {
        LoanRepayModel fakeLoanRepayModel = new LoanRepayModel();
        fakeLoanRepayModel.setId(IdGenerator.generate());
        fakeLoanRepayModel.setPeriod(period);
        fakeLoanRepayModel.setStatus(repayStatus);
        fakeLoanRepayModel.setLoanId(fakeLoanModel.getId());
        fakeLoanRepayModel.setRepayDate(repayDate);
        fakeLoanRepayModel.setActualRepayDate(actualRepayDate);
        fakeLoanRepayModel.setCorpus(corpus);
        fakeLoanRepayModel.setExpectedInterest(expectedInterest);
        fakeLoanRepayModel.setActualInterest(actualInterest);
        fakeLoanRepayModel.setDefaultInterest(defaultInterest);
        return fakeLoanRepayModel;
    }

    private InvestRepayModel getFakeInvestRepayModel(InvestModel fakeInvestModel,
                                                     int period,
                                                     RepayStatus repayStatus,
                                                     Date repayDate,
                                                     Date actualRepayDate,
                                                     long corpus,
                                                     long expectedInterest,
                                                     long actualInterest,
                                                     long defaultInterest
    ) {
        InvestRepayModel fakeInvestRepayModel = new InvestRepayModel();
        fakeInvestRepayModel.setId(IdGenerator.generate());
        fakeInvestRepayModel.setPeriod(period);
        fakeInvestRepayModel.setStatus(repayStatus);
        fakeInvestRepayModel.setInvestId(fakeInvestModel.getId());
        fakeInvestRepayModel.setRepayDate(repayDate);
        fakeInvestRepayModel.setActualRepayDate(actualRepayDate);
        fakeInvestRepayModel.setCorpus(corpus);
        fakeInvestRepayModel.setExpectedInterest(expectedInterest);
        fakeInvestRepayModel.setActualInterest(actualInterest);
        fakeInvestRepayModel.setDefaultInterest(defaultInterest);
        return fakeInvestRepayModel;
    }

    private InvestModel createInvest(String loginName, long loanId, InvestStatus investStatus, TransferStatus transferStatus) {
        InvestModel model = new InvestModel(IdGenerator.generate(), loanId, null, loginName, 1, 0.1, false, new Date(), Source.WEB, null);
        model.setStatus(investStatus);
        model.setTransferStatus(transferStatus);
        model.setCreatedTime(new Date());
        investMapper.create(model);
        return model;
    }

    private UserModel createUser(String userId) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(userId);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("1" + RandomStringUtils.randomNumeric(10));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
        return userModelTest;
    }

    private LoanModel createLoanByUserId(String userId, long loanId, LoanStatus loanStatus) {
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
        loanDto.setType(LoanType.LOAN_INTEREST_MONTHLY_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setProductType(ProductType._180);
        loanDto.setLoanStatus(LoanStatus.REPAYING);
        loanDto.setRecheckTime(new DateTime().minusDays(10).toDate());
        loanDto.setPledgeType(PledgeType.HOUSE);
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setStatus(loanStatus);
        loanMapper.create(loanModel);
        return loanModel;
    }

    private LoanDetailsModel createLoanDetailsByLoanId(LoanModel loanModel) {
        LoanDetailsModel loanDetailsModel = new LoanDetailsModel();
        loanDetailsModel.setNonTransferable(false);
        loanDetailsModel.setDeclaration("declaration");
        loanDetailsModel.setActivity(false);
        loanDetailsModel.setActivityDesc("desc");
        loanDetailsModel.setLoanId(loanModel.getId());
        loanDetailsMapper.create(loanDetailsModel);
        return loanDetailsModel;

    }

    private TransferApplicationModel getFakeTransferApplicationModel(String loginName, TransferStatus transferStatus, long investId, long loanId) {
        TransferApplicationModel transferApplicationModel = new TransferApplicationModel();
        transferApplicationModel.setLoginName(loginName);
        transferApplicationModel.setName("name");
        transferApplicationModel.setTransferAmount(1000l);
        transferApplicationModel.setInvestAmount(1200l);
        transferApplicationModel.setTransferTime(new Date());
        transferApplicationModel.setStatus(transferStatus);
        transferApplicationModel.setTransferInvestId(investId);
        transferApplicationModel.setDeadline(new Date());
        transferApplicationModel.setApplicationTime(new Date());
        transferApplicationModel.setLoanId(loanId);
        return transferApplicationModel;
    }

    @Test
    public void shouldFindHeroRankingByTradingTimeIsSuccess() {
        UserModel investor1 = createUserByUserId("investor1");
        UserModel investor2 = createUserByUserId("investor2");
        UserModel investor3 = createUserByUserId("investor3");
        BankAccountModel accountModel1 = new BankAccountModel(investor1.getLoginName(), "payUserId1", "payAccountId1", "111", "111","");
        bankAccountMapper.createInvestor(accountModel1);
        BankAccountModel accountModel2 = new BankAccountModel(investor2.getLoginName(), "payUserId2", "payAccountId2", "111", "111","");
        bankAccountMapper.createInvestor(accountModel2);
        BankAccountModel accountModel3 = new BankAccountModel(investor3.getLoginName(), "payUserId3", "payAccountId3", "111", "111","");
        bankAccountMapper.createInvestor(accountModel3);

        InvestModel investModel1 = this.getFakeInvestModelByLoginName(investor1.getLoginName());
        investModel1.setAmount(2000);
        investModel1.setTradingTime(new DateTime("2016-07-05").toDate());
        investMapper.create(investModel1);
        InvestModel investModel2 = this.getFakeInvestModelByLoginName(investor2.getLoginName());
        investModel2.setAmount(1000);
        investModel2.setTradingTime(new DateTime("2016-07-05").toDate());
        investMapper.create(investModel2);
        InvestModel investModel3 = this.getFakeInvestModelByLoginName(investor3.getLoginName());
        investModel3.setAmount(3000);
        investModel3.setTradingTime(new DateTime("2016-07-05").toDate());
        investMapper.create(investModel3);

        List<HeroRankingView> heroRankingViews = investMapper.findHeroRankingByTradingTime(new DateTime("2016-07-05").toDate(), heroRankingActivityPeriod.get(0), heroRankingActivityPeriod.get(1));
        assertEquals(3, heroRankingViews.size());
        assertEquals(investModel3.getLoginName(), heroRankingViews.get(0).getLoginName());
        assertEquals(investModel3.getAmount(), heroRankingViews.get(0).getSumAmount());
        assertEquals(investor3.getUserName(), heroRankingViews.get(0).getUserName());
        assertEquals(investor3.getMobile(), heroRankingViews.get(0).getMobile());

        assertEquals(investModel1.getLoginName(), heroRankingViews.get(1).getLoginName());
        assertEquals(investModel1.getAmount(), heroRankingViews.get(1).getSumAmount());
        assertEquals(investor1.getUserName(), heroRankingViews.get(1).getUserName());
        assertEquals(investor1.getMobile(), heroRankingViews.get(1).getMobile());

        assertEquals(investModel2.getLoginName(), heroRankingViews.get(2).getLoginName());
        assertEquals(investModel2.getAmount(), heroRankingViews.get(2).getSumAmount());
        assertEquals(investor2.getUserName(), heroRankingViews.get(2).getUserName());
        assertEquals(investor2.getMobile(), heroRankingViews.get(2).getMobile());

    }

    @Test
    public void shouldFindHeroRankingByTradingTimeTransferIsSuccess() {
        UserModel investor1 = createUserByUserId("investor1");
        UserModel investor2 = createUserByUserId("investor2");
        UserModel investor3 = createUserByUserId("investor3");
        BankAccountModel accountModel1 = new BankAccountModel(investor1.getLoginName(), "payUserId1", "payAccountId1", "111", "111","");
        bankAccountMapper.createInvestor(accountModel1);
        BankAccountModel accountModel2 = new BankAccountModel(investor2.getLoginName(), "payUserId2", "payAccountId2", "111", "111","");
        bankAccountMapper.createInvestor(accountModel2);
        BankAccountModel accountModel3 = new BankAccountModel(investor3.getLoginName(), "payUserId3", "payAccountId3", "111", "111","");
        bankAccountMapper.createInvestor(accountModel3);

        InvestModel investModel1 = this.getFakeInvestModelByLoginName(investor1.getLoginName());
        investModel1.setAmount(2000);
        investModel1.setTradingTime(new DateTime("2016-07-05").toDate());
        investMapper.create(investModel1);
        InvestModel investModel2 = this.getFakeInvestModelByLoginName(investor2.getLoginName());
        investModel2.setAmount(1000);
        investModel2.setTradingTime(new DateTime("2016-07-05").toDate());
        investMapper.create(investModel2);
        InvestModel investModel3 = this.getFakeInvestModelByLoginName(investor3.getLoginName());
        investModel3.setAmount(3000);
        investModel3.setTradingTime(new DateTime("2016-07-05").toDate());
        investMapper.create(investModel3);

        TransferApplicationModel transferApplicationModel = fakeTransferApplicationModel(investModel2.getId(), investModel2.getLoginName(), new DateTime(2016, 7, 6, 0, 0, 1).toDate());
        transferApplicationMapper.create(transferApplicationModel);

        List<HeroRankingView> heroRankingViews = investMapper.findHeroRankingByTradingTime(new DateTime("2016-07-05").toDate(), heroRankingActivityPeriod.get(0), heroRankingActivityPeriod.get(1));
        assertEquals(3, heroRankingViews.size());
        assertEquals(investModel3.getLoginName(), heroRankingViews.get(0).getLoginName());
        assertEquals(investModel3.getAmount(), heroRankingViews.get(0).getSumAmount());
        assertEquals(investor3.getUserName(), heroRankingViews.get(0).getUserName());
        assertEquals(investor3.getMobile(), heroRankingViews.get(0).getMobile());

        assertEquals(investModel1.getLoginName(), heroRankingViews.get(1).getLoginName());
        assertEquals(investModel1.getAmount(), heroRankingViews.get(1).getSumAmount());
        assertEquals(investor1.getUserName(), heroRankingViews.get(1).getUserName());
        assertEquals(investor1.getMobile(), heroRankingViews.get(1).getMobile());

        assertEquals(investModel2.getLoginName(), heroRankingViews.get(2).getLoginName());
        assertEquals(investModel2.getAmount(), heroRankingViews.get(2).getSumAmount());
        assertEquals(investor2.getUserName(), heroRankingViews.get(2).getUserName());
        assertEquals(investor2.getMobile(), heroRankingViews.get(2).getMobile());

    }

    @Test
    public void shouldFindHeroRankingByTradingTimeTransferIsFail() {
        UserModel investor1 = createUserByUserId("investor1");
        UserModel investor2 = createUserByUserId("investor2");
        UserModel investor3 = createUserByUserId("investor3");
        BankAccountModel accountModel1 = new BankAccountModel(investor1.getLoginName(), "payUserId1", "payAccountId1", "111", "111","");
        bankAccountMapper.createInvestor(accountModel1);
        BankAccountModel accountModel2 = new BankAccountModel(investor2.getLoginName(), "payUserId2", "payAccountId2", "111", "111","");
        bankAccountMapper.createInvestor(accountModel2);
        BankAccountModel accountModel3 = new BankAccountModel(investor3.getLoginName(), "payUserId3", "payAccountId3", "111", "111","");
        bankAccountMapper.createInvestor(accountModel3);

        InvestModel investModel1 = this.getFakeInvestModelByLoginName(investor1.getLoginName());
        investModel1.setAmount(2000);
        investModel1.setTradingTime(new DateTime("2016-07-05").toDate());
        investMapper.create(investModel1);
        InvestModel investModel2 = this.getFakeInvestModelByLoginName(investor2.getLoginName());
        investModel2.setAmount(1000);
        investModel2.setTradingTime(new DateTime("2016-07-05").toDate());
        investMapper.create(investModel2);
        InvestModel investModel3 = this.getFakeInvestModelByLoginName(investor3.getLoginName());
        investModel3.setAmount(3000);
        investModel3.setTradingTime(new DateTime("2016-07-05").toDate());
        investMapper.create(investModel3);

        TransferApplicationModel transferApplicationModel = fakeTransferApplicationModel(investModel2.getId(), investModel2.getLoginName(), new DateTime(2016, 7, 5, 0, 0, 1).toDate());
        transferApplicationMapper.create(transferApplicationModel);

        List<HeroRankingView> heroRankingViews = investMapper.findHeroRankingByTradingTime(new DateTime(2016, 7, 5, 23, 59, 59).toDate(), heroRankingActivityPeriod.get(0), heroRankingActivityPeriod.get(1));
        assertEquals(2, heroRankingViews.size());

        assertEquals(investModel3.getLoginName(), heroRankingViews.get(0).getLoginName());
        assertEquals(investModel3.getAmount(), heroRankingViews.get(0).getSumAmount());
        assertEquals(investor3.getUserName(), heroRankingViews.get(0).getUserName());
        assertEquals(investor3.getMobile(), heroRankingViews.get(0).getMobile());

        assertEquals(investModel1.getLoginName(), heroRankingViews.get(1).getLoginName());
        assertEquals(investModel1.getAmount(), heroRankingViews.get(1).getSumAmount());
        assertEquals(investor1.getUserName(), heroRankingViews.get(1).getUserName());
        assertEquals(investor1.getMobile(), heroRankingViews.get(1).getMobile());

    }

    @Test
    public void shouldFindHeroRankingByTradingTimeTransferNextDayIsFail() {
        UserModel investor1 = createUserByUserId("investor1");
        UserModel investor2 = createUserByUserId("investor2");
        UserModel investor3 = createUserByUserId("investor3");
        BankAccountModel accountModel1 = new BankAccountModel(investor1.getLoginName(), "payUserId1", "payAccountId1", "111", "111","");
        bankAccountMapper.createInvestor(accountModel1);
        BankAccountModel accountModel2 = new BankAccountModel(investor2.getLoginName(), "payUserId2", "payAccountId2", "111", "111","");
        bankAccountMapper.createInvestor(accountModel2);
        BankAccountModel accountModel3 = new BankAccountModel(investor3.getLoginName(), "payUserId3", "payAccountId3", "111", "111","");
        bankAccountMapper.createInvestor(accountModel3);


        InvestModel investModel1 = this.getFakeInvestModelByLoginName(investor1.getLoginName());
        investModel1.setAmount(2000);
        investModel1.setTradingTime(new DateTime("2016-07-05").toDate());
        investMapper.create(investModel1);
        InvestModel investModel2 = this.getFakeInvestModelByLoginName(investor2.getLoginName());
        investModel2.setAmount(1000);
        investModel2.setTradingTime(new DateTime("2016-07-05").toDate());
        investMapper.create(investModel2);
        InvestModel investModel3 = this.getFakeInvestModelByLoginName(investor3.getLoginName());
        investModel3.setAmount(3000);
        investModel3.setTradingTime(new DateTime("2016-07-05").toDate());
        investMapper.create(investModel3);

        InvestModel investModel11 = this.getFakeInvestModelByLoginName(investor1.getLoginName());
        investModel11.setAmount(2000);
        investModel11.setTradingTime(new DateTime("2016-07-06").toDate());
        investMapper.create(investModel11);
        InvestModel investModel22 = this.getFakeInvestModelByLoginName(investor2.getLoginName());
        investModel22.setAmount(1000);
        investModel22.setTradingTime(new DateTime("2016-07-06").toDate());
        investMapper.create(investModel22);
        InvestModel investModel33 = this.getFakeInvestModelByLoginName(investor3.getLoginName());
        investModel33.setAmount(3000);
        investModel33.setTradingTime(new DateTime("2016-07-06").toDate());
        investMapper.create(investModel33);

        TransferApplicationModel transferApplicationModel = fakeTransferApplicationModel(investModel2.getId(), investModel2.getLoginName(), new DateTime(2016, 7, 5, 0, 0, 1).toDate());
        transferApplicationMapper.create(transferApplicationModel);

        List<HeroRankingView> heroRankingViews = investMapper.findHeroRankingByTradingTime(new DateTime(2016, 7, 6, 23, 59, 59).toDate(), heroRankingActivityPeriod.get(0), heroRankingActivityPeriod.get(1));
        assertEquals(2, heroRankingViews.size());

        assertEquals(investModel3.getLoginName(), heroRankingViews.get(0).getLoginName());
        assertEquals(investModel3.getAmount(), heroRankingViews.get(0).getSumAmount());
        assertEquals(investor3.getUserName(), heroRankingViews.get(0).getUserName());
        assertEquals(investor3.getMobile(), heroRankingViews.get(0).getMobile());

        assertEquals(investModel1.getLoginName(), heroRankingViews.get(1).getLoginName());
        assertEquals(investModel1.getAmount(), heroRankingViews.get(1).getSumAmount());
        assertEquals(investor1.getUserName(), heroRankingViews.get(1).getUserName());
        assertEquals(investor1.getMobile(), heroRankingViews.get(1).getMobile());

    }

    private TransferApplicationModel fakeTransferApplicationModel(long transferInvestId, String loginName, Date applicationTime) {

        TransferApplicationModel transferApplicationModel = new TransferApplicationModel();
        transferApplicationModel.setLoginName(loginName);
        transferApplicationModel.setName("name");
        transferApplicationModel.setTransferAmount(1000l);
        transferApplicationModel.setInvestAmount(1200l);
        transferApplicationModel.setTransferTime(new DateTime("2016-01-02").toDate());
        transferApplicationModel.setStatus(TransferStatus.TRANSFERRING);
        transferApplicationModel.setLoanId(Loan_ID);
        transferApplicationModel.setTransferInvestId(transferInvestId);
        transferApplicationModel.setInvestId(null);
        transferApplicationModel.setDeadline(new Date());
        transferApplicationModel.setApplicationTime(applicationTime);

        return transferApplicationModel;
    }


    @Test
    public void shouldSumSuccessInvestAmountIsOk() {
        long newbieLoanId = IdGenerator.generate();
        createLoan(User_ID, newbieLoanId, ActivityType.NEWBIE);

        InvestModel investModel = this.getFakeInvestModel();
        investModel.setLoanId(newbieLoanId);
        investModel.setLoginName(User_ID2);
        investModel.setInvestTime(DateUtils.addHours(new Date(), -1));
        investModel.setStatus(InvestStatus.SUCCESS);

        InvestModel investModel2 = this.getFakeInvestModel();
        investModel2.setLoanId(newbieLoanId);
        investModel2.setLoginName(User_ID2);
        investModel2.setInvestTime(DateUtils.addHours(new Date(), -2));
        investModel2.setStatus(InvestStatus.SUCCESS);

        investModel.setTransferInvestId(investModel2.getId());
        investMapper.create(investModel2);
        investMapper.create(investModel);

        long investAmount = investMapper.sumSuccessInvestAmount(newbieLoanId);
        assertEquals(investAmount, investModel.getAmount());
    }

    @Test
    public void shouldFindCountInvestProductTypeSuccessByLoginName() {
        long newbieLoanId = IdGenerator.generate();
        createLoan(User_ID, newbieLoanId, ActivityType.NEWBIE);

        InvestModel investModel = this.getFakeInvestModel();
        investModel.setLoanId(newbieLoanId);
        investModel.setLoginName(User_ID2);
        investModel.setInvestTime(DateUtils.addHours(new Date(), -1));
        investModel.setStatus(InvestStatus.SUCCESS);

        InvestModel investModel2 = this.getFakeInvestModel();
        investModel2.setLoanId(newbieLoanId);
        investModel2.setLoginName(User_ID2);
        investModel2.setInvestTime(DateUtils.addHours(new Date(), -2));
        investModel2.setStatus(InvestStatus.SUCCESS);

        investModel.setTransferInvestId(investModel2.getId());
        investMapper.create(investModel2);
        investMapper.create(investModel);
        int count = investMapper.findCountSuccessByLoginNameAndProductTypes(User_ID2, Lists.newArrayList(ProductType._30, ProductType._90, ProductType._180, ProductType._360, ProductType.EXPERIENCE));
        assertTrue(count > 0);
    }

    @Test
    public void shouldFindCountNormalAndNewBieSuccessByInvestTimeIsOk() {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(IdGenerator.generate());
        fakeLoanModel.setName(User_ID);
        fakeLoanModel.setLoanerLoginName(User_ID);
        fakeLoanModel.setLoanerUserName(User_ID);
        fakeLoanModel.setLoanerIdentityNumber("111111111111111111");
        fakeLoanModel.setAgentLoginName(User_ID);
        fakeLoanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        fakeLoanModel.setPeriods(3);
        fakeLoanModel.setStatus(LoanStatus.RAISING);
        fakeLoanModel.setActivityType(ActivityType.NORMAL);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setCreatedTime(new Date());
        fakeLoanModel.setProductType(ProductType.EXPERIENCE);
        fakeLoanModel.setPledgeType(PledgeType.HOUSE);
        loanMapper.create(fakeLoanModel);

        InvestModel investModel = this.getFakeInvestModel();
        investModel.setLoanId(fakeLoanModel.getId());
        investModel.setLoginName(User_ID2);
        investModel.setInvestTime(DateUtils.addHours(new Date(), -1));
        investModel.setStatus(InvestStatus.SUCCESS);
        investModel.setInvestTime(DateTime.now().toDate());

        InvestModel investModel2 = this.getFakeInvestModel();
        investModel2.setLoanId(fakeLoanModel.getId());
        investModel2.setLoginName(User_ID2);
        investModel2.setInvestTime(DateUtils.addHours(new Date(), -2));
        investModel2.setStatus(InvestStatus.SUCCESS);
        investModel.setInvestTime(DateTime.now().toDate());
        investModel.setTransferInvestId(investModel2.getId());
        investMapper.create(investModel2);
        investMapper.create(investModel);
        int count = investMapper.findCountSuccessByLoginNameAndProductTypes(User_ID2, Lists.newArrayList(ProductType._30, ProductType._90, ProductType._180, ProductType._360));
        assertTrue(count == 0);
    }

    @Test
    public void shouldCountInvestorSuccessInvestByInvestTimeIsOk() {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(IdGenerator.generate());
        fakeLoanModel.setName(User_ID);
        fakeLoanModel.setLoanerLoginName(User_ID);
        fakeLoanModel.setLoanerUserName(User_ID);
        fakeLoanModel.setLoanerIdentityNumber("111111111111111111");
        fakeLoanModel.setAgentLoginName(User_ID);
        fakeLoanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        fakeLoanModel.setPeriods(3);
        fakeLoanModel.setStatus(LoanStatus.RAISING);
        fakeLoanModel.setActivityType(ActivityType.NORMAL);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setCreatedTime(new Date());
        fakeLoanModel.setProductType(ProductType._180);
        fakeLoanModel.setPledgeType(PledgeType.HOUSE);
        loanMapper.create(fakeLoanModel);

        InvestModel investModel2 = this.getFakeInvestModel();
        investModel2.setLoanId(fakeLoanModel.getId());
        investModel2.setLoginName(User_ID2);
        investModel2.setInvestTime(DateTime.now().toDate());
        investModel2.setStatus(InvestStatus.SUCCESS);
        investModel2.setInvestTime(DateTime.now().toDate());
        investModel2.setTransferInvestId(investModel2.getId());
        investMapper.create(investModel2);

        long count = investMapper.countInvestorSuccessInvestByInvestTime(User_ID2, DateUtils.addMonths(DateTime.now().toDate(), -1), DateUtils.addMonths(DateTime.now().toDate(), 1));
        assertEquals(count, 1);
    }

    @Test
    public void shouldSumSuccessActivityInvestAmountIsOk() {
        UserModel investor1 = createUserByUserId("investor1");
        LoanDetailsModel loanDetailsModel = new LoanDetailsModel();
        loanDetailsModel.setLoanId(Loan_ID);
        loanDetailsModel.setId(IdGenerator.generate());
        loanDetailsModel.setActivity(true);
        loanDetailsModel.setActivityDesc("春节专享");
        loanDetailsModel.setDeclaration("1");
        loanDetailsMapper.create(loanDetailsModel);
        InvestModel investModel = this.getFakeInvestModel();
        investModel.setLoginName(investor1.getLoginName());
        investModel.setStatus(InvestStatus.SUCCESS);
        investMapper.create(investModel);

        long sumActivityAmount = this.investMapper.sumSuccessActivityInvestAmount(investor1.getLoginName(), loanDetailsModel.getActivityDesc(), DateTime.now().plusDays(-1).toDate(), DateTime.now().plusDays(1).toDate());
        assertTrue(sumActivityAmount == investModel.getAmount());

        loanDetailsModel.setActivity(false);
        loanDetailsMapper.updateByLoanId(loanDetailsModel);
        sumActivityAmount = this.investMapper.sumSuccessActivityInvestAmount(investor1.getLoginName(), loanDetailsModel.getActivityDesc(), DateTime.now().plusDays(-1).toDate(), DateTime.now().plusDays(1).toDate());
        assertTrue(sumActivityAmount != investModel.getAmount());
    }

    @Test
    public void shouldSumInvestAmountConsoleIsOk() {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(IdGenerator.generate());
        fakeLoanModel.setName(User_ID);
        fakeLoanModel.setLoanerLoginName(User_ID);
        fakeLoanModel.setLoanerUserName(User_ID);
        fakeLoanModel.setLoanerIdentityNumber("111111111111111111");
        fakeLoanModel.setAgentLoginName(User_ID);
        fakeLoanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        fakeLoanModel.setPeriods(3);
        fakeLoanModel.setStatus(LoanStatus.RAISING);
        fakeLoanModel.setActivityType(ActivityType.NORMAL);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setCreatedTime(new Date());
        fakeLoanModel.setProductType(ProductType._180);
        fakeLoanModel.setPledgeType(PledgeType.HOUSE);
        loanMapper.create(fakeLoanModel);

        Date startDate = DateTime.parse("2014-10-01 00:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date endDate = DateTime.parse("2014-10-01 23:59:59", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        long investAmount = investMapper.sumInvestAmountConsole(true,null, null, null, null, null, startDate, endDate, null, null, null);
        assertEquals(investAmount, 0);

        InvestModel investModel2 = this.getFakeInvestModel();
        investModel2.setLoanId(fakeLoanModel.getId());
        investModel2.setLoginName(User_ID2);
        investModel2.setInvestTime(DateTime.now().toDate());
        investModel2.setStatus(InvestStatus.SUCCESS);
        investModel2.setInvestTime(DateTime.now().toDate());
        investModel2.setCreatedTime(DateTime.parse("2014-10-01 15:31:12", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate());
        investModel2.setAmount(100);
        investMapper.create(investModel2);

        investAmount = investMapper.sumInvestAmountConsole(true,null, null, null, null, null, startDate, endDate, null, null, null);
        assertEquals(investAmount, investModel2.getAmount());

    }

    @Test
    public void shouldInvestAmountConsoleNotExistsExperienceInvestIsOk() {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(IdGenerator.generate());
        fakeLoanModel.setName(User_ID);
        fakeLoanModel.setLoanerLoginName(User_ID);
        fakeLoanModel.setLoanerUserName(User_ID);
        fakeLoanModel.setLoanerIdentityNumber("111111111111111111");
        fakeLoanModel.setAgentLoginName(User_ID);
        fakeLoanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        fakeLoanModel.setPeriods(3);
        fakeLoanModel.setStatus(LoanStatus.RAISING);
        fakeLoanModel.setActivityType(ActivityType.NORMAL);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setCreatedTime(new Date());
        fakeLoanModel.setProductType(ProductType._180);
        fakeLoanModel.setPledgeType(PledgeType.HOUSE);
        loanMapper.create(fakeLoanModel);

        LoanModel experienceLoan = new LoanModel();
        experienceLoan.setId(IdGenerator.generate());
        experienceLoan.setName(User_ID);
        experienceLoan.setLoanerLoginName(User_ID);
        experienceLoan.setLoanerUserName(User_ID);
        experienceLoan.setLoanerIdentityNumber("111111111111111111");
        experienceLoan.setAgentLoginName(User_ID);
        experienceLoan.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        experienceLoan.setPeriods(3);
        experienceLoan.setStatus(LoanStatus.RAISING);
        experienceLoan.setActivityType(ActivityType.NORMAL);
        experienceLoan.setFundraisingStartTime(new Date());
        experienceLoan.setFundraisingEndTime(new Date());
        experienceLoan.setDescriptionHtml("html");
        experienceLoan.setDescriptionText("text");
        experienceLoan.setCreatedTime(new Date());
        experienceLoan.setProductType(ProductType.EXPERIENCE);
        experienceLoan.setPledgeType(PledgeType.HOUSE);
        loanMapper.create(experienceLoan);

        InvestModel investModel1 = this.getFakeInvestModel();
        investModel1.setLoanId(experienceLoan.getId());
        investModel1.setLoginName(User_ID2);
        investModel1.setInvestTime(DateTime.now().toDate());
        investModel1.setStatus(InvestStatus.SUCCESS);
        investModel1.setInvestTime(DateTime.now().toDate());
        investMapper.create(investModel1);

        InvestModel investModel2 = this.getFakeInvestModel();
        investModel2.setLoanId(fakeLoanModel.getId());
        investModel2.setLoginName(User_ID2);
        investModel2.setInvestTime(DateTime.now().toDate());
        investModel2.setStatus(InvestStatus.SUCCESS);
        investModel2.setInvestTime(DateTime.now().toDate());
        investMapper.create(investModel2);

        long sum = investMapper.sumInvestAmountConsole(true,null, User_ID2, null, null, null, null, null, null, null, null);
        assertEquals(sum, 1000000L);

        long count = investMapper.findCountInvestPagination(true,null, User_ID2, null, null, null, null, null, null, null, null);
        assertEquals(count, 1L);

        List<InvestPaginationItemView> list = investMapper.findInvestPagination(true,null, User_ID2, null, null, null, null, null, null, null, null, 0, Integer.MAX_VALUE);
        assertEquals(list.size(), 1);
    }

    @Test
    public void shouldFindSumSuccessInvestByInvestTimeIsOk() {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(IdGenerator.generate());
        fakeLoanModel.setName(User_ID);
        fakeLoanModel.setLoanerLoginName(User_ID);
        fakeLoanModel.setLoanerUserName(User_ID);
        fakeLoanModel.setLoanerIdentityNumber("111111111111111111");
        fakeLoanModel.setAgentLoginName(User_ID);
        fakeLoanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        fakeLoanModel.setPeriods(3);
        fakeLoanModel.setStatus(LoanStatus.RAISING);
        fakeLoanModel.setActivityType(ActivityType.NORMAL);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setCreatedTime(new Date());
        fakeLoanModel.setProductType(ProductType._180);
        fakeLoanModel.setPledgeType(PledgeType.HOUSE);
        loanMapper.create(fakeLoanModel);

        LoanModel experienceLoan = new LoanModel();
        experienceLoan.setId(IdGenerator.generate());
        experienceLoan.setName(User_ID);
        experienceLoan.setLoanerLoginName(User_ID);
        experienceLoan.setLoanerUserName(User_ID);
        experienceLoan.setLoanerIdentityNumber("111111111111111111");
        experienceLoan.setAgentLoginName(User_ID);
        experienceLoan.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        experienceLoan.setPeriods(3);
        experienceLoan.setStatus(LoanStatus.RAISING);
        experienceLoan.setActivityType(ActivityType.NORMAL);
        experienceLoan.setFundraisingStartTime(new Date());
        experienceLoan.setFundraisingEndTime(new Date());
        experienceLoan.setDescriptionHtml("html");
        experienceLoan.setDescriptionText("text");
        experienceLoan.setCreatedTime(new Date());
        experienceLoan.setProductType(ProductType.EXPERIENCE);
        experienceLoan.setPledgeType(PledgeType.HOUSE);
        loanMapper.create(experienceLoan);

        InvestModel investModel1 = this.getFakeInvestModel();
        investModel1.setLoanId(experienceLoan.getId());
        investModel1.setLoginName(User_ID2);
        investModel1.setInvestTime(DateTime.now().toDate());
        investModel1.setStatus(InvestStatus.SUCCESS);
        investModel1.setInvestTime(DateTime.now().toDate());
        investMapper.create(investModel1);

        InvestModel investModel2 = this.getFakeInvestModel();
        investModel2.setTransferInvestId(investModel1.getId());
        investModel2.setLoanId(fakeLoanModel.getId());
        investModel2.setLoginName(User_ID2);
        investModel2.setInvestTime(DateTime.now().toDate());
        investModel2.setStatus(InvestStatus.SUCCESS);
        investModel2.setInvestTime(DateTime.now().toDate());
        investMapper.create(investModel2);

        List<InvestModel> successInvestByInvestTime = investMapper.findSuccessInvestByInvestTime(User_ID2, true, true, DateTime.now().plusDays(-5).toDate(), DateTime.now().plusDays(5).toDate());
        assertEquals(successInvestByInvestTime.size(), 2);

        successInvestByInvestTime = investMapper.findSuccessInvestByInvestTime(User_ID2, false, true, DateTime.now().plusDays(-5).toDate(), DateTime.now().plusDays(5).toDate());
        assertEquals(successInvestByInvestTime.size(), 1);

        successInvestByInvestTime = investMapper.findSuccessInvestByInvestTime(User_ID2, true, false, DateTime.now().plusDays(-5).toDate(), DateTime.now().plusDays(5).toDate());
        assertEquals(successInvestByInvestTime.size(), 1);

        successInvestByInvestTime = investMapper.findSuccessInvestByInvestTime(User_ID2, false, false, DateTime.now().plusDays(-5).toDate(), DateTime.now().plusDays(5).toDate());
        assertEquals(successInvestByInvestTime.size(), 0);
    }
}
