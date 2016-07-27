package com.tuotiansudai.coupon.repository.mapper;

import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.repository.mapper.BaseMapperTest;
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
        LoanModel fakeLoan = this.createFakeLoan("loaner", ProductType._30, 1, LoanStatus.REPAYING);
        InvestModel fakeInvest = this.createFakeInvest(fakeLoan.getId(), 1, investor.getLoginName());
        CouponModel fakeInterestCoupon = this.createFakeInterestCoupon(1);
        UserCouponModel fakeUserCoupon = this.createFakeUserCoupon(investor.getLoginName(), fakeInterestCoupon.getId(), fakeLoan.getId(), fakeInvest.getId());

        CouponRepayModel couponRepayModel = new CouponRepayModel(investor.getLoginName(), fakeInterestCoupon.getId(), fakeUserCoupon.getId(), fakeInvest.getId(), 100, 10, 1, new DateTime().withDate(2016, 1, 1).toDate());
        couponRepayMapper.create(couponRepayModel);

        couponRepayModel.setActualInterest(200);
        couponRepayModel.setActualFee(20);
        couponRepayModel.setActualRepayDate(new DateTime().withDate(2016, 12, 31).withTimeAtStartOfDay().toDate());
        couponRepayModel.setStatus(RepayStatus.COMPLETE);

        couponRepayMapper.update(couponRepayModel);

        CouponRepayModel updatedCouponRepay = couponRepayMapper.findByUserCouponIdAndPeriod(fakeUserCoupon.getId(), 1);

        assertThat(updatedCouponRepay.getActualInterest(), is(couponRepayModel.getActualInterest()));
        assertThat(updatedCouponRepay.getActualFee(), is(couponRepayModel.getActualFee()));
        assertThat(updatedCouponRepay.getStatus(), is(couponRepayModel.getStatus()));
        assertThat(updatedCouponRepay.getActualRepayDate(), is(couponRepayModel.getActualRepayDate()));
    }
}
