package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v2_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.LoanListResponseDataDto;
import com.tuotiansudai.api.service.v2_0.impl.MobileAppLoanListV2ServiceImpl;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
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
    @InjectMocks
    private MobileAppLoanListV2ServiceImpl mobileAppLoanListV2Service;
    @Mock
    private UserMembershipEvaluator userMembershipEvaluator;


    @Test
    public void shouldGenerateIndexLoanIsOk(){
        ReflectionTestUtils.setField(mobileAppLoanListV2Service, "defaultFee", 0.1);
        when(investMapper.sumSuccessInvestCountByLoginName(anyString())).thenReturn(1);

        List<LoanModel> loanModels = Lists.newArrayList();
        loanModels.add(getFakeExperienceLoan("test1"));
        loanModels.add(getFakeLoan("test1"));
        MembershipModel membershipModel = new MembershipModel(idGenerator.generate(),1,100l,0.09);

        when(loanMapper.findHomeLoanByIsContainNewbie(any(LoanStatus.class), anyBoolean())).thenReturn(loanModels);
        when(userMembershipEvaluator.evaluate(anyString())).thenReturn(membershipModel);

        BaseResponseDto<LoanListResponseDataDto> baseResponseDto = mobileAppLoanListV2Service.generateIndexLoan("shenjiaojiao");

        assertNotNull(baseResponseDto.getData());
        assertThat(baseResponseDto.getData().getLoanList().get(0).getProductNewType(), is(ProductType.EXPERIENCE.name()));
        assertEquals("0.1", baseResponseDto.getData().getLoanList().get(0).getInvestFeeRate());
        assertThat(baseResponseDto.getData().getLoanList().get(1).getProductNewType(), is(ProductType._30.name()));
        assertEquals(String.valueOf(membershipModel.getFee()), baseResponseDto.getData().getLoanList().get(1).getInvestFeeRate());

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
        fakeLoanModel.setProductType(ProductType._30);
        return fakeLoanModel;
    }

    private LoanModel getFakeExperienceLoan(String loginName) {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(idGenerator.generate());
        fakeLoanModel.setName("loanName");
        fakeLoanModel.setLoanerLoginName(loginName);
        fakeLoanModel.setLoanerUserName("借款人");
        fakeLoanModel.setLoanerIdentityNumber("111111111111111111");
        fakeLoanModel.setAgentLoginName(loginName);
        fakeLoanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        fakeLoanModel.setPeriods(3);
        fakeLoanModel.setStatus(LoanStatus.RAISING);
        fakeLoanModel.setActivityType(ActivityType.NEWBIE);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setCreatedTime(new Date());
        fakeLoanModel.setProductType(ProductType.EXPERIENCE);
        return fakeLoanModel;
    }

}
