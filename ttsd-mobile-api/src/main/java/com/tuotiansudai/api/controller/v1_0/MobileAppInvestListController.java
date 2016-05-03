package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.InvestListRequestDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.dto.UserInvestListRequestDto;
import com.tuotiansudai.api.service.MobileAppInvestListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MobileAppInvestListController extends MobileAppBaseController {
    @Autowired
    private MobileAppInvestListService mobileAppInvestListService;

    @RequestMapping(value = "/get/invests", method = RequestMethod.POST)
    public BaseResponseDto queryInvestList(@Valid @RequestBody InvestListRequestDto investListRequestDto,BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseResponseDto(errorCode, errorMessage);
        } else {

            investListRequestDto.getBaseParam().setUserId(getLoginName());
            return mobileAppInvestListService.generateInvestList(investListRequestDto);
        }
    }

    @RequestMapping(value = "/get/userinvests", method = RequestMethod.POST)
    public BaseResponseDto queryUserInvestList(@RequestBody UserInvestListRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppInvestListService.generateUserInvestList(requestDto);
    }
}
