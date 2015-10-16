package com.ttsd.api.controller;

import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.NodeListRequestDto;
import com.ttsd.api.service.MobileAppNodeListService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class MobileAppNodeListController {
    @Resource
    private MobileAppNodeListService mobileAppNodeListService;
    @RequestMapping(value="/get/nodes",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto queryLoanList(@RequestBody NodeListRequestDto requestDto){
        return mobileAppNodeListService.generateNodeList(requestDto);
    }
}
