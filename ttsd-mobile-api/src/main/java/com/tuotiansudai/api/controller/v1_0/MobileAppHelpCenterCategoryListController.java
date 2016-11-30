package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.HelpCenterCategoryListResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.HelpCenterCategoryRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppHelpCenterCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "帮助中心类型")
public class MobileAppHelpCenterCategoryListController extends MobileAppBaseController {

    @Autowired
    private MobileAppHelpCenterCategoryService mobileAppHeloCenterCategoryService;

    @RequestMapping(value = "/get/help-center-category-list", method = RequestMethod.POST)
    @ApiOperation("帮助中心问题类型")
    public BaseResponseDto<HelpCenterCategoryListResponseDataDto> queryLoanList(@RequestBody HelpCenterCategoryRequestDto helpCenterCategoryRequestDto) {
        helpCenterCategoryRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppHeloCenterCategoryService.generateHelpCenterCategoryList(helpCenterCategoryRequestDto);
    }

}