package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppInvestListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(description = "出借管理")
public class MobileAppInvestListController extends MobileAppBaseController {
    @Autowired
    private MobileAppInvestListService mobileAppInvestListService;

    @RequestMapping(value = "/get/invests", method = RequestMethod.POST)
    @ApiOperation("出借管理-个人出借详情")
    public BaseResponseDto<InvestListResponseDataDto> queryInvestList(@Valid @RequestBody InvestListRequestDto investListRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseResponseDto<>(errorCode, errorMessage);
        } else {
            investListRequestDto.getBaseParam().setUserId(getLoginName());
            return mobileAppInvestListService.generateInvestList(investListRequestDto);
        }
    }

    @RequestMapping(value = "/get/userinvests", method = RequestMethod.POST)
    @ApiOperation("已出借项目")
    public BaseResponseDto<UserInvestListResponseDataDto> queryUserInvestList(@RequestBody UserInvestListRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppInvestListService.generateUserInvestList(requestDto);
    }
}
