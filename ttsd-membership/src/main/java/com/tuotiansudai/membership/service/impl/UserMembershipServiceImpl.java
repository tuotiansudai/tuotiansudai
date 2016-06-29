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
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.membership.service.UserMembershipService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class UserMembershipServiceImpl implements UserMembershipService {

    static Logger logger = Logger.getLogger(UserMembershipServiceImpl.class);

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

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
            membershipPoint = 0l;
        }
        int currentLevel = userMembershipMapper.findRealLevelByLoginName(loginName);
        MembershipModel membershipModel = membershipMapper.findByLevel(currentLevel);
        MembershipModel NextLevelMembershipModel = this.getMembershipByLevel(currentLevel >= 5 ? currentLevel : (currentLevel + 1));
        double changeable = ((membershipPoint - membershipModel.getExperience()) / (double) (NextLevelMembershipModel.getExperience() - membershipModel.getExperience())) * 0.2 * 100;
        return (int) (membershipModel.getLevel() * 20 == 100 ? 100 : (membershipModel.getLevel() * 20 + changeable));
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

    private List<UserMembershipItemView> filterUpgradeUserMembershipItems(List<UserMembershipItemView> userMembershipItemViews) {
        class Data {
            public int index;
            public Date createdTime;

            public Data(int index, Date createdTime) {
                this.index = index;
                this.createdTime = createdTime;
            }
        }
        HashMap<String, Data> membershipFilterInfo = new HashMap<>();
        for (UserMembershipItemView userMembershipItemView : userMembershipItemViews) {
            Data data = membershipFilterInfo.get(userMembershipItemView.getLoginName());
            if (null != data) {
                if (data.createdTime.before(userMembershipItemView.getCreatedTime())) {
                    data.index = userMembershipItemViews.indexOf(userMembershipItemView);
                    data.createdTime = userMembershipItemView.getCreatedTime();
                }
            } else {
                if (userMembershipItemView.getUserMembershipType().equals(UserMembershipType.UPGRADE)) {
                    membershipFilterInfo.put(userMembershipItemView.getLoginName(),
                            new Data(userMembershipItemViews.indexOf(userMembershipItemView), userMembershipItemView.getCreatedTime()));
                }
            }
        }
        List<UserMembershipItemView> filteredUserMembershipItems = new ArrayList<>();
        for (Data data : membershipFilterInfo.values()) {
            filteredUserMembershipItems.add(userMembershipItemViews.get(data.index));
        }
        return filteredUserMembershipItems;
    }

    private List<UserMembershipItemView> filterGivenUserMembershipItems(List<UserMembershipItemView> userMembershipItemViews) {
        class Data {
            public int index;
            public int membershipLevel;

            public Data(int index, int membershipLevel) {
                this.index = index;
                this.membershipLevel = membershipLevel;
            }
        }
        HashMap<String, Data> membershipFilterInfo = new HashMap<>();
        for (UserMembershipItemView userMembershipItemView : userMembershipItemViews) {
            Data data = membershipFilterInfo.get(userMembershipItemView.getLoginName());
            if (null != data) {
                if (data.membershipLevel < userMembershipItemView.getMembershipLevel()) {
                    data.index = userMembershipItemViews.indexOf(userMembershipItemView);
                    data.membershipLevel = userMembershipItemView.getMembershipLevel();
                }
            } else {
                if (userMembershipItemView.getUserMembershipType().equals(UserMembershipType.GIVEN) &&
                        userMembershipItemView.getExpiredTime().after(new Date())) {
                    membershipFilterInfo.put(userMembershipItemView.getLoginName(),
                            new Data(userMembershipItemViews.indexOf(userMembershipItemView), userMembershipItemView.getMembershipLevel()));
                }
            }
        }
        List<UserMembershipItemView> filteredUserMembershipItems = new ArrayList<>();
        for (Data data : membershipFilterInfo.values()) {
            filteredUserMembershipItems.add(userMembershipItemViews.get(data.index));
        }
        return filteredUserMembershipItems;
    }

    @Override
    public List<UserMembershipItemDto> getUserMembershipItems(String loginName, String mobile,
                                                              Date registerStartTime, Date registerEndTime,
                                                              UserMembershipType userMembershipType,
                                                              List<Integer> levels,
                                                              int index,
                                                              int pageSize) {
        List<UserMembershipItemView> userMembershipItemViews = userMembershipMapper.findUserMembershipItemViews(loginName, mobile, registerStartTime, registerEndTime, userMembershipType, levels,index,pageSize);

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
    public void createUserMemberByLevel(long level,String loginName) {
        MembershipModel membershipModel = membershipMapper.findByLevel(level);
        UserMembershipModel userMembershipModel = new UserMembershipModel(loginName, membershipModel.getId(), new DateTime(2200, 1, 1, 1, 1).toDate(), UserMembershipType.UPGRADE);
        userMembershipMapper.create(userMembershipModel);
    }
}
