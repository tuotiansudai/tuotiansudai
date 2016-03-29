package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.AgreementOperateRequestDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.dto.TransferApplicationRequestDto;
import com.tuotiansudai.api.service.MobileAppAgreementService;
import com.tuotiansudai.api.service.MobileAppTransferApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MobileAppTransferApplyController extends MobileAppBaseController{

    @Autowired
    private MobileAppTransferApplyService mobileAppTransferApplyService;

    @RequestMapping(value = "/transfer-apply", method = RequestMethod.POST)
    public BaseResponseDto generateAgreementRequest(@Valid @RequestBody TransferApplicationRequestDto requestDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseResponseDto(errorCode,errorMessage);
        } else {
            requestDto.getBaseParam().setUserId(getLoginName());
            return mobileAppTransferApplyService.transferApply(requestDto);
        }
    }

}
