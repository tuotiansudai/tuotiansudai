package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.AgreementOperateRequestDto;
import com.tuotiansudai.api.dto.v1_0.AgreementOperateResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.service.v1_0.MobileAppAgreementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "开通自动投标")
public class MobileAppAgreementController extends MobileAppBaseController {

    @Autowired
    private MobileAppAgreementService mobileAppAgreementService;

    @RequestMapping(value = "/agreement", method = RequestMethod.POST)
    @ApiOperation("开通自动投标签约")
    public BaseResponseDto<AgreementOperateResponseDataDto> generateAgreementRequest(@Validated @RequestBody AgreementOperateRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppAgreementService.generateAgreementRequest(requestDto);
    }

}
