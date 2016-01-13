package com.tuotiansudai.web.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.HomeLoanDto;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.service.AnnounceService;
import com.tuotiansudai.service.HomeService;
import com.tuotiansudai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private HomeService homeService;

    @Autowired
    private UserService userService;


    @Autowired
    private AnnounceService announceService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/index", "responsive", true);
        List<HomeLoanDto> loans = homeService.getLoans();
        BaseDto<BasePaginationDataDto> baseDto = announceService.getAnnouncementList(1, 3);
        int userCount = userService.findUserCount();
        modelAndView.addObject("loans", loans);
        modelAndView.addObject("announces", baseDto.getData().getRecords());
        modelAndView.addObject("userCount",userCount);
        modelAndView.addObject("productTypes", Lists.newArrayList(ProductType.values()));
        return modelAndView;
    }
}
