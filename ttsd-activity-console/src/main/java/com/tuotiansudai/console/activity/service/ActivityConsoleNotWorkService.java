package com.tuotiansudai.console.activity.service;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.dto.NotWorkDto;
import com.tuotiansudai.activity.repository.mapper.NotWorkMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.NotWorkModel;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ActivityConsoleNotWorkService {
    @Autowired
    NotWorkMapper notWorkMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    AccountMapper accountMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.notWork.startTime}\")}")
    private Date activityStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.notWork.endTime}\")}")
    private Date activityEndTime;

    final private Map<Long, String> noWorkRewardMap = new HashMap<Long, String>() {{
        put(300000L, "20元红包");
        put(800000L, "30元话费");
        put(3000000L, "京东E卡");
        put(5000000L, "300元旅游基金(芒果卡)");
        put(10000000L, "索尼数码相机");
        put(20000000L, "联想YOGA 平板3代");
        put(30000000L, "CAN看尚42英寸液晶电视");
        put(52000000L, "锤子手机M1");
        put(80000000L, "浪琴手表康卡斯系列");
        put(120000000L, "Apple MacBook Air笔记本电脑");
    }};

    final private Map<Long, String> annualRewardMap = new HashMap<Long, String>() {{
        put(500000L, "20元红包");
        put(1000000L, "爱奇艺会员月卡");
        put(2000000L, "报销50元电影票");
        put(3000000L, "50元话费  ");
        put(5000000L, "100元中国石化加油卡");
        put(10000000L, "报销300元火车票");
        put(20000000L, "700元京东E卡");
        put(30000000L, "800元红包（50元激活");
        put(50000000L, "1600元芒果卡");
        put(70000000L, "小米手机5");
    }};

    public BasePaginationDataDto<NotWorkDto> findNotWorkPagination(String mobile, ActivityCategory activityCategory, int index, int pageSize) {
        if(activityCategory.equals(ActivityCategory.NO_WORK_ACTIVITY)){
            insertOnlyRegisterOrIdentityData();
        }

        Map<Long, String> rewardRecord = getRewardMap(activityCategory);
        long count = notWorkMapper.findAllCount();
        List<NotWorkModel> notWorkModels = notWorkMapper.findPagination(mobile, activityCategory, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        List<NotWorkDto> records = notWorkModels.stream().map(notWorkModel -> {
            NotWorkDto notWorkDto = new NotWorkDto(notWorkModel);
            List<String> rewardList = new ArrayList<>();
            rewardRecord.forEach((k, v) -> {
                if (k <= notWorkModel.getInvestAmount()) {
                    rewardList.add(v);
                }
            });
            if (rewardList.size() > 0) {
                notWorkDto.setRewards(Joiner.on("、").join(rewardList));
            }
            List<UserModel> users = userMapper.findUsersByRegisterTimeOrReferrer(activityStartTime, activityEndTime, notWorkModel.getLoginName());
            notWorkDto.setRecommendedRegisterAmount(String.valueOf(users.size()));

            int recommendIdentifyAmount = 0;
            for (UserModel userModel : users) {
                AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
                if (null != accountModel && accountModel.getRegisterTime().after(activityStartTime) && accountModel.getRegisterTime().before(activityEndTime)) {
                    ++recommendIdentifyAmount;
                }
            }
            notWorkDto.setRecommendedIdentifyAmount(String.valueOf(recommendIdentifyAmount));

            return notWorkDto;
        }).collect(Collectors.toList());
        return new BasePaginationDataDto<>(index, pageSize, count, records);
    }

    private void insertOnlyRegisterOrIdentityData() {
        List<UserModel> recommendedRegisterUsers = userMapper.findUsersByRegisterTimeOrReferrer(activityStartTime, activityEndTime, null).stream().filter(userModel -> !Strings.isNullOrEmpty(userModel.getReferrer())).collect(Collectors.toList());
        Set<String> referrers = new HashSet<>();
        for (UserModel userModel : recommendedRegisterUsers) {
            referrers.add(userModel.getReferrer());
        }
        for (String loginName : referrers) {
            NotWorkModel existedNotWorkModel = notWorkMapper.findByLoginName(loginName, ActivityCategory.NO_WORK_ACTIVITY);
            if (null != existedNotWorkModel) {
                continue;
            }
            UserModel userModel = userMapper.findByLoginName(loginName);
            if (null != userModel) {
                NotWorkModel notWorkModel = new NotWorkModel(loginName, userModel.getUserName(), userModel.getMobile(), false, ActivityCategory.NO_WORK_ACTIVITY);
                notWorkMapper.create(notWorkModel);
            }
        }
    }

    private Map<Long, String> getRewardMap(ActivityCategory activityCategory) {
        return Maps.newHashMap(ImmutableMap.<ActivityCategory, Map<Long, String>>builder()
                .put(ActivityCategory.NO_WORK_ACTIVITY, noWorkRewardMap)
                .put(ActivityCategory.ANNUAL_ACTIVITY, annualRewardMap)
                .build()).get(activityCategory);
    }
}
