package com.tuotiansudai.api.service.v1_0.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.repository.mapper.ActivityMapper;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.ActivityModel;
import com.tuotiansudai.activity.repository.model.ActivityStatus;
import com.tuotiansudai.activity.service.LotteryDrawActivityService;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppActivityService;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MobileAppActivityServiceImpl implements MobileAppActivityService {

    @Autowired
    ActivityMapper activityMapper;

    @Value("${common.static.server}")
    private String commonStaticServer;

    @Autowired
    private PageValidUtils pageValidUtils;

    @Autowired
    private UserLotteryPrizeMapper userLotteryPrizeMapper;

    @Autowired
    private UserMapper userMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.school.season.startTime}\")}")
    private Date activitySchoolSeasonStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.school.season.endTime}\")}")
    private Date activitySchoolSeasonEndTime;

    @Value(value = "${web.server}")
    private String url;



    @Override
    public ActivityCenterResponseDto getAppActivityCenterResponseData(ActivityCenterRequestDto requestDto) {
        Source source = Source.valueOf(requestDto.getBaseParam().getPlatform().toUpperCase());
        Integer index = requestDto.getIndex();
        Integer pageSize = pageValidUtils.validPageSizeLimit(requestDto.getPageSize());
        if (null == index) {
            index = 1;
        }

        return fillActivityCenterData(requestDto.getActivityType() == null ? ActivityCenterType.CURRENT : requestDto.getActivityType(), index, pageSize, source);
    }

    private ActivityCenterResponseDto fillActivityCenterData(ActivityCenterType activityType, int index, int pageSize, Source source) {
        pageSize = pageValidUtils.validPageSizeLimit(pageSize);
        List<ActivityCenterDataDto> activityCenterDataDtos = new ArrayList<>();
        List<ActivityModel> activityModels = Lists.newArrayList();
        int totalCount = 0;

        switch (activityType) {
            case CURRENT:
                activityModels = activityMapper.findActivity(source, ActivityStatus.APPROVED, new Date(), null, "true", (index - 1) * pageSize, pageSize);
                totalCount = activityMapper.countActivity(source, ActivityStatus.APPROVED, new Date(), null, "true");
                break;
            case PREVIOUS:
                activityModels = activityMapper.findActivity(source, ActivityStatus.APPROVED, null, new Date(), "false", (index - 1) * pageSize, pageSize);
                totalCount = activityMapper.countActivity(source, ActivityStatus.APPROVED, null, new Date(), "false");
                break;
        }
        for (ActivityModel activityModel : activityModels) {
            ActivityCenterDataDto activityCenterDataDto = new ActivityCenterDataDto(activityModel);
            activityCenterDataDto.setImageUrl(commonStaticServer + activityModel.getAppPictureUrl());
            activityCenterDataDto.setVerticalImageUrl(commonStaticServer + activityModel.getAppVerticalPictureUrl());
            activityCenterDataDtos.add(activityCenterDataDto);
        }
        ActivityCenterResponseDto activityCenterResponseDto = new ActivityCenterResponseDto();
        activityCenterResponseDto.setIndex(index);
        activityCenterResponseDto.setPageSize(pageSize);
        activityCenterResponseDto.setTotalCount(totalCount);
        activityCenterResponseDto.setActivities(activityCenterDataDtos);
        return activityCenterResponseDto;
    }

    @Override
    @Transactional
    public synchronized ActivitySchoolSeasonStatusResponseDto getActivitySchoolSeasonStatusResponseDto(ActivityCategory activityCategory,String loginName){
        UserModel userModel = userMapper.findByLoginName(loginName);

        if (StringUtils.isEmpty(loginName)) {
            return new ActivitySchoolSeasonStatusResponseDto(ActivitySchoolSeasonStatus.DISABLED, "");
        }

        if (userModel == null) {
            return new ActivitySchoolSeasonStatusResponseDto(ActivitySchoolSeasonStatus.DISABLED, "");
        }

        Date nowDate = DateTime.now().toDate();
        if (!nowDate.before(activitySchoolSeasonEndTime) || !nowDate.after(activitySchoolSeasonStartTime)) {
            return new ActivitySchoolSeasonStatusResponseDto(ActivitySchoolSeasonStatus.DISABLED, "");
        }

        int drawTime = userLotteryPrizeMapper.findUserLotteryPrizeCountViews(userModel.getMobile(), null, activityCategory,
                        DateTime.now().withTimeAtStartOfDay().toDate(), DateTime.now().plusDays(1).withTimeAtStartOfDay().plusMillis(-1).toDate());

        if(drawTime==1){
            return new ActivitySchoolSeasonStatusResponseDto(ActivitySchoolSeasonStatus.DONE, url+"/activity/school-season?source=app");
        }

        return new ActivitySchoolSeasonStatusResponseDto(ActivitySchoolSeasonStatus.PENDING, url+"/activity/school-season?source=app");

    }
}
