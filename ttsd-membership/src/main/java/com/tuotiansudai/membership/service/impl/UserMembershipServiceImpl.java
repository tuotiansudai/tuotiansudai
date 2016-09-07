package com.tuotiansudai.membership.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.membership.dto.UserMembershipItemDto;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipItemView;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.membership.service.UserMembershipService;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserMembershipServiceImpl implements UserMembershipService {

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Value("#{'${web.heroRanking.activity.period}'.split('\\~')}")
    private List<String> heroRankingActivityPeriod;

    @Override
    public MembershipModel getMembershipByLevel(int level) {
        return membershipMapper.findByLevel(level);
    }

    @Override
    public int getProgressBarPercent(String loginName) {
        Long membershipPoint = userMembershipMapper.findMembershipPointByLoginName(loginName);
        if(membershipPoint == null){
            membershipPoint = 0L;
        }
        int currentLevel = userMembershipMapper.findRealLevelByLoginName(loginName);
        MembershipModel membershipModel = membershipMapper.findByLevel(currentLevel);
        MembershipModel NextLevelMembershipModel = this.getMembershipByLevel(currentLevel >= 5 ? currentLevel : (currentLevel + 1));
        double changeable = ((membershipPoint - membershipModel.getExperience()) / (double) (NextLevelMembershipModel.getExperience() - membershipModel.getExperience())) * 0.2 * 100;
        return (int) (membershipModel.getLevel() * 20 == 100 ? 100 : (membershipModel.getLevel() * 20 + changeable) > 100 ? 100 : (membershipModel.getLevel() * 20 + changeable));
    }

    @Override
    public int getExpireDayByLoginName(String loginName) {
        UserMembershipModel userMembershipModel = userMembershipMapper.findActiveByLoginName(loginName);
        if(userMembershipModel != null){
            return Days.daysBetween(new DateTime(), new DateTime(userMembershipModel.getExpiredTime()).plusDays(1)).getDays();
        }
        return 0;
    }

    @Override
    public UserMembershipModel findByLoginNameByMembershipId(String loginName, long membershipId){
        UserMembershipModel returnUserMembershipModel = new UserMembershipModel();
        List<UserMembershipModel> userMembershipModels = userMembershipMapper.findByLoginNameByMembershipId(loginName, membershipId);

        if (CollectionUtils.isEmpty(userMembershipModels)) {
            return null;
        }

        for(UserMembershipModel userMembershipModel : userMembershipModels){
            if (userMembershipModel.getType().equals(UserMembershipType.GIVEN)) {
                returnUserMembershipModel = userMembershipModel;
            }
        }

        return userMembershipModels.size() == 1 ? userMembershipModels.get(0) : returnUserMembershipModel;
    }

    @Override
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

    @Override
    public List<Integer> getAllLevels() {
        return membershipMapper.findAllLevels();
    }

    @Override
    public String getMembershipLevelByLoginNameAndInvestTime(String loginName,Date investTime){
        long level = userMembershipMapper.findByLoginNameOrInvestTime(loginName, investTime);
        return String.valueOf(membershipMapper.findById(level).getLevel());
    }
}
