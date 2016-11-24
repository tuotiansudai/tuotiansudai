package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.LoanDetailRequestDto;
import com.tuotiansudai.api.dto.v1_0.LoanDetailResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppLoanDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(description = "标的详情")
public class MobileAppLoanDetailController extends MobileAppBaseController {
    @Autowired
    private MobileAppLoanDetailService mobileAppLoanDetailService;

    @RequestMapping(value = "/get/loan", method = RequestMethod.POST)
    @ApiOperation("标的详情")
    public BaseResponseDto<LoanDetailResponseDataDto> queryLoanList(@Valid @RequestBody LoanDetailRequestDto loanDetailRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseResponseDto<>(errorCode, errorMessage);
        } else {
            loanDetailRequestDto.getBaseParam().setUserId(getLoginName());
            return mobileAppLoanDetailService.generateLoanDetail(loanDetailRequestDto);
        }
    }

}
