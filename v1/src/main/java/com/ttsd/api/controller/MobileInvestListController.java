package com.ttsd.api.controller;

import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.InvestListRequestDto;
import com.ttsd.api.service.MobileInvestListAppService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class MobileInvestListController {
    @Resource
    private MobileInvestListAppService mobileInvestListAppService;
    @RequestMapping(value="/queryinvestlist",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto queryInvestList(@RequestBody InvestListRequestDto investListRequestDto){
        return mobileInvestListAppService.generateInvestList(investListRequestDto);
    }

}
