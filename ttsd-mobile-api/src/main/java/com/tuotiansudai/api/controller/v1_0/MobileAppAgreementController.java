package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.AgreementOperateRequestDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.service.v1_0.MobileAppAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppAgreementController extends MobileAppBaseController{

    @Autowired
    private MobileAppAgreementService mobileAppAgreementService;

    @RequestMapping(value = "/agreement", method = RequestMethod.POST)
    public BaseResponseDto generateAgreementRequest(@RequestBody AgreementOperateRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppAgreementService.generateAgreementRequest(requestDto);
    }

}
