package com.tuotiansudai.coupon.service.impl;

import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.membership.service.MembershipPrivilegePurchaseService;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponServiceImpl implements CouponService {

    static Logger logger = Logger.getLogger(CouponServiceImpl.class);

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Value(value = "${pay.interest.fee}")
    private double defaultFee;

    @Override
    public long estimateCouponExpectedInterest(String loginName, double investFeeRate, long loanId, List<Long> couponIds, long amount, Date investTime) {
        long totalInterest = 0;

        //根据loginNameName查询出当前会员的相关信息,需要判断是否为空,如果为空则安装在费率0.1计算
        LoanModel loanModel = loanMapper.findById(loanId);
        if(loanModel != null && ProductType.EXPERIENCE == loanModel.getProductType()){
            investFeeRate = 0;
            totalInterest = InterestCalculator.estimateExperienceExpectedInterest(amount, loanModel);
        }

        for (Long couponId : couponIds) {
            CouponModel couponModel = couponMapper.findById(couponId);
            if (loanModel == null || couponModel == null) {
                continue;
            }
            long expectedInterest = InterestCalculator.estimateCouponExpectedInterest(amount, loanModel, couponModel, investTime);
            long expectedFee = InterestCalculator.estimateCouponExpectedFee(loanModel, couponModel, amount, investFeeRate);
            totalInterest += expectedInterest - expectedFee;
        }

        return totalInterest;
    }

    @Override
    public long findExperienceInvestAmount(List<InvestModel> investModelList) {
        long amount = 0;
        if (CollectionUtils.isNotEmpty(investModelList)) {
            List<UserCouponModel> userCouponModels = userCouponMapper.findByInvestId(investModelList.get(0).getId());
            if (userCouponModels.size() > 0){
                CouponModel couponModel = couponMapper.findById(userCouponModels.get(0).getCouponId());
                amount = new BigDecimal(investModelList.size() % 100).multiply(new BigDecimal(couponModel.getAmount())).setScale(0, BigDecimal.ROUND_DOWN).longValue();
            }
        }
        return amount;
    }

    @Override
    public CouponModel findExchangeableCouponById(long couponId){
        return couponMapper.findExchangeableCouponById(couponId);
    }

    @Override
    public List<CouponModel> findCouponByUserGroup(List<UserGroup> userGroups) {
        return couponMapper.findAllActiveCoupons().stream().filter(input -> userGroups.contains(input.getUserGroup())).collect(Collectors.toList());
    }
}
