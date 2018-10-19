package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.TransferApplicationDto;
import com.tuotiansudai.dto.TransferApplicationPaginationItemDataDto;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class InvestTransferServiceTest {

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private InvestTransferService investTransferService;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private TransferRuleMapper transferRuleMapper;

    public static String redisTransferApplicationNumber = "web:{0}:transferApplicationNumber";

    private final static String TRANSFER_APPLY_NAME = "ZR{0}-{1}";

    private LoanModel createLoanByUserId(String userId, long loanId) {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName(userId);
        loanDto.setAgentLoginName(userId);
        loanDto.setLoanerUserName("借款人");
        loanDto.setLoanerIdentityNumber("111111111111111111");
        loanDto.setAgentLoginName(userId);
        loanDto.setBasicRate("16.00");
        loanDto.setId(loanId);
        loanDto.setProjectName("店铺资金周转");
        loanDto.setActivityRate("12");
        loanDto.setShowOnHome(true);
        loanDto.setPeriods(6);
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
        loanDto.setLoanStatus(LoanStatus.REPAYING);
        loanDto.setRecheckTime(new Date());
        loanDto.setProductType(ProductType._180);
        loanDto.setPledgeType(PledgeType.HOUSE);
        LoanModel loanModel = new LoanModel(loanDto);
        loanMapper.create(loanModel);
        return loanModel;
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

    private InvestModel createInvest(String loginName, long loanId) {
        InvestModel model = new InvestModel(IdGenerator.generate(), loanId, null, 1, loginName, new Date(), Source.WEB, null, 0.1);
        model.setStatus(InvestStatus.SUCCESS);
        investMapper.create(model);
        return model;
    }

    private List<InvestRepayModel> createInvestRepay(long investId) {
        InvestRepayModel model1 = new InvestRepayModel(IdGenerator.generate(), investId, 1, 0, 10, 1, new DateTime().plusDays(-60).toDate(), RepayStatus.REPAYING);
        InvestRepayModel model2 = new InvestRepayModel(IdGenerator.generate(), investId, 2, 0, 10, 1, new DateTime().plusDays(-30).toDate(), RepayStatus.REPAYING);
        InvestRepayModel model3 = new InvestRepayModel(IdGenerator.generate(), investId, 3, 0, 10, 1, new DateTime().plusDays(0).toDate(), RepayStatus.REPAYING);
        InvestRepayModel model4 = new InvestRepayModel(IdGenerator.generate(), investId, 4, 0, 10, 1, new DateTime().plusDays(30).toDate(), RepayStatus.REPAYING);
        InvestRepayModel model5 = new InvestRepayModel(IdGenerator.generate(), investId, 5, 0, 10, 1, new DateTime().plusDays(60).toDate(), RepayStatus.REPAYING);
        InvestRepayModel model6 = new InvestRepayModel(IdGenerator.generate(), investId, 6, 100, 10, 1, new DateTime().plusDays(90).toDate(), RepayStatus.REPAYING);
        List<InvestRepayModel> investRepayModels = Lists.newArrayList(model1, model2, model3, model4, model5, model6);
        investRepayMapper.create(investRepayModels);
        return investRepayModels;
    }

    @Test
    public void shouldInvestTransferApplyCancel() {
        long loanId = IdGenerator.generate();
        UserModel userModel = createUserByUserId("testuser");
        LoanModel loanModel = createLoanByUserId("testuser", loanId);
        InvestModel investModel = createInvest("testuser", loanId);
        TransferApplicationModel transferApplicationModel = new TransferApplicationModel(investModel, "ZR20151010-001", 2, 1, 1, new Date(), 3, Source.WEB);
        transferApplicationMapper.create(transferApplicationModel);
        InvestRepayModel investRepayModel = new InvestRepayModel();
        investRepayMapper.create(Lists.newArrayList(new InvestRepayModel(IdGenerator.generate(), investModel.getId(), loanModel.getPeriods(), 1L, 0L, 0L, new Date(), RepayStatus.REPAYING)));
        assertTrue(investTransferService.cancelTransferApplication(transferApplicationModel.getId()));
        TransferApplicationModel transferApplicationModel1 = transferApplicationMapper.findById(transferApplicationModel.getId());
        assertThat(transferApplicationModel1.getStatus(), is(TransferStatus.CANCEL));
    }

    @Test
    public void shouldInvestTransferApplyCancelFailed() {
        long loanId = IdGenerator.generate();
        UserModel userModel = createUserByUserId("testuser");
        LoanModel loanModel = createLoanByUserId("testuser", loanId);
        InvestModel investModel = createInvest("testuser", loanId);
        TransferApplicationModel transferApplicationModel = new TransferApplicationModel(investModel, "ZR20151010-001", 2, 1, 1, new Date(), 3, Source.WEB);
        transferApplicationModel.setStatus(TransferStatus.SUCCESS);
        transferApplicationMapper.create(transferApplicationModel);

        assertFalse(investTransferService.cancelTransferApplication(transferApplicationModel.getId()));
        TransferApplicationModel transferApplicationModel1 = transferApplicationMapper.findById(transferApplicationModel.getId());
        assertThat(transferApplicationModel1.getStatus(), is(TransferStatus.SUCCESS));
    }

    @Test
    public void shouldInvestTransferApply() throws Exception {
        long loanId = IdGenerator.generate();
        UserModel userModel = createUserByUserId("testuser");
        LoanModel loanModel = createLoanByUserId(userModel.getLoginName(), loanId);
        loanModel.setStatus(LoanStatus.REPAYING);
        loanMapper.update(loanModel);
        InvestModel investModel = createInvest(userModel.getLoginName(), loanId);

        LoanRepayModel repayingLoan1Repay1 = this.getFakeLoanRepayModel(loanModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(30).toDate(), null, 0, 1, 0, 0);
        LoanRepayModel repayingLoan1Repay2 = this.getFakeLoanRepayModel(loanModel, 2, RepayStatus.REPAYING, new DateTime().plusDays(60).toDate(), null, 1, 1, 0, 0);


        loanRepayMapper.create(Lists.newArrayList(repayingLoan1Repay1, repayingLoan1Repay2));

        TransferApplicationDto transferApplicationDto = new TransferApplicationDto();
        transferApplicationDto.setTransferInvestId(investModel.getId());
        transferApplicationDto.setTransferAmount(1L);
        investTransferService.investTransferApply(transferApplicationDto);

        List<TransferApplicationModel> transferApplicationModels = transferApplicationMapper.findByTransferInvestId(investModel.getId(), Lists.newArrayList(TransferStatus.TRANSFERRING));

        assertThat(transferApplicationModels.get(0).getName(), is(MessageFormat.format(TRANSFER_APPLY_NAME, new DateTime().toString("yyyyMMdd"), String.format("%03d", Integer.parseInt(redisWrapperClient.get(MessageFormat.format(redisTransferApplicationNumber, new DateTime().toString("yyyyMMdd"))))))));
        assertThat(transferApplicationModels.get(0).getPeriod(), is(1));
    }

    @Test
    public void shouldInvestTransferApplyFailWhenInvestIsApplied() throws Exception {
        long loanId = IdGenerator.generate();
        UserModel userModel = createUserByUserId("testuser");
        LoanModel loanModel = createLoanByUserId(userModel.getLoginName(), loanId);
        loanModel.setStatus(LoanStatus.REPAYING);
        loanMapper.update(loanModel);
        InvestModel investModel = createInvest(userModel.getLoginName(), loanId);

        LoanRepayModel repayingLoan1Repay1 = this.getFakeLoanRepayModel(loanModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(30).toDate(), null, 0, 1, 0, 0);
        LoanRepayModel repayingLoan1Repay2 = this.getFakeLoanRepayModel(loanModel, 2, RepayStatus.REPAYING, new DateTime().plusDays(60).toDate(), null, 1, 1, 0, 0);


        loanRepayMapper.create(Lists.newArrayList(repayingLoan1Repay1, repayingLoan1Repay2));

        TransferApplicationDto transferApplicationDto = new TransferApplicationDto();
        transferApplicationDto.setTransferInvestId(investModel.getId());
        transferApplicationDto.setTransferAmount(1L);
        investTransferService.investTransferApply(transferApplicationDto);

        List<TransferApplicationModel> transferApplicationModels = transferApplicationMapper.findByTransferInvestId(investModel.getId(), Lists.newArrayList(TransferStatus.TRANSFERRING));

        assertThat(transferApplicationModels.get(0).getName(), is(MessageFormat.format(TRANSFER_APPLY_NAME, new DateTime().toString("yyyyMMdd"), String.format("%03d", Integer.parseInt(redisWrapperClient.get(MessageFormat.format(redisTransferApplicationNumber, new DateTime().toString("yyyyMMdd"))))))));
        assertThat(transferApplicationModels.get(0).getPeriod(), is(1));

        assertFalse(investTransferService.investTransferApply(transferApplicationDto));
    }

    @Test
    public void shouldIsTransferByInvestTransfer() throws Exception {
        long loanId = IdGenerator.generate();
        UserModel userModel = createUserByUserId("testuser");
        LoanModel loanModel = createLoanByUserId("testuser", loanId);
        loanModel.setStatus(LoanStatus.REPAYING);
        loanMapper.update(loanModel);
        InvestModel investModel = createInvest("testuser", loanId);

        InvestModel investModelError = createInvest("testuser", loanId);

        LoanRepayModel repayingLoan1Repay1 = this.getFakeLoanRepayModel(loanModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(30).toDate(), null, 0, 1, 0, 0);
        LoanRepayModel repayingLoan1Repay2 = this.getFakeLoanRepayModel(loanModel, 2, RepayStatus.REPAYING, new DateTime().plusDays(60).toDate(), null, 1, 1, 0, 0);

        loanRepayMapper.create(Lists.newArrayList(repayingLoan1Repay1, repayingLoan1Repay2));

        TransferApplicationDto transferApplicationDto = new TransferApplicationDto();
        transferApplicationDto.setTransferInvestId(investModel.getId());
        transferApplicationDto.setTransferAmount(1L);

        investTransferService.investTransferApply(transferApplicationDto);

        List<TransferApplicationModel> transferApplicationModels = transferApplicationMapper.findByTransferInvestId(investModel.getId(), Lists.newArrayList(TransferStatus.TRANSFERRING));
        TransferApplicationModel transferApplicationModel = transferApplicationModels.get(0);
        transferApplicationModel.setStatus(TransferStatus.SUCCESS);
        transferApplicationModel.setInvestId(investModelError.getId());
        transferApplicationMapper.update(transferApplicationModel);

        boolean result = investTransferService.isTransferable(investModelError.getId());

        assertFalse(result);
    }

    @Test
    public void shouldIsTransferableByOverDaysLimit() {
        UserModel userModel = createUserByUserId("testuser");
        long loanId = IdGenerator.generate();
        LoanModel loanModel = createLoanByUserId("testuser", loanId);
        loanModel.setStatus(LoanStatus.REPAYING);
        loanMapper.update(loanModel);
        InvestModel investModel = createInvest("testuser", loanId);

        LoanRepayModel repayingLoan1Repay1 = this.getFakeLoanRepayModel(loanModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(4).toDate(), new DateTime().plusDays(4).toDate(), 0, 1, 0, 0);
        LoanRepayModel repayingLoan1Repay2 = this.getFakeLoanRepayModel(loanModel, 2, RepayStatus.REPAYING, new DateTime().plusDays(53).toDate(), new DateTime().plusDays(53).toDate(), 1, 1, 0, 0);

        loanRepayMapper.create(Lists.newArrayList(repayingLoan1Repay1, repayingLoan1Repay2));

        boolean result = investTransferService.isTransferable(investModel.getId());

        assertFalse(result);
    }

    @Test
    public void shouldIsTransferableByYesterdayCancel() {
        UserModel userModel = createUserByUserId("testuser");
        long loanId = IdGenerator.generate();
        LoanModel loanModel = createLoanByUserId("testuser", loanId);
        loanModel.setStatus(LoanStatus.REPAYING);
        loanMapper.update(loanModel);
        InvestModel investModel = createInvest("testuser", loanId);

        TransferApplicationModel transferApplicationModel = new TransferApplicationModel(investModel, "ZR", loanModel.getPeriods(), investModel.getAmount(), 0l, new Date(), loanModel.getPeriods(), Source.WEB);
        transferApplicationModel.setStatus(TransferStatus.CANCEL);
        transferApplicationMapper.create(transferApplicationModel);

        boolean result = investTransferService.isTransferable(investModel.getId());

        assertFalse(result);
    }

    @Test
    public void shouldFindWebTransferApplicationPaginationListIsSuccess() {
        long loanId = IdGenerator.generate();
        UserModel transferrerModel = createUserByUserId("transferrerTestuser");
        UserModel transfereeModel = createUserByUserId("transfereeTestUser");
        LoanModel loanModel = createLoanByUserId("transferrerTestUser", loanId);
        InvestModel transferrerInvestModel = createInvest(transferrerModel.getLoginName(), loanId);
        InvestModel transfereeInvestModel = createInvest(transfereeModel.getLoginName(), loanId);
        TransferApplicationModel transferApplicationModel = new TransferApplicationModel();
        transferApplicationModel.setLoginName(transferrerModel.getLoginName());
        transferApplicationModel.setName("name");
        transferApplicationModel.setTransferAmount(1000l);
        transferApplicationModel.setInvestAmount(1200l);
        transferApplicationModel.setTransferTime(new DateTime("2016-01-02").toDate());
        transferApplicationModel.setStatus(TransferStatus.TRANSFERRING);
        transferApplicationModel.setLoanId(loanId);
        transferApplicationModel.setTransferInvestId(transferrerInvestModel.getId());
        transferApplicationModel.setInvestId(transfereeInvestModel.getId());
        transferApplicationModel.setDeadline(new DateTime("2016-01-07").toDate());
        transferApplicationModel.setApplicationTime(new Date());
        transferApplicationMapper.create(transferApplicationModel);
        createInvestRepay(transferApplicationModel.getTransferInvestId());

        BasePaginationDataDto<TransferApplicationPaginationItemDataDto> basePaginationDataDto = investTransferService.findWebTransferApplicationPaginationList(transferrerInvestModel.getLoginName(), Lists.newArrayList(TransferStatus.TRANSFERRING), 1, 10);

        assertTrue(basePaginationDataDto.getStatus());
        assertNotNull(basePaginationDataDto.getRecords().get(0));
        assertEquals(1, basePaginationDataDto.getIndex());
        assertEquals(10, basePaginationDataDto.getPageSize());
        assertEquals(1, basePaginationDataDto.getCount());
        assertEquals("10.00", basePaginationDataDto.getRecords().get(0).getTransferAmount());
        assertEquals("12.00", basePaginationDataDto.getRecords().get(0).getInvestAmount());
        assertEquals(new DateTime("2016-01-02").toDate(), basePaginationDataDto.getRecords().get(0).getTransferTime());
        assertEquals(new DateTime("2016-01-07").toDate(), basePaginationDataDto.getRecords().get(0).getDeadLine());
        assertEquals(TransferStatus.TRANSFERRING.getDescription(), basePaginationDataDto.getRecords().get(0).getTransferStatus());
    }

    @Test
    public void shouldValidTransferIsDayLimitIsOk() {
        TransferRuleModel transferRuleModel = new TransferRuleModel();
        transferRuleModel.setId(1);
        transferRuleModel.setDaysLimit(1);
        transferRuleMapper.update(transferRuleModel);

        long loanId = IdGenerator.generate();
        createUserByUserId("transferrerTestuser");
        createUserByUserId("transfereeTestUser");
        createLoanByUserId("transferrerTestUser", loanId);
        createLoanRepayModel(loanId);

        assertTrue(!investTransferService.validTransferIsDayLimit(loanId));
    }

    @Test
    public void shouldValidTransferIsCanceledIsOk() {
        String loginName = "transferrerTestuser";
        long loanId = IdGenerator.generate();
        createUserByUserId(loginName);
        createUserByUserId("transfereeTestUser");
        LoanModel loanModel = createLoanByUserId("transferrerTestUser", loanId);

        InvestModel transferrerInvestModel = new InvestModel(IdGenerator.generate(), loanId, null, 1, loginName, new Date(), Source.WEB, null, 0.1);
        transferrerInvestModel.setStatus(InvestStatus.SUCCESS);
        investMapper.create(transferrerInvestModel);

        InvestModel transfereeInvestModel = new InvestModel(IdGenerator.generate(), loanId, null, 1, loginName, new Date(), Source.WEB, null, 0.1);
        transferrerInvestModel.setStatus(InvestStatus.SUCCESS);
        investMapper.create(transfereeInvestModel);

        TransferApplicationModel transferApplicationModel = new TransferApplicationModel();
        transferApplicationModel.setLoginName(loginName);
        transferApplicationModel.setName("name");
        transferApplicationModel.setTransferAmount(1000l);
        transferApplicationModel.setInvestAmount(1200l);
        transferApplicationModel.setTransferTime(new DateTime("2016-01-02").toDate());
        transferApplicationModel.setStatus(TransferStatus.TRANSFERRING);
        transferApplicationModel.setLoanId(loanModel.getId());
        transferApplicationModel.setTransferInvestId(transferrerInvestModel.getId());
        transferApplicationModel.setInvestId(transfereeInvestModel.getId());
        transferApplicationModel.setDeadline(new Date());
        transferApplicationModel.setApplicationTime(new Date());
        transferApplicationMapper.create(transferApplicationModel);

        assertTrue(!investTransferService.validTransferIsCanceled(transferrerInvestModel.getId()));

        transferApplicationModel.setStatus(TransferStatus.CANCEL);
        transferApplicationModel.setApplicationTime(DateTime.now().plusDays(-1).toDate());
        transferApplicationMapper.update(transferApplicationModel);
        assertTrue(investTransferService.validTransferIsCanceled(transferrerInvestModel.getId()));
    }

    public List<LoanRepayModel> createLoanRepayModel(long loanId) {
        List<LoanRepayModel> loanRepayModels = Lists.newArrayList();
        LoanRepayModel loanRepayModel = new LoanRepayModel();
        loanRepayModel.setId(IdGenerator.generate());
        loanRepayModel.setDefaultInterest(0);
        loanRepayModel.setActualInterest(0);
        loanRepayModel.setPeriod(1);
        loanRepayModel.setStatus(RepayStatus.REPAYING);
        loanRepayModel.setLoanId(loanId);
        loanRepayModel.setRepayDate(DateTime.now().plusDays(-3).toDate());
        loanRepayModel.setCorpus(0);
        loanRepayModel.setExpectedInterest(0);
        loanRepayModels.add(loanRepayModel);

        LoanRepayModel loanRepayModel2 = new LoanRepayModel();
        loanRepayModel2.setId(IdGenerator.generate());
        loanRepayModel2.setDefaultInterest(0);
        loanRepayModel2.setActualInterest(0);
        loanRepayModel2.setPeriod(2);
        loanRepayModel2.setStatus(RepayStatus.REPAYING);
        loanRepayModel2.setLoanId(loanId);
        loanRepayModel2.setRepayDate(DateTime.now().plusDays(-3).toDate());
        loanRepayModel2.setCorpus(0);
        loanRepayModel2.setExpectedInterest(0);
        loanRepayModels.add(loanRepayModel2);

        loanRepayMapper.create(loanRepayModels);
        return loanRepayModels;
    }
}
