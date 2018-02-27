package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.ExchangeCouponDto;
import com.tuotiansudai.dto.InvestRepayDataDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class RepayServiceTest {

    @Autowired
    private RepayService repayService;

    @Autowired
    private FakeUserHelper userMapper;

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

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Test
    public void shouldInvestRepayFindInvestorInvestRepayIsOk() {
        String loginName = "testInvestRepay";
        getUserModelTest(loginName);
        LoanModel loanModel = getFakeLoan(loginName, loginName, LoanStatus.REPAYING, ActivityType.NORMAL);
        InvestModel investModel = getFakeInvestModel(loanModel.getId(), loginName, TransferStatus.NONTRANSFERABLE);
        getInvestRepayModel(investModel.getId(), 1, DateTime.parse("2010-01-01").toDate(), RepayStatus.COMPLETE, null, 100);
        getInvestRepayModel(investModel.getId(), 2, DateTime.parse("2010-02-01").toDate(), RepayStatus.REPAYING, null, 100);
        getInvestRepayModel(investModel.getId(), 3, DateTime.parse("2010-03-01").toDate(), RepayStatus.REPAYING, null, 100);
        BaseDto<InvestRepayDataDto> investRepayDataDtoBaseDto = repayService.findInvestorInvestRepay(loginName, investModel.getId());
        assertTrue(investRepayDataDtoBaseDto.getData().getRecords().size() == 3);
        assertEquals(investRepayDataDtoBaseDto.getData().getRecords().get(0).getAmount(), "0.50");
        assertEquals(investRepayDataDtoBaseDto.getData().getRecords().get(0).getExpectedFee(), "0.50");
        assertEquals(investRepayDataDtoBaseDto.getData().getRecords().get(0).getExpectedInterest(), "1.00");
        assertEquals(investRepayDataDtoBaseDto.getData().getRecords().get(0).getActualInterest(), "1.00");
        assertEquals(investRepayDataDtoBaseDto.getData().getRecords().get(0).getActualFee(), "0.50");
    }

    @Test
    public void shouldInvestRepayAndInvestRepayFindInvestorInvestRepayIsOk() {
        String loginName = "testInvestRepay";
        getUserModelTest(loginName);
        LoanModel loanModel = getFakeLoan(loginName, loginName, LoanStatus.REPAYING, ActivityType.NORMAL);
        InvestModel investModel = getFakeInvestModel(loanModel.getId(), loginName, TransferStatus.NONTRANSFERABLE);
        getInvestRepayModel(investModel.getId(), 1, DateTime.parse("2010-01-01").toDate(), RepayStatus.COMPLETE, null, 100);
        getInvestRepayModel(investModel.getId(), 2, DateTime.parse("2010-02-01").toDate(), RepayStatus.REPAYING, null, 100);
        getInvestRepayModel(investModel.getId(), 3, DateTime.parse("2010-03-01").toDate(), RepayStatus.REPAYING, null, 100);
        CouponModel investCoupon = fakeCoupon(CouponType.INVEST_COUPON, UserGroup.ALL_USER, loginName);
        CouponModel interestCoupon = fakeCoupon(CouponType.INTEREST_COUPON, UserGroup.ALL_USER, loginName);
        UserCouponModel userCouponModel = getUserCouponModel(loginName, investCoupon.getId(), investModel.getId(), loanModel.getId());
        CouponRepayModel couponRepayModel = getCouponRepayModel(loginName, investCoupon.getId(), userCouponModel.getId(), investModel.getId(), DateTime.parse("2010-01-01").toDate());
        BaseDto<InvestRepayDataDto> investRepayDataDtoBaseDto = repayService.findInvestorInvestRepay(loginName, investModel.getId());
        assertEquals(investRepayDataDtoBaseDto.getData().getRecords().size(), 3);
        assertEquals(investRepayDataDtoBaseDto.getData().getRecords().get(0).getAmount(), "1.50");
        assertEquals(investRepayDataDtoBaseDto.getData().getRecords().get(0).getCouponExpectedInterest(), "2.00");
        assertEquals(investRepayDataDtoBaseDto.getData().getSumExpectedInterest(), "1.00");
    }

    @Test
    public void shouldInvestRepayAndInvestRepayAndTransferFindInvestorInvestRepayIsOk() {
        String loginName = "testInvestRepay";
        getUserModelTest(loginName);
        LoanModel loanModel = getFakeLoan(loginName, loginName, LoanStatus.REPAYING, ActivityType.NORMAL);
        InvestModel investModel = getFakeInvestModel(loanModel.getId(), loginName, TransferStatus.SUCCESS);
        getInvestRepayModel(investModel.getId(), 1, DateTime.parse("2010-01-01").toDate(), RepayStatus.COMPLETE, TransferStatus.SUCCESS, 0l);
        getInvestRepayModel(investModel.getId(), 2, DateTime.parse("2010-02-01").toDate(), RepayStatus.COMPLETE, TransferStatus.SUCCESS, 0l);
        getInvestRepayModel(investModel.getId(), 3, DateTime.parse("2010-03-01").toDate(), RepayStatus.COMPLETE, TransferStatus.SUCCESS, 0l);
        CouponModel investCoupon = fakeCoupon(CouponType.INVEST_COUPON, UserGroup.ALL_USER, loginName);
        CouponModel interestCoupon = fakeCoupon(CouponType.INTEREST_COUPON, UserGroup.ALL_USER, loginName);
        UserCouponModel userCouponModel = getUserCouponModel(loginName, investCoupon.getId(), investModel.getId(), loanModel.getId());
        CouponRepayModel couponRepayModel = getCouponRepayModel(loginName, investCoupon.getId(), userCouponModel.getId(), investModel.getId(), DateTime.parse("2010-01-01").toDate());
        BaseDto<InvestRepayDataDto> investRepayDataDtoBaseDto = repayService.findInvestorInvestRepay(loginName, investModel.getId());
        assertEquals(investRepayDataDtoBaseDto.getData().getRecords().get(0).getStatus(), TransferStatus.SUCCESS.getDescription());
    }

    private CouponRepayModel getCouponRepayModel(String loginName, long couponId, long userCouponId, long investId, Date date) {
        CouponRepayModel couponRepayModel = new CouponRepayModel(loginName, couponId, userCouponId, investId, 200, 100, 1, date);
        couponRepayModel.setPeriod(1);
        couponRepayMapper.create(couponRepayModel);
        return couponRepayModel;
    }

    private UserCouponModel getUserCouponModel(String loginName, long couponId, long investId, long loanId) {
        UserCouponModel userCouponModel = new UserCouponModel();
        userCouponModel.setId(IdGenerator.generate());
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

    private CouponModel fakeCoupon(CouponType couponType, UserGroup userGroup, String loginName) {
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
        couponModel.setCouponSource("");
        couponMapper.create(couponModel);
        return couponModel;
    }

    private InvestRepayModel getInvestRepayModel(long investId, int period, Date date, RepayStatus repayStatus, TransferStatus transferStatus, long expectedInterest) {
        List<InvestRepayModel> investRepayModels = Lists.newArrayList();
        InvestRepayModel investRepayModel = new InvestRepayModel();
        investRepayModel.setId(IdGenerator.generate());
        investRepayModel.setInvestId(investId);
        investRepayModel.setPeriod(period);
        investRepayModel.setStatus(repayStatus);
        investRepayModel.setRepayDate(new Date());
        investRepayModel.setExpectedInterest(expectedInterest);
        investRepayModel.setExpectedFee(50);
        investRepayModel.setActualInterest(100);
        investRepayModel.setActualFee(50);
        investRepayModel.setRepayDate(date);
        investRepayModel.setTransferStatus(transferStatus);
        investRepayModels.add(investRepayModel);
        investRepayMapper.create(investRepayModels);
        return investRepayModel;
    }

    private InvestModel getFakeInvestModel(long loanId, String loginName, TransferStatus transferStatus) {
        InvestModel model = new InvestModel(IdGenerator.generate(), loanId, null, 1000000L, loginName, new DateTime().withTimeAtStartOfDay().toDate(), Source.WEB, null, 0.1);
        model.setStatus(InvestStatus.SUCCESS);
        model.setTransferStatus(transferStatus);
        investMapper.create(model);
        return model;
    }

    private LoanModel getFakeLoan(String loanerLoginName, String agentLoginName, LoanStatus loanStatus, ActivityType activityType) {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(IdGenerator.generate());
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
        userMembershipMapper.create(new UserMembershipModel(loginName, 1, new DateTime().minusDays(1).toDate(), UserMembershipType.UPGRADE));
        return userModelTest;
    }
}
