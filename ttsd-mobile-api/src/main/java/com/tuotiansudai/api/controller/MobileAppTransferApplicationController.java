package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppTransferApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class MobileAppTransferApplicationController extends MobileAppBaseController {

    @Autowired
    private MobileAppTransferApplicationService mobileAppTransferApplicationService;

    @RequestMapping(value = "get/transferrer-transfer-application-list", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto generateTransferApplication(@RequestBody TransferApplicationRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppTransferApplicationService.generateTransferApplication(requestDto);
    }

    @RequestMapping(value = "get/transferee-transfer-application-list", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto generateTransferApplication(@RequestBody PaginationRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppTransferApplicationService.generateTransfereeApplication(requestDto);
    }

    @RequestMapping(value = "/get/transfer-apply", method = RequestMethod.POST)
    @ResponseBody
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
    @ResponseBody
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

    @RequestMapping(value = "/get/transfer-purchase", method = RequestMethod.POST)
    public BaseResponseDto transferPurchase(@RequestBody TransferPurchaseRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppTransferApplicationService.transferPurchase(requestDto);
    }

}
