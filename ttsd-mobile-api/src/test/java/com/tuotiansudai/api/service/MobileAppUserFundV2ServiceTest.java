package com.tuotiansudai.api.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.UserFundResponseDataDto;
import com.tuotiansudai.api.service.v2_0.MobileAppUserFundV2Service;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.CouponRepayModel;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.enums.WithdrawStatus;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MobileAppUserFundV2ServiceTest extends ServiceTestBase {

    @Autowired
    private MobileAppUserFundV2Service mobileAppUserFundV2Service;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

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
    private InvestReferrerRewardMapper investReferrerRewardMapper;

    @Autowired
    private InvestExtraRateMapper investExtraRateMapper;

    @Autowired
    private WithdrawMapper withdrawMapper;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Test
    public void shouldGetUserFund() throws Exception {
        UserModel myUserFund = getFakeUser("myUserFund");
        userMapper.create(myUserFund);
        createFakeAccount(myUserFund);
        LoanModel fakeLoanModel1 = createFakeLoanModel(myUserFund.getLoginName(), LoanStatus.REPAYING);
        InvestModel fakeInvest1 = createFakeInvest(fakeLoanModel1.getId(), 1, myUserFund.getLoginName(), InvestStatus.SUCCESS, TransferStatus.TRANSFERABLE);
        InvestRepayModel fakeInvestRepay1 = createFakeInvestRepay(fakeInvest1.getId(), 1, 0, 10, 1, new Date(), null, RepayStatus.COMPLETE);
        fakeInvestRepay1.setActualInterest(10);
        fakeInvestRepay1.setActualFee(1);
        investRepayMapper.update(fakeInvestRepay1);
        createFakeInvestRepay(fakeInvest1.getId(), 2, 1, 20, 2, new Date(), null, RepayStatus.REPAYING);

        LoanModel fakeLoanModel2 = createFakeLoanModel(myUserFund.getLoginName(), LoanStatus.RAISING);
        createFakeInvest(fakeLoanModel2.getId(), 2, myUserFund.getLoginName(), InvestStatus.SUCCESS, TransferStatus.TRANSFERABLE);

        CouponModel fakeInterestCoupon = createFakeCoupon(myUserFund.getLoginName(), CouponType.INTEREST_COUPON, 0);
        UserCouponModel userInterestCouponModel = new UserCouponModel(myUserFund.getLoginName(), fakeInterestCoupon.getId(), new Date(), new Date());
        userCouponMapper.create(userInterestCouponModel);
        userInterestCouponModel.setInvestId(fakeInvest1.getId());
        userInterestCouponModel.setStatus(InvestStatus.SUCCESS);
        userCouponMapper.update(userInterestCouponModel);

        CouponRepayModel couponRepayModel1 = new CouponRepayModel(myUserFund.getLoginName(), fakeInterestCoupon.getId(), userInterestCouponModel.getId(), fakeInvest1.getId(), 10, 1, 1, new Date());
        couponRepayMapper.create(couponRepayModel1);
        couponRepayModel1.setActualInterest(10);
        couponRepayModel1.setActualFee(1);
        couponRepayModel1.setStatus(RepayStatus.COMPLETE);
        couponRepayMapper.update(couponRepayModel1);

        CouponRepayModel couponRepayModel2 = new CouponRepayModel(myUserFund.getLoginName(), fakeInterestCoupon.getId(), userInterestCouponModel.getId(), fakeInvest1.getId(), 20, 2, 2, new Date());
        couponRepayMapper.create(couponRepayModel2);
        couponRepayModel2.setStatus(RepayStatus.REPAYING);
        couponRepayMapper.update(couponRepayModel2);

        CouponModel fakeRedEnvelopeCoupon = createFakeCoupon(myUserFund.getLoginName(), CouponType.RED_ENVELOPE, 10);
        UserCouponModel userRedEnvelopeCouponModel = new UserCouponModel(myUserFund.getLoginName(), fakeRedEnvelopeCoupon.getId(), new Date(), new Date());
        userCouponMapper.create(userRedEnvelopeCouponModel);
        userRedEnvelopeCouponModel.setInvestId(fakeInvest1.getId());
        userRedEnvelopeCouponModel.setStatus(InvestStatus.SUCCESS);
        userRedEnvelopeCouponModel.setActualInterest(10);
        userRedEnvelopeCouponModel.setActualFee(0);
        userCouponMapper.update(userRedEnvelopeCouponModel);

        InvestReferrerRewardModel investReferrerRewardModel = new InvestReferrerRewardModel(idGenerator.generate(), fakeInvest1.getId(), 10, myUserFund.getLoginName(), Role.INVESTOR);
        investReferrerRewardModel.setStatus(ReferrerRewardStatus.SUCCESS);
        investReferrerRewardMapper.create(investReferrerRewardModel);

        InvestExtraRateModel investExtraRateModel1 = new InvestExtraRateModel();
        investExtraRateModel1.setId(idGenerator.generate());
        investExtraRateModel1.setLoanId(fakeLoanModel1.getId());
        investExtraRateModel1.setInvestId(fakeInvest1.getId());
        investExtraRateModel1.setRepayDate(new Date());
        investExtraRateModel1.setLoginName(myUserFund.getLoginName());
        investExtraRateModel1.setActualInterest(10);
        investExtraRateModel1.setActualFee(1);
        investExtraRateModel1.setStatus(RepayStatus.REPAYING);
        investExtraRateMapper.create(investExtraRateModel1);

        InvestExtraRateModel investExtraRateModel2 = new InvestExtraRateModel();
        investExtraRateModel2.setId(idGenerator.generate());
        investExtraRateModel2.setLoanId(fakeLoanModel1.getId());
        investExtraRateModel2.setInvestId(fakeInvest1.getId());
        investExtraRateModel2.setRepayDate(new Date());
        investExtraRateModel2.setLoginName(myUserFund.getLoginName());
        investExtraRateModel2.setExpectedInterest(20);
        investExtraRateModel2.setExpectedFee(2);
        investExtraRateModel2.setStatus(RepayStatus.REPAYING);
        investExtraRateMapper.create(investExtraRateModel2);


        BankCardModel bankCardModel = new BankCardModel();
        bankCardModel.setLoginName(myUserFund.getLoginName());
        bankCardModel.setBankCode("ICBC");
        bankCardModel.setCardNumber("123");
        bankCardModel.setStatus(BankCardStatus.PASSED);
        bankCardModel.setCreatedTime(new Date());
        bankCardMapper.create(bankCardModel);

        WithdrawModel withdrawModel = new WithdrawModel();
        withdrawModel.setId(idGenerator.generate());
        withdrawModel.setBankCardId(bankCardModel.getId());
        withdrawModel.setLoginName(myUserFund.getLoginName());
        withdrawModel.setAmount(100);
        withdrawModel.setStatus(WithdrawStatus.APPLY_SUCCESS);
        withdrawModel.setSource(Source.WEB);
        withdrawModel.setCreatedTime(new Date());
        withdrawMapper.create(withdrawModel);

        BaseResponseDto<UserFundResponseDataDto> userFund = mobileAppUserFundV2Service.getUserFund(myUserFund.getLoginName());

        UserFundResponseDataDto data = userFund.getData();

        assertThat(data.getActualTotalInterest(), is(18L));
        assertThat(data.getActualTotalExtraInterest(), is(9L));
        assertThat(data.getReferRewardAmount(), is(10L));
        assertThat(data.getRedEnvelopeAmount(), is(10L));
        assertThat(data.getExpectedTotalCorpus(), is(1L));
        assertThat(data.getExpectedTotalInterest(), is(36L));
        assertThat(data.getExpectedTotalExtraInterest(), is(18L));
        assertThat(data.getInvestFrozeAmount(), is(2L));
        assertThat(data.getWithdrawFrozeAmount(), is(100L));
    }

    private LoanModel createFakeLoanModel(String loginName, LoanStatus loanStatus) {
        LoanModel loanModel = new LoanModel();
        loanModel.setAgentLoginName(loginName);
        loanModel.setId(idGenerator.generate());
        loanModel.setName("name");
        loanModel.setBaseRate(0);
        loanModel.setActivityRate(0);
        loanModel.setPeriods(1);
        loanModel.setActivityType(ActivityType.NORMAL);
        loanModel.setContractId(123);
        loanModel.setDescriptionHtml("html");
        loanModel.setDescriptionText("text");
        loanModel.setFundraisingEndTime(new Date());
        loanModel.setFundraisingStartTime(new Date());
        loanModel.setInvestIncreasingAmount(1);
        loanModel.setLoanAmount(1);
        loanModel.setType(LoanType.LOAN_INTEREST_MONTHLY_REPAY);
        loanModel.setMinInvestAmount(1);
        loanModel.setMaxInvestAmount(1);
        loanModel.setCreatedTime(new Date());
        loanModel.setStatus(loanStatus);
        loanModel.setLoanerLoginName(loginName);
        loanModel.setLoanerUserName("username");
        loanModel.setLoanerIdentityNumber(RandomStringUtils.randomNumeric(18));
        loanModel.setDuration(ProductType._90.getDuration());
        loanModel.setRecheckTime(new DateTime().minusDays(10).withTimeAtStartOfDay().toDate());
        loanModel.setPledgeType(PledgeType.HOUSE);
        loanMapper.create(loanModel);
        return loanModel;
    }

    private AccountModel createFakeAccount(UserModel userModel) {
        AccountModel accountModel = new AccountModel(userModel.getLoginName(), RandomStringUtils.randomNumeric(32), RandomStringUtils.randomNumeric(14), userModel.getRegisterTime());
        accountMapper.create(accountModel);
        return accountModel;
    }

    private InvestModel createFakeInvest(long loanId, long amount, String loginName, InvestStatus investStatus, TransferStatus transferStatus) {
        InvestModel fakeInvestModel = new InvestModel(idGenerator.generate(), loanId, null, amount, loginName, new Date(), Source.WEB, null, 0.1);
        fakeInvestModel.setStatus(investStatus);
        fakeInvestModel.setTransferStatus(transferStatus);
        investMapper.create(fakeInvestModel);
        return fakeInvestModel;
    }

    private InvestRepayModel createFakeInvestRepay(long investId, int period, long corpus, long expectedInterest, long expectedFee, Date expectedRepayDate, Date actualRepayDate, RepayStatus repayStatus) {
        InvestRepayModel fakeInvestRepayModel = new InvestRepayModel(idGenerator.generate(), investId, period, corpus, expectedInterest, expectedFee, expectedRepayDate, repayStatus);
        fakeInvestRepayModel.setActualRepayDate(actualRepayDate);
        investRepayMapper.create(Lists.newArrayList(fakeInvestRepayModel));
        return fakeInvestRepayModel;
    }

    private CouponModel createFakeCoupon(String loginName, CouponType couponType, long amount) {
        CouponModel couponModel = new CouponModel();
        couponModel.setAmount(amount);
        couponModel.setActivatedBy(loginName);
        couponModel.setActive(true);
        couponModel.setCreatedTime(new Date());
        couponModel.setEndTime(new Date());
        couponModel.setDeadline(10);
        couponModel.setStartTime(new Date());
        couponModel.setCreatedBy(loginName);
        couponModel.setTotalCount(1L);
        couponModel.setUsedCount(1);
        couponModel.setInvestLowerLimit(0L);
        couponModel.setCouponType(couponType);
        couponModel.setCouponSource("source");
        couponModel.setProductTypes(Lists.newArrayList(ProductType._30, ProductType._90, ProductType._180));
        couponMapper.create(couponModel);
        return couponModel;
    }

}
