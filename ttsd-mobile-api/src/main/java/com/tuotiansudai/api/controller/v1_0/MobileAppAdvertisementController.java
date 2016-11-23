package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.AdvertisementResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.service.v1_0.MobileAppAdvertisementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "广告页")
public class MobileAppAdvertisementController extends MobileAppBaseController {

    @Autowired
    private MobileAppAdvertisementService mobileAppAdvertisementService;

    @RequestMapping(value = "/get/advertisement", method = RequestMethod.POST)
    @ApiOperation("获取广告页")
    public BaseResponseDto<AdvertisementResponseDataDto> getAppAdvertisement(@RequestBody BaseParamDto requestDto) {
        return mobileAppAdvertisementService.generateAdvertisement(requestDto);
    }

}