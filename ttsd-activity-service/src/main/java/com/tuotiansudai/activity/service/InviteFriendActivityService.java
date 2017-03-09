package com.tuotiansudai.activity.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.ReferrerManageService;
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
    private ReferrerManageService referrerManageService;

    @Value(value = "${activity.invite.friend.startTime}")
    private String activityStartTimeStr;

    private List<Long> activityCouponIds = Lists.newArrayList(400l, 401l, 403l, 404l, 405l, 406l, 407l, 408l);

    public Map<String, String> getActivityReferrer(String loginName){
        Map<String, String> activityParam = Maps.newConcurrentMap();
        if(Strings.isNullOrEmpty(loginName)){
            activityParam.put("referrerCount", "0");
            activityParam.put("referrerRedEnvelop", "0");
            activityParam.put("referrerAmount", "0");
            return activityParam;
        }

        Date activityStartTime = DateTime.parse(activityStartTimeStr, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<UserModel> registerUsers = userMapper.findUsersByRegisterTimeOrReferrer(activityStartTime, DateTime.now().toDate(), loginName);

        activityParam.put("referrerCount", String.valueOf(registerUsers.size()));
        activityParam.put("referrerRedEnvelop", AmountConverter.convertCentToString(userCouponMapper.findSumAmountByCouponId(loginName, activityCouponIds)));
        activityParam.put("referrerAmount", referrerManageService.findReferInvestTotalAmount(loginName, null, activityStartTime, DateTime.now().toDate()));
        return activityParam;
    }
}
