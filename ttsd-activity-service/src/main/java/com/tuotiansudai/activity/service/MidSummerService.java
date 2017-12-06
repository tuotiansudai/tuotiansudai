package com.tuotiansudai.activity.service;

import com.tuotiansudai.activity.repository.mapper.MidSummerInvestMapper;
import com.tuotiansudai.activity.repository.mapper.MidSummerSharedUsersMapper;
import com.tuotiansudai.activity.repository.model.MidSummerInvestModel;
import com.tuotiansudai.activity.repository.model.MidSummerSharedUsersModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MidSummerService {

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.mid.summer.startTime}\")}")
    private Date activityStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.mid.summer.endTime}\")}")
    private Date activityEndTime;

    private final UserMapper userMapper;

    private final MidSummerInvestMapper midSummerInvestMapper;

    private final MidSummerSharedUsersMapper midSummerSharedUsersMapper;

    @Autowired
    public MidSummerService(UserMapper userMapper, MidSummerInvestMapper midSummerInvestMapper, MidSummerSharedUsersMapper midSummerSharedUsersMapper) {
        this.userMapper = userMapper;
        this.midSummerInvestMapper = midSummerInvestMapper;
        this.midSummerSharedUsersMapper = midSummerSharedUsersMapper;
    }

    public long getInvitedCount(String loginNameOrMobile) {
        List<MidSummerInvestModel> models = this.midSummerInvestMapper.findByReferrerLoginName(userMapper.findByLoginNameOrMobile(loginNameOrMobile).getLoginName());

        return models.stream()
                .filter(item -> item.getTradingTime().after(activityStartTime) && item.getTradingTime().before(activityEndTime))
                .collect(Collectors.groupingBy(MidSummerInvestModel::getLoginName, Collectors.summingLong(MidSummerInvestModel::getAmount)))
                .entrySet().stream().filter(item -> item.getValue() >= 20000)
                .count();

    }

    public void saveSharedUser(String loginName) {
        if (midSummerSharedUsersMapper.findByLoginName(loginName) == null) {
            midSummerSharedUsersMapper.create(new MidSummerSharedUsersModel(loginName));
        }
    }

    public boolean isUserShared(String mobile) {
        return userMapper.findByMobile(mobile) != null && midSummerSharedUsersMapper.findByLoginName(userMapper.findByMobile(mobile).getLoginName()) != null;
    }
}










