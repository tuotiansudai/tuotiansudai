package com.ttsd.api.controller;

import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.WithdrawListRequestDto;
import com.ttsd.api.service.MobileAppBannerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class MobileAppBannerController {
    @Resource
    private MobileAppBannerService mobileAppBannerService;

    @RequestMapping(value = "/get/banner", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto getAppBanner() {
        return mobileAppBannerService.getAppBanner();
    }

}
