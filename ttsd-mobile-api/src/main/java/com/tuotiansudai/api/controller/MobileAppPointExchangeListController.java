package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.PointExchangeListRequestDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.dto.UserInvestListRequestDto;
import com.tuotiansudai.api.service.MobileAppInvestListService;
import com.tuotiansudai.api.service.MobileAppPointExchangeListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MobileAppPointExchangeListController extends MobileAppBaseController {
    @Autowired
    private MobileAppPointExchangeListService mobileAppPointExchangeListService;

    @RequestMapping(value = "/get/point-exchange", method = RequestMethod.POST)
    public BaseResponseDto queryPointExchangeList(@RequestBody PointExchangeListRequestDto pointExchangeListRequestDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseResponseDto(errorCode, errorMessage);
        } else {

            pointExchangeListRequestDto.getBaseParam().setUserId(getLoginName());
            return mobileAppPointExchangeListService.generatePointExchangeList(pointExchangeListRequestDto);
        }
    }
}
