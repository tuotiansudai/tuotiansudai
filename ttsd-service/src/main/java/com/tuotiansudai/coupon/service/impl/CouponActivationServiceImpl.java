package com.tuotiansudai.coupon.service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponActivationService;
import com.tuotiansudai.coupon.util.UserCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class CouponActivationServiceImpl implements CouponActivationService {

    @Resource(name = "investedUserCollector")
    private UserCollector investedUserCollector;

    @Resource(name = "registeredNotInvestedUserCollector")
    private UserCollector registeredNotInvestedUserCollector;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Transactional
    @Override
    public void active(String operator, long couponId) {
        CouponModel couponModel = couponMapper.findById(couponId);
        if (couponModel.isActive()) {
            return;
        }

        UserCollector collector = this.getCollector(couponModel.getUserGroup());

        if (collector != null) {
            List<String> loginNames = collector.collect();
            for (String loginName : loginNames) {
                UserCouponModel userCouponModel = new UserCouponModel(loginName, couponId);
                userCouponMapper.create(userCouponModel);
            }
            couponModel.setTotalCount(loginNames.size());
            couponModel.setIssuedCount(loginNames.size());
        }

        couponModel.setActive(true);
        couponModel.setActiveUser(operator);
        couponModel.setActiveTime(new Date());
        couponMapper.updateCoupon(couponModel);
    }


    private UserCollector getCollector(UserGroup userGroup) {
        return Maps.newHashMap(ImmutableMap.<UserGroup, UserCollector>builder()
                .put(UserGroup.INVESTED_USER, this.investedUserCollector)
                .put(UserGroup.REGISTERED_NOT_INVESTED_USER, this.registeredNotInvestedUserCollector)
                .build()).get(userGroup);
    }
}
