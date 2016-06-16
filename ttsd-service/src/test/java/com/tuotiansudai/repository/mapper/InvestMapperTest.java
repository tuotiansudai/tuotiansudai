package com.tuotiansudai.repository.mapper;


import com.google.common.collect.Lists;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.transfer.repository.model.TransferableInvestView;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class InvestMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;
    @Autowired
    private LoanRepayMapper loanRepayMapper;
    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    private String User_ID = "helloworld";
    private String User_ID2 = "testuser";
    private long Loan_ID = 200093022L;
    private long Loan_ID2 = 300093022L;
    private long Loan_ID3 = 400093022L;

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
        InvestModel model = new InvestModel(idGenerator.generate(), Loan_ID, null, 1000000L, User_ID, new DateTime().withTimeAtStartOfDay().toDate(), Source.WEB, null, 0.1);
        model.setStatus(InvestStatus.SUCCESS);
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
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setStatus(loanStatus);
        loanMapper.create(loanModel);
    }

    @Before
    public void createUser() throws Exception {
        createUserByUserId(User_ID);
        createUserByUserId(User_ID2);

        UserModel userModel = userMapper.findByLoginName(User_ID);
        assertNotNull(userModel);
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
    public void shouldSumSuccessInvestCount(){
        long newbieLoanId = idGenerator.generate();
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
        List<InvestModel> investModelList = investMapper.findByLoginName(User_ID2, 0, Integer.MAX_VALUE);
        assertEquals(4, investModelList.size());
        long investCount = investMapper.findCountByLoginName(User_ID2);
        assertEquals(4, investCount);
    }

    @Test
    public void shouldHasSuccessInvest() throws Exception {
        InvestModel fakeInvestModel = getFakeInvestModel();
        fakeInvestModel.setStatus(InvestStatus.SUCCESS);
        investMapper.create(fakeInvestModel);
        long amount = investMapper.sumSuccessInvestAmountByLoginName(null, User_ID);
        assertTrue(amount > 0);
    }

    @Test
    public void shouldHasNoSuccessInvest() throws Exception {
        long amount = investMapper.sumSuccessInvestAmountByLoginName(null, User_ID);
        assertTrue(amount == 0);
    }

    @Test
    public void shouldGetInvestDetail() throws Exception{
        List<InvestDataView> investDataViews = investMapper.getInvestDetail();
        assertTrue(investDataViews.size() >= 0);
    }

    @Test
    public void shouldFindWebTransferAbleApplicationPaginationByLoginNameIsSuccess(){
        long loanId = idGenerator.generate();
        UserModel investorModel = createUser("investorModelRound4Test");
        UserModel loanerModel = createUser("loanerModelRound4Test");
        LoanModel loanModel = createLoanByUserId(loanerModel.getLoginName(), loanId,LoanStatus.REPAYING);
        InvestModel investModel = createInvest(investorModel.getLoginName(), loanId, InvestStatus.SUCCESS,TransferStatus.TRANSFERABLE);
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
        assertEquals(new DateTime(loanRepayModel.getRepayDate()).withTimeAtStartOfDay(),new DateTime(transferableInvestViews.get(0).getNextRepayDate()).withTimeAtStartOfDay());

    }
    @Test
    public void shouldFindCountWebTransferableApplicationPaginationByLoginNameIsSuccess(){
        long loanId = idGenerator.generate();
        UserModel investorModel = createUser("investorModelRound4Test");
        UserModel loanerModel = createUser("loanerModelRound4Test");
        LoanModel loanModel = createLoanByUserId(loanerModel.getLoginName(), loanId,LoanStatus.REPAYING);
        InvestModel investModel = createInvest(investorModel.getLoginName(), loanId,InvestStatus.SUCCESS,TransferStatus.TRANSFERABLE);
        LoanRepayModel loanRepayModel = getFakeLoanRepayModel(loanModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        loanRepayMapper.create(Lists.newArrayList(loanRepayModel));
        InvestRepayModel investRepayModel = getFakeInvestRepayModel(investModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        investRepayMapper.create(Lists.newArrayList(investRepayModel));
        long count = investMapper.findWebCountTransferableApplicationPaginationByLoginName(investorModel.getLoginName());
        assertEquals(1, count);
    }
    @Test
    public void shouldFindWebTransferableApplicationPaginationByLoginNameOverdueIsFail() {
        long loanId = idGenerator.generate();
        UserModel investorModel = createUser("investorModelRound4Test");
        UserModel loanerModel = createUser("loanerModelRound4Test");
        LoanModel loanModel = createLoanByUserId(loanerModel.getLoginName(), loanId, LoanStatus.OVERDUE);
        InvestModel investModel = createInvest(investorModel.getLoginName(), loanId, InvestStatus.SUCCESS,TransferStatus.TRANSFERABLE);
        LoanRepayModel loanRepayModel = getFakeLoanRepayModel(loanModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        loanRepayMapper.create(Lists.newArrayList(loanRepayModel));
        InvestRepayModel investRepayModel = getFakeInvestRepayModel(investModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        investRepayMapper.create(Lists.newArrayList(investRepayModel));
        List<TransferableInvestView> transferableInvestViews = investMapper.findWebTransferableApplicationPaginationByLoginName(investorModel.getLoginName(), 0, 10);
        assertEquals(0,transferableInvestViews.size());
    }
    @Test
    public void shouldFindWebTransferableApplicationPaginationByLoginNameInvestTransferStatusCancelIsFail(){
        long loanId = idGenerator.generate();
        UserModel investorModel = createUser("investorModelRound4Test");
        UserModel loanerModel = createUser("loanerModelRound4Test");
        LoanModel loanModel = createLoanByUserId(loanerModel.getLoginName(), loanId,LoanStatus.REPAYING);
        InvestModel investModel = createInvest(investorModel.getLoginName(), loanId, InvestStatus.SUCCESS,TransferStatus.CANCEL);
        LoanRepayModel loanRepayModel = getFakeLoanRepayModel(loanModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        loanRepayMapper.create(Lists.newArrayList(loanRepayModel));
        InvestRepayModel investRepayModel = getFakeInvestRepayModel(investModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        investRepayMapper.create(Lists.newArrayList(investRepayModel));
        List<TransferableInvestView> transferableInvestViews = investMapper.findWebTransferableApplicationPaginationByLoginName(investorModel.getLoginName(), 0, 10);
        assertEquals(0,transferableInvestViews.size());
    }
    @Test
    public void shouldFindWebTransferableApplicationPaginationByLoginNameTransferStatusCancelDiffDayIsSuccess(){
        long loanId = idGenerator.generate();
        UserModel investorModel = createUser("investorModelRound4Test");
        UserModel loanerModel = createUser("loanerModelRound4Test");
        LoanModel loanModel = createLoanByUserId(loanerModel.getLoginName(), loanId,LoanStatus.REPAYING);
        InvestModel investModel = createInvest(investorModel.getLoginName(), loanId, InvestStatus.SUCCESS,TransferStatus.TRANSFERABLE);
        TransferApplicationModel transferApplicationModel = getFakeTransferApplicationModel(investorModel.getLoginName(), TransferStatus.CANCEL, investModel.getId(), loanModel.getId());
        transferApplicationModel.setApplicationTime(new DateTime().minusDays(1).toDate());
        transferApplicationMapper.create(transferApplicationModel);
        LoanRepayModel loanRepayModel = getFakeLoanRepayModel(loanModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        loanRepayMapper.create(Lists.newArrayList(loanRepayModel));
        InvestRepayModel investRepayModel = getFakeInvestRepayModel(investModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        investRepayMapper.create(Lists.newArrayList(investRepayModel));
        List<TransferableInvestView> transferableInvestViews = investMapper.findWebTransferableApplicationPaginationByLoginName(investorModel.getLoginName(), 0, 10);
        assertEquals(1,transferableInvestViews.size());
    }

    @Test
    public void shouldFindWebTransferableApplicationPaginationByLoginNameTransferStatusCancelSameDayIsSuccess(){
        long loanId = idGenerator.generate();
        UserModel investorModel = createUser("investorModelRound4Test");
        UserModel loanerModel = createUser("loanerModelRound4Test");
        LoanModel loanModel = createLoanByUserId(loanerModel.getLoginName(), loanId,LoanStatus.REPAYING);
        InvestModel investModel = createInvest(investorModel.getLoginName(), loanId, InvestStatus.SUCCESS,TransferStatus.TRANSFERABLE);
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
        assertEquals(0,transferableInvestViews.size());
    }
    @Test
    public void shouldFindWebTransferableApplicationPaginationByLoginNameLessLimitDayIsFail(){
        long loanId = idGenerator.generate();
        UserModel investorModel = createUser("investorModelRound4Test");
        UserModel loanerModel = createUser("loanerModelRound4Test");
        LoanModel loanModel = createLoanByUserId(loanerModel.getLoginName(), loanId,LoanStatus.REPAYING);
        InvestModel investModel = createInvest(investorModel.getLoginName(), loanId, InvestStatus.SUCCESS,TransferStatus.TRANSFERABLE);
        LoanRepayModel loanRepayModel = getFakeLoanRepayModel(loanModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(1).toDate(), new DateTime().plusDays(1).toDate(), 1000l, 2000l, 3000l, 4000l);
        loanRepayMapper.create(Lists.newArrayList(loanRepayModel));
        InvestRepayModel investRepayModel = getFakeInvestRepayModel(investModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(1).toDate(), new DateTime().plusDays(1).toDate(), 1000l, 2000l, 3000l, 4000l);
        investRepayMapper.create(Lists.newArrayList(investRepayModel));
        List<TransferableInvestView> transferableInvestViews = investMapper.findWebTransferableApplicationPaginationByLoginName(investorModel.getLoginName(), 0, 10);
        assertEquals(0, transferableInvestViews.size());
    }

    @Test
    public void shouldFindTransferableApplicationPaginationByLoginNameIsSuccess(){
        long loanId = idGenerator.generate();
        UserModel investorModel = createUser("investorModelRound4Test");
        UserModel loanerModel = createUser("loanerModelRound4Test");
        LoanModel loanModel = createLoanByUserId(loanerModel.getLoginName(), loanId, LoanStatus.REPAYING);
        InvestModel investModel = createInvest(investorModel.getLoginName(), loanId, InvestStatus.SUCCESS,TransferStatus.TRANSFERABLE);
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
    public void shouldFindCountTransferAbleApplicationPaginationByLoginNameIsSuccess(){
        long loanId = idGenerator.generate();
        UserModel investorModel = createUser("investorModelRound4Test");
        UserModel loanerModel = createUser("loanerModelRound4Test");
        LoanModel loanModel = createLoanByUserId(loanerModel.getLoginName(), loanId,LoanStatus.REPAYING);
        InvestModel investModel = createInvest(investorModel.getLoginName(), loanId,InvestStatus.SUCCESS,TransferStatus.TRANSFERABLE);
        LoanRepayModel loanRepayModel = getFakeLoanRepayModel(loanModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        loanRepayMapper.create(Lists.newArrayList(loanRepayModel));
        InvestRepayModel investRepayModel = getFakeInvestRepayModel(investModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        investRepayMapper.create(Lists.newArrayList(investRepayModel));
        long count = investMapper.findCountTransferableApplicationPaginationByLoginName(investorModel.getLoginName());
        assertEquals(1,count);
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
        fakeLoanRepayModel.setId(idGenerator.generate());
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
        fakeInvestRepayModel.setId(idGenerator.generate());
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

    private InvestModel createInvest(String loginName, long loanId,InvestStatus investStatus,TransferStatus transferStatus) {
        InvestModel model = new InvestModel(idGenerator.generate(), loanId, null, 1, loginName, new Date(), Source.WEB, null,0.1);
        model.setStatus(investStatus);
        model.setTransferStatus(transferStatus);
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
    private LoanModel createLoanByUserId(String userId, long loanId,LoanStatus loanStatus) {
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
//        loanDto.setInvestFeeRate("15");
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("10000");
        loanDto.setType(LoanType.LOAN_INTEREST_MONTHLY_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setProductType(ProductType._180);
        loanDto.setLoanStatus(LoanStatus.REPAYING);
        loanDto.setRecheckTime(new DateTime().minusDays(10).toDate());
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setStatus(loanStatus);
        loanMapper.create(loanModel);
        return loanModel;
    }
    private TransferApplicationModel getFakeTransferApplicationModel(String loginName,TransferStatus transferStatus,long investId,long loanId){
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
    public void shouldSumSuccessInvestAmountIsOk() {
        long newbieLoanId = idGenerator.generate();
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

}
