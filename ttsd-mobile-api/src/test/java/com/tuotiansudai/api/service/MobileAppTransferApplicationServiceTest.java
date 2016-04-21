package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.impl.MobileAppTransferApplicationServiceImpl;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.TransferApplicationPaginationItemDataDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.dto.TransferApplicationDto;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.mapper.TransferRuleMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.transfer.repository.model.TransferApplicationRecordDto;
import com.tuotiansudai.transfer.repository.model.TransferRuleModel;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.transfer.service.TransferService;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
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
    @Autowired
    private IdGenerator idGenerator;
    @Mock
    private TransferRuleMapper transferRuleMapper;
    @Mock
    private InvestRepayMapper investRepayMapper;

    @Test
    public void shouldGenerateTransferApplicationIsSuccess() {
        TransferApplicationRecordDto transferApplicationRecordDto = createTransferApplicationRecordDto();
        TransferApplicationRequestDto transferApplicationRequestDto = new TransferApplicationRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("test");
        transferApplicationRequestDto.setBaseParam(baseParam);
        transferApplicationRequestDto.setPageSize(10);
        transferApplicationRequestDto.setIndex(1);
        transferApplicationRequestDto.setTransferStatus(Lists.newArrayList(TransferStatus.TRANSFERRING));
        List<TransferApplicationRecordDto> transferApplicationRecordDtos = Lists.newArrayList(transferApplicationRecordDto);

        when(transferApplicationMapper.findTransferApplicationPaginationByLoginName(anyString(), any(List.class), anyInt(), anyInt())).thenReturn(transferApplicationRecordDtos);
        when(transferApplicationMapper.findCountTransferApplicationPaginationByLoginName(anyString(), any(List.class))).thenReturn(1);

        BaseResponseDto<TransferApplicationResponseDataDto> baseResponseDto = mobileAppTransferApplicationService.generateTransferApplication(transferApplicationRequestDto);
        assertEquals(TransferStatus.TRANSFERRING, baseResponseDto.getData().getTransferApplication().get(0).getTransferStatus());
        assertEquals("17", baseResponseDto.getData().getTransferApplication().get(0).getActivityRate());
        assertEquals("16", baseResponseDto.getData().getTransferApplication().get(0).getBaseRate());
        assertEquals("name", baseResponseDto.getData().getTransferApplication().get(0).getName());
        assertEquals("10.00", baseResponseDto.getData().getTransferApplication().get(0).getTransferAmount());
        assertEquals("12.00", baseResponseDto.getData().getTransferApplication().get(0).getInvestAmount());
        assertEquals("2016-02-09 00:00:00", baseResponseDto.getData().getTransferApplication().get(0).getTransferTime());

    }
    @Test
    public void shouldGenerateTransfereeApplicationIsSuccess() {
        TransferApplicationRecordDto transferApplicationRecordDto = createTransferApplicationRecordDto();
        PaginationRequestDto paginationRequestDto = new PaginationRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("test");
        paginationRequestDto.setBaseParam(baseParam);
        paginationRequestDto.setPageSize(10);
        paginationRequestDto.setIndex(1);
        List<TransferApplicationRecordDto> transferApplicationRecordDtos = Lists.newArrayList(transferApplicationRecordDto);

        when(transferApplicationMapper.findTransfereeApplicationPaginationByLoginName(anyString(), anyInt(), anyInt())).thenReturn(transferApplicationRecordDtos);
        when(transferApplicationMapper.findCountTransfereeApplicationPaginationByLoginName(anyString())).thenReturn(1);

        BaseResponseDto<TransferApplicationResponseDataDto> baseResponseDto = mobileAppTransferApplicationService.generateTransfereeApplication(paginationRequestDto);
        assertEquals(TransferStatus.TRANSFERRING, baseResponseDto.getData().getTransferApplication().get(0).getTransferStatus());
        assertEquals("17", baseResponseDto.getData().getTransferApplication().get(0).getActivityRate());
        assertEquals("16", baseResponseDto.getData().getTransferApplication().get(0).getBaseRate());
        assertEquals("name", baseResponseDto.getData().getTransferApplication().get(0).getName());
        assertEquals("10.00", baseResponseDto.getData().getTransferApplication().get(0).getTransferAmount());
        assertEquals("12.00", baseResponseDto.getData().getTransferApplication().get(0).getInvestAmount());
        assertEquals("2016-02-09 00:00:00", baseResponseDto.getData().getTransferApplication().get(0).getTransferTime());

    }

    private TransferApplicationRecordDto createTransferApplicationRecordDto() {
        TransferApplicationRecordDto transferApplicationRecordDto = new TransferApplicationRecordDto();
        transferApplicationRecordDto.setName("name");
        transferApplicationRecordDto.setTransferAmount(1000);
        transferApplicationRecordDto.setInvestAmount(1200);
        transferApplicationRecordDto.setTransferTime(new DateTime("2016-02-09").toDate());
        transferApplicationRecordDto.setBaseRate(0.16);
        transferApplicationRecordDto.setActivityRate(0.17);
        transferApplicationRecordDto.setTransferStatus(TransferStatus.TRANSFERRING);
        return transferApplicationRecordDto;

    }

    @Test
    public void shouldTransferApplyIsSuccess() throws Exception {
        doNothing().when(investTransferService).investTransferApply(any(TransferApplicationDto.class));
        TransferApplyRequestDto transferApplyRequestDto = new TransferApplyRequestDto();
        transferApplyRequestDto.setTransferInvestId("123");
        transferApplyRequestDto.setTransferAmount("1.00");
        transferApplyRequestDto.setTransferInterest(true);
        BaseResponseDto baseResponseDto = mobileAppTransferApplicationService.transferApply(transferApplyRequestDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
    }

    @Test
    public void shouldTransferApplyQueryIsSuccess() {
        long loanId = idGenerator.generate();
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

    }

    @Test
    public void sholudTransferApplicationListIsSuccess(){
        TransferApplicationModel transferApplicationModel1 = createTransferAppLication("ZR0001", 100001, 1000001, 10000001, 2, "testuer1", TransferStatus.SUCCESS);
        List<TransferApplicationPaginationItemDataDto> transferApplicationRecordDtos = new ArrayList<TransferApplicationPaginationItemDataDto>();
        transferApplicationRecordDtos.add(createTransferApplicationRecordDto(transferApplicationModel1));
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

        BaseResponseDto<TransferApplicationResponseDataDto> baseResponseDto = mobileAppTransferApplicationService.transferApplicationList(transferApplicationListRequestDto);

        assertEquals("ZR0001", baseResponseDto.getData().getTransferApplication().get(0).getName());
        assertEquals("500", baseResponseDto.getData().getTransferApplication().get(0).getInvestAmount());
        assertEquals("450", baseResponseDto.getData().getTransferApplication().get(0).getTransferAmount());
        assertEquals("1", baseResponseDto.getData().getTransferApplication().get(0).getActivityRate());
        assertEquals("12", baseResponseDto.getData().getTransferApplication().get(0).getBaseRate());
        assertEquals(TransferStatus.SUCCESS, baseResponseDto.getData().getTransferApplication().get(0).getTransferStatus());

    }

    private InvestRepayModel createInvestRepayModel(long investId, int period) {
        InvestRepayModel investRepayModel = new InvestRepayModel();
        investRepayModel.setInvestId(investId);
        investRepayModel.setPeriod(period);
        return investRepayModel;
    }

    private TransferApplicationPaginationItemDataDto createTransferApplicationRecordDto(TransferApplicationModel transferApplicationModel) {
        TransferApplicationPaginationItemDataDto transferApplicationPaginationItemDataDto = new TransferApplicationPaginationItemDataDto();
        transferApplicationPaginationItemDataDto.setTransferApplicationId(transferApplicationModel.getId());
        transferApplicationPaginationItemDataDto.setTransferName(transferApplicationModel.getName());
        transferApplicationPaginationItemDataDto.setInvestAmount(String.valueOf(transferApplicationModel.getInvestAmount()));
        transferApplicationPaginationItemDataDto.setTransferAmount(String.valueOf(transferApplicationModel.getTransferAmount()));
        transferApplicationPaginationItemDataDto.setBaseRate(12);
        transferApplicationPaginationItemDataDto.setActivityRate(1);
        transferApplicationPaginationItemDataDto.setTransferStatus(transferApplicationModel.getStatus());

        return transferApplicationPaginationItemDataDto;
    }

    private TransferApplicationModel createTransferAppLication(String name, long loanId, long transferInvestId, long investId,int period, String loginName, TransferStatus transferStatus) {
        TransferApplicationModel transferApplicationModel = new TransferApplicationModel();
        transferApplicationModel.setId(idGenerator.generate());
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
        return transferApplicationModel;
    }

    private InvestModel createInvest(String loginName, long loanId) {
        InvestModel model = new InvestModel(idGenerator.generate(), loanId, null, 10000, loginName, Source.WEB, null);
        model.setCreatedTime(new Date());
        model.setStatus(com.tuotiansudai.repository.model.InvestStatus.SUCCESS);
        return model;
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
        loanDto.setLoanStatus(com.tuotiansudai.repository.model.LoanStatus.REPAYING);
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setRecheckTime(new Date());
        loanMapper.create(loanModel);
        return loanModel;
    }

}
