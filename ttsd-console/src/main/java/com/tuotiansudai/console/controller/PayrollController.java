package com.tuotiansudai.console.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Controller
@RequestMapping(value = "/finance-manage/payroll")
public class PayrollController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView payroll(@RequestParam(name = "createStartTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date createStartTime,
                                @RequestParam(name = "createEndTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date createEndTime,
                                @RequestParam(name = "sendStartTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date sendStartTime,
                                @RequestParam(name = "sendEndTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date sendEndTime,
                                @RequestParam(name = "amountMin", required = false) String amountMin,
                                @RequestParam(name = "amountMax", required = false) String amountMax,
                                @RequestParam(name = "sendStatus", required = false) String sendStatus,
                                @RequestParam(name = "title", required = false) String title,
                                @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize){
        ModelAndView modelAndView = new ModelAndView("/payroll-list");

        modelAndView.addObject("createStartTime", createEndTime);
        modelAndView.addObject("createEndTime", createEndTime);
        modelAndView.addObject("sendStartTime", sendStartTime);
        modelAndView.addObject("sendEndTime", sendEndTime);
        modelAndView.addObject("amountMin", amountMin);
        modelAndView.addObject("amountMax", amountMax);
        modelAndView.addObject("sendStatus", sendStatus);
        modelAndView.addObject("title", title);
        return modelAndView;
    }
}
