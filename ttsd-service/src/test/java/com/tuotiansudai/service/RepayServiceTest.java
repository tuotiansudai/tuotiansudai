package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestRepayDataDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static sun.nio.cs.Surrogate.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class RepayServiceTest {

    @Autowired
    private RepayService repayService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponRepayMapper couponRepayMapper;

    @Test
    public void shouldInvestRepayFindInvestorInvestRepayIsOk() {
        String loginName = "testInvestRepay";
        getUserModelTest(loginName);
        LoanModel loanModel = getFakeLoan(loginName, loginName, LoanStatus.REPAYING, ActivityType.NORMAL);
        InvestModel investModel = getFakeInvestModel(loanModel.getId(), loginName);
        getInvestRepayModel(investModel.getId(), 1, DateTime.parse("2010-01-01").toDate(), RepayStatus.COMPLETE);
        getInvestRepayModel(investModel.getId(), 2, DateTime.parse("2010-02-01").toDate(), RepayStatus.REPAYING);
        getInvestRepayModel(investModel.getId(), 3, DateTime.parse("2010-03-01").toDate(), RepayStatus.REPAYING);
        BaseDto<InvestRepayDataDto>  investRepayDataDtoBaseDto = repayService.findInvestorInvestRepay(loginName, investModel.getId());
        assertTrue(investRepayDataDtoBaseDto.getData().getRecords().size() == 3);
        assertEquals(investRepayDataDtoBaseDto.getData().getRecords().get(0).getAmount(), "0.50");
        assertEquals(investRepayDataDtoBaseDto.getData().getRecords().get(0).getExpectedFee(),"0.50");
        assertEquals(investRepayDataDtoBaseDto.getData().getRecords().get(0).getExpectedInterest(),"1.00");
        assertEquals(investRepayDataDtoBaseDto.getData().getRecords().get(0).getActualInterest(),"1.00");
        assertEquals(investRepayDataDtoBaseDto.getData().getRecords().get(0).getActualFee(),"0.50");
    }

    @Test
    public void shouldInvestRepayAndInvestRepayFindInvestorInvestRepayIsOk() {
        String loginName = "testInvestRepay";
        getUserModelTest(loginName);
        LoanModel loanModel = getFakeLoan(loginName, loginName, LoanStatus.REPAYING, ActivityType.NORMAL);
        InvestModel investModel = getFakeInvestModel(loanModel.getId(), loginName);
        getInvestRepayModel(investModel.getId(), 1, DateTime.parse("2010-01-01").toDate(), RepayStatus.COMPLETE);
        getInvestRepayModel(investModel.getId(), 2, DateTime.parse("2010-02-01").toDate(), RepayStatus.REPAYING);
        getInvestRepayModel(investModel.getId(), 3, DateTime.parse("2010-03-01").toDate(), RepayStatus.REPAYING);
        CouponModel investCoupon = fakeCoupon(CouponType.INVEST_COUPON, UserGroup.ALL_USER,loginName);
        CouponModel interestCoupon = fakeCoupon(CouponType.INTEREST_COUPON,UserGroup.ALL_USER,loginName);
        UserCouponModel userCouponModel = getUserCouponModel(loginName, investCoupon.getId(), investModel.getId(), loanModel.getId());
        CouponRepayModel couponRepayModel = getCouponRepayModel(loginName,investCoupon.getId(),userCouponModel.getId(), investModel.getId(), DateTime.parse("2010-01-01").toDate());
        BaseDto<InvestRepayDataDto>  investRepayDataDtoBaseDto = repayService.findInvestorInvestRepay(loginName, investModel.getId());
        assertEquals(investRepayDataDtoBaseDto.getData().getRecords().size(),3);
        assertEquals(investRepayDataDtoBaseDto.getData().getRecords().get(0).getAmount(),"1.50");
        assertEquals(investRepayDataDtoBaseDto.getData().getRecords().get(0).getCouponExpectedInterest(),"2.00");
    }


    private CouponRepayModel getCouponRepayModel(String loginName,long couponId,long userCouponId,long investId,Date date){
        CouponRepayModel couponRepayModel = new CouponRepayModel(loginName, couponId, userCouponId, investId, 200, 100, 1, date);
        couponRepayMapper.create(Lists.newArrayList(couponRepayModel));
        return couponRepayModel;
    }

    private UserCouponModel getUserCouponModel(String loginName,long couponId,long investId,long loanId){
        UserCouponModel userCouponModel = new UserCouponModel();
        userCouponModel.setId(idGenerator.generate());
        userCouponModel.setActualFee(100);
        userCouponModel.setActualInterest(200);
        userCouponModel.setCouponId(couponId);
        userCouponModel.setEndTime(DateTime.now().toDate());
        userCouponModel.setCreatedTime(DateTime.now().toDate());
        userCouponModel.setExpectedFee(100);
        userCouponModel.setExpectedInterest(200);
        userCouponModel.setInvestId(investId);
        userCouponModel.setLoanId(loanId);
        userCouponModel.setLoginName(loginName);
        userCouponModel.setStatus(InvestStatus.SUCCESS);
        userCouponModel.setStartTime(DateTime.now().toDate());
        userCouponMapper.create(userCouponModel);
        return userCouponModel;
    }

    private CouponModel fakeCoupon(CouponType couponType, UserGroup userGroup,String loginName) {
        ExchangeCouponDto exchangeCouponDto = new ExchangeCouponDto();
        exchangeCouponDto.setAmount("1000.00");
        exchangeCouponDto.setTotalCount(100L);
        exchangeCouponDto.setEndTime(new Date());
        exchangeCouponDto.setStartTime(new Date());
        exchangeCouponDto.setInvestLowerLimit("1000.00");
        exchangeCouponDto.setCouponType(couponType);
        List<ProductType> productTypes = Lists.newArrayList();
        productTypes.add(ProductType._180);
        exchangeCouponDto.setProductTypes(productTypes);
        exchangeCouponDto.setInvestLowerLimit("1000.00");
        exchangeCouponDto.setUserGroup(userGroup);
        exchangeCouponDto.setDeadline(2);
        CouponModel couponModel = new CouponModel(exchangeCouponDto);
        couponModel.setCreatedBy(loginName);
        couponModel.setCreatedTime(DateTime.now().toDate());
        couponMapper.create(couponModel);
        return couponModel;
    }

    private InvestRepayModel getInvestRepayModel(long investId,int period,Date date,RepayStatus repayStatus){
        List<InvestRepayModel> investRepayModels = Lists.newArrayList();
        InvestRepayModel investRepayModel = new InvestRepayModel();
        investRepayModel.setId(idGenerator.generate());
        investRepayModel.setInvestId(investId);
        investRepayModel.setPeriod(period);
        investRepayModel.setStatus(repayStatus);
        investRepayModel.setRepayDate(new Date());
        investRepayModel.setExpectedInterest(100);
        investRepayModel.setExpectedFee(50);
        investRepayModel.setActualInterest(100);
        investRepayModel.setActualFee(50);
        investRepayModel.setRepayDate(date);
        investRepayModels.add(investRepayModel);
        investRepayMapper.create(investRepayModels);
        return investRepayModel;
    }

    private CouponModel fakeCouponModel(){
        CouponModel couponModel = new CouponModel();
        couponModel.setAmount(1000L);
        couponModel.setActivatedBy("couponTest");
        couponModel.setActive(false);
        couponModel.setCreatedTime(new Date());
        couponModel.setEndTime(new Date());
        couponModel.setDeadline(10);
        couponModel.setStartTime(new Date());
        couponModel.setCreatedBy("couponTest");
        couponModel.setTotalCount(1000L);
        couponModel.setUsedCount(500L);
        couponModel.setInvestLowerLimit(10000L);
        couponModel.setCouponType(CouponType.INVEST_COUPON);
        couponModel.setProductTypes(Lists.newArrayList(ProductType._30, ProductType._90, ProductType._180));
        return couponModel;
    }

    private InvestModel getFakeInvestModel(long loanId,String loginName) {
        InvestModel model = new InvestModel(idGenerator.generate(), loanId, null, 1000000L, loginName, new DateTime().withTimeAtStartOfDay().toDate(), Source.WEB, null, 0.1);
        model.setStatus(InvestStatus.SUCCESS);
        investMapper.create(model);
        return model;
    }

    private LoanModel getFakeLoan(String loanerLoginName, String agentLoginName, LoanStatus loanStatus, ActivityType activityType) {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(idGenerator.generate());
        fakeLoanModel.setName("loanName");
        fakeLoanModel.setLoanerLoginName(loanerLoginName);
        fakeLoanModel.setLoanerUserName("借款人");
        fakeLoanModel.setLoanerIdentityNumber("111111111111111111");
        fakeLoanModel.setAgentLoginName(agentLoginName);
        fakeLoanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        fakeLoanModel.setPeriods(3);
        fakeLoanModel.setStatus(loanStatus);
        fakeLoanModel.setActivityType(activityType);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setPledgeType(PledgeType.HOUSE);
        fakeLoanModel.setCreatedTime(new Date());
        loanMapper.create(fakeLoanModel);
        return fakeLoanModel;
    }

    public UserModel getUserModelTest(String loginName) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
        return userModelTest;
    }
}
