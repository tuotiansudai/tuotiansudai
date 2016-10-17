package coupon.service;

import coupon.repository.mapper.CouponRepayMapper;
import coupon.repository.model.CouponRepayModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CouponRepayService {
    @Autowired
    private CouponRepayMapper couponRepayMapper;

    public void create(CouponRepayModel couponRepayModel) {
        couponRepayMapper.create(couponRepayModel);
    }

    public CouponRepayModel findByUserCouponIdAndPeriod(long userCouponId, long period) {
        return couponRepayMapper.findByUserCouponIdAndPeriod(userCouponId, period);
    }

    public long update(CouponRepayModel couponRepayModel) {
        return couponRepayMapper.update(couponRepayModel);
    }

    public List<CouponRepayModel> findCouponRepayByInvestIdAndRepayDate(String loginName, long investId, String year,
                                                                        String month, String day) {
        return couponRepayMapper.findCouponRepayByInvestIdAndRepayDate(loginName, investId, year, month, day);
    }

    public List<CouponRepayModel> findByUserCouponByInvestId(long investId) {
        return couponRepayMapper.findByUserCouponByInvestId(investId);
    }

    public CouponRepayModel findByUserCouponByInvestIdAndPeriod(long investId, int period) {
        return couponRepayMapper.findByUserCouponByInvestIdAndPeriod(investId, period);
    }

    public List<CouponRepayModel> findCouponRepayByLoanIdAndPeriod(long loanId, int period) {
        return couponRepayMapper.findCouponRepayByLoanIdAndPeriod(loanId, period);
    }

    public CouponRepayModel findCouponRepayByInvestIdAndPeriod(long investId, int period) {
        return couponRepayMapper.findCouponRepayByInvestIdAndPeriod(investId, period);
    }
}
