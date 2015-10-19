package com.ttsd.api.controller;

import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.LoanDetailRequestDto;
import com.ttsd.api.service.MobileAppLoanDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class MobileAppLoanDetailController {
    @Resource
    private MobileAppLoanDetailService mobileAppLoanDetailService;
    @RequestMapping(value="/get/loan",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto queryLoanList(@RequestBody LoanDetailRequestDto loanDetailRequestDto){
        return mobileAppLoanDetailService.generateLoanDetail(loanDetailRequestDto);
    }

}
