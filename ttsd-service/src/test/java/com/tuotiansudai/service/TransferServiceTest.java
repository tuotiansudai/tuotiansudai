package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.service.TransferService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class TransferServiceTest {
    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private TransferService transferService;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private RandomUtils randomUtils;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;


    public static final long loanId = 10000000001L;
    public static final long investId = 10000000002L;

    public static final long transferSuccessloanId = 20000000001L;
    public static final long transferSuccessInvestId = 20000000002L;

    private LoanModel createLoanByUserId(String userId, long loanId) {
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
        loanDto.setPeriods(3);
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
        loanDto.setProductType(ProductType._90);
        loanDto.setPledgeType(PledgeType.HOUSE);
        loanDto.setRecheckTime(new DateTime().minusDays(10).toDate());
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setStatus(LoanStatus.REPAYING);
        loanMapper.create(loanModel);
        return loanModel;
    }

    private TransferApplicationModel createTransferApplicationModel(long loanId, long investId, TransferStatus transferStatus) {
        TransferApplicationModel transferApplicationModel = new TransferApplicationModel();
        transferApplicationModel.setName("ZR00001");
        transferApplicationModel.setLoanId(loanId);
        transferApplicationModel.setTransferInvestId(investId);
        transferApplicationModel.setInvestId(investId);
        transferApplicationModel.setPeriod(2);
        transferApplicationModel.setLoginName("testuser");
        transferApplicationModel.setInvestAmount(100000L);
        transferApplicationModel.setTransferAmount(90000L);
        transferApplicationModel.setTransferFee(10L);
        transferApplicationModel.setStatus(transferStatus);
        transferApplicationModel.setDeadline(new Date());
        transferApplicationModel.setTransferTime(new Date());
        transferApplicationModel.setApplicationTime(new Date());
        transferApplicationModel.setLeftPeriod(4);
        transferApplicationMapper.create(transferApplicationModel);
        return transferApplicationModel;
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

    private InvestModel createInvests(String loginName, long loanId, long investId) {
        InvestModel model = new InvestModel(investId, loanId, null, loginName, 1, 0.1, false, new Date(), Source.WEB, null);
        model.setStatus(InvestStatus.SUCCESS);
        investMapper.create(model);
        return model;
    }

    private void createInvestsTransferSuccess(String loginName, long loanId, long investId) {
        InvestModel model = new InvestModel(investId, loanId, null, loginName, 1, 0.1, false, new Date(), Source.WEB, null);
        model.setTransferStatus(TransferStatus.SUCCESS);
        investMapper.create(model);
    }

    private void createInvestRepay(long investId) {
        InvestRepayModel model1 = new InvestRepayModel(IdGenerator.generate(), investId, 1, 0, 100, 10, strToDate("2016-02-29 23:59:59"), RepayStatus.COMPLETE);
        InvestRepayModel model2 = new InvestRepayModel(IdGenerator.generate(), investId, 2, 0, 100, 10, strToDate("2016-03-29 23:59:59"), RepayStatus.REPAYING);
        InvestRepayModel model3 = new InvestRepayModel(IdGenerator.generate(), investId, 3, 0, 100, 10, strToDate("2016-04-29 23:59:59"), RepayStatus.REPAYING);
        InvestRepayModel model4 = new InvestRepayModel(IdGenerator.generate(), investId, 4, 0, 100, 10, strToDate("2016-05-29 23:59:59"), RepayStatus.REPAYING);
        InvestRepayModel model5 = new InvestRepayModel(IdGenerator.generate(), investId, 5, 10000, 100, 10, strToDate("2016-06-29 23:59:59"), RepayStatus.REPAYING);
        List<InvestRepayModel> investRepayModels = new ArrayList<InvestRepayModel>();
        investRepayModels.add(model1);
        investRepayModels.add(model2);
        investRepayModels.add(model3);
        investRepayModels.add(model4);
        investRepayModels.add(model5);
        investRepayMapper.create(investRepayModels);
    }

    private Date strToDate(String strDate) {
        try {
            return DateUtils.parseDate(strDate, new String[]{"yyyy-MM-dd HH:mm:ss"});
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createAccount(String loginName) {
        AccountModel accountModel = new AccountModel(loginName, "2", "2", new Date());
        accountModel.setBalance(1000);
        accountMapper.create(accountModel);
    }

    @Before
    public void setup() throws Exception {
        createUserByUserId("testuser");
        createAccount("testuser");
    }

    @Test
    public void shouldFindAllTransferApplicationPaginationListIsTest() {
        createLoanByUserId("testuser", loanId);
        createInvests("testuser", loanId, investId);
        createInvestRepay(investId);
        createTransferApplicationModel(loanId, investId, TransferStatus.TRANSFERRING);
        List<TransferStatus> transferStatuses = new ArrayList<TransferStatus>();
        transferStatuses.add(TransferStatus.TRANSFERRING);
        BasePaginationDataDto<TransferApplicationPaginationItemDataDto> basePaginationDataDto = transferService.findAllTransferApplicationPaginationList(transferStatuses, 0.15, 0.18, 1, 10);

        assertTrue(basePaginationDataDto.getRecords().size() > 0);
    }

    @Test
    public void shouldGetTransferApplicationDetailDtoIsOk() {
        createLoanByUserId("testuser", loanId);
        createInvests("testuser", loanId, investId);
        createInvestRepay(investId);
        TransferApplicationModel transferApplicationModel = createTransferApplicationModel(loanId, investId, TransferStatus.TRANSFERRING);
        TransferApplicationDetailDto transferApplicationDetailDto = transferService.getTransferApplicationDetailDto(transferApplicationModel.getId(), "testuser", 6);

        assertThat(transferApplicationDetailDto.getNextRefundDate(), is(strToDate("2016-03-29 23:59:59")));
        assertThat(transferApplicationDetailDto.getTransferAmount(), is("900.00"));
        assertThat(transferApplicationDetailDto.getExpecedInterest(), is("3.60"));
        assertThat(transferApplicationDetailDto.getInvestAmount(), is("1000.00"));
        assertThat(transferApplicationDetailDto.getExpecedInterest(), is("3.60"));
        assertThat(transferApplicationDetailDto.getNextExpecedInterest(), is("0.90"));
        assertThat(transferApplicationDetailDto.getBalance(), is("10.00"));
    }

    @Test
    public void shouldGetTransferApplicationLastPeriodIsOk() {
        createLoanByUserId("testuser", loanId);
        createInvests("testuser", loanId, investId);
        createInvestRepay(investId);
        TransferApplicationModel transferApplicationModel = createTransferApplicationModel(loanId, investId, TransferStatus.TRANSFERRING);
        transferApplicationModel.setPeriod(5);
        transferApplicationMapper.update(transferApplicationModel);
        TransferApplicationDetailDto transferApplicationDetailDto = transferService.getTransferApplicationDetailDto(transferApplicationModel.getId(), "testuser", 6);

        assertThat(transferApplicationDetailDto.getNextExpecedInterest(), is("0.90"));
        assertThat(transferApplicationDetailDto.getBalance(), is("10.00"));
    }

    @Test
    public void shouldTransferApplicationRecodesDtoIsNoRecodes() {
        createLoanByUserId("testuser", loanId);
        createInvests("testuser", loanId, investId);
        createInvestRepay(investId);
        TransferApplicationModel transferApplicationModel = createTransferApplicationModel(loanId, investId, TransferStatus.TRANSFERRING);

        TransferApplicationRecodesDto transferApplicationRecodesDto = transferService.getTransferee(transferApplicationModel.getId(), "testuser");

        assertThat(transferApplicationRecodesDto.getStatus(), is(false));
    }

    @Test
    public void shouldTransferApplicationRecodesDtoIsHasRecords() {
        createLoanByUserId("testuser", transferSuccessloanId);
        createInvestsTransferSuccess("testuser", transferSuccessloanId, transferSuccessInvestId);
        createInvestRepay(transferSuccessInvestId);
        TransferApplicationModel transferApplicationModel = createTransferApplicationModel(transferSuccessloanId, transferSuccessInvestId, TransferStatus.SUCCESS);

        TransferApplicationRecodesDto transferApplicationRecodesDto = transferService.getTransferee(transferApplicationModel.getId(), "");

        assertThat(transferApplicationRecodesDto.getStatus(), is(true));
        assertThat(transferApplicationRecodesDto.getReceiveAmount(), is("900.00"));

        assertThat(transferApplicationRecodesDto.getTransferApplicationReceiver(), is(randomUtils.encryptMobile("", "testuser")));
        assertThat(transferApplicationRecodesDto.getExpecedInterest(), is("103.60"));
        assertThat(transferApplicationRecodesDto.getSource(), is(Source.WEB));
        assertThat(transferApplicationRecodesDto.getInvestAmount(), is("1000.00"));
    }

    @Test
    public void shouldGenerateTransferableInvestIsSuccess() {
        long loanId = IdGenerator.generate();
        UserModel investorModel = createUserByUserId("investorModelRound2Test");
        UserModel loanerModel = createUserByUserId("loanerModelRound2Test");
        LoanModel loanModel = createLoanByUserId(loanerModel.getLoginName(), loanId);
        this.createLoanDetailsByLoanId(loanModel);
        InvestModel investModel = createInvests(investorModel.getLoginName(), loanId, IdGenerator.generate());
        LoanRepayModel loanRepayModel = getFakeLoanRepayModel(loanModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 1000l, 2000l, 3000l, 4000l);
        loanRepayMapper.create(Lists.newArrayList(loanRepayModel));
        InvestRepayModel investRepayModel = getFakeInvestRepayModel(investModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(6).toDate(), new DateTime().plusDays(6).toDate(), 0l, 2000l, 3000l, 4000l);
        InvestRepayModel investRepayModel2 = getFakeInvestRepayModel(investModel, 2, RepayStatus.REPAYING, new DateTime().plusDays(30).toDate(), new DateTime().plusDays(30).toDate(), 0l, 2000l, 3000l, 4000l);
        InvestRepayModel investRepayModel3 = getFakeInvestRepayModel(investModel, 3, RepayStatus.REPAYING, new DateTime().plusDays(90).toDate(), new DateTime().plusDays(90).toDate(), 1000l, 2000l, 3000l, 4000l);
        investRepayMapper.create(Lists.newArrayList(investRepayModel, investRepayModel2, investRepayModel3));
        BasePaginationDataDto<TransferableInvestPaginationItemDataView> baseDto = transferService.generateTransferableInvest(investorModel.getLoginName(), 1, 10);
        TransferableInvestPaginationItemDataView dataDto = baseDto.getRecords().get(0);
        assertEquals(1, baseDto.getRecords().size());
        assertEquals(investModel.getId(), dataDto.getInvestId());
        assertEquals(investModel.getLoanId(), dataDto.getLoanId());
        assertEquals(loanModel.getName(), dataDto.getLoanName());
        assertEquals(AmountConverter.convertCentToString(investModel.getAmount()), dataDto.getAmount());
        assertEquals(AmountConverter.convertCentToString(investRepayModel.getExpectedInterest() + investRepayModel.getCorpus() - investRepayModel.getExpectedFee()), dataDto.getNextRepayAmount());
        assertEquals(new DateTime(loanRepayModel.getRepayDate()).withTimeAtStartOfDay(), new DateTime(dataDto.getNextRepayDate()).withTimeAtStartOfDay());
        assertEquals("28", dataDto.getSumRate());
        assertEquals(TransferStatus.TRANSFERABLE.getDescription(), dataDto.getTransferStatus());
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

    private LoanDetailsModel createLoanDetailsByLoanId(LoanModel loanModel) {
        LoanDetailsModel loanDetailsModel = new LoanDetailsModel();
        loanDetailsModel.setNonTransferable(false);
        loanDetailsModel.setDeclaration("declaration");
        loanDetailsModel.setActivity(false);
        loanDetailsModel.setActivityDesc("activityDesc");
        loanDetailsModel.setLoanId(loanModel.getId());
        loanDetailsMapper.create(loanDetailsModel);
        return loanDetailsModel;

    }

}
