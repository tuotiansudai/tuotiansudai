package com.tuotiansudai.service;


import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.impl.ExperienceLoanDetailServiceImpl;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class ExperienceLoanDetailServiceTest {

    @InjectMocks
    private ExperienceLoanDetailServiceImpl experienceLoanDetailService;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private InvestMapper investMapper;

    @Mock
    private CouponService couponService;

    @Autowired
    private FakeUserHelper userMapper;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldFindExperienceLoanDtoDetailsIsOk() {
        String fakeUserName = "loginName";
        UserModel userModel = getFakeUser(fakeUserName);
        userMapper.create(userModel);

        LoanModel loanModel = getLoanModel(fakeUserName);
        List<CouponModel> couponModels = new ArrayList<>();
        CouponModel couponModel = new CouponModel();
        couponModel.setAmount(1000);
        couponModel.setRate(15);
        couponModels.add(couponModel);
        List<InvestModel> investModels = new ArrayList<>();
        InvestModel investModel1 = getFakeInvestModel(loanModel.getId(), fakeUserName);
        investModel1.setInvestTime(new Date());
        investModels.add(investModel1);
        long investAmount = 1000;
        when(couponService.findExperienceInvestAmount(investModels)).thenReturn(investAmount);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(investMapper.findByLoanIdAndLoginName(anyLong(), anyString())).thenReturn(investModels);
        when(investMapper.countSuccessInvestByInvestTime(loanModel.getId(), new DateTime(new Date()).withTimeAtStartOfDay().toDate(), new DateTime(new Date()).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate())).thenReturn(investModels);
        ExperienceLoanDto experienceLoanDto = experienceLoanDetailService.findExperienceLoanDtoDetail(loanModel.getId(), fakeUserName);
        assertTrue(experienceLoanDto.getProgress() == 1);
        investModel1.setInvestTime(DateUtils.addDays(new Date(), -2));
        experienceLoanDto = experienceLoanDetailService.findExperienceLoanDtoDetail(loanModel.getId(), fakeUserName);
        assertTrue(experienceLoanDto.getProgress() == 100);
        investModel1.setInvestTime(DateUtils.addDays(new Date(), -3));
        experienceLoanDto = experienceLoanDetailService.findExperienceLoanDtoDetail(loanModel.getId(), fakeUserName);
        assertTrue(experienceLoanDto.getProgress() == 100);
    }

    public LoanModel getLoanModel(String fakeUserName) {
        LoanModel loanModel = new LoanModel();
        loanModel.setAgentLoginName(fakeUserName);
        loanModel.setBaseRate(16.00);
        long id = IdGenerator.generate();
        loanModel.setId(id);
        loanModel.setName("店铺资金周转");
        loanModel.setActivityRate(12);
        loanModel.setShowOnHome(true);
        loanModel.setPeriods(30);
        loanModel.setActivityType(ActivityType.EXCLUSIVE);
        loanModel.setContractId(123);
        loanModel.setDescriptionHtml("asdfasdf");
        loanModel.setDescriptionText("asdfasd");
        loanModel.setFundraisingEndTime(new Date());
        loanModel.setFundraisingStartTime(new Date());
        loanModel.setInvestIncreasingAmount(1);
        loanModel.setLoanAmount(10000);
        loanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanModel.setMaxInvestAmount(100000000000l);
        loanModel.setMinInvestAmount(0);
        loanModel.setCreatedTime(new Date());
        loanModel.setStatus(LoanStatus.WAITING_VERIFY);
        loanModel.setLoanerLoginName(fakeUserName);
        loanModel.setLoanerUserName("借款人");
        loanModel.setLoanerIdentityNumber("111111111111111111");
        return loanModel;
    }


    public UserModel getFakeUser(String loginName) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setPassword("password");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13" + RandomStringUtils.randomNumeric(9));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }

    private InvestModel getFakeInvestModel(long loanId, String loginName) {
        return new InvestModel(IdGenerator.generate(), loanId, null, loginName, 50, 0.1, false, null, Source.WEB, null);
    }

}
