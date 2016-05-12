package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.PointBillRequestDto;
import com.tuotiansudai.api.dto.v1_0.PointTaskRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppPointController extends MobileAppBaseController {

    @Autowired
    private MobileAppPointService mobileAppPointService;

    @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
    public BaseResponseDto signIn(@RequestBody BaseParamDto baseParamDto) {
        baseParamDto.getBaseParam().setUserId(getLoginName());
        return mobileAppPointService.signIn(baseParamDto);
    }

    @RequestMapping(value = "/get/last-sign-in-time", method = RequestMethod.POST)
    public BaseResponseDto lastSignInTime(@RequestBody BaseParamDto baseParamDto) {
        baseParamDto.getBaseParam().setUserId(getLoginName());
        return mobileAppPointService.getLastSignInTime(baseParamDto);
    }

    @RequestMapping(value = "/get/point-bill", method = RequestMethod.POST)
    public BaseResponseDto getPointBillData(@RequestBody PointBillRequestDto pointBillRequestDto) {
        pointBillRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppPointService.queryPointBillList(pointBillRequestDto);
    }

    @RequestMapping(value = "/get/point", method = RequestMethod.POST)
    public BaseResponseDto getPointBillData(@RequestBody BaseParamDto baseParamDto) {
        baseParamDto.getBaseParam().setUserId(getLoginName());
        return mobileAppPointService.queryPoint(baseParamDto);
    }

    @RequestMapping(value = "/get/point-task", method = RequestMethod.POST)
    public BaseResponseDto getPointTask(@RequestBody PointTaskRequestDto pointTaskRequestDto) {
        pointTaskRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppPointService.queryPointTaskList(pointTaskRequestDto);
    }
}
