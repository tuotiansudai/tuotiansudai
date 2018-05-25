package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.LoanListResponseDataDto;
import com.tuotiansudai.api.service.v2_0.impl.MobileAppLoanListV2ServiceImpl;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanDetailsMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MobileAppLoanListV2ServiceTest extends ServiceTestBase {

    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private InvestMapper investMapper;
    @Autowired
    private MobileAppLoanListV2ServiceImpl mobileAppLoanListV2Service;
    @Autowired
    private FakeUserHelper userMapper;
    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Before
    public void before() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        BaseParamDto baseParamDto = new BaseParamDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setAppVersion("4.2");
        baseParam.setUserId("userId");
        baseParamDto.setBaseParam(baseParam);
        request.setAttribute("baseParam", baseParamDto);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void shouldNoLoginNameGenerateIndexLoanIsOk() {
        String loginName = "testHomeFindName";
        userMapper.create(getUserModelTest(loginName));
        LoanModel loanModel = getFakeLoan(loginName, ActivityType.NORMAL, ProductType._90, LoanStatus.RAISING);
        loanMapper.create(loanModel);
        InvestModel investModel = getInvestModel(loginName, loanModel.getId());
        LoanDetailsModel loanDetailsModel = new LoanDetailsModel(loanModel.getId(), "", Lists.newArrayList(Source.MOBILE, Source.WEB), false, "");
        loanDetailsMapper.create(loanDetailsModel);
        investMapper.create(investModel);
        LoanModel loanModel1 = getFakeLoan(loginName, ActivityType.NEWBIE, ProductType.EXPERIENCE, LoanStatus.RAISING);
        loanMapper.create(loanModel1);
        LoanDetailsModel loanDetailsModel1 = new LoanDetailsModel(loanModel1.getId(), "", Lists.newArrayList(Source.MOBILE, Source.WEB), false, "");
        loanDetailsMapper.create(loanDetailsModel1);
        BaseResponseDto<LoanListResponseDataDto> dto = mobileAppLoanListV2Service.generateIndexLoan(null);
        assertTrue(dto.getData().getLoanList().get(0).getProductNewType().equals("EXPERIENCE"));
    }

    @Test
    public void shouldLoginInvestExperienceGenerateIndexLoanIsOk() {
        String loginName = "testHomeFindName";
        userMapper.create(getUserModelTest(loginName));
        LoanModel loanModel = getFakeLoan(loginName, ActivityType.NORMAL, ProductType._90, LoanStatus.RAISING);
        loanMapper.create(loanModel);
        LoanDetailsModel loanDetailsModel = new LoanDetailsModel(loanModel.getId(), "", Lists.newArrayList(Source.MOBILE, Source.WEB), false, "");
        loanDetailsMapper.create(loanDetailsModel);
        InvestModel investModel = getInvestModel(loginName, loanModel.getId());
        investMapper.create(investModel);
        LoanModel loanModel1 = getFakeLoan(loginName, ActivityType.NEWBIE, ProductType.EXPERIENCE, LoanStatus.RAISING);
        loanMapper.create(loanModel1);
        LoanDetailsModel loanDetailsModel1 = new LoanDetailsModel(loanModel1.getId(), "", Lists.newArrayList(Source.MOBILE, Source.WEB), false, "");
        loanDetailsMapper.create(loanDetailsModel1);
        InvestModel investModel1 = getInvestModel(loginName, loanModel1.getId());
        investMapper.create(investModel1);
        BaseResponseDto<LoanListResponseDataDto> dto = mobileAppLoanListV2Service.generateIndexLoan(loginName);
        assertTrue(!dto.getData().getLoanList().get(0).getProductNewType().equals("EXPERIENCE"));
        assertTrue(!dto.getData().getLoanList().get(0).getActivityType().equals("NEWBIE"));
    }

    @Test
    public void shouldLoginInvestNewBieGenerateIndexLoanIsOk() {
        String loginName = "testHomeFindName";
        userMapper.create(getUserModelTest(loginName));
        LoanModel loanModel = getFakeLoan(loginName, ActivityType.NORMAL, ProductType._90, LoanStatus.RAISING);
        loanMapper.create(loanModel);
        InvestModel investModel = getInvestModel(loginName, loanModel.getId());
        LoanDetailsModel loanDetailsModel = new LoanDetailsModel(loanModel.getId(), "", Lists.newArrayList(Source.MOBILE, Source.WEB), false, "");
        loanDetailsMapper.create(loanDetailsModel);
        investMapper.create(investModel);
        LoanModel loanModel1 = getFakeLoan(loginName, ActivityType.NEWBIE, ProductType._30, LoanStatus.RAISING);
        loanMapper.create(loanModel1);
        LoanDetailsModel loanDetailsModel1 = new LoanDetailsModel(loanModel1.getId(), "", Lists.newArrayList(Source.MOBILE, Source.WEB), false, "");
        loanDetailsMapper.create(loanDetailsModel1);
        InvestModel investModel1 = getInvestModel(loginName, loanModel1.getId());
        investModel1.setInvestTime(DateTime.parse("2016-06-12").toDate());
        investMapper.create(investModel1);
        BaseResponseDto<LoanListResponseDataDto> dto = mobileAppLoanListV2Service.generateIndexLoan(loginName);
        assertTrue(!dto.getData().getLoanList().get(0).getProductNewType().equals("NEWBIE"));
    }

    @Test
    public void shouldLoginNormalGenerateIndexLoanIsOk() {
        String loginName = "testHomeFindName";
        userMapper.create(getUserModelTest(loginName));
        LoanModel loanModel = getFakeLoan(loginName, ActivityType.NORMAL, ProductType._90, LoanStatus.RAISING);
        loanMapper.create(loanModel);
        InvestModel investModel = getInvestModel(loginName, loanModel.getId());
        LoanDetailsModel loanDetailsModel = new LoanDetailsModel(loanModel.getId(), "", Lists.newArrayList(Source.MOBILE, Source.WEB), false, "");
        loanDetailsMapper.create(loanDetailsModel);
        investMapper.create(investModel);
        LoanModel loanModel1 = getFakeLoan(loginName, ActivityType.NEWBIE, ProductType._30, LoanStatus.RAISING);
        loanMapper.create(loanModel1);
        LoanDetailsModel loanDetailsModel1 = new LoanDetailsModel(loanModel1.getId(), "", Lists.newArrayList(Source.MOBILE, Source.WEB), false, "");
        loanDetailsMapper.create(loanDetailsModel1);
        InvestModel investModel1 = getInvestModel(loginName, loanModel1.getId());
        investModel1.setInvestTime(DateTime.parse("2016-06-12").toDate());
        investMapper.create(investModel1);
        LoanModel loanModel2 = getFakeLoan(loginName, ActivityType.NEWBIE, ProductType.EXPERIENCE, LoanStatus.RAISING);
        loanMapper.create(loanModel2);
        InvestModel investModel2 = getInvestModel(loginName, loanModel1.getId());
        LoanDetailsModel loanDetailsModel2 = new LoanDetailsModel(loanModel2.getId(), "", Lists.newArrayList(Source.MOBILE, Source.WEB), false, "");
        loanDetailsMapper.create(loanDetailsModel2);
        investMapper.create(investModel2);
        BaseResponseDto<LoanListResponseDataDto> dto = mobileAppLoanListV2Service.generateIndexLoan(loginName);
        assertTrue(dto.getData().getLoanList().get(0).getActivityType().equals("NORMAL"));
    }

    private InvestModel getInvestModel(String loginName, long loanId) {
        InvestModel investModel = new InvestModel(IdGenerator.generate(), loanId, null, loginName, 1, 0.1, false, new Date(), Source.WEB, null);
        investModel.setStatus(InvestStatus.SUCCESS);
        return investModel;
    }

    private UserModel getUserModelTest(String loginName) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }

    private LoanModel getFakeLoan(String loanerLoginName, ActivityType activityType, ProductType productType, LoanStatus loanStatus) {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(IdGenerator.generate());
        fakeLoanModel.setName(loanerLoginName);
        fakeLoanModel.setLoanerLoginName(loanerLoginName);
        fakeLoanModel.setLoanerUserName(loanerLoginName);
        fakeLoanModel.setLoanerIdentityNumber("111111111111111111");
        fakeLoanModel.setAgentLoginName(loanerLoginName);
        fakeLoanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        fakeLoanModel.setPeriods(3);
        fakeLoanModel.setStatus(loanStatus);
        fakeLoanModel.setActivityType(activityType);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setCreatedTime(new Date());
        fakeLoanModel.setProductType(productType);
        fakeLoanModel.setActivityType(activityType);
        fakeLoanModel.setPledgeType(PledgeType.HOUSE);
        return fakeLoanModel;
    }
}
