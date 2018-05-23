package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v1_0.UserTransferInvestRepayRequestDto;
import com.tuotiansudai.api.dto.v1_0.UserTransferInvestRepayResponseDataDto;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppUserTransferInvestRepayServiceImpl;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.repository.model.TransferApplicationModel;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class MobileAppUserTransferInvestRepayServiceTest extends ServiceTestBase{
    @InjectMocks
    private MobileAppUserTransferInvestRepayServiceImpl mobileAppUserTransferInvestRepayService;
    @Mock
    private InvestRepayMapper investRepayMapper;

    @Mock
    private InvestService investService;

    @Mock
    private TransferApplicationMapper transferApplicationMapper;

    @Test
    public void shouldUserTransferInvestRepayIsOk(){

        LoanModel loanModel = createLoanModel();
        InvestModel investModel = getFakeInvestModel(loanModel.getId(), "loginuserInvestName");

        TransferApplicationModel createTransferAppLication = createTransferAppLication("loginuserInvestName", createLoanModel().getId(),investModel.getId(), 0L, 1, "loginuserInvestName", TransferStatus.TRANSFERRING);

        List<InvestRepayModel> investRepayModels = Lists.newArrayList();

        InvestRepayModel investRepayModel1 = getFakeInvestReapyModel(investModel.getId(), 1, new DateTime().minusDays(30).toDate(), new DateTime().minusDays(30).toDate(), 12, 0, 2, 10, 0, RepayStatus.COMPLETE);
        InvestRepayModel investRepayModel2 = getFakeInvestReapyModel(investModel.getId(), 2, new DateTime().minusDays(-30).toDate(), null, 12, 0, 2, 0, 0, RepayStatus.REPAYING);
        InvestRepayModel investRepayModel3 = getFakeInvestReapyModel(investModel.getId(), 3, new DateTime().minusDays(-60).toDate(), null, 12, 0, 2, 0, 5000, RepayStatus.REPAYING);
        investRepayModels.add(investRepayModel1);
        investRepayModels.add(investRepayModel2);
        investRepayModels.add(investRepayModel3);

        when(transferApplicationMapper.findById(anyLong())).thenReturn(createTransferAppLication);
        when(investService.findById(anyLong())).thenReturn(investModel);
        when(investRepayMapper.findByInvestIdAndPeriodAsc(anyLong())).thenReturn(investRepayModels);

        UserTransferInvestRepayRequestDto userTransferInvestRepayRequestDto =  new UserTransferInvestRepayRequestDto();
        userTransferInvestRepayRequestDto.setTransferApplicationId(String.valueOf(createTransferAppLication.getId()));

        BaseResponseDto<UserTransferInvestRepayResponseDataDto> responseDto = mobileAppUserTransferInvestRepayService.userTransferInvestRepay(userTransferInvestRepayRequestDto);

        assertEquals(ReturnMessage.SUCCESS.getCode(), responseDto.getCode());
        assertEquals(3, responseDto.getData().getTransferInvestRepays().size());
    }

    private LoanModel createLoanModel(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        LoanModel loanModel = new LoanModel();
        loanModel.setAgentLoginName("loginName");
        loanModel.setBaseRate(0.16);
        loanModel.setActivityRate(0.01);
        long id = IdGenerator.generate();
        loanModel.setId(id);
        loanModel.setName("店铺资金周转");
        loanModel.setShowOnHome(true);
        loanModel.setPeriods(3);
        loanModel.setActivityType(ActivityType.EXCLUSIVE);
        loanModel.setContractId(123);
        loanModel.setDescriptionHtml("asdfasdf");
        loanModel.setDescriptionText("asdfasd");
        loanModel.setFundraisingEndTime(new Date());
        loanModel.setFundraisingStartTime(new Date());
        //loanModel.setInvestFeeRate(15);
        loanModel.setInvestIncreasingAmount(1);
        loanModel.setLoanAmount(10000);
        loanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanModel.setMaxInvestAmount(1000l);
        loanModel.setMinInvestAmount(0);
        loanModel.setCreatedTime(new Date());
        loanModel.setStatus(LoanStatus.WAITING_VERIFY);
        loanModel.setLoanerLoginName("loginName");
        loanModel.setLoanerUserName("借款人");
        loanModel.setLoanerIdentityNumber("111111111111111111");
        loanModel.setRecheckTime(new Date());
        loanModel.setVerifyTime(new Date());
        loanModel.setUpdateTime(new Date());
        loanModel.setRaisingCompleteTime(new Date());
        loanModel.setDuration(90);
        loanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanModel.setProductType(ProductType._90);
        loanModel.setRecheckTime(new Date());
        return loanModel;
    }

    private InvestModel getFakeInvestModel(long loanId, String loginName) {
        InvestModel model = new InvestModel();
        model.setAmount(5000);
        model.setId(IdGenerator.generate());
        model.setLoginName(loginName);
        model.setLoanId(loanId);
        model.setSource(Source.ANDROID);
        model.setStatus(InvestStatus.SUCCESS);
        model.setInvestTime(new Date());
        return model;
    }

    private InvestRepayModel getFakeInvestReapyModel(long investId, int period, Date repayDate, Date actualRepayDate, long expectedInterest, long defaultInterest, long expectedFee, long repayAmount, long corpus, RepayStatus repayStatus){
        InvestRepayModel investRepayModel = new InvestRepayModel();
        investRepayModel.setId(IdGenerator.generate());
        investRepayModel.setInvestId(investId);
        investRepayModel.setPeriod(period);
        investRepayModel.setRepayDate(repayDate);
        investRepayModel.setActualRepayDate(actualRepayDate);
        investRepayModel.setExpectedInterest(expectedInterest);
        investRepayModel.setDefaultInterest(defaultInterest);
        investRepayModel.setExpectedFee(expectedFee);
        investRepayModel.setRepayAmount(repayAmount);
        investRepayModel.setCorpus(corpus);
        investRepayModel.setStatus(repayStatus);
        return investRepayModel;
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

}
