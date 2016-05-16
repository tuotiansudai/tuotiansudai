package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v2_0.BaseParamDto;
import com.tuotiansudai.api.dto.v2_0.BaseResponseDto;
import com.tuotiansudai.api.service.v2_0.MobileAppLoanListV2Service;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.LoanType;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MobileAppLoanListV2ServiceTest extends ServiceTestBase{

    @Mock
    private LoanMapper loanMapper;
    @Mock
    private InvestMapper investMapper;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private MobileAppLoanListV2Service mobileAppLoanListV2Service;

    @Test
    public void shouldGenerateIndexLoanIsOk(){
        List<LoanModel> loanModels = new ArrayList<>();
        loanModels.add(getFakeLoan("shenjiaojiao"));
        loanModels.add(getFakeLoan("jiaoshenshen"));
        BaseParamDto baseParamDto = new BaseParamDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("shenjiaojiao");
        baseParamDto.setBaseParam(baseParam);

        when(loanMapper.findHomeLoanByIsContainNewBie("false")).thenReturn(loanModels);
        BaseResponseDto baseResponseDto = mobileAppLoanListV2Service.generateIndexLoan(baseParamDto);
        assertNotNull(baseResponseDto.getData());

    }

    private LoanModel getFakeLoan(String loanerLoginName) {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(idGenerator.generate());
        fakeLoanModel.setName("loanName");
        fakeLoanModel.setLoanerLoginName(loanerLoginName);
        fakeLoanModel.setLoanerUserName("借款人");
        fakeLoanModel.setLoanerIdentityNumber("111111111111111111");
        fakeLoanModel.setAgentLoginName(loanerLoginName);
        fakeLoanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        fakeLoanModel.setPeriods(3);
        fakeLoanModel.setStatus(LoanStatus.COMPLETE);
        fakeLoanModel.setActivityType(ActivityType.NEWBIE);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setCreatedTime(new Date());
        return fakeLoanModel;
    }
}
