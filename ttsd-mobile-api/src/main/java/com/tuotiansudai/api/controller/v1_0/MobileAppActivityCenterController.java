package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(description = "活动中心")
public class MobileAppActivityCenterController extends MobileAppBaseController {

    @Autowired
    private MobileAppActivityService mobileAppActivityService;

    @RequestMapping(value = "/get/activities", method = RequestMethod.POST)
    @ApiOperation("获取全部正在进行中的活动")
    public BaseResponseDto<ActivityCenterResponseDto> getAllOperatingActivities(@Valid @RequestBody ActivityCenterRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        ActivityCenterResponseDto activityCenterResponseDto = mobileAppActivityService.getAppActivityCenterResponseData(requestDto);
        BaseResponseDto<ActivityCenterResponseDto> baseResponseDto = new BaseResponseDto<>();
        baseResponseDto.setData(activityCenterResponseDto);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }

    @RequestMapping(value = "/get/activity/school-season-status", method = RequestMethod.POST)
    @ApiOperation("获取开学季活动状态")
    public BaseResponseDto<ActivitySchoolSeasonStatusResponseDto> getSchoolSeasonStatus(@Valid @RequestBody BaseParamDto requestDto) {
        ActivitySchoolSeasonStatusResponseDto activityStatusResponseDto= mobileAppActivityService.getActivitySchoolSeasonStatusResponseDto(ActivityCategory.SCHOOL_SEASON_ACTIVITY, requestDto.getBaseParam().getPhoneNum());
        BaseResponseDto<ActivitySchoolSeasonStatusResponseDto> baseResponseDto = new BaseResponseDto<>();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(activityStatusResponseDto);
        return baseResponseDto;
    }
}
