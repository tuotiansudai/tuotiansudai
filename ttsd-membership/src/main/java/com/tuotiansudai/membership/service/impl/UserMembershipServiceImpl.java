package com.tuotiansudai.membership.service.impl;

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
        MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);
        MembershipModel NextLevelMembershipModel = this.getMembershipByLevel(membershipModel.getLevel() >= 5 ? membershipModel.getLevel() : (membershipModel.getLevel() + 1));
        double changeable = ((membershipPoint - membershipModel.getExperience()) / (double) (NextLevelMembershipModel.getExperience() - membershipModel.getExperience())) * 0.2 * 100;
        return (int) (membershipModel.getLevel() * 20 == 100 ? 100 : (membershipModel.getLevel() * 20 + changeable));
    }

    @Override
    public int getExpireDayByLoginName(String loginName) {
        UserMembershipModel userMembershipModel = userMembershipMapper.findActiveByLoginName(loginName);
        return Days.daysBetween(new DateTime(), new DateTime(userMembershipModel.getExpiredTime()).plusDays(1)).getDays();
    }

    @Override
    public UserMembershipModel findByLoginNameByMembershipId(String loginName, long membershipId){
        return userMembershipMapper.findByLoginNameByMembershipId(loginName, membershipId);
    }

    @Override
    public List<UserMembershipItemDto> getUserMembershipItems(String loginName, String mobile,
                                                              Date registerStartTime, Date registerEndTime,
                                                              UserMembershipType userMembershipType,
                                                              List<Integer> levels) {
        if (StringUtils.isEmpty(loginName)) {
            loginName = null;
        }
        if (StringUtils.isEmpty(mobile)) {
            mobile = null;
        }
        if (UserMembershipType.ALL == userMembershipType) {
            userMembershipType = null;
        }
        if (CollectionUtils.isEmpty(levels)) {
            return new ArrayList<UserMembershipItemDto>();
        }
        List<UserMembershipItemView> userMembershipItemViews = userMembershipMapper.findUserMembershipItemViews(loginName, mobile, registerStartTime, registerEndTime, userMembershipType, levels);
        Collections.sort(userMembershipItemViews, new Comparator<UserMembershipItemView>() {
            @Override
            public int compare(UserMembershipItemView o1, UserMembershipItemView o2) {
                return o2.getRegisterTime().after(o1.getRegisterTime()) ? 1 : -1;
            }
        });
        List<UserMembershipItemDto> userMembershipItemDtos = new ArrayList<>();
        for(UserMembershipItemView userMembershipItemView : userMembershipItemViews) {
            userMembershipItemDtos.add(new UserMembershipItemDto(userMembershipItemView));
        }
        return userMembershipItemDtos;
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
