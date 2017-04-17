package com.tuotiansudai.api.controller;


import com.tuotiansudai.api.dto.HTrackingRequestDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppHTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/htracking")
public class MobileAppHTrackingController {

    @Autowired
    private MobileAppHTrackingService mobileAppHTrackingService;


    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    public BaseResponseDto callback(@RequestBody HTrackingRequestDto hTrackingRequestDto) {
        return mobileAppHTrackingService.save(hTrackingRequestDto);
    }


}
