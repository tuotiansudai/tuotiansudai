package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.ActivityCenterRequestDto;
import com.tuotiansudai.api.dto.v1_0.ActivityCenterResponseDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MobileAppActivityCenterController extends MobileAppBaseController {
    @Autowired
    private MobileAppActivityService mobileAppActivityService;

    @RequestMapping(value = "/get/activities", method = RequestMethod.POST)
    public BaseResponseDto getAllOperatingActivities(@Valid @RequestBody ActivityCenterRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        ActivityCenterResponseDto activityCenterResponseDto = mobileAppActivityService.getAppActivityCenterResponseData(requestDto);
        BaseResponseDto<ActivityCenterResponseDto> baseResponseDto = new BaseResponseDto<>();
        baseResponseDto.setData(activityCenterResponseDto);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }
}
