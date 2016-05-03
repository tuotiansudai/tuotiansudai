package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.InvestRepayListRequestDto;
import com.tuotiansudai.api.service.MobileAppInvestRepayListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppInvestRepayListController extends MobileAppBaseController {

    @Autowired
    private MobileAppInvestRepayListService mobileAppInvestRepayListService;

    @RequestMapping(value = "/get/investrepays", method = RequestMethod.POST)
    public BaseResponseDto queryUserInvestList(@RequestBody InvestRepayListRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        BaseResponseDto dto = mobileAppInvestRepayListService.generateUserInvestRepayList(requestDto);
        return dto;
    }
}
