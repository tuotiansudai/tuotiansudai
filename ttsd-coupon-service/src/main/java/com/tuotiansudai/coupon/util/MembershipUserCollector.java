package com.tuotiansudai.coupon.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserGroup;
import com.tuotiansudai.repository.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MembershipUserCollector implements UserCollector {

    private Map<Integer, UserGroup> mapping = Maps.newHashMap(new ImmutableMap.Builder<Integer, UserGroup>()
            .put(0, UserGroup.MEMBERSHIP_V0)
            .put(1, UserGroup.MEMBERSHIP_V1)
            .put(2, UserGroup.MEMBERSHIP_V2)
            .put(3, UserGroup.MEMBERSHIP_V3)
            .put(4, UserGroup.MEMBERSHIP_V4)
            .put(5, UserGroup.MEMBERSHIP_V5)
            .build());

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    @Override
    public boolean contains(CouponModel couponModel, UserModel userModel) {
        if (couponModel == null || userModel == null) {
            return false;
        }

        MembershipModel membershipModel = userMembershipEvaluator.evaluate(userModel.getLoginName());

        return membershipModel != null && mapping.get(membershipModel.getLevel()) == couponModel.getUserGroup();
    }
}
