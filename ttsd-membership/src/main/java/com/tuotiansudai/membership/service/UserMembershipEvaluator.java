package com.tuotiansudai.membership.service;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Ordering;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserMembershipEvaluator {

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    public MembershipModel evaluateUpgradeLevel(String loginName) {
        List<UserMembershipModel> userMembershipModels = userMembershipMapper.findByLoginName(loginName);

        UnmodifiableIterator<UserMembershipModel> filter = Iterators.filter(userMembershipModels.iterator(), new Predicate<UserMembershipModel>() {
            @Override
            public boolean apply(UserMembershipModel input) {
                return UserMembershipType.UPGRADE == input.getType();
            }
        });

        UserMembershipModel max = new Ordering<UserMembershipModel>() {
            @Override
            public int compare(UserMembershipModel left, UserMembershipModel right) {
                return Ints.compare(membershipMapper.findById(left.getMembershipId()).getLevel(), membershipMapper.findById(right.getMembershipId()).getLevel());
            }
        }.max(filter);

        return membershipMapper.findById(max.getMembershipId());
    }

    public MembershipModel evaluate(String loginName) {
        UserMembershipModel userMembershipModel = this.evaluateUserMembership(loginName, new Date());
        if (userMembershipModel == null) {
            return null;
        }
        return membershipMapper.findById(userMembershipModel.getMembershipId());
    }

    public MembershipModel evaluateSpecifiedDate(String loginName, Date date) {
        UserMembershipModel userMembershipModel = this.evaluateUserMembership(loginName, date);
        if (userMembershipModel == null) {
            return null;
        }
        return membershipMapper.findById(userMembershipModel.getMembershipId());
    }


    public UserMembershipModel evaluateUserMembership(String loginName, final Date date) {
        List<UserMembershipModel> userMembershipModels = userMembershipMapper.findByLoginName(loginName);

        if (CollectionUtils.isEmpty(userMembershipModels)) {
            return null;
        }

        UnmodifiableIterator<UserMembershipModel> filter = Iterators.filter(userMembershipModels.iterator(), new Predicate<UserMembershipModel>() {
            @Override
            public boolean apply(UserMembershipModel input) {
                return input.getExpiredTime().after(date);
            }
        });

        return new Ordering<UserMembershipModel>() {
            @Override
            public int compare(UserMembershipModel left, UserMembershipModel right) {
                MembershipModel leftMembershipModel = membershipMapper.findById(left.getMembershipId());
                MembershipModel rightMembershipModel = membershipMapper.findById(right.getMembershipId());
                return leftMembershipModel.getLevel() == rightMembershipModel.getLevel() ?
                        Longs.compare(left.getExpiredTime().getTime(), right.getExpiredTime().getTime()) : Ints.compare(leftMembershipModel.getLevel(), rightMembershipModel.getLevel());
            }
        }.max(filter);
    }

}
