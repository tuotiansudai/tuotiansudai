package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.HelpCenterCategoryRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppHelpCenterCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppHelpCenterCategoryListController extends MobileAppBaseController {

    @Autowired
    private MobileAppHelpCenterCategoryService mobileAppHeloCenterCategoryService;

    @RequestMapping(value = "/get/help-center-category-list", method = RequestMethod.POST)
    public BaseResponseDto queryLoanList(@RequestBody HelpCenterCategoryRequestDto helpCenterCategoryRequestDto) {
        helpCenterCategoryRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppHeloCenterCategoryService.generateHelpCenterCategoryList(helpCenterCategoryRequestDto);
    }

}