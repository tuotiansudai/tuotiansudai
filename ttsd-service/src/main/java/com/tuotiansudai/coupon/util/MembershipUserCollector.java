package com.tuotiansudai.coupon.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MembershipUserCollector implements UserCollector {

    private Map<Long, UserGroup> mapping = Maps.newHashMap( new ImmutableMap.Builder<Long, UserGroup>()
            .put(0l, UserGroup.MEMBERSHIP_V0)
            .put(1l, UserGroup.MEMBERSHIP_V1)
            .put(2l, UserGroup.MEMBERSHIP_V2)
            .put(3l, UserGroup.MEMBERSHIP_V3)
            .put(4l, UserGroup.MEMBERSHIP_V4)
            .put(5l, UserGroup.MEMBERSHIP_V5)
            .build());

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Override
    public List<String> collect(long couponId) {
        CouponModel couponModel = couponMapper.findById(couponId);
        for (Map.Entry integerUserGroupEntry : mapping.entrySet()) {
            if(integerUserGroupEntry.getValue().equals(couponModel.getUserGroup())){
                return userMembershipMapper.findLoginNameMembershipByLevel((Long) integerUserGroupEntry.getKey());
            }
        }
        return Lists.newArrayList();
    }

    @Override
    public boolean contains(long couponId, String loginName) {
        CouponModel couponModel = couponMapper.findById(couponId);

        UserGroup userGroup = couponModel.getUserGroup();

        MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);

        return membershipModel != null && mapping.get(membershipModel.getLevel()) == userGroup;
    }
}
