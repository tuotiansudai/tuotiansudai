package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.service.ConsoleExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value = "/experience-manage")
public class ExperienceController {
    @Autowired
    private ConsoleExperienceService consoleExperienceService;

    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public ModelAndView balance(@RequestParam(value = "mobile", required = false) String mobile,
                                @RequestParam(value = "balanceMin", required = false) String balanceMin,
                                @RequestParam(value = "balanceMax", required = false) String balanceMax,
                                @RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        ModelAndView modelAndView = new ModelAndView("/experience-balance");
        int pageSize = 10;
        

        return modelAndView;
    }
}
