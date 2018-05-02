package com.tuotiansudai.api.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.LoanDetailV2RequestDto;
import com.tuotiansudai.api.dto.v2_0.LoanDetailV2ResponseDataDto;
import com.tuotiansudai.api.service.v2_0.MobileAppLoanDetailV2Service;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.mapper.ExtraLoanRateMapper;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.mapper.LoanDetailsMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class MobileAppLoanDetailV2ServiceTest extends ServiceTestBase {

    @Autowired
    private MobileAppLoanDetailV2Service mobileAppLoanDetailV2Service;

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;

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

    @Ignore
    @Test
    public void shouldFindLoanDetailIsOk() {
        UserModel fakeUserModel = this.getUserModelTest();
        userMapper.create(fakeUserModel);
        LoanModel fakeLoan = this.getFakeLoan(fakeUserModel.getLoginName(), fakeUserModel.getLoginName());
        loanMapper.create(fakeLoan);
        loanDetailsMapper.create(createLoanDetails(fakeLoan.getId()));
        LoanDetailV2RequestDto loanDetailV2RequestDto = new LoanDetailV2RequestDto();
        loanDetailV2RequestDto.setLoanId(String.valueOf(fakeLoan.getId()));
        List<ExtraLoanRateModel> extraLoanRateModels = createExtraLoanRate(fakeLoan.getId());
        extraLoanRateMapper.create(extraLoanRateModels);

        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(fakeUserModel.getLoginName());
        loanDetailV2RequestDto.setBaseParam(baseParam);
        BaseResponseDto<LoanDetailV2ResponseDataDto> loanDetail = mobileAppLoanDetailV2Service.findLoanDetail(loanDetailV2RequestDto);
        assertTrue(loanDetail.getData().isNonTransferable());
        assertEquals(loanDetail.getData().getContent(), "个人经营借款投资项目，总额10001元期限30天，年化利率28%，先到先抢！！！");
        assertTrue(Long.parseLong(loanDetail.getData().getInterestPerTenThousands()) == 29787);
    }

    private LoanDetailsModel createLoanDetails(long loanId) {
        LoanDetailsModel loanDetailsModel = new LoanDetailsModel();
        loanDetailsModel.setId(IdGenerator.generate());
        loanDetailsModel.setDeclaration("声明材料");
        loanDetailsModel.setExtraSource(Lists.newArrayList(Source.WEB));
        loanDetailsModel.setLoanId(loanId);
        loanDetailsModel.setNonTransferable(true);
        return loanDetailsModel;
    }


    private List<ExtraLoanRateModel> createExtraLoanRate(long loanId) {
        ExtraLoanRateModel model = new ExtraLoanRateModel();
        model.setLoanId(loanId);
        model.setExtraRateRuleId(100001);
        model.setCreatedTime(new Date());
        model.setMinInvestAmount(100000);
        model.setMaxInvestAmount(1000000);
        model.setRate(0.1);
        ExtraLoanRateModel model2 = new ExtraLoanRateModel();
        model2.setLoanId(loanId);
        model2.setExtraRateRuleId(100001);
        model2.setCreatedTime(new Date());
        model2.setMinInvestAmount(1000000);
        model2.setMaxInvestAmount(5000000);
        model2.setRate(0.3);
        ExtraLoanRateModel model3 = new ExtraLoanRateModel();
        model3.setLoanId(loanId);
        model3.setExtraRateRuleId(100001);
        model3.setCreatedTime(new Date());
        model3.setMinInvestAmount(5000000);
        model3.setMaxInvestAmount(0);
        model3.setRate(0.5);
        List<ExtraLoanRateModel> list = Lists.newArrayList();
        list.add(model);
        list.add(model2);
        list.add(model3);
        return list;
    }


    private LoanModel getFakeLoan(String loanerLoginName, String agentLoginName) {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName(loanerLoginName);
        loanDto.setLoanerUserName("借款人");
        loanDto.setLoanerIdentityNumber("111111111111111111");
        loanDto.setAgentLoginName(agentLoginName);
        loanDto.setBasicRate("16.00");
        loanDto.setId(IdGenerator.generate());
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
        loanDto.setLoanAmount("10001");
        loanDto.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.RAISING);
        loanDto.setProductType(ProductType._30);
        loanDto.setPledgeType(PledgeType.HOUSE);
        loanDto.setDuration(30);
        loanDto.setVerifyTime(DateTime.now().toDate());
        loanDto.setRecheckTime(DateTime.now().toDate());
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setStatus(LoanStatus.RAISING);
        loanModel.setDeadline(new DateTime().plusDays(10).toDate());
        return loanModel;
    }

    public UserModel getUserModelTest() {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("helloworld");
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }
}
