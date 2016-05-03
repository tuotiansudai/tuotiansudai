package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.FundManagementRequestDto;
import com.tuotiansudai.api.service.MobileAppFundManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppFundManagementController extends MobileAppBaseController {
    @Autowired
    private MobileAppFundManagementService mobileAppFundManagementService;

    @RequestMapping(value = "/get/userfund", method = RequestMethod.POST)
    public BaseResponseDto queryFundManagement(@RequestBody FundManagementRequestDto fundManagementRequestDto) {
        fundManagementRequestDto.getBaseParam().setUserId(getLoginName());
        fundManagementRequestDto.setUserId(getLoginName());
        return mobileAppFundManagementService.queryFundByUserId(fundManagementRequestDto.getUserId());
    }

}
