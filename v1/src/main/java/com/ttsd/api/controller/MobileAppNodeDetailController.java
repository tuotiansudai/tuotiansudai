package com.ttsd.api.controller;

import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.LoanListRequestDto;
import com.ttsd.api.dto.NodeDetailRequestDto;
import com.ttsd.api.service.MobileAppLoanListService;
import com.ttsd.api.service.MobileAppNodeDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class MobileAppNodeDetailController {
    @Resource
    private MobileAppNodeDetailService mobileAppNodeDetailService;
    @RequestMapping(value="/get/node",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto queryLoanList(@RequestBody NodeDetailRequestDto requestDto){
        return mobileAppNodeDetailService.generateNodeDetail(requestDto);
    }
}
