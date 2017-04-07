package com.tuotiansudai.api.controller.v2_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v2_0.LoanDetailV2RequestDto;
import com.tuotiansudai.api.dto.v2_0.LoanDetailV2ResponseDataDto;
import com.tuotiansudai.api.service.v2_0.MobileAppLoanDetailV2Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(description = "V2.0标的详情")
public class MobileAppLoanDetailV2Controller extends MobileAppBaseController {

    static Logger logger = Logger.getLogger(MobileAppLoanDetailV2Controller.class);

    @Autowired
    private MobileAppLoanDetailV2Service mobileAppLoanDetailV2Service;

    @RequestMapping(value = "/get/loan", method = RequestMethod.POST)
    @ApiOperation("标的详情")
    public BaseResponseDto<LoanDetailV2ResponseDataDto> queryLoanDetail(@Valid @RequestBody LoanDetailV2RequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseResponseDto<>(errorCode, errorMessage);
        }
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppLoanDetailV2Service.findLoanDetail(requestDto);
    }
}
