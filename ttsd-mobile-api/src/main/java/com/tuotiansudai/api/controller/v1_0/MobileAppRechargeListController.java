package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.RechargeListRequestDto;
import com.tuotiansudai.api.service.MobileAppRechargeListService;
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
