package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BannerResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.service.v1_0.MobileAppBannerService;
import com.tuotiansudai.repository.model.Source;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "banner列表")
public class MobileAppBannerController extends MobileAppBaseController {

    @Autowired
    private MobileAppBannerService mobileAppBannerService;

    @RequestMapping(value = "/get/banner", method = RequestMethod.POST)
    @ApiOperation("获取banner列表")
    public BaseResponseDto<BannerResponseDataDto> getAppBanner(@RequestBody BaseParamDto baseParamDto) {
        return mobileAppBannerService.generateBannerList(getLoginName(), Source.valueOf(baseParamDto.getBaseParam().getPlatform().toUpperCase()));
    }

}