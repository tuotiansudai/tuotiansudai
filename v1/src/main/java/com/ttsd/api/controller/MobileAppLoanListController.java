package com.ttsd.api.controller;

import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.LoanListRequestDto;
import com.ttsd.api.service.MobileAppLoanListService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class MobileAppLoanListController {
    @Resource
    private MobileAppLoanListService mobileAppLoanListService;
    @RequestMapping(value="/get/loans",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto queryLoanList(@RequestBody LoanListRequestDto loanListRequestDto){
        return mobileAppLoanListService.generateLoanList(loanListRequestDto);
    }

}
