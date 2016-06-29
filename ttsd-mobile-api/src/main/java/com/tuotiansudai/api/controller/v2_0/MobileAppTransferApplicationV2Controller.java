package com.tuotiansudai.api.controller.v2_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v2_0.TransferableInvestListRequestDto;

import com.tuotiansudai.api.service.v2_0.MobileAppTransferApplicationV2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
public class MobileAppTransferApplicationV2Controller extends MobileAppBaseController {
    @Autowired
    private MobileAppTransferApplicationV2Service mobileAppTransferApplicationV2Service;


    @RequestMapping(value = "/get/transferable", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto generateTransferableInvest(@RequestBody TransferableInvestListRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseResponseDto(errorCode, errorMessage);
        } else {
            requestDto.getBaseParam().setUserId(getLoginName());
            return mobileAppTransferApplicationV2Service.generateTransferableInvest(requestDto);
        }
    }

}
