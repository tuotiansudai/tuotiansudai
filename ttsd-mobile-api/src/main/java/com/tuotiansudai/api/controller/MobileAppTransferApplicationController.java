package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.TransferApplicationRequestDto;
import com.tuotiansudai.api.service.MobileAppTransferApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppTransferApplicationController extends MobileAppBaseController {

    @Autowired
    private MobileAppTransferApplicationService mobileAppTransferApplicationService;

    @RequestMapping(value = "get/transferrer-transfer-application-list", method = RequestMethod.POST)
    public BaseResponseDto generateTransferApplication(@RequestBody TransferApplicationRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppTransferApplicationService.generateTransferApplication(requestDto);
    }

}
