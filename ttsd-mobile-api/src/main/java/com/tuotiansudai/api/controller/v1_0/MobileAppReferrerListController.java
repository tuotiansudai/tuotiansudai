package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReferrerListRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppReferrerListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppReferrerListController extends MobileAppBaseController {
    @Autowired
    private MobileAppReferrerListService mobileAppReferrerListService;

    @RequestMapping(value = "/get/referrers", method = RequestMethod.POST)
    public BaseResponseDto queryInvestList(@RequestBody ReferrerListRequestDto referrerListRequestDto) {
        referrerListRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppReferrerListService.generateReferrerList(referrerListRequestDto);
    }

}
