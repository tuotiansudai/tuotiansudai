package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.ActivityCenterDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v2_0.BaseParamDto;
import com.tuotiansudai.dto.ActivityDto;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MobileAppActivityCenterController extends MobileAppBaseController {
    @Autowired
    private ActivityService activityService;

    @RequestMapping(value = "/get/activities", method = RequestMethod.GET)
    public BaseResponseDto getAllOperatingActivities(@RequestBody BaseParamDto baseParamDto) {
        String loginName = baseParamDto.getBaseParam().getUserId();
        List<ActivityDto> activityDtos = activityService.getAllOperatingActivities(loginName, Source.MOBILE);

        ActivityCenterDataDto activityCenterDataDto = new ActivityCenterDataDto();
        activityCenterDataDto.setActivities(activityDtos);

        BaseResponseDto<ActivityCenterDataDto> baseResponseDto = new BaseResponseDto<>();
        baseResponseDto.setData(activityCenterDataDto);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());

        return baseResponseDto;
    }
}
