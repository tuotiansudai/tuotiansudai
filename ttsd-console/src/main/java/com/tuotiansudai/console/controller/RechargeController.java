package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.RechargeSource;
import com.tuotiansudai.repository.model.RechargeStatus;
import com.tuotiansudai.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;


@Controller
public class RechargeController {

    @Autowired
    RechargeService rechargeService;

    @RequestMapping(value = "/recharge", method = RequestMethod.GET)
    public ModelAndView getRechargeList(@RequestParam(value = "rechargeId", required = false) String rechargeId,
                                        @RequestParam(value = "loginName", required = false) String loginName,
                                        @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date startTime,
                                        @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endTime,
                                        @RequestParam(value = "status", required = false) RechargeStatus status,
                                        @RequestParam(value = "source", required = false) RechargeSource source,
                                        @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

        ModelAndView modelAndView = new ModelAndView("/recharge");
        BaseDto<BasePaginationDataDto> baseDto = rechargeService.findRechargePagination(rechargeId,
                loginName,
                source,
                status,
                index,
                pageSize,
                startTime,
                endTime);

        modelAndView.addObject("baseDto", baseDto);
        modelAndView.addObject("rechargeStatusList", Lists.newArrayList(RechargeStatus.values()));
        modelAndView.addObject("rechargeSourceList", Lists.newArrayList(RechargeSource.values()));
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("rechargeId", rechargeId);
        modelAndView.addObject("loginName", loginName);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        modelAndView.addObject("source", source);
        modelAndView.addObject("status", status);
        if (status != null) {
            modelAndView.addObject("rechargeStatus", status);
        }
        return modelAndView;
    }
}
