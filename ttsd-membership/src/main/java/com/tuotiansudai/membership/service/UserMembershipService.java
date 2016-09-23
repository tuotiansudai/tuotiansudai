package com.tuotiansudai.membership.service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Longs;
import com.tuotiansudai.membership.dto.UserMembershipItemDto;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipItemView;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.repository.mapper.AccountMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserMembershipService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    public MembershipModel getMembershipByLevel(int level) {
        return membershipMapper.findByLevel(level);
    }

    public int getProgressBarPercent(String loginName) {
        long membershipPoint = accountMapper.findByLoginName(loginName) != null ? accountMapper.findByLoginName(loginName).getMembershipPoint() : 0;
        int currentLevel = userMembershipEvaluator.evaluateUpgradeLevel(loginName).getLevel();
        MembershipModel membershipModel = membershipMapper.findByLevel(currentLevel);
        MembershipModel NextLevelMembershipModel = this.getMembershipByLevel(currentLevel >= 5 ? currentLevel : (currentLevel + 1));
        double changeable = ((membershipPoint - membershipModel.getExperience()) / (double) (NextLevelMembershipModel.getExperience() - membershipModel.getExperience())) * 0.2 * 100;
        return (int) (membershipModel.getLevel() * 20 == 100 ? 100 : (membershipModel.getLevel() * 20 + changeable) > 100 ? 100 : (membershipModel.getLevel() * 20 + changeable));
    }

    public Integer getMembershipExpireDay(String loginName) {
        MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);
        List<UserMembershipModel> userMembershipModels = userMembershipMapper.findByLoginNameAndMembershipId(loginName, membershipModel.getId());
        UserMembershipModel max = new Ordering<UserMembershipModel>() {
            @Override
            public int compare(UserMembershipModel left, UserMembershipModel right) {
                return Longs.compare(left.getExpiredTime().getTime(), right.getExpiredTime().getTime());
            }
        }.max(userMembershipModels);

        if (new DateTime(max.getExpiredTime()).getYear() == 9999) {
            return null;
        }

        return Days.daysBetween(new DateTime(), new DateTime(max.getExpiredTime()).plusDays(1)).getDays();
    }

    public List<UserMembershipItemDto> getUserMembershipItems(String loginName, String mobile,
                                                              Date registerStartTime, Date registerEndTime,
                                                              UserMembershipType userMembershipType,
                                                              List<Integer> levels,
                                                              int index,
                                                              int pageSize) {
        List<UserMembershipItemView> userMembershipItemViews = userMembershipMapper.findUserMembershipItemViews(loginName, mobile, registerStartTime, registerEndTime, userMembershipType, levels, index, pageSize);

        return Lists.transform(userMembershipItemViews, new Function<UserMembershipItemView, UserMembershipItemDto>() {
            @Override
            public UserMembershipItemDto apply(UserMembershipItemView input) {
                return new UserMembershipItemDto(input);
            }
        });
    }

    public List<Integer> getAllLevels() {
        List<MembershipModel> allMembership = membershipMapper.findAllMembership();
        return Lists.transform(allMembership, new Function<MembershipModel, Integer>() {
            @Override
            public Integer apply(MembershipModel input) {
                return input.getLevel();
            }
        });
    }

    public List<UserMembershipModel> getExpiredUserMembership(Date expiredDate) {
        return userMembershipMapper.findExpiredUserMembership(expiredDate);
    }
}
