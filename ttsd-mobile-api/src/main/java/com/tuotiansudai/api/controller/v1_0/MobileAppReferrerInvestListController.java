package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReferrerInvestListRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppReferrerInvestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppReferrerInvestListController extends MobileAppBaseController {
    @Autowired
    private MobileAppReferrerInvestService mobileAppReferrerInvestService;

    @RequestMapping(value = "/get/referrerinvests", method = RequestMethod.POST)
    public BaseResponseDto queryInvestList(@RequestBody ReferrerInvestListRequestDto referrerInvestListRequestDto) {
        referrerInvestListRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppReferrerInvestService.generateReferrerInvestList(referrerInvestListRequestDto);
    }

}
