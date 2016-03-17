package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BannerResponseDataDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.service.MobileAppBannerService;
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