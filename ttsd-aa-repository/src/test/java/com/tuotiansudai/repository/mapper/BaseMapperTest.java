package com.tuotiansudai.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public abstract class BaseMapperTest {


    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    public UserModel createFakeUser(String loginName) {
//        if (userMapper.findByLoginName(loginName) != null) {
//            return userMapper.findByLoginName(loginName);
//        }
        UserModel model = new UserModel();
        model.setLoginName(loginName);
        model.setPassword("password");
        model.setEmail(MessageFormat.format("{0}@tuotiansudai.com", loginName));
        model.setMobile(RandomStringUtils.randomNumeric(11));
        model.setRegisterTime(new Date());
        model.setStatus(UserStatus.ACTIVE);
        model.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(model);
        return model;
    }

    public LoanModel createFakeLoan(String loaner, ProductType productType, long amount, LoanStatus loanStatus) {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(IdGenerator.generate());
        fakeLoanModel.setName("Fake Loan");
//        fakeLoanModel.setLoanerLoginName(userMapper.findByLoginName(loaner) != null ? loaner : createFakeUser(loaner).getLoginName());
        fakeLoanModel.setLoanerLoginName(loaner);
        fakeLoanModel.setLoanerUserName("借款人");
        fakeLoanModel.setLoanerIdentityNumber(RandomStringUtils.randomNumeric(18));
        fakeLoanModel.setAgentLoginName(loaner);
        fakeLoanModel.setBaseRate(0.1);
        fakeLoanModel.setPeriods(productType.getPeriods());
        fakeLoanModel.setActivityType(ActivityType.NORMAL);
        fakeLoanModel.setContractId(1);
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setInvestIncreasingAmount(1);
        fakeLoanModel.setLoanAmount(amount);
        fakeLoanModel.setType(LoanType.LOAN_INTEREST_MONTHLY_REPAY);
        fakeLoanModel.setMaxInvestAmount(amount);
        fakeLoanModel.setMinInvestAmount(1);
        fakeLoanModel.setCreatedTime(new Date());
        fakeLoanModel.setStatus(loanStatus);
        fakeLoanModel.setProductType(productType);
        fakeLoanModel.setPledgeType(PledgeType.HOUSE);
        loanMapper.create(fakeLoanModel);
        return fakeLoanModel;
    }

    public InvestModel createFakeInvest(long loanId, long amount, String loginName) {
        InvestModel fakeInvestModel = new InvestModel(IdGenerator.generate(), loanId, null, loginName, amount, 0.1, false, new Date(), Source.WEB, null);
        fakeInvestModel.setTradingTime(new Date());
        fakeInvestModel.setStatus(InvestStatus.SUCCESS);
        investMapper.create(fakeInvestModel);
        return fakeInvestModel;
    }

    public CouponModel createFakeInterestCoupon(double rate) {
//        if (userMapper.findByLoginName("couponCreator") == null) {
        this.createFakeUser("couponCreator");
//        }
        CouponModel couponModel = new CouponModel();
        couponModel.setRate(rate);
        couponModel.setActive(true);
        couponModel.setCreatedTime(new Date());
        couponModel.setStartTime(new Date());
        couponModel.setEndTime(new DateTime().plusDays(1).toDate());
        couponModel.setDeadline(30);
        couponModel.setActivatedBy("couponCreator");
        couponModel.setCreatedBy("couponCreator");
        couponModel.setTotalCount(10L);
        couponModel.setUsedCount(0L);
        couponModel.setInvestLowerLimit(10000L);
        couponModel.setCouponType(CouponType.INTEREST_COUPON);
        couponModel.setProductTypes(Lists.newArrayList(ProductType._30, ProductType._90, ProductType._180, ProductType._360));
        couponModel.setCouponSource("couponSource");
        couponMapper.create(couponModel);
        return couponModel;
    }

    public UserCouponModel createFakeUserCoupon(String loginName, long couponId, long loanId, long investId) {
        UserCouponModel userCouponModel = new UserCouponModel(loginName, couponId, new Date(), new Date());
        userCouponModel.setLoanId(loanId);
        userCouponModel.setUsedTime(new Date());
        userCouponModel.setInvestId(investId);
        userCouponModel.setStatus(InvestStatus.SUCCESS);
        userCouponMapper.create(userCouponModel);
        return userCouponModel;
    }

}
