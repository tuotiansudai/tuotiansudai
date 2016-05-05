package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.dto.HomeLoanDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Override
    public List<HomeLoanDto> getLoans() {
        final List<CouponModel> allActiveCoupons = couponMapper.findAllActiveCoupons();

        List<LoanModel> loanModels = loanMapper.findHomeLoan();

        return Lists.transform(loanModels, new Function<LoanModel, HomeLoanDto>() {
            @Override
            public HomeLoanDto apply(LoanModel loan) {
                List<InvestModel> investModels = investMapper.findSuccessInvestsByLoanId(loan.getId());
                long investAmount = 0;
                for (InvestModel investModel : investModels) {
                    investAmount += investModel.getAmount();
                }

                CouponModel newbieInterestCouponModel = null;
                for (CouponModel activeCoupon : allActiveCoupons) {
                    if (activeCoupon.getCouponType() == CouponType.INTEREST_COUPON
                            && activeCoupon.getProductTypes().contains(ProductType.SYL)
                            && (newbieInterestCouponModel == null || activeCoupon.getRate() > newbieInterestCouponModel.getRate())) {
                        newbieInterestCouponModel = activeCoupon;
                    }
                }

                List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loan.getId());
                return new HomeLoanDto(newbieInterestCouponModel, loan.getId(),
                        loan.getName(),
                        loan.getProductType(),
                        loan.getActivityType(),
                        loan.getType().getLoanPeriodUnit(),
                        loan.getBaseRate(),
                        loan.getActivityRate(),
                        loan.getPeriods(),
                        loan.getLoanAmount(),
                        investAmount,
                        loan.getStatus(),
                        loan.getFundraisingStartTime(),
                        loanRepayModels
                        );
            }
        });
    }
}
