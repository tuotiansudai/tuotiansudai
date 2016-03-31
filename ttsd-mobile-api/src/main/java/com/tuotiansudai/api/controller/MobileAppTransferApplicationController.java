package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppTransferApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MobileAppTransferApplicationController extends MobileAppBaseController {

    @Autowired
    private MobileAppTransferApplicationService mobileAppTransferApplicationService;

    @RequestMapping(value = "get/transferrer-transfer-application-list", method = RequestMethod.POST)
    public BaseResponseDto generateTransferApplication(@RequestBody TransferApplicationRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppTransferApplicationService.generateTransferApplication(requestDto);
    }

    @RequestMapping(value = "/get/transfer-apply", method = RequestMethod.POST)
    public BaseResponseDto transferApplyQuery(@Valid @RequestBody TransferApplyQueryRequestDto requestDto,BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseResponseDto(errorCode, errorMessage);
        } else {
            requestDto.getBaseParam().setUserId(getLoginName());
            return mobileAppTransferApplicationService.transferApplyQuery(requestDto);
        }
    }

    @RequestMapping(value = "/transfer-apply", method = RequestMethod.POST)
    public BaseResponseDto generateAgreementRequest(@Valid @RequestBody TransferApplyRequestDto requestDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseResponseDto(errorCode,errorMessage);
        } else {
            requestDto.getBaseParam().setUserId(getLoginName());
            return mobileAppTransferApplicationService.transferApply(requestDto);
        }
    }

}
