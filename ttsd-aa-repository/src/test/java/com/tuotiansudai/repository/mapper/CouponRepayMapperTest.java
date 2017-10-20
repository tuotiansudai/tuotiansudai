package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.*;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class CouponRepayMapperTest extends BaseMapperTest {

    @Autowired
    private CouponRepayMapper couponRepayMapper;

    @Test
    public void shouldCreateCouponRepay() throws Exception {
        UserModel investor = this.createFakeUser("investor");
        UserModel loaner = this.createFakeUser("loaner");
        LoanModel fakeLoan = this.createFakeLoan("loaner", ProductType._30, 1, LoanStatus.REPAYING);
        InvestModel fakeInvest = this.createFakeInvest(fakeLoan.getId(), 1, investor.getLoginName());
        CouponModel fakeInterestCoupon = this.createFakeInterestCoupon(1);
        UserCouponModel fakeUserCoupon = this.createFakeUserCoupon(investor.getLoginName(), fakeInterestCoupon.getId(), fakeLoan.getId(), fakeInvest.getId());
        couponRepayMapper.create(new CouponRepayModel(investor.getLoginName(), fakeInterestCoupon.getId(), fakeUserCoupon.getId(), fakeInvest.getId(), 100, 10, 1, new DateTime().withDate(2016, 1, 1).toDate()));

        CouponRepayModel couponRepayModel = couponRepayMapper.findByUserCouponIdAndPeriod(fakeUserCoupon.getId(), 1);

        assertNotNull(couponRepayModel);
    }

    @Test
    public void shouldUpdateCouponRepay() throws Exception {
        UserModel investor = this.createFakeUser("investor");
        UserModel loaner = this.createFakeUser("loaner");
        LoanModel fakeLoan = this.createFakeLoan("loaner", ProductType._30, 1, LoanStatus.REPAYING);
        InvestModel fakeInvest = this.createFakeInvest(fakeLoan.getId(), 1, investor.getLoginName());
        CouponModel fakeInterestCoupon = this.createFakeInterestCoupon(1);
        UserCouponModel fakeUserCoupon = this.createFakeUserCoupon(investor.getLoginName(), fakeInterestCoupon.getId(), fakeLoan.getId(), fakeInvest.getId());

        CouponRepayModel couponRepayModel = new CouponRepayModel(investor.getLoginName(), fakeInterestCoupon.getId(), fakeUserCoupon.getId(), fakeInvest.getId(), 100, 10, 1, new DateTime().withDate(2016, 1, 1).toDate());
        couponRepayMapper.create(couponRepayModel);
        CouponRepayModel repayModel = couponRepayMapper.findByUserCouponIdAndPeriod(fakeUserCoupon.getId(), 1);
        repayModel.setActualInterest(200);
        repayModel.setActualFee(20);
        repayModel.setActualRepayDate(new DateTime().withDate(2016, 12, 31).withTimeAtStartOfDay().toDate());
        repayModel.setStatus(RepayStatus.COMPLETE);

        couponRepayMapper.update(repayModel);

        CouponRepayModel updatedCouponRepay = couponRepayMapper.findByUserCouponIdAndPeriod(fakeUserCoupon.getId(), 1);

        assertThat(updatedCouponRepay.getActualInterest(), is(repayModel.getActualInterest()));
        assertThat(updatedCouponRepay.getActualFee(), is(repayModel.getActualFee()));
        assertThat(updatedCouponRepay.getStatus(), is(repayModel.getStatus()));
        assertThat(updatedCouponRepay.getActualRepayDate(), is(repayModel.getActualRepayDate()));
    }
}
