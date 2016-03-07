package com.tuotiansudai.coupon.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.UserCouponView;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.util.UserBirthdayUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserCouponServiceImpl implements UserCouponService {

    static Logger logger = Logger.getLogger(UserCouponServiceImpl.class);

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserBirthdayUtil userBirthdayUtil;

    public List<UserCouponView> getUnusedUserCoupons(String loginName) {
        List<UserCouponView> unusedCoupons = userCouponMapper.findUnusedCoupons(loginName);

        for (int i = unusedCoupons.size() - 1; i >= 0; i--) {
            if (unusedCoupons.get(i).getCouponType() == CouponType.BIRTHDAY_COUPON) {
                unusedCoupons.remove(i);
            }
        }
        Collections.sort(unusedCoupons);
        return unusedCoupons;
    }

    public List<UserCouponView> findUseRecords(String loginName) {
        List<UserCouponView> useRecordViews = userCouponMapper.findUseRecords(loginName);
        return useRecordViews;
    }

    public List<UserCouponView> getExpiredUserCoupons(String loginName) {
        List<UserCouponView> expiredCoupons = userCouponMapper.findExpiredCoupons(loginName);

        for (int i = expiredCoupons.size() - 1; i >= 0; i--) {
            if (expiredCoupons.get(i).getCouponType() == CouponType.BIRTHDAY_COUPON) {
                expiredCoupons.remove(i);
            }
        }
        Collections.sort(expiredCoupons);
        return expiredCoupons;
    }


    @Override
    public List<UserCouponDto> getUsableCoupons(String loginName, final long loanId) {
        final LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel == null) {
            return Lists.newArrayList();
        }

        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoginName(loginName, null);
        List<UserCouponDto> dtoList = Lists.transform(userCouponModels, new Function<UserCouponModel, UserCouponDto>() {
            @Override
            public UserCouponDto apply(UserCouponModel userCouponModel) {
                return new UserCouponDto(couponMapper.findById(userCouponModel.getCouponId()), userCouponModel);
            }
        });

        return Lists.newArrayList(Iterators.filter(dtoList.iterator(), new Predicate<UserCouponDto>() {
            @Override
            public boolean apply(UserCouponDto dto) {
                boolean isUsable = dto.getProductTypeList().contains(loanModel.getProductType()) && dto.isUnused();
                switch (dto.getCouponType()) {
                    case BIRTHDAY_COUPON:
                        return isUsable && userBirthdayUtil.isBirthMonth(dto.getLoginName());
                    default:
                        return isUsable;
                }
            }
        }));
    }

}