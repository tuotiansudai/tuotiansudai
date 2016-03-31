package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.impl.MobileAppTransferApplicationServiceImpl;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.dto.TransferApplicationDto;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.mapper.TransferRuleMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationRecordDto;
import com.tuotiansudai.transfer.repository.model.TransferRuleModel;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

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
    private InvestMapper investMapper;
    @Mock
    private LoanMapper loanMapper;
    @Mock
    private LoanRepayMapper loanRepayMapper;
    @Autowired
    private IdGenerator idGenerator;
    @Mock
    private TransferRuleMapper transferRuleMapper;

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
        assertEquals("25", baseResponseDto.getData().getTransferApplication().get(0).getTransferInterestDays());

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
        assertEquals("25", baseResponseDto.getData().getTransferApplication().get(0).getTransferInterestDays());

    }

    private TransferApplicationRecordDto createTransferApplicationRecordDto() {
        TransferApplicationRecordDto transferApplicationRecordDto = new TransferApplicationRecordDto();
        transferApplicationRecordDto.setName("name");
        transferApplicationRecordDto.setTransferAmount(1000);
        transferApplicationRecordDto.setInvestAmount(1200);
        transferApplicationRecordDto.setTransferTime(new DateTime("2016-02-09").toDate());
        transferApplicationRecordDto.setBaseRate(0.16);
        transferApplicationRecordDto.setActivityRate(0.17);
        transferApplicationRecordDto.setTransferInterestDays(25);
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
        assertEquals("10", baseResponseDto.getData().getTransferInterestDays());
        assertEquals("0.76", baseResponseDto.getData().getTransferInterest());
        assertEquals("1.00", baseResponseDto.getData().getTransferFee());

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
