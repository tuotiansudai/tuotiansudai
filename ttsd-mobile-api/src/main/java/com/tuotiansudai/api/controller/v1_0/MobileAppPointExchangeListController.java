package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.PointExchangeListRequestDto;
import com.tuotiansudai.api.dto.v1_0.PointExchangeRequestDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppPointExchangeListService;
import com.tuotiansudai.api.service.v1_0.MobileAppPointExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppPointExchangeListController extends MobileAppBaseController {
    @Autowired
    private MobileAppPointExchangeListService mobileAppPointExchangeListService;
    @Autowired
    private MobileAppPointExchangeService mobileAppPointExchangeService;

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

    @RequestMapping(value = "/point-exchange", method = RequestMethod.POST)
    public BaseResponseDto PointExchange(@RequestBody PointExchangeRequestDto pointExchangeRequestDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseResponseDto(errorCode, errorMessage);
        } else {
            pointExchangeRequestDto.getBaseParam().setUserId(getLoginName());
            return mobileAppPointExchangeService.generatePointExchange(pointExchangeRequestDto);
        }
    }
}
