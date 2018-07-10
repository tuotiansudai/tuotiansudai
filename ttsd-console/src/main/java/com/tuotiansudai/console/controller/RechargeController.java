package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.dto.AccountType;
import com.tuotiansudai.console.service.ConsoleRechargeService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.RechargePaginationItemDataDto;
import com.tuotiansudai.enums.BankRechargeStatus;
import com.tuotiansudai.repository.model.BankRechargePaginationView;
import com.tuotiansudai.repository.model.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;


@Controller
@RequestMapping(value = "/finance-manage")
public class RechargeController {

    @Autowired
    private ConsoleRechargeService consoleRechargeService;

    @RequestMapping(value = "/recharge", method = RequestMethod.GET)
    public ModelAndView getRechargeList(@RequestParam(value = "accountType", required = false,defaultValue = "INVESTOR") AccountType accountType,
                                        @RequestParam(value = "rechargeId", required = false) String rechargeId,
                                        @RequestParam(value = "mobile", required = false) String mobile,
                                        @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                        @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                        @RequestParam(value = "status", required = false) BankRechargeStatus status,
                                        @RequestParam(value = "source", required = false) Source source,
                                        @RequestParam(value = "channel", required = false) String channel,
                                        @RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        int pageSize = 10;
        ModelAndView modelAndView = new ModelAndView("/recharge");
        BaseDto<BasePaginationDataDto<RechargePaginationItemDataDto>> baseDto = consoleRechargeService.findRechargePagination(accountType,rechargeId, mobile, source,
                status, channel, index, pageSize, startTime, endTime,null);
        List<String> channelList = consoleRechargeService.findAllChannel();
        long sumAmount = consoleRechargeService.findSumRechargeAmount(accountType,rechargeId, mobile, source, status, channel, startTime, endTime,null);
        modelAndView.addObject("baseDto", baseDto);
        modelAndView.addObject("sumAmount", sumAmount);
        modelAndView.addObject("rechargeStatusList", Lists.newArrayList(BankRechargeStatus.values()));
        modelAndView.addObject("rechargeSourceList", Lists.newArrayList(Source.values()));
        modelAndView.addObject("rechargeChannelList", channelList);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("rechargeId", rechargeId);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        modelAndView.addObject("source", source);
        modelAndView.addObject("status", status);
        modelAndView.addObject("channel", channel);
        if (status != null) {
            modelAndView.addObject("rechargeStatus", status);
        }
        return modelAndView;
    }

}
