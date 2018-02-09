package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v1_0.UserInvestListResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.TransferableInvestListRequestDto;
import com.tuotiansudai.api.service.v2_0.impl.MobileAppTransferApplicationV2ServiceImpl;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.repository.mapper.TransferRuleMapper;
import com.tuotiansudai.repository.model.TransferRuleModel;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;


public class MobileAppTransferApplicationV2ServiceTest extends ServiceTestBase {

    @InjectMocks
    private MobileAppTransferApplicationV2ServiceImpl mobileAppTransferApplicationV2Service;

    @Mock
    private TransferApplicationMapper transferApplicationMapper; //Do not remove

    @Mock
    private LoanRepayMapper loanRepayMapper; //Do not remove

    @Mock
    private InvestMapper investMapper;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private TransferRuleMapper transferRuleMapper;

    @Mock
    private InvestRepayMapper investRepayMapper;

    @Mock
    private InvestService investService;

    @Mock
    private PageValidUtils pageValidUtils;

    @Test
    public void shouldGenerateTransferableInvestIsSuccess() {
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
        TransferableInvestListRequestDto transferableInvestRequestDto = new TransferableInvestListRequestDto();
        transferableInvestRequestDto.setIndex(1);
        transferableInvestRequestDto.setPageSize(10);
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("testuser");
        transferableInvestRequestDto.setBaseParam(baseParam);
        long loanId = IdGenerator.generate();
        LoanModel loanModel = createLoanByUserId("testuser", loanId);
        InvestModel investModel = createInvest("testuser", loanId);
        InvestRepayModel investRepayModel = createInvestRepay(investModel.getId(), 2, 0, 10, 0, new DateTime().plus(2).toDate(), RepayStatus.REPAYING);
        when(investMapper.findTransferableApplicationPaginationByLoginName(anyString(), anyInt(), anyInt())).thenReturn(Lists.newArrayList(investModel));
        when(investMapper.findCountTransferableApplicationPaginationByLoginName(anyString())).thenReturn(1L);
        when(transferRuleMapper.find()).thenReturn(transferRuleModel);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(investService.estimateInvestIncome(anyLong(), anyDouble(), anyString(), anyLong(), any(Date.class))).thenReturn(1000l);
        when(investRepayMapper.findByInvestIdAndPeriod(anyLong(), anyInt())).thenReturn(investRepayModel);
        when(pageValidUtils.validPageSizeLimit(anyInt())).thenReturn(10);
        BaseResponseDto<UserInvestListResponseDataDto> baseResponseDto = mobileAppTransferApplicationV2Service.generateTransferableInvest(transferableInvestRequestDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
        assertEquals(String.valueOf(loanId), baseResponseDto.getData().getInvestList().get(0).getLoanId());
        assertEquals(investModel.getTransferStatus().name(), baseResponseDto.getData().getInvestList().get(0).getTransferStatus());
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

    private InvestModel createInvest(String loginName, long loanId) {
        InvestModel model = new InvestModel(IdGenerator.generate(), loanId, null, 10000, loginName, new Date(), Source.WEB, null, 0.1);
        model.setStatus(com.tuotiansudai.repository.model.InvestStatus.SUCCESS);
        return model;
    }

    private InvestRepayModel createInvestRepay(long investId, int period, long corpus, long expectedInterest, long expectedFee, Date repayDate, RepayStatus status) {
        InvestRepayModel model = new InvestRepayModel(IdGenerator.generate(), investId, period, corpus, expectedInterest,expectedFee, repayDate, status);
        return model;
    }


}
