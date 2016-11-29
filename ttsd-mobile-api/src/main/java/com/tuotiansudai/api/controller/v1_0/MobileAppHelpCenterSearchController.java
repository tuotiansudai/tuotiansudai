package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.HelpCenterSearchRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppHelpCenterSearchService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppHelpCenterSearchController extends MobileAppBaseController {

    @Autowired
    private MobileAppHelpCenterSearchService mobileAppHelpCenterSearchService;

    @RequestMapping(value = "/get/help-center-search", method = RequestMethod.POST)
    public BaseResponseDto getHelpCenterSearchResult(@RequestBody HelpCenterSearchRequestDto dto) {
        dto.getBaseParam().setUserId(getLoginName());
        return mobileAppHelpCenterSearchService.getHelpCenterSearchResult(dto);
    }
}
