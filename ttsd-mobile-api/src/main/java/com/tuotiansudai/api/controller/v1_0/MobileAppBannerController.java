package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BannerResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.service.v1_0.MobileAppBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppBannerController extends MobileAppBaseController {

    @Autowired
    private MobileAppBannerService mobileAppBannerService;

    @RequestMapping(value = "/get/banner", method = RequestMethod.POST)
    public BaseResponseDto<BannerResponseDataDto> getAppBanner() {
        return mobileAppBannerService.generateBannerList();
    }

}