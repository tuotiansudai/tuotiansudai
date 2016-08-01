package com.tuotiansudai.console.controller;


import com.tuotiansudai.dto.PrepareUserDto;
import com.tuotiansudai.service.PrepareService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/activity-manage")
public class PrepareController {

    private static Logger logger = Logger.getLogger(CouponController.class);

    @Autowired
    private PrepareService prepareService;

    @RequestMapping(value = "/prepare-users", method = RequestMethod.GET)
    public ModelAndView findPrepareUser(@RequestParam(name = "mobile", required = false) String mobile,
                                        @RequestParam(name = "beginTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date beginTime,
                                        @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                        @Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                        @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/prepare-users");
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("beginTime", beginTime);
        modelAndView.addObject("endTime", endTime);
        List<PrepareUserDto> prepareUsers = prepareService.findPrepareUser(mobile, beginTime, endTime, index, pageSize);
        modelAndView.addObject("prepareUsers", prepareUsers);
        long prepareUserCount = prepareService.findPrepareUserCount(mobile, beginTime, endTime);
        modelAndView.addObject("prepareUserCount", prepareUserCount);
        long totalPages = prepareUserCount / pageSize + (prepareUserCount % pageSize > 0 || prepareUserCount == 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        return modelAndView;
    }
}
