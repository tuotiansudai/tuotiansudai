package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppRepayCalendarService;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class MobileAppRepayCalendarServiceTest extends ServiceTestBase {

    @Autowired
    private MobileAppRepayCalendarService mobileAppRepayCalendarService;

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private BankAccountMapper bankAccountMapper;

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

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Test
    public void shouldGetYearRepayCalendarByIsOk() {
        String loginName = "testRepayCalender";
        long loanId = IdGenerator.generate();
        createUserByUserId(loginName);
        createAccountByUserId(loginName);
        createLoanByUserId(loginName, loanId);
        InvestModel investModel1 = createInvest(loginName, loanId);
        InvestModel investModel2 = createInvest(loginName, loanId);
        InvestModel investModel3 = createInvest(loginName, loanId);
        InvestModel investModel4 = createInvest(loginName, loanId);
        InvestModel investModel5 = createInvest(loginName, loanId);
        InvestModel investModel6 = createInvest(loginName, loanId);

        CouponModel couponModel = fakeCouponModel(loginName);
        couponMapper.create(couponModel);
        UserCouponModel userCouponModel = fakeUserCouponModel(couponModel.getId(), loginName);
        userCouponMapper.create(userCouponModel);
        CouponRepayModel couponRepayModel = createCouponRepay(investModel4.getId(), userCouponModel.getId(), couponModel.getId(), loginName, DateTime.parse("2200-11-01").toDate());
        CouponRepayModel couponRepayModel2 = createCouponRepay(investModel2.getId(), userCouponModel.getId(), couponModel.getId(), loginName, DateTime.parse("2200-10-02").toDate());
        CouponRepayModel couponRepayModel3 = createCouponRepay(investModel2.getId(), userCouponModel.getId(), couponModel.getId(), loginName, DateTime.parse("2200-10-02").toDate());
        CouponRepayModel couponRepayModel4 = createCouponRepay(investModel1.getId(), userCouponModel.getId(), couponModel.getId(), loginName, DateTime.parse("2200-09-03").toDate());
        couponRepayMapper.create(couponRepayModel);
        couponRepayMapper.create(couponRepayModel2);
        couponRepayMapper.create(couponRepayModel3);
        couponRepayMapper.create(couponRepayModel4);

        String toYear = String.valueOf(DateTime.now().getYear());
        List<InvestRepayModel> investRepayModels = new ArrayList<>();
        investRepayModels.add(createInvestRepay(investModel3.getId(), DateTime.parse(toYear + "-11-01").toDate()));
        investRepayModels.add(createInvestRepay(investModel4.getId(), DateTime.parse(toYear + "-11-01").toDate()));
        investRepayModels.add(createInvestRepay(investModel2.getId(), DateTime.parse(toYear + "-10-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel5.getId(), DateTime.parse(toYear + "-10-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel6.getId(), DateTime.parse(toYear + "-10-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel1.getId(), DateTime.parse(toYear + "-09-03").toDate()));

        investRepayMapper.create(investRepayModels);

        RepayCalendarRequestDto repayCalendarRequestDto = new RepayCalendarRequestDto();
        repayCalendarRequestDto.setYear(toYear);
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(loginName);
        repayCalendarRequestDto.setBaseParam(baseParam);
        BaseResponseDto<RepayCalendarListResponseDto> repayCalendarListResponseDtoBaseResponseDto = mobileAppRepayCalendarService.getYearRepayCalendar(repayCalendarRequestDto);
        List<RepayCalendarYearResponseDto> repayCalendarYearResponseDtoList = repayCalendarListResponseDtoBaseResponseDto.getData().getRepayCalendarYearResponseDtos();
        assertThat(repayCalendarYearResponseDtoList.size(), is(12));
        assertEquals(repayCalendarYearResponseDtoList.get(10).getExpectedRepayAmount(), "0.02");
        assertEquals(repayCalendarYearResponseDtoList.get(9).getExpectedRepayAmount(), "0.03");
        assertEquals(repayCalendarYearResponseDtoList.get(8).getExpectedRepayAmount(), "0.01");
    }

    @Test
    public void shouldGetMonthRepayCalendarByIsOk() {
        String loginName = "testRepayCalender";
        long loanId = IdGenerator.generate();
        createUserByUserId(loginName);
        createAccountByUserId(loginName);
        createLoanByUserId(loginName, loanId);
        InvestModel investModel1 = createInvest(loginName, loanId);
        InvestModel investModel2 = createInvest(loginName, loanId);
        InvestModel investModel3 = createInvest(loginName, loanId);
        InvestModel investModel4 = createInvest(loginName, loanId);
        InvestModel investModel5 = createInvest(loginName, loanId);
        InvestModel investModel6 = createInvest(loginName, loanId);

        CouponModel couponModel = fakeCouponModel(loginName);
        couponMapper.create(couponModel);
        UserCouponModel userCouponModel = fakeUserCouponModel(couponModel.getId(), loginName);
        userCouponMapper.create(userCouponModel);
        CouponRepayModel couponRepayModel = createCouponRepay(investModel1.getId(), userCouponModel.getId(), couponModel.getId(), loginName, DateTime.parse("2200-11-01").toDate());
        CouponRepayModel couponRepayModel1 = createCouponRepay(investModel1.getId(), userCouponModel.getId(), couponModel.getId(), loginName, DateTime.parse("2200-11-01").toDate());
        couponRepayMapper.create(couponRepayModel);
        couponRepayMapper.create(couponRepayModel1);

        String toYear = String.valueOf(DateTime.now().getYear());
        List<InvestRepayModel> investRepayModels = new ArrayList<>();
        investRepayModels.add(createInvestRepay(investModel3.getId(), DateTime.parse(toYear + "-11-03").toDate()));
        investRepayModels.add(createInvestRepay(investModel4.getId(), DateTime.parse(toYear + "-11-03").toDate()));
        investRepayModels.add(createInvestRepay(investModel2.getId(), DateTime.parse(toYear + "-11-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel5.getId(), DateTime.parse(toYear + "-11-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel6.getId(), DateTime.parse(toYear + "-11-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel1.getId(), DateTime.parse(toYear + "-11-01").toDate()));

        investRepayMapper.create(investRepayModels);

        RepayCalendarRequestDto repayCalendarRequestDto = new RepayCalendarRequestDto();
        repayCalendarRequestDto.setYear(toYear);
        repayCalendarRequestDto.setMonth("11");
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(loginName);
        repayCalendarRequestDto.setBaseParam(baseParam);
        BaseResponseDto<RepayCalendarMonthResponseDto> baseResponseDto = mobileAppRepayCalendarService.getMonthRepayCalendar(repayCalendarRequestDto);
        assertThat(baseResponseDto.getData().getRepayDate().size(), is(3));
        assertEquals(baseResponseDto.getData().getExpectedRepayAmount(), "0.06");
    }

    @Test
    public void shouldGetDateRepayCalendarIsOk() {
        String loginName = "testRepayCalender";
        long loanId = IdGenerator.generate();
        createUserByUserId(loginName);
        createAccountByUserId(loginName);
        createLoanByUserId(loginName, loanId);
        InvestModel investModel1 = createInvest(loginName, loanId);
        InvestModel investModel2 = createInvest(loginName, loanId);
        InvestModel investModel3 = createInvest(loginName, loanId);
        InvestModel investModel4 = createInvest(loginName, loanId);
        InvestModel investModel5 = createInvest(loginName, loanId);
        InvestModel investModel6 = createInvest(loginName, loanId);

        TransferApplicationModel transferApplicationModel = getTransferApplicationModel(loginName, investModel6.getId(), investModel6.getId(), loanId, new DateTime("2200-11-03").toDate());
        transferApplicationMapper.create(transferApplicationModel);

        CouponModel couponModel = fakeCouponModel(loginName);
        couponMapper.create(couponModel);
        UserCouponModel userCouponModel = fakeUserCouponModel(couponModel.getId(), loginName);
        userCouponMapper.create(userCouponModel);
        CouponRepayModel couponRepayModel = createCouponRepay(investModel6.getId(), userCouponModel.getId(), couponModel.getId(), loginName, DateTime.parse("2200-11-02").toDate());
        CouponRepayModel couponRepayModel1 = createCouponRepay(investModel1.getId(), userCouponModel.getId(), couponModel.getId(), loginName, DateTime.parse("2200-11-01").toDate());
        couponRepayMapper.create(couponRepayModel);
        couponRepayMapper.create(couponRepayModel1);

        String toYear = String.valueOf(DateTime.now().getYear());
        List<InvestRepayModel> investRepayModels = new ArrayList<>();
        investRepayModels.add(createInvestRepay(investModel6.getId(), DateTime.parse(toYear + "-11-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel6.getId(), DateTime.parse(toYear + "-11-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel6.getId(), DateTime.parse(toYear + "-11-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel6.getId(), DateTime.parse(toYear + "-11-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel6.getId(), DateTime.parse(toYear + "-11-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel1.getId(), DateTime.parse(toYear + "-11-01").toDate()));

        investRepayMapper.create(investRepayModels);

        RepayCalendarRequestDto repayCalendarRequestDto = new RepayCalendarRequestDto();
        repayCalendarRequestDto.setDate(toYear + "-11-01");
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(loginName);
        repayCalendarRequestDto.setBaseParam(baseParam);
        BaseResponseDto<RepayCalendarDateListResponseDto> baseResponseDto = mobileAppRepayCalendarService.getDateRepayCalendar(repayCalendarRequestDto);
        assertThat(baseResponseDto.getData().getRepayCalendarDateResponseDtoList().size(), is(1));
        repayCalendarRequestDto.setDate(toYear + "-11-02");
        baseResponseDto = mobileAppRepayCalendarService.getDateRepayCalendar(repayCalendarRequestDto);
        assertThat(baseResponseDto.getData().getRepayCalendarDateResponseDtoList().size(), is(5));
    }


    @Test
    public void shouldGetDateRepayCalendarIsFault() {
        String loginName = "testRepayCalender";
        long loanId = IdGenerator.generate();
        createUserByUserId(loginName);
        createAccountByUserId(loginName);
        createLoanByUserId(loginName, loanId);
        InvestModel investModel1 = createInvest(loginName, loanId);
        InvestModel investModel6 = createInvest(loginName, loanId);

        TransferApplicationModel transferApplicationModel = getTransferApplicationModel(loginName, investModel6.getId(), investModel6.getId(), loanId, new DateTime("2200-11-03").toDate());
        transferApplicationMapper.create(transferApplicationModel);

        CouponModel couponModel = fakeCouponModel(loginName);
        couponMapper.create(couponModel);
        UserCouponModel userCouponModel = fakeUserCouponModel(couponModel.getId(), loginName);
        userCouponMapper.create(userCouponModel);
        CouponRepayModel couponRepayModel = createCouponRepay(investModel6.getId(), userCouponModel.getId(), couponModel.getId(), loginName, DateTime.parse("2200-11-02").toDate());
        CouponRepayModel couponRepayModel1 = createCouponRepay(investModel1.getId(), userCouponModel.getId(), couponModel.getId(), loginName, DateTime.parse("2200-11-01").toDate());
        couponRepayMapper.create(couponRepayModel);
        couponRepayMapper.create(couponRepayModel1);

        String toYear = String.valueOf(DateTime.now().getYear());
        List<InvestRepayModel> investRepayModels = new ArrayList<>();
        investRepayModels.add(createInvestRepay(investModel6.getId(), DateTime.parse(toYear + "-11-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel6.getId(), DateTime.parse(toYear + "-11-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel6.getId(), DateTime.parse(toYear + "-11-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel6.getId(), DateTime.parse(toYear + "-11-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel6.getId(), DateTime.parse(toYear + "-11-02").toDate()));
        investRepayModels.add(createInvestRepay(investModel1.getId(), DateTime.parse(toYear + "-11-01").toDate()));

        investRepayMapper.create(investRepayModels);

        RepayCalendarRequestDto repayCalendarRequestDto = new RepayCalendarRequestDto();
        repayCalendarRequestDto.setDate("2014-11-01");
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(loginName);
        repayCalendarRequestDto.setBaseParam(baseParam);
        BaseResponseDto<RepayCalendarDateListResponseDto> baseResponseDto = mobileAppRepayCalendarService.getDateRepayCalendar(repayCalendarRequestDto);
        assertTrue(CollectionUtils.isEmpty(baseResponseDto.getData().getRepayCalendarDateResponseDtoList()));
        repayCalendarRequestDto.setDate(toYear + "-11-02");
        baseResponseDto = mobileAppRepayCalendarService.getDateRepayCalendar(repayCalendarRequestDto);
        assertThat(baseResponseDto.getData().getRepayCalendarDateResponseDtoList().size(), is(5));
    }

    private TransferApplicationModel getTransferApplicationModel(String userId, long investId, long transferInvestId, long loanId, Date date) {
        TransferApplicationModel transferApplicationModel = new TransferApplicationModel();
        transferApplicationModel.setLoginName(userId);
        transferApplicationModel.setName("name");
        transferApplicationModel.setTransferAmount(1000l);
        transferApplicationModel.setInvestAmount(1200l);
        transferApplicationModel.setTransferTime(date);
        transferApplicationModel.setStatus(TransferStatus.SUCCESS);
        transferApplicationModel.setLoanId(loanId);
        transferApplicationModel.setTransferInvestId(transferInvestId);
        transferApplicationModel.setInvestId(investId);
        transferApplicationModel.setDeadline(new Date());
        transferApplicationModel.setApplicationTime(new Date());
        return transferApplicationModel;
    }

    private UserCouponModel fakeUserCouponModel(long couponId, String loginName) {
        return new UserCouponModel(loginName, couponId, new Date(), new Date());
    }

    private CouponRepayModel createCouponRepay(long investId, long userCouponId, long couponId, String loginName, Date date) {
        CouponRepayModel couponRepayModel = new CouponRepayModel();
        couponRepayModel.setId(IdGenerator.generate());
        couponRepayModel.setInvestId(investId);
        couponRepayModel.setExpectedInterest(1);
        couponRepayModel.setActualFee(0);
        couponRepayModel.setActualInterest(1);
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

    private CouponModel fakeCouponModel(String loginName) {
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
        couponModel.setCouponSource("couponSource");
        return couponModel;
    }

    private InvestRepayModel createInvestRepay(long investId, Date time) {
        InvestRepayModel investRepayModel = new InvestRepayModel();
        investRepayModel.setInvestId(investId);
        investRepayModel.setId(IdGenerator.generate());
        investRepayModel.setStatus(RepayStatus.REPAYING);
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
        loanDto.setPledgeType(PledgeType.HOUSE);
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
        loanModel.setPledgeType(PledgeType.HOUSE);
        loanMapper.create(loanModel);

        LoanDetailsModel loanDetailsModel = new LoanDetailsModel(loanModel.getId(), "", Lists.newArrayList(Source.MOBILE,Source.WEB), false, "");
        loanDetailsMapper.create(loanDetailsModel);
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
        model.setId(IdGenerator.generate());
        model.setLoginName(loginName);
        model.setLoanId(loanId);
        model.setSource(Source.ANDROID);
        model.setStatus(InvestStatus.SUCCESS);
        model.setTransferStatus(TransferStatus.TRANSFERRING);
        investMapper.create(model);
        return model;
    }

    private BankAccountModel createAccountByUserId(String userId) {
        BankAccountModel accountModel = new BankAccountModel(userId, "payUserId", "payAccountId", "111", "111");
        bankAccountMapper.createInvestor(accountModel);
        accountModel.setBalance(10000);
        accountModel.setAutoInvest(true);
        accountModel.setAuthorization(true);
        bankAccountMapper.update(accountModel);
        return accountModel;
    }

}
