package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.LoanDetailRequestDto;
import com.tuotiansudai.api.service.MobileAppLoanDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MobileAppLoanDetailController extends MobileAppBaseController {
    @Autowired
    private MobileAppLoanDetailService mobileAppLoanDetailService;

    @RequestMapping(value = "/get/loan", method = RequestMethod.POST)
    public BaseResponseDto queryLoanList(@Valid @RequestBody LoanDetailRequestDto loanDetailRequestDto) {
        return mobileAppLoanDetailService.generateLoanDetail(loanDetailRequestDto);
    }

}
