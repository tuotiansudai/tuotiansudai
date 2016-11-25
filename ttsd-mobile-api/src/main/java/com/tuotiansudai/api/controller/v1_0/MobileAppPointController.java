package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppPointService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "积分")
public class MobileAppPointController extends MobileAppBaseController {

    @Autowired
    private MobileAppPointService mobileAppPointService;

    @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
    @ApiOperation("签到")
    public BaseResponseDto<SignInResponseDataDto> signIn(@RequestBody BaseParamDto baseParamDto) {
        baseParamDto.getBaseParam().setUserId(getLoginName());
        return mobileAppPointService.signIn(baseParamDto);
    }

    @RequestMapping(value = "/get/last-sign-in-time", method = RequestMethod.POST)
    @ApiOperation("查询当日签到")
    public BaseResponseDto<LastSignInTimeResponseDataDto> lastSignInTime(@RequestBody BaseParamDto baseParamDto) {
        baseParamDto.getBaseParam().setUserId(getLoginName());
        return mobileAppPointService.getLastSignInTime(baseParamDto);
    }

    @RequestMapping(value = "/get/point-bill", method = RequestMethod.POST)
    @ApiOperation("积分明细")
    public BaseResponseDto<PointBillResponseDataDto> getPointBillData(@RequestBody PointBillRequestDto pointBillRequestDto) {
        pointBillRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppPointService.queryPointBillList(pointBillRequestDto);
    }

    @RequestMapping(value = "/get/point", method = RequestMethod.POST)
    @ApiOperation("查询积分")
    public BaseResponseDto<PointResponseDataDto> getPointBillData(@RequestBody BaseParamDto baseParamDto) {
        baseParamDto.getBaseParam().setUserId(getLoginName());
        return mobileAppPointService.queryPoint(baseParamDto);
    }

}
