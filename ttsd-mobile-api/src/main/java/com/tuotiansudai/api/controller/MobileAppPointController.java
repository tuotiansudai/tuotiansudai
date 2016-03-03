package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.PointBillRequestDto;
import com.tuotiansudai.api.service.MobileAppPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppPointController extends MobileAppBaseController{

    @Autowired
    private MobileAppPointService mobileAppPointService;

    @RequestMapping(value = "/get/point-bill", method = RequestMethod.POST)
    public BaseResponseDto getPointBillData(@RequestBody PointBillRequestDto pointBillRequestDto) {
        pointBillRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppPointService.queryPointBillList(pointBillRequestDto);
    }

}
