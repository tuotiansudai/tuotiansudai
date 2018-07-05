package com.tuotiansudai.membership.service;

import com.tuotiansudai.membership.dto.UserMembershipItemDto;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipItemView;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserMembershipService {

    @Value(value = "${pay.interest.fee}")
    private double defaultFee;

    @Autowired
    private BankAccountMapper bankAccountMapper;

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    public long findCountMembershipByLevel(long level) {
        return userMembershipMapper.findCountMembershipByLevel(level);
    }

    public MembershipModel getMembershipByLevel(int level) {
        return membershipMapper.findByLevel(level);
    }

    public int getProgressBarPercent(String loginName) {
        //判断当前会员的类型,如果是赠送或者购买则进度条为100%
        UserMembershipModel userMembershipModel = userMembershipEvaluator.evaluateUserMembership(loginName, new Date());
        if (userMembershipModel != null && (userMembershipModel.getType() == UserMembershipType.GIVEN)) {
            return 100;
        }
        long membershipPoint = bankAccountMapper.findInvestorByLoginName(loginName) != null ? bankAccountMapper.findInvestorByLoginName(loginName).getMembershipPoint() : 0;
        int currentLevel = userMembershipEvaluator.evaluateUpgradeLevel(loginName).getLevel();
        MembershipModel membershipModel = membershipMapper.findByLevel(currentLevel);
        MembershipModel NextLevelMembershipModel = this.getMembershipByLevel(currentLevel >= 5 ? currentLevel : (currentLevel + 1));
        double changeable = ((membershipPoint - membershipModel.getExperience()) / (double) (NextLevelMembershipModel.getExperience() - membershipModel.getExperience())) * 0.2 * 100;
        return (int) (membershipModel.getLevel() * 20 == 100 ? 100 : (membershipModel.getLevel() * 20 + changeable) > 100 ? 100 : (membershipModel.getLevel() * 20 + changeable));
    }

    public Integer getMembershipExpireDay(String loginName) {
        MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);
        List<UserMembershipModel> userMembershipModels = userMembershipMapper.findByLoginNameAndMembershipId(loginName, membershipModel.getId());

        Optional<UserMembershipModel> maxExpiredTime = userMembershipModels.stream().max(Comparator.comparingLong(userMembershipModel -> userMembershipModel.getExpiredTime().getTime()));

        if (!maxExpiredTime.isPresent()) {
            return 0;
        }

        if (new DateTime(maxExpiredTime.get().getExpiredTime()).getYear() == 9999) {
            return null;
        }


        return Days.daysBetween(new DateTime(), new DateTime(maxExpiredTime.get().getExpiredTime()).plusDays(1)).getDays();
    }

    public List<UserMembershipItemDto> getUserMembershipItems(String loginName, String mobile,
                                                              Date registerStartTime, Date registerEndTime,
                                                              UserMembershipType userMembershipType,
                                                              List<Integer> levels,
                                                              int index,
                                                              int pageSize) {
        List<UserMembershipItemView> userMembershipItemViews = userMembershipMapper.findUserMembershipItemViews(loginName, mobile, registerStartTime, registerEndTime, userMembershipType, levels, index, pageSize);


        return userMembershipItemViews.stream().map(UserMembershipItemDto::new).collect(Collectors.toList());
    }

    public List<Integer> getAllLevels() {
        List<MembershipModel> allMembership = membershipMapper.findAllMembership();
        return allMembership.stream().map(MembershipModel::getLevel).collect(Collectors.toList());
    }

    public double obtainServiceFee(String loginName) {
        if (StringUtils.isEmpty(loginName)) {
            return defaultFee;
        }

        MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);
        return membershipModel != null ? membershipModel.getFee() : defaultFee;

    }
}
