package com.tuotiansudai.membership.service;

import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserMembershipEvaluator {

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    public MembershipModel evaluateUpgradeLevel(String loginName) {
        List<UserMembershipModel> userMembershipModels = userMembershipMapper.findByLoginName(loginName);

        Optional<UserMembershipModel> max = userMembershipModels.stream().filter(input -> input.getType() == UserMembershipType.UPGRADE)
                .max(Comparator.comparingInt(input -> membershipMapper.findById(input.getMembershipId()).getLevel()));

        return max.isPresent() ? membershipMapper.findById(max.get().getMembershipId()) : null;
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

        Optional<UserMembershipModel> max = userMembershipModels.stream().filter(input -> input.getCreatedTime().before(date) && input.getExpiredTime().after(date)).
                max((left, right) -> left.getMembershipId() == right.getMembershipId() ?
                        Long.compare(left.getExpiredTime().getTime(), right.getExpiredTime().getTime()) : Long.compare(left.getMembershipId(), right.getMembershipId()));

        return max.orElse(userMembershipModels.stream().sorted(Comparator.comparing(UserMembershipModel::getCreatedTime)).collect(Collectors.toList()).get(0));
    }
}
