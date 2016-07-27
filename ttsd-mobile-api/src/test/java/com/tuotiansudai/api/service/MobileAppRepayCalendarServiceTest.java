package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppRepayCalendarService;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class MobileAppRepayCalendarServiceTest {

    @Autowired
    private MobileAppRepayCalendarService mobileAppRepayCalendarService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponRepayMapper couponRepayMapper;

    @Test
    public void shouldGetYearRepayCalendarByIsOk(){
        String loginName = "testRepayCalender";
        long loanId = idGenerator.generate();
        createUserByUserId(loginName);
        createAccountByUserId(loginName);
        createLoanByUserId(loginName,loanId);
        InvestModel investModel1 = createInvest(loginName,loanId);
        InvestModel investModel2 = createInvest(loginName,loanId);
        InvestModel investModel3 = createInvest(loginName,loanId);
        InvestModel investModel4 = createInvest(loginName,loanId);
        InvestModel investModel5 = createInvest(loginName,loanId);
        InvestModel investModel6 = createInvest(loginName,loanId);

        CouponModel couponModel = fakeCouponModel(loginName);
        couponMapper.create(couponModel);
        UserCouponModel userCouponModel = fakeUserCouponModel(couponModel.getId(),loginName);
        userCouponMapper.create(userCouponModel);
        CouponRepayModel couponRepayModel = createCouponRepay(investModel4.getId(),userCouponModel.getId(),couponModel.getId(),loginName,DateTime.parse("2200-11-01").toDate());
        CouponRepayModel couponRepayModel2 = createCouponRepay(investModel2.getId(),userCouponModel.getId(),couponModel.getId(),loginName,DateTime.parse("2200-10-02").toDate());
        CouponRepayModel couponRepayModel3 = createCouponRepay(investModel2.getId(),userCouponModel.getId(),couponModel.getId(),loginName,DateTime.parse("2200-10-02").toDate());
        CouponRepayModel couponRepayModel4 = createCouponRepay(investModel1.getId(),userCouponModel.getId(),couponModel.getId(),loginName,DateTime.parse("2200-09-03").toDate());
        couponRepayMapper.create(couponRepayModel);
        couponRepayMapper.create(couponRepayModel2);
        couponRepayMapper.create(couponRepayModel3);
        couponRepayMapper.create(couponRepayModel4);

        List<InvestRepayModel> investRepayModels = new ArrayList<>();
        investRepayModels.add(createInvestRepay(investModel3.getId(), DateTime.parse("2200-11-01").toDate()));
        investRepayModels.add(createInvestRepay(investModel4.getId(), DateTime.parse("2200-11-01").toDate()));
        investRepayModels.add(createInvestRepay(investModel2.getId(), DateTime.parse("2200-10-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel5.getId(), DateTime.parse("2200-10-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel6.getId(), DateTime.parse("2200-10-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel1.getId(), DateTime.parse("2200-09-03").toDate()));

        investRepayMapper.create(investRepayModels);

        RepayCalendarRequestDto repayCalendarRequestDto = new RepayCalendarRequestDto();
        repayCalendarRequestDto.setYear("2200");
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(loginName);
        repayCalendarRequestDto.setBaseParam(baseParam);
        BaseResponseDto<RepayCalendarListResponseDto> repayCalendarListResponseDtoBaseResponseDto =  mobileAppRepayCalendarService.getYearRepayCalendar(repayCalendarRequestDto);
        List<RepayCalendarYearResponseDto> repayCalendarYearResponseDtoList = repayCalendarListResponseDtoBaseResponseDto.getData().getRepayCalendarYearResponseDtos();
        assertThat(repayCalendarYearResponseDtoList.size(),is(12));
        assertEquals(repayCalendarYearResponseDtoList.get(10).getRepayAmount(),"3");
        assertEquals(repayCalendarYearResponseDtoList.get(9).getRepayAmount(),"5");
        assertEquals(repayCalendarYearResponseDtoList.get(8).getRepayAmount(),"2");
    }

    @Test
    public void shouldGetMonthRepayCalendarByIsOk(){
        String loginName = "testRepayCalender";
        long loanId = idGenerator.generate();
        createUserByUserId(loginName);
        createAccountByUserId(loginName);
        createLoanByUserId(loginName,loanId);
        InvestModel investModel1 = createInvest(loginName,loanId);
        InvestModel investModel2 = createInvest(loginName,loanId);
        InvestModel investModel3 = createInvest(loginName,loanId);
        InvestModel investModel4 = createInvest(loginName,loanId);
        InvestModel investModel5 = createInvest(loginName,loanId);
        InvestModel investModel6 = createInvest(loginName,loanId);

        CouponModel couponModel = fakeCouponModel(loginName);
        couponMapper.create(couponModel);
        UserCouponModel userCouponModel = fakeUserCouponModel(couponModel.getId(),loginName);
        userCouponMapper.create(userCouponModel);
        CouponRepayModel couponRepayModel = createCouponRepay(investModel1.getId(),userCouponModel.getId(),couponModel.getId(),loginName,DateTime.parse("2200-11-01").toDate());
        CouponRepayModel couponRepayModel1 = createCouponRepay(investModel1.getId(),userCouponModel.getId(),couponModel.getId(),loginName,DateTime.parse("2200-11-01").toDate());
        couponRepayMapper.create(couponRepayModel);
        couponRepayMapper.create(couponRepayModel1);

        List<InvestRepayModel> investRepayModels = new ArrayList<>();
        investRepayModels.add(createInvestRepay(investModel3.getId(), DateTime.parse("2200-11-03").toDate()));
        investRepayModels.add(createInvestRepay(investModel4.getId(), DateTime.parse("2200-11-03").toDate()));
        investRepayModels.add(createInvestRepay(investModel2.getId(), DateTime.parse("2200-11-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel5.getId(), DateTime.parse("2200-11-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel6.getId(), DateTime.parse("2200-11-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel1.getId(), DateTime.parse("2200-11-01").toDate()));

        investRepayMapper.create(investRepayModels);

        RepayCalendarRequestDto repayCalendarRequestDto = new RepayCalendarRequestDto();
        repayCalendarRequestDto.setYear("2200");
        repayCalendarRequestDto.setMonth("11");
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(loginName);
        repayCalendarRequestDto.setBaseParam(baseParam);
        BaseResponseDto<RepayCalendarMonthResponseDto> baseResponseDto = mobileAppRepayCalendarService.getMonthRepayCalendar(repayCalendarRequestDto);
        assertThat(baseResponseDto.getData().getRepayDate().size(),is(3));
        assertEquals(baseResponseDto.getData().getRepayAmount(),"8");
    }

    @Test
    public void shouldGetDateRepayCalendarIsOk(){
        String loginName = "testRepayCalender";
        long loanId = idGenerator.generate();
        createUserByUserId(loginName);
        createAccountByUserId(loginName);
        createLoanByUserId(loginName,loanId);
        InvestModel investModel1 = createInvest(loginName,loanId);
        InvestModel investModel2 = createInvest(loginName,loanId);
        InvestModel investModel3 = createInvest(loginName,loanId);
        InvestModel investModel4 = createInvest(loginName,loanId);
        InvestModel investModel5 = createInvest(loginName,loanId);
        InvestModel investModel6 = createInvest(loginName,loanId);

        CouponModel couponModel = fakeCouponModel(loginName);
        couponMapper.create(couponModel);
        UserCouponModel userCouponModel = fakeUserCouponModel(couponModel.getId(),loginName);
        userCouponMapper.create(userCouponModel);
        CouponRepayModel couponRepayModel = createCouponRepay(investModel6.getId(),userCouponModel.getId(),couponModel.getId(),loginName,DateTime.parse("2200-11-02").toDate());
        CouponRepayModel couponRepayModel1 = createCouponRepay(investModel1.getId(),userCouponModel.getId(),couponModel.getId(),loginName,DateTime.parse("2200-11-01").toDate());
        couponRepayMapper.create(couponRepayModel);
        couponRepayMapper.create(couponRepayModel1);

        List<InvestRepayModel> investRepayModels = new ArrayList<>();
        investRepayModels.add(createInvestRepay(investModel6.getId(), DateTime.parse("2200-11-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel6.getId(), DateTime.parse("2200-11-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel6.getId(), DateTime.parse("2200-11-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel6.getId(), DateTime.parse("2200-11-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel6.getId(), DateTime.parse("2200-11-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel1.getId(), DateTime.parse("2200-11-01").toDate()));

        investRepayMapper.create(investRepayModels);

        RepayCalendarRequestDto repayCalendarRequestDto = new RepayCalendarRequestDto();
        repayCalendarRequestDto.setDate("2200-11-01");
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(loginName);
        repayCalendarRequestDto.setBaseParam(baseParam);
        BaseResponseDto<RepayCalendarDateListResponseDto> baseResponseDto = mobileAppRepayCalendarService.getDateRepayCalendar(repayCalendarRequestDto);
        assertThat(baseResponseDto.getData().getRepayCalendarDateResponseDtoList().size(),is(1));
        assertEquals(baseResponseDto.getData().getRepayCalendarDateResponseDtoList().get(0).getStatus(),RepayStatus.REPAYING.name());
        repayCalendarRequestDto.setDate("2200-11-02");
        baseResponseDto = mobileAppRepayCalendarService.getDateRepayCalendar(repayCalendarRequestDto);
        assertThat(baseResponseDto.getData().getRepayCalendarDateResponseDtoList().size(),is(5));
    }

    private UserCouponModel fakeUserCouponModel(long couponId,String loginName) {
        return new UserCouponModel(loginName, couponId, new Date(), new Date());
    }

    private CouponRepayModel createCouponRepay(long investId,long userCouponId,long couponId,String loginName,Date date){
        CouponRepayModel couponRepayModel = new CouponRepayModel();
        couponRepayModel.setId(idGenerator.generate());
        couponRepayModel.setInvestId(investId);
        couponRepayModel.setExpectedInterest(1);
        couponRepayModel.setActualFee(0);
        couponRepayModel.setActualInterest(1);
        couponRepayModel.setActualRepayDate(new Date());
        couponRepayModel.setCouponId(couponId);
        couponRepayModel.setCreatedTime(new Date());
        couponRepayModel.setLoginName(loginName);
        couponRepayModel.setPeriod(1);
        couponRepayModel.setRepayDate(date);
        couponRepayModel.setStatus(RepayStatus.COMPLETE);
        couponRepayModel.setTransferred(true);
        couponRepayModel.setUserCouponId(userCouponId);
        couponRepayModel.setCouponId(couponId);
        return couponRepayModel;
    }

    private CouponModel fakeCouponModel(String loginName){
        CouponModel couponModel = new CouponModel();
        couponModel.setAmount(1000L);
        couponModel.setActivatedBy(loginName);
        couponModel.setActive(false);
        couponModel.setCreatedTime(new Date());
        couponModel.setEndTime(new Date());
        couponModel.setDeadline(10);
        couponModel.setStartTime(new Date());
        couponModel.setCreatedBy(loginName);
        couponModel.setTotalCount(1000L);
        couponModel.setUsedCount(500L);
        couponModel.setInvestLowerLimit(10000L);
        couponModel.setCouponType(CouponType.INVEST_COUPON);
        couponModel.setProductTypes(Lists.newArrayList(ProductType._30, ProductType._90, ProductType._180));
        return couponModel;
    }

    private InvestRepayModel createInvestRepay(long investId,Date time){
        InvestRepayModel investRepayModel = new InvestRepayModel();
        investRepayModel.setInvestId(investId);
        investRepayModel.setId(idGenerator.generate());
        investRepayModel.setStatus(RepayStatus.REPAYING);
        investRepayModel.setActualRepayDate(new Date());
        investRepayModel.setRepayDate(time);
        investRepayModel.setExpectedInterest(1);
        investRepayModel.setActualInterest(1);
        investRepayModel.setPeriod(3);
        return investRepayModel;
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
        loanDto.setActivityType(ActivityType.NEWBIE);
        loanDto.setContractId(123);
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("10000");
        loanDto.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.WAITING_VERIFY);
        loanDto.setProductType(ProductType._30);
        com.tuotiansudai.repository.model.LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setStatus(LoanStatus.RAISING);
        loanMapper.create(loanModel);
        return loanModel;
    }

    private UserModel createUserByUserId(String userId) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(userId);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("1" + RandomStringUtils.randomNumeric(10));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
        return userModelTest;
    }

    private InvestModel createInvest(String loginName, long loanId) {
        InvestModel model = new InvestModel();
        model.setAmount(10);
        model.setInvestTime(new Date());
        model.setId(idGenerator.generate());
        model.setIsAutoInvest(false);
        model.setLoginName(loginName);
        model.setLoanId(loanId);
        model.setSource(Source.ANDROID);
        model.setStatus(InvestStatus.SUCCESS);
        model.setTransferStatus(TransferStatus.TRANSFERRING);
        investMapper.create(model);
        return model;
    }

    private AccountModel createAccountByUserId(String userId) {
        AccountModel accountModel = new AccountModel(userId, userId, "120101198810012010", "", "", new Date());
        accountModel.setBalance(10000);
        accountModel.setFreeze(10000);
        accountMapper.create(accountModel);
        accountModel.setAutoInvest(true);
        accountModel.setNoPasswordInvest(true);
        accountMapper.update(accountModel);
        return accountModel;
    }

}
