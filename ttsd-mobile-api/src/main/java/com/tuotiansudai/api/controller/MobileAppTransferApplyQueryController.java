package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.TransferApplyQueryRequestDto;
import com.tuotiansudai.api.service.MobileAppTransferApplyQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppTransferApplyQueryController extends MobileAppBaseController{

    @Autowired
    private MobileAppTransferApplyQueryService mobileAppTransferApplyQueryService;

    @RequestMapping(value = "/agreement", method = RequestMethod.POST)
    public BaseResponseDto generateAgreementRequest(@RequestBody TransferApplyQueryRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppTransferApplyQueryService.transferApplyQuery(requestDto);
    }

}
