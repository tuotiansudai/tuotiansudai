package com.tuotiansudai.activity.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.mapper.ReferrerManageMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class InviteFriendActivityService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private ReferrerManageMapper referrerManageMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Value(value = "${activity.invite.friend.startTime}")
    private String activityStartTimeStr;

    @Value(value = "${pay.user.reward}")
    private String userReward;

    @Value(value = "${pay.staff.reward}")
    private String staffReward;

    private List<Long> activityCouponIds = Lists.newArrayList(400l, 401l, 403l, 404l, 405l, 406l, 407l, 408l);

    public Map<String, String> getActivityReferrer(String loginName) {
        Map<String, String> activityParam = Maps.newConcurrentMap();
        if (Strings.isNullOrEmpty(loginName)) {
            activityParam.put("referrerCount", "0");
            activityParam.put("referrerRedEnvelop", "0");
            activityParam.put("referrerAmount", "0");
            return activityParam;
        }

        Date activityStartTime = DateTime.parse(activityStartTimeStr, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        long referrerCount = userMapper.findUserCountByRegisterTimeAndReferrer(activityStartTime, DateTime.now().toDate(), loginName);

        activityParam.put("referrerCount", String.valueOf(referrerCount));
        activityParam.put("referrerRedEnvelop", AmountConverter.convertCentToString(userCouponMapper.findSumAmountByCouponId(loginName, activityCouponIds)));
        activityParam.put("referrerAmount", findReferInvestTotalAmount(loginName, null, activityStartTime, DateTime.now().toDate()));
        return activityParam;
    }

    private String findReferInvestTotalAmount(String referrerLoginName, String loginName, Date startTime, Date endTime) {
        String level = getUserRewardDisplayLevel(referrerLoginName);
        endTime = new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        Long totalAmount = referrerManageMapper.findReferInvestTotalAmount(referrerLoginName, loginName, startTime, endTime, level);
        return AmountConverter.convertCentToString(totalAmount);
    }

    private String getUserRewardDisplayLevel(String loginName) {
        int level = 0;
        int merLevel = staffReward.split("\\|").length;
        int userLevel = userReward.split("\\|").length;

        List<UserRoleModel> userRoleModelList = userRoleMapper.findByLoginName(loginName);
        for (UserRoleModel model : userRoleModelList) {
            if (Lists.newArrayList(Role.ZC_STAFF, Role.SD_STAFF).contains(model.getRole())) {
                level = merLevel > level ? merLevel : level;
            } else if (model.getRole().equals(Role.USER)) {
                level = userLevel > level ? userLevel : level;
            }
        }
        return level == 0 ? null : String.valueOf(level);
    }

}
