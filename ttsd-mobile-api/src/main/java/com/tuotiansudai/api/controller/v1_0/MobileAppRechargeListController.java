package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.RechargeListRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppRechargeListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppRechargeListController extends MobileAppBaseController {
    @Autowired
    private MobileAppRechargeListService rechargeListService;

    @RequestMapping(value = "/get/userrecharges", method = RequestMethod.POST)
    public BaseResponseDto queryList(@RequestBody RechargeListRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return rechargeListService.generateRechargeList(requestDto);
    }
}
