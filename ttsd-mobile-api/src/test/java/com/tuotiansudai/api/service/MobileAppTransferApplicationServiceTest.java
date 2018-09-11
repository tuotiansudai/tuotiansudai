package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppTransferApplicationServiceImpl;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.membership.service.MembershipPrivilegePurchaseService;
import com.tuotiansudai.membership.service.UserMembershipService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.transfer.service.TransferService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;


public class MobileAppTransferApplicationServiceTest extends ServiceTestBase {

    @InjectMocks
    private MobileAppTransferApplicationServiceImpl mobileAppTransferApplicationService;
    @Mock
    private TransferApplicationMapper transferApplicationMapper;
    @Mock
    private InvestTransferService investTransferService;
    @Mock
    private TransferService transferService;
    @Mock
    private InvestMapper investMapper;
    @Mock
    private LoanMapper loanMapper;
    @Mock
    private LoanRepayMapper loanRepayMapper;
    @Mock
    private BankAccountMapper bankAccountMapper;
    @Mock
    private TransferRuleMapper transferRuleMapper;
    @Mock
    private InvestRepayMapper investRepayMapper;
    @Mock
    private PageValidUtils pageValidUtils;
    @Mock
    private LoanDetailsMapper loanDetailsMapper;

    @Mock
    private MembershipPrivilegePurchaseService membershipPrivilegePurchaseService;

    @Mock
    private UserMembershipService userMembershipService;

    @Test
    public void shouldGenerateTransferApplicationIsSuccess() {
        TransferApplicationRecordView transferApplicationRecordDto = createTransferApplicationRecordView();
        TransferApplicationRequestDto transferApplicationRequestDto = new TransferApplicationRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("test");
        transferApplicationRequestDto.setBaseParam(baseParam);
        transferApplicationRequestDto.setPageSize(10);
        transferApplicationRequestDto.setIndex(1);
        transferApplicationRequestDto.setTransferStatus(Lists.newArrayList(TransferStatus.TRANSFERRING));
        LoanModel loanModel = createLoanByUserId("test", IdGenerator.generate());
        InvestRepayModel investRepayModel = createInvestRepayModel(transferApplicationRecordDto.getTransferInvestId(), loanModel.getPeriods());
        List<TransferApplicationRecordView> transferApplicationRecordDtos = Lists.newArrayList(transferApplicationRecordDto);

        when(transferApplicationMapper.findTransferApplicationPaginationByLoginName(anyString(), any(List.class), anyInt(), anyInt())).thenReturn(transferApplicationRecordDtos);
        when(transferApplicationMapper.findCountTransferApplicationPaginationByLoginName(anyString(), any(List.class))).thenReturn(1);
        when(loanMapper.findById(transferApplicationRecordDto.getLoanId())).thenReturn(loanModel);
        when(investRepayMapper.findByInvestIdAndPeriod(transferApplicationRecordDto.getTransferInvestId(), loanModel.getPeriods())).thenReturn(investRepayModel);
        when(pageValidUtils.validPageSizeLimit(anyInt())).thenReturn(10);

        BaseResponseDto<TransferApplicationResponseDataDto> baseResponseDto = mobileAppTransferApplicationService.generateTransferApplication(transferApplicationRequestDto);
        assertEquals(TransferStatus.TRANSFERRING, baseResponseDto.getData().getTransferApplication().get(0).getTransferStatus());
        assertEquals("17", baseResponseDto.getData().getTransferApplication().get(0).getActivityRate());
        assertEquals("16", baseResponseDto.getData().getTransferApplication().get(0).getBaseRate());
        assertEquals("name", baseResponseDto.getData().getTransferApplication().get(0).getName());
        assertEquals("10.00", baseResponseDto.getData().getTransferApplication().get(0).getTransferAmount());
        assertEquals("12.00", baseResponseDto.getData().getTransferApplication().get(0).getInvestAmount());
        assertEquals("2016-02-09 00:00:00", baseResponseDto.getData().getTransferApplication().get(0).getTransferTime());
        assertEquals("4", baseResponseDto.getData().getTransferApplication().get(0).getLeftPeriod());
        assertEquals("400", baseResponseDto.getData().getTransferApplication().get(0).getLeftDays());

        investRepayModel.setRepayDate(DateTime.now().plusDays(-90).toDate());
        when(investRepayMapper.findByInvestIdAndPeriod(transferApplicationRecordDto.getInvestId(), loanModel.getPeriods())).thenReturn(investRepayModel);
        baseResponseDto = mobileAppTransferApplicationService.generateTransferApplication(transferApplicationRequestDto);
        assertEquals("0", baseResponseDto.getData().getTransferApplication().get(0).getLeftDays());
    }
    @Test
    public void shouldGenerateTransfereeApplicationIsSuccess() {
        TransferApplicationRecordView transferApplicationRecordView = createTransferApplicationRecordView();
        PaginationRequestDto paginationRequestDto = new PaginationRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("test");
        paginationRequestDto.setBaseParam(baseParam);
        paginationRequestDto.setPageSize(10);
        paginationRequestDto.setIndex(1);
        List<TransferApplicationRecordView> transferApplicationRecordDtos = Lists.newArrayList(transferApplicationRecordView);
        LoanModel loanModel = createLoanByUserId("test", transferApplicationRecordView.getLoanId());
        InvestRepayModel investRepayModel = createInvestRepay("test", transferApplicationRecordView.getInvestId(), 100, loanModel.getPeriods());

        when(transferApplicationMapper.findTransfereeApplicationPaginationByLoginName(anyString(), anyInt(), anyInt())).thenReturn(transferApplicationRecordDtos);
        when(transferApplicationMapper.findCountTransfereeApplicationPaginationByLoginName(anyString())).thenReturn(1);
        when(loanMapper.findById(transferApplicationRecordView.getLoanId())).thenReturn(loanModel);
        when(investRepayMapper.findByInvestIdAndPeriod(transferApplicationRecordView.getInvestId(), loanModel.getPeriods())).thenReturn(investRepayModel);
        when(pageValidUtils.validPageSizeLimit(anyInt())).thenReturn(10);

        BaseResponseDto<TransferApplicationResponseDataDto> baseResponseDto = mobileAppTransferApplicationService.generateTransfereeApplication(paginationRequestDto);
        assertEquals(TransferStatus.TRANSFERRING, baseResponseDto.getData().getTransferApplication().get(0).getTransferStatus());
        assertEquals("17", baseResponseDto.getData().getTransferApplication().get(0).getActivityRate());
        assertEquals("16", baseResponseDto.getData().getTransferApplication().get(0).getBaseRate());
        assertEquals("name", baseResponseDto.getData().getTransferApplication().get(0).getName());
        assertEquals("10.00", baseResponseDto.getData().getTransferApplication().get(0).getTransferAmount());
        assertEquals("12.00", baseResponseDto.getData().getTransferApplication().get(0).getInvestAmount());
        assertEquals("2016-02-09 00:00:00", baseResponseDto.getData().getTransferApplication().get(0).getTransferTime());
        assertEquals("4", baseResponseDto.getData().getTransferApplication().get(0).getLeftPeriod());
        assertEquals("90", baseResponseDto.getData().getTransferApplication().get(0).getLeftDays());

        investRepayModel.setRepayDate(DateTime.now().plusDays(-90).toDate());
        when(investRepayMapper.findByInvestIdAndPeriod(transferApplicationRecordView.getInvestId(), loanModel.getPeriods())).thenReturn(investRepayModel);
        baseResponseDto = mobileAppTransferApplicationService.generateTransfereeApplication(paginationRequestDto);
        assertEquals("0", baseResponseDto.getData().getTransferApplication().get(0).getLeftDays());
    }

    private TransferApplicationRecordView createTransferApplicationRecordView() {
        TransferApplicationRecordView transferApplicationRecordView = new TransferApplicationRecordView();
        transferApplicationRecordView.setLoanId(IdGenerator.generate());
        transferApplicationRecordView.setTransferInvestId(IdGenerator.generate());
        transferApplicationRecordView.setName("name");
        transferApplicationRecordView.setTransferAmount(1000);
        transferApplicationRecordView.setInvestAmount(1200);
        transferApplicationRecordView.setTransferTime(new DateTime("2016-02-09").toDate());
        transferApplicationRecordView.setBaseRate(0.16);
        transferApplicationRecordView.setActivityRate(0.17);
        transferApplicationRecordView.setTransferStatus(TransferStatus.TRANSFERRING);
        transferApplicationRecordView.setLeftPeriod(4);
        return transferApplicationRecordView;

    }

    @Test
    public void shouldTransferApplyIsSuccess() throws Exception {
        long loanId = IdGenerator.generate();
        TransferRuleModel transferRuleModel = new TransferRuleModel();
        transferRuleModel.setLevelOneFee(0.01);
        transferRuleModel.setLevelOneLower(1);
        transferRuleModel.setLevelOneUpper(29);
        transferRuleModel.setLevelTwoFee(0.005);
        transferRuleModel.setLevelTwoLower(30);
        transferRuleModel.setLevelTwoUpper(90);
        transferRuleModel.setLevelThreeFee(0.005);
        transferRuleModel.setLevelThreeLower(91);
        transferRuleModel.setLevelThreeUpper(365);
        transferRuleModel.setDiscount(0.005);
        transferRuleModel.setDaysLimit(10);

        InvestModel investModel = createInvest("testuser", loanId);

        LoanModel loanModel = new LoanModel();
        loanModel.setStatus(LoanStatus.RAISING);

        when(investTransferService.investTransferApply(any(TransferApplicationDto.class))).thenReturn(true);
        when(investMapper.findById(anyLong())).thenReturn(investModel);
        when(transferRuleMapper.find()).thenReturn(transferRuleModel);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(investTransferService.validTransferIsDayLimit(anyLong())).thenReturn(true);
        when(investTransferService.validTransferIsCanceled(anyLong())).thenReturn(true);
        when(investRepayMapper.findByInvestIdAndPeriodAsc(anyLong())).thenReturn(Lists.newArrayList(new InvestRepayModel()));
        TransferApplyRequestDto transferApplyRequestDto = new TransferApplyRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setPlatform("Android");
        transferApplyRequestDto.setBaseParam(baseParam);
        transferApplyRequestDto.setTransferInvestId("123");
        transferApplyRequestDto.setTransferAmount("99.50");
        BaseResponseDto baseResponseDto = mobileAppTransferApplicationService.transferApply(transferApplyRequestDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
    }

    @Test
    public void shouldTransferApplyValidIsSuccess() throws Exception {
        long loanId = IdGenerator.generate();
        TransferRuleModel transferRuleModel = new TransferRuleModel();
        transferRuleModel.setLevelOneFee(0.01);
        transferRuleModel.setLevelOneLower(1);
        transferRuleModel.setLevelOneUpper(29);
        transferRuleModel.setLevelTwoFee(0.005);
        transferRuleModel.setLevelTwoLower(30);
        transferRuleModel.setLevelTwoUpper(90);
        transferRuleModel.setLevelThreeFee(0.005);
        transferRuleModel.setLevelThreeLower(91);
        transferRuleModel.setLevelThreeUpper(365);
        transferRuleModel.setDiscount(0.005);
        transferRuleModel.setDaysLimit(10);

        InvestModel investModel = createInvest("testuser", loanId);

        LoanModel loanModel = new LoanModel();
        loanModel.setStatus(LoanStatus.RAISING);

        when(investTransferService.investTransferApply(any(TransferApplicationDto.class))).thenReturn(true);
        when(investMapper.findById(anyLong())).thenReturn(investModel);
        when(transferRuleMapper.find()).thenReturn(transferRuleModel);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(investTransferService.validTransferIsDayLimit(anyLong())).thenReturn(true);
        when(investTransferService.validTransferIsCanceled(anyLong())).thenReturn(false);
        when(investRepayMapper.findByInvestIdAndPeriodAsc(anyLong())).thenReturn(Lists.newArrayList(new InvestRepayModel()));

        TransferApplyRequestDto transferApplyRequestDto = new TransferApplyRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setPlatform("Android");
        transferApplyRequestDto.setBaseParam(baseParam);
        transferApplyRequestDto.setTransferInvestId("123");
        transferApplyRequestDto.setTransferAmount("99.50");
        BaseResponseDto baseResponseDto = mobileAppTransferApplicationService.transferApply(transferApplyRequestDto);
        assertEquals(ReturnMessage.TRANSFER_ALREADY_CANCELED_TODAY.getCode(), baseResponseDto.getCode());

        when(investTransferService.validTransferIsDayLimit(anyLong())).thenReturn(false);
        baseResponseDto = mobileAppTransferApplicationService.transferApply(transferApplyRequestDto);
        assertEquals(ReturnMessage.TRANSFER_IMPEND_REPAYING.getCode(), baseResponseDto.getCode());

        LoanModel overdueLoanModel = new LoanModel();
        overdueLoanModel.setStatus(LoanStatus.OVERDUE);
        when(loanMapper.findById(anyLong())).thenReturn(overdueLoanModel);
        baseResponseDto = mobileAppTransferApplicationService.transferApply(transferApplyRequestDto);
        assertEquals(ReturnMessage.TRANSFER_IS_OVERDUE.getCode(), baseResponseDto.getCode());
    }

    @Test
    public void shouldTransferPurchaseIsSuccess() throws Exception {
        long transferApplicationId = IdGenerator.generate();
        long investId = IdGenerator.generate();

        BankAccountModel accountModel = createAccountByUserId("testuser");
        TransferPurchaseRequestDto transferPurchaseRequestDto = new TransferPurchaseRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("testuser");
        transferPurchaseRequestDto.setBaseParam(baseParam);
        transferPurchaseRequestDto.setTransferApplicationId(String.valueOf(transferApplicationId));


        TransferApplicationModel transferApplicationModel = new TransferApplicationModel();
        transferApplicationModel.setTransferAmount(90000);
        transferApplicationModel.setInvestAmount(100000);
        transferApplicationModel.setLoginName("testuser");
        transferApplicationModel.setInvestId(investId);
        transferApplicationModel.setPeriod(2);
        transferApplicationModel.setId(transferApplicationId);

        InvestRepayModel investRepayModel1 = createInvestRepay("testuser", investId, 0, 1);
        InvestRepayModel investRepayModel2 = createInvestRepay("testuser", investId, 0, 2);
        InvestRepayModel investRepayModel3 = createInvestRepay("testuser", investId, 100000, 3);
        List<InvestRepayModel> investRepayModels = new ArrayList<InvestRepayModel>();
        investRepayModels.add(investRepayModel1);
        investRepayModels.add(investRepayModel2);
        investRepayModels.add(investRepayModel3);

        when(transferApplicationMapper.findById(anyLong())).thenReturn(transferApplicationModel);
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), any())).thenReturn(accountModel);
        when(investRepayMapper.findByInvestIdAndPeriodAsc(anyLong())).thenReturn(investRepayModels);
        when(membershipPrivilegePurchaseService.obtainServiceFee(anyString())).thenReturn(0.4);
        when(userMembershipService.obtainServiceFee(anyString())).thenReturn(0.1);

        BaseResponseDto<TransferPurchaseResponseDataDto> baseResponseDto = mobileAppTransferApplicationService.transferPurchase(transferPurchaseRequestDto);

        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());

        assertEquals("1000.00", baseResponseDto.getData().getBalance());
        assertEquals("900.00", baseResponseDto.getData().getTransferAmount());
        assertEquals("0.22", baseResponseDto.getData().getExpectedInterestAmount());
    }

    @Test
    public void shouldTransferApplicationCancelIsSuccess() throws Exception {
        TransferCancelRequestDto transferCancelRequestDto = new TransferCancelRequestDto();
        transferCancelRequestDto.setTransferApplicationId(100000L);
        when(investTransferService.cancelTransferApplicationManually(transferCancelRequestDto.getTransferApplicationId())).thenReturn(true);
        BaseResponseDto baseResponseDto = mobileAppTransferApplicationService.transferApplicationCancel(transferCancelRequestDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
    }

    @Test
    public void shouldTransferApplyQueryIsSuccess() {
        long loanId = IdGenerator.generate();
        UserModel userModel = createUserByUserId("testuser");
        LoanModel loanModel = createLoanByUserId("testuser", loanId);
        InvestModel investModel = createInvest("testuser", loanId);

        LoanRepayModel repayingLoan1Repay1 = this.getFakeLoanRepayModel(loanModel, 1, RepayStatus.COMPLETE, new DateTime().minusDays(9).toDate(), new DateTime().minusDays(10).toDate(), 0, 1, 0, 0);
        LoanRepayModel repayingLoan1Repay2 = this.getFakeLoanRepayModel(loanModel, 2, RepayStatus.REPAYING, new DateTime().plusDays(30).toDate(), new DateTime().plusDays(30).toDate(), 1, 1, 0, 0);
        List<LoanRepayModel> repayingLoanRepays = Lists.newArrayList(repayingLoan1Repay1, repayingLoan1Repay2);
        TransferRuleModel transferRuleModel = new TransferRuleModel();
        transferRuleModel.setLevelOneFee(0.01);
        transferRuleModel.setLevelOneLower(1);
        transferRuleModel.setLevelOneUpper(29);

        transferRuleModel.setLevelTwoFee(0.005);
        transferRuleModel.setLevelTwoLower(30);
        transferRuleModel.setLevelTwoUpper(90);

        transferRuleModel.setLevelThreeFee(0.005);
        transferRuleModel.setLevelThreeLower(91);
        transferRuleModel.setLevelThreeUpper(365);

        transferRuleModel.setDiscount(0.005);

        when(investTransferService.isTransferable(anyLong())).thenReturn(true);
        when(investTransferService.getDeadlineFromNow()).thenReturn(new DateTime().withTimeAtStartOfDay().toDate());
        when(investMapper.findById(anyLong())).thenReturn(investModel);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(loanRepayMapper.findByLoanIdOrderByPeriodAsc(anyLong())).thenReturn(repayingLoanRepays);
        when(transferRuleMapper.find()).thenReturn(transferRuleModel);

        TransferApplyQueryRequestDto transferApplyQueryRequestDto = new TransferApplyQueryRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(userModel.getLoginName());
        transferApplyQueryRequestDto.setBaseParam(baseParam);
        transferApplyQueryRequestDto.setInvestId(String.valueOf(investModel.getId()));
        BaseResponseDto<TransferApplyQueryResponseDataDto> baseResponseDto = mobileAppTransferApplicationService.transferApplyQuery(transferApplyQueryRequestDto);
        assertEquals("100.00", baseResponseDto.getData().getInvestAmount());
        assertEquals("1.00", baseResponseDto.getData().getTransferFee());
        assertEquals(new DateTime().withTimeAtStartOfDay().toString("yyyy/MM/dd HH:mm:ss"),baseResponseDto.getData().getDeadLine());
        assertEquals("99.49",baseResponseDto.getData().getDiscountLower());
        assertEquals(AmountConverter.convertCentToString(investModel.getAmount()),baseResponseDto.getData().getDiscountUpper());
    }

    @Test
    public void sholudTransferApplicationListIsSuccess(){
        TransferApplicationModel transferApplicationModel1 = createTransferAppLication("ZR0001", 100001, 1000001, 10000001, 2, "testuer1", TransferStatus.SUCCESS);
        List<TransferApplicationPaginationItemDataDto> transferApplicationRecordDtos = new ArrayList<>();
        transferApplicationRecordDtos.add(createTransferApplicationRecordView(transferApplicationModel1));
        List<InvestRepayModel> investRepayModels = new ArrayList<InvestRepayModel>();
        investRepayModels.add(createInvestRepayModel(10000001,1));
        investRepayModels.add(createInvestRepayModel(10000001,2));
        investRepayModels.add(createInvestRepayModel(10000001,3));

        TransferApplicationListRequestDto transferApplicationListRequestDto = new TransferApplicationListRequestDto();
        transferApplicationListRequestDto.setRateLower("");
        transferApplicationListRequestDto.setRateUpper("");
        transferApplicationListRequestDto.setIndex(1);
        transferApplicationListRequestDto.setPageSize(10);

        BasePaginationDataDto<TransferApplicationPaginationItemDataDto> transferApplicationRecordDto = new BasePaginationDataDto(1, 10, 1, transferApplicationRecordDtos);

        when(transferService.findAllTransferApplicationPaginationList(anyList(), anyDouble(), anyDouble(), anyInt(), anyInt())).thenReturn(transferApplicationRecordDto);
        when(transferService.findCountAllTransferApplicationPaginationList(anyList(), anyDouble(), anyDouble())).thenReturn(1);
        when(pageValidUtils.validPageSizeLimit(anyInt())).thenReturn(10);

        BaseResponseDto<TransferApplicationResponseDataDto> baseResponseDto = mobileAppTransferApplicationService.transferApplicationList(transferApplicationListRequestDto);

        assertEquals("ZR0001", baseResponseDto.getData().getTransferApplication().get(0).getName());
        assertEquals("500", baseResponseDto.getData().getTransferApplication().get(0).getInvestAmount());
        assertEquals("450", baseResponseDto.getData().getTransferApplication().get(0).getTransferAmount());
        assertEquals("1", baseResponseDto.getData().getTransferApplication().get(0).getActivityRate());
        assertEquals("12", baseResponseDto.getData().getTransferApplication().get(0).getBaseRate());
        assertEquals(TransferStatus.SUCCESS, baseResponseDto.getData().getTransferApplication().get(0).getTransferStatus());
        assertEquals("400", baseResponseDto.getData().getTransferApplication().get(0).getLeftDays());
    }

    @Test
    public void sholudTransferApplicationDetailIsSuccess(){
        TransferApplicationModel transferApplicationModel1 = createTransferAppLication("ZR0001", 100001, 1000001, 10000001, 2, "testuer1", TransferStatus.SUCCESS);
        TransferApplicationDetailRequestDto transferApplicationDetailRequestDto = new TransferApplicationDetailRequestDto();
        transferApplicationDetailRequestDto.setTransferApplicationId(transferApplicationModel1.getId());
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("testuer1");
        transferApplicationDetailRequestDto.setBaseParam(baseParam);
        LoanModel loanModel = createLoanByUserId("testuer1",10001);
        InvestRepayModel investRepayModel = new InvestRepayModel();
        investRepayModel.setRepayDate(DateTime.now().toDate());
        TransferApplicationDetailDto transferApplicationDetailDto = createTransferApplicationDetailDto(transferApplicationModel1, loanModel);
        when(transferService.getTransferApplicationDetailDto(anyLong(),anyString(), anyInt())).thenReturn(transferApplicationDetailDto);
        when(loanMapper.findById(anyLong())).thenReturn(new LoanModel());
        when(transferApplicationMapper.findById(anyLong())).thenReturn(new TransferApplicationModel());
        when(investRepayMapper.findByInvestIdAndPeriod(anyLong(),anyInt())).thenReturn(investRepayModel);
        when(loanDetailsMapper.getByLoanId(anyLong())).thenReturn(new LoanDetailsModel());

        BaseResponseDto<TransferApplicationDetailResponseDataDto> transferApplicationDetailResponseDataDto = mobileAppTransferApplicationService.transferApplicationById(transferApplicationDetailRequestDto);

        assertEquals("ZR0001", transferApplicationDetailResponseDataDto.getData().getName());
        assertEquals("10001", transferApplicationDetailResponseDataDto.getData().getLoanId());
        assertEquals("店铺资金周转", transferApplicationDetailResponseDataDto.getData().getLoanName());

    }

    private InvestRepayModel createInvestRepayModel(long investId, int period) {
        InvestRepayModel investRepayModel = new InvestRepayModel();
        investRepayModel.setInvestId(investId);
        investRepayModel.setPeriod(period);
        investRepayModel.setRepayDate(new DateTime().plusDays(400).toDate());
        return investRepayModel;
    }

    private TransferApplicationPaginationItemDataDto createTransferApplicationRecordView(TransferApplicationModel transferApplicationModel) {
        TransferApplicationPaginationItemDataDto transferApplicationPaginationItemDataDto = new TransferApplicationPaginationItemDataDto();
        transferApplicationPaginationItemDataDto.setTransferApplicationId(String.valueOf(transferApplicationModel.getId()));
        transferApplicationPaginationItemDataDto.setName(transferApplicationModel.getName());
        transferApplicationPaginationItemDataDto.setInvestAmount(String.valueOf(transferApplicationModel.getInvestAmount()));
        transferApplicationPaginationItemDataDto.setTransferAmount(String.valueOf(transferApplicationModel.getTransferAmount()));
        transferApplicationPaginationItemDataDto.setBaseRate(12);
        transferApplicationPaginationItemDataDto.setActivityRate(1);
        transferApplicationPaginationItemDataDto.setTransferStatus(transferApplicationModel.getStatus().name());
        transferApplicationPaginationItemDataDto.setLeftDays("400");

        return transferApplicationPaginationItemDataDto;
    }

    private TransferApplicationModel createTransferAppLication(String name, long loanId, long transferInvestId, long investId,int period, String loginName, TransferStatus transferStatus) {
        TransferApplicationModel transferApplicationModel = new TransferApplicationModel();
        transferApplicationModel.setId(IdGenerator.generate());
        transferApplicationModel.setName(name);
        transferApplicationModel.setLoanId(loanId);
        transferApplicationModel.setTransferInvestId(transferInvestId);
        transferApplicationModel.setInvestId(investId);
        transferApplicationModel.setPeriod(period);
        transferApplicationModel.setLoginName(loginName);
        transferApplicationModel.setInvestAmount(500);
        transferApplicationModel.setTransferAmount(450);
        transferApplicationModel.setStatus(transferStatus);
        transferApplicationModel.setTransferTime(new Date());
        transferApplicationModel.setApplicationTime(new Date());
        transferApplicationModel.setLeftPeriod(2);
        transferApplicationModel.setDeadline(new Date());
        return transferApplicationModel;
    }

    private InvestModel createInvest(String loginName, long loanId) {
        InvestModel model = new InvestModel(IdGenerator.generate(), loanId, null, loginName, 10000, 0.1, false, new Date(), Source.WEB, null);
        model.setStatus(com.tuotiansudai.repository.model.InvestStatus.SUCCESS);
        return model;
    }


    private InvestRepayModel createInvestRepay(String loginName, long InvestId, long corpus, int period) {
        InvestRepayModel investRepayModel = new InvestRepayModel();
        investRepayModel.setId(IdGenerator.generate());
        investRepayModel.setInvestId(InvestId);
        investRepayModel.setCorpus(corpus);
        investRepayModel.setExpectedInterest(12);
        investRepayModel.setExpectedFee(5);
        investRepayModel.setPeriod(period);
        investRepayModel.setRepayDate(DateTime.now().plusDays(90).toDate());
        return investRepayModel;
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
        return userModelTest;
    }

    private BankAccountModel createAccountByUserId(String userId) {
        BankAccountModel bankAccountModel = new BankAccountModel();
        bankAccountModel.setBalance(100000L);
        return bankAccountModel;
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
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("10000");
        loanDto.setType(LoanType.LOAN_INTEREST_MONTHLY_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setProductType(ProductType._180);
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(com.tuotiansudai.repository.model.LoanStatus.REPAYING);
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setRecheckTime(new Date());
        loanMapper.create(loanModel);
        return loanModel;
    }

    private TransferApplicationDetailDto createTransferApplicationDetailDto(TransferApplicationModel transferApplicationModel, LoanModel loanModel){
        TransferApplicationDetailDto transferApplicationDetailDto = new TransferApplicationDetailDto();
        transferApplicationDetailDto.setId(transferApplicationModel.getId());
        transferApplicationDetailDto.setName(transferApplicationModel.getName());
        transferApplicationDetailDto.setLoginName(transferApplicationModel.getLoginName());
        transferApplicationDetailDto.setLoanId(loanModel.getId());
        transferApplicationDetailDto.setProductType(ProductType._180);
        transferApplicationDetailDto.setLoanName(loanModel.getName());
        transferApplicationDetailDto.setLoanType(loanModel.getType().getName());
        transferApplicationDetailDto.setInvestId(transferApplicationModel.getInvestAmount());
        transferApplicationDetailDto.setTransferAmount(String.valueOf(transferApplicationModel.getTransferAmount()));
        transferApplicationDetailDto.setBaseRate(loanModel.getBaseRate());
        transferApplicationDetailDto.setActivityRate(loanModel.getActivityRate());
        transferApplicationDetailDto.setLeftPeriod(transferApplicationModel.getLeftPeriod());
        transferApplicationDetailDto.setExpecedInterest("1000");
        transferApplicationDetailDto.setDeadLine(transferApplicationModel.getDeadline());
        transferApplicationDetailDto.setDueDate(new Date());
        transferApplicationDetailDto.setTransferStatus(transferApplicationModel.getStatus());
        return transferApplicationDetailDto;
    }

}
