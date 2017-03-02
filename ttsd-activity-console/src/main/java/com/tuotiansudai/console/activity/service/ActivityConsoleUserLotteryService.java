package com.tuotiansudai.console.activity.service;


import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.LotteryPrize;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.repository.model.UserLotteryTimeView;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityConsoleUserLotteryService {

    @Autowired
    private UserLotteryPrizeMapper userLotteryPrizeMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private ActivityCountDrawLotteryService commonCountTimeService;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.autumn.startTime}\")}")
    private Date activityAutumnStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.autumn.endTime}\")}")
    private Date activityAutumnEndTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.national.startTime}\")}")
    private Date activityNationalStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.national.endTime}\")}")
    private Date activityNationalEndTime;

    @Value("#{'${activity.carnival.period}'.split('\\~')}")
    private List<String> carnivalTime = Lists.newArrayList();

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.christmas.startTime}\")}")
    private Date activityChristmasStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.christmas.secondStartTime}\")}")
    private Date activityChristmasSecondStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.christmas.endTime}\")}")
    private Date activityChristmasEndTime;

    public List<UserLotteryTimeView> findUserLotteryTimeViews(String mobile, final ActivityCategory prizeType, Integer index, Integer pageSize) {
        List<UserModel> userModels = userMapper.findUserModelByMobile(mobile, index, pageSize);

        Iterator<UserLotteryTimeView> transform = Iterators.transform(userModels.iterator(), input -> {
            UserLotteryTimeView model = new UserLotteryTimeView(input.getMobile(), input.getLoginName());
            model.setUseCount(userLotteryPrizeMapper.findUserLotteryPrizeCountViews(input.getMobile(), null, prizeType, null, null));
            int unUserCount = 0;
            if (prizeType.name().startsWith("MONEY_TREE")) {
                long usedLoginCounts = userLotteryPrizeMapper.findUserLotteryPrizeCountViews(input.getMobile(), null, ActivityCategory.MONEY_TREE, new DateTime(new Date()).withTimeAtStartOfDay().toDate(), new DateTime(new Date()).withTimeAtStartOfDay().plusHours(23).plusMinutes(59).plusSeconds(59).toDate());
                int unReferrerCounts = commonCountTimeService.countDrawLotteryTime(model.getMobile(), prizeType) - model.getUseCount();
                unUserCount = usedLoginCounts == 0 ? (1 + unReferrerCounts) : unReferrerCounts;
            } else {
                unUserCount = commonCountTimeService.countDrawLotteryTime(model.getMobile(), prizeType) - model.getUseCount();
            }
            model.setUnUseCount(unUserCount < 0 ? 0 : unUserCount);
            return model;
        });

        return Lists.newArrayList(transform);
    }

    public int findUserLotteryTimeCountViews(String mobile) {
        return userMapper.findUserModelByMobile(mobile, null, null).size();
    }

    public List<UserLotteryPrizeView> findUserLotteryPrizeViews(String mobile, LotteryPrize selectPrize, ActivityCategory prizeType, Date startTime, Date endTime, Integer index, Integer pageSize) {
        return userLotteryPrizeMapper.findUserLotteryPrizeViews(mobile, selectPrize, prizeType, startTime, endTime, index, pageSize);
    }

    public List<UserLotteryPrizeView> findUserLotteryPrizeViewsByHeadlinesToday(String mobile, LotteryPrize selectPrize, ActivityCategory prizeType, Date startTime, Date endTime, Integer index, Integer pageSize, String authenticationType) {
        List<UserLotteryPrizeView> lotteryPrizeViewList = userLotteryPrizeMapper.findUserLotteryPrizeViews(mobile, selectPrize, prizeType, startTime, endTime, index, pageSize);
        //0-代表未实名认证用户 1-代表已实名认证用户
        return lotteryPrizeViewList.stream()
                .filter((UserLotteryPrizeView userLotteryPrizeView) -> isSpecialAuthType(authenticationType, userLotteryPrizeView))
                .map(userLotteryPrizeView -> {
                    userLotteryPrizeView.setInvestFlag(investMapper.sumSuccessInvestCountByLoginName(userLotteryPrizeView.getLoginName()) > 0 ? "是" : "否");
                    return userLotteryPrizeView;
                }).collect(Collectors.toList());
    }

    public boolean isSpecialAuthType(String authenticationType, UserLotteryPrizeView userLotteryPrizeView) {
        switch (authenticationType) {
            case "0":
                return Strings.isNullOrEmpty(userLotteryPrizeView.getUserName());
            case "1":
                return !Strings.isNullOrEmpty(userLotteryPrizeView.getUserName());
        }
        return true;
    }

    public int findUserLotteryPrizeCountViews(String mobile, LotteryPrize selectPrize, ActivityCategory prizeType, Date startTime, Date endTime) {
        return userLotteryPrizeMapper.findUserLotteryPrizeCountViews(mobile, selectPrize, prizeType, startTime, endTime);
    }
}
