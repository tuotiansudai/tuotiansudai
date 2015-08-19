package com.ttsd.api.controller;

import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.NodeListRequestDto;
import com.ttsd.api.service.MobileAppNodeListService;
import com.ttsd.api.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class MobileAppNodeListController {
    @Autowired
    private HttpServletRequest request;

    @Resource
    private MobileAppNodeListService mobileAppNodeListService;

    @RequestMapping(value = "/get/nodes", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto queryLoanList(@RequestBody NodeListRequestDto requestDto) {
        String baseUrl = CommonUtils.getRequestBaseUrl(request);
        return mobileAppNodeListService.generateNodeList(requestDto, baseUrl);
    }
}
