package com.ttsd.api.controller;

import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.NodeDetailRequestDto;
import com.ttsd.api.service.MobileAppNodeDetailService;
import com.ttsd.api.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MobileAppNodeDetailController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private MobileAppNodeDetailService mobileAppNodeDetailService;

    @RequestMapping(value = "/get/node", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto queryLoanList(@RequestBody NodeDetailRequestDto requestDto) {
        String baseUrl = CommonUtils.getRequestBaseUrl(request);
        return mobileAppNodeDetailService.generateNodeDetail(requestDto, baseUrl);
    }
}
