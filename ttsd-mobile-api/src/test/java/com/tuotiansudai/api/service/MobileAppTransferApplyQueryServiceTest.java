package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.impl.MobileAppTransferApplyQueryServiceImpl;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.transfer.repository.mapper.TransferRuleMapper;
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
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class MobileAppTransferApplyQueryServiceTest extends ServiceTestBase{

    @InjectMocks
    private MobileAppTransferApplyQueryServiceImpl mobileAppTransferApplyQueryService;
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

        when(investTransferService.isTransfer(anyLong())).thenReturn(true);
        when(investMapper.findById(anyLong())).thenReturn(investModel);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(loanRepayMapper.findByLoanIdOrderByPeriodAsc(anyLong())).thenReturn(repayingLoanRepays);
        when(transferRuleMapper.find()).thenReturn(transferRuleModel);

        TransferApplyQueryRequestDto transferApplyQueryRequestDto = new TransferApplyQueryRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(userModel.getLoginName());
        transferApplyQueryRequestDto.setBaseParam(baseParam);
        transferApplyQueryRequestDto.setInvestId(String.valueOf(investModel.getId()));
        BaseResponseDto<TransferApplyQueryResponseDataDto> baseResponseDto = mobileAppTransferApplyQueryService.transferApplyQuery(transferApplyQueryRequestDto);
        assertEquals("100.00",baseResponseDto.getData().getInvestAmount());
        assertEquals("10",baseResponseDto.getData().getTransferInterestDays());
        assertEquals("0.76",baseResponseDto.getData().getTransferInterest());
        assertEquals("1.00",baseResponseDto.getData().getTransferFee());

    }
    private InvestModel createInvest(String loginName, long loanId) {
        InvestModel model = new InvestModel(idGenerator.generate(), loanId, null, 10000, loginName, Source.WEB, null);
        model.setCreatedTime(new Date());
        model.setStatus(InvestStatus.SUCCESS);
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
        loanDto.setLoanStatus(LoanStatus.REPAYING);
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setRecheckTime(new Date());
        loanMapper.create(loanModel);
        return loanModel;
    }



}
