package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.TransferApplicationPaginationItemDataDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.dto.TransferApplicationDto;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class InvestTransferServiceTest {

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private InvestTransferService investTransferService;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    public static String redisTransferApplicationNumber = "web:{0}:transferApplicationNumber";

    private final static String TRANSFER_APPLY_NAME = "ZR{0}-{1}";

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
        loanDto.setPeriods(30);
        loanDto.setActivityType(ActivityType.NORMAL);
        loanDto.setContractId(123);
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        loanDto.setInvestFeeRate("15");
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("10000");
        loanDto.setType(LoanType.LOAN_INTEREST_MONTHLY_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.REPAYING);
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

    private InvestModel createInvest(String loginName, long loanId) {
        InvestModel model = new InvestModel(idGenerator.generate(), loanId, null, 1, loginName, Source.WEB, null);
        model.setCreatedTime(new Date());
        model.setStatus(InvestStatus.SUCCESS);
        investMapper.create(model);
        return model;
    }

    @Test
    public void shouldInvestTransferApplyCancel() {
        long loanId = idGenerator.generate();
        UserModel userModel = createUserByUserId("testuser");
        LoanModel loanModel = createLoanByUserId("testuser", loanId);
        InvestModel investModel = createInvest("testuser", loanId);
        TransferApplicationModel transferApplicationModel = new TransferApplicationModel(investModel, "ZR20151010-001", 2, 1, 1, new Date(),30);
        transferApplicationMapper.create(transferApplicationModel);

        assertTrue(investTransferService.cancelTransferApplication(transferApplicationModel.getId()));
        TransferApplicationModel transferApplicationModel1 = transferApplicationMapper.findById(transferApplicationModel.getId());
        assertThat(transferApplicationModel1.getStatus(), is(TransferStatus.CANCEL));
    }

    @Test
    public void shouldInvestTransferApplyCancelFailed() {
        long loanId = idGenerator.generate();
        UserModel userModel = createUserByUserId("testuser");
        LoanModel loanModel = createLoanByUserId("testuser", loanId);
        InvestModel investModel = createInvest("testuser", loanId);
        TransferApplicationModel transferApplicationModel = new TransferApplicationModel(investModel, "ZR20151010-001", 2, 1, 1, new Date(),30);
        transferApplicationModel.setStatus(TransferStatus.SUCCESS);
        transferApplicationMapper.create(transferApplicationModel);

        assertFalse(investTransferService.cancelTransferApplication(transferApplicationModel.getId()));
        TransferApplicationModel transferApplicationModel1 = transferApplicationMapper.findById(transferApplicationModel.getId());
        assertThat(transferApplicationModel1.getStatus(), is(TransferStatus.SUCCESS));
    }

    @Test
    public void shouldInvestTransferApply() throws Exception{
        long loanId = idGenerator.generate();
        UserModel userModel = createUserByUserId("testuser");
        LoanModel loanModel = createLoanByUserId("testuser", loanId);
        loanModel.setStatus(LoanStatus.REPAYING);
        loanMapper.update(loanModel);
        InvestModel investModel = createInvest("testuser", loanId);

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
    public void shouldIsTransferByInvestTransfer() throws Exception {
        long loanId = idGenerator.generate();
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

        List<TransferApplicationModel> transferApplicationModels = transferApplicationMapper.findByTransferInvestId(investModel.getId(),Lists.newArrayList(TransferStatus.TRANSFERRING));
        TransferApplicationModel transferApplicationModel = transferApplicationModels.get(0);
        transferApplicationModel.setStatus(TransferStatus.SUCCESS);
        transferApplicationModel.setInvestId(investModelError.getId());
        transferApplicationMapper.update(transferApplicationModel);

        boolean result = investTransferService.isTransferable(investModelError.getId());

        assertFalse(result);
    }
    @Test
    public void shouldIsTransferableByOverDaysLimit(){
        long loanId = idGenerator.generate();
        LoanModel loanModel = createLoanByUserId("testuser", loanId);
        loanModel.setStatus(LoanStatus.REPAYING);
        loanMapper.update(loanModel);
        InvestModel investModel = createInvest("testuser", loanId);

        LoanRepayModel repayingLoan1Repay1 = this.getFakeLoanRepayModel(loanModel, 1, RepayStatus.REPAYING, new DateTime().plusDays(7).toDate(), new DateTime().plusDays(7).toDate(), 0, 1, 0, 0);
        LoanRepayModel repayingLoan1Repay2 = this.getFakeLoanRepayModel(loanModel, 2, RepayStatus.REPAYING, new DateTime().plusDays(60).toDate(), new DateTime().plusDays(60).toDate(), 1, 1, 0, 0);

        loanRepayMapper.create(Lists.newArrayList(repayingLoan1Repay1, repayingLoan1Repay2));

        boolean result = investTransferService.isTransferable(investModel.getId());

        assertFalse(result);
    }
    @Test
    public void shouldFindTransferApplicationPaginationListIsSuccess(){
        long loanId = idGenerator.generate();
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
        transferApplicationModel.setLoanId(loanModel.getId());
        transferApplicationModel.setTransferInvestId(transferrerInvestModel.getId());
        transferApplicationModel.setInvestId(transfereeInvestModel.getId());
        transferApplicationModel.setDeadline(new Date());
        transferApplicationModel.setApplicationTime(new Date());
        transferApplicationMapper.create(transferApplicationModel);

        BasePaginationDataDto<TransferApplicationPaginationItemDataDto> basePaginationDataDto =  investTransferService.findTransferApplicationPaginationList(null, null, null,null,null,transfereeModel.getLoginName(),null,1,10);

        assertTrue(basePaginationDataDto.getStatus());
        assertNotNull(basePaginationDataDto.getRecords().get(0));
        assertEquals(1, basePaginationDataDto.getIndex());
        assertEquals(10,basePaginationDataDto.getPageSize());
        assertEquals(1,basePaginationDataDto.getCount());
        assertEquals("10.00", basePaginationDataDto.getRecords().get(0).getTransferAmount());
        assertEquals("12.00", basePaginationDataDto.getRecords().get(0).getInvestAmount());
        assertEquals(new DateTime("2016-01-02").toDate(), basePaginationDataDto.getRecords().get(0).getTransferTime());
        assertEquals(TransferStatus.TRANSFERRING, basePaginationDataDto.getRecords().get(0).getTransferStatus());
        assertEquals(transfereeInvestModel.getLoginName(), basePaginationDataDto.getRecords().get(0).getTransfereeLoginName());
        assertEquals(transferrerInvestModel.getLoginName(), basePaginationDataDto.getRecords().get(0).getTransferrerLoginName());
    }

}
