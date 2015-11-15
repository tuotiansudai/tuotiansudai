package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.HomeLoanDto;
import com.tuotiansudai.service.HomeService;
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

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        List<HomeLoanDto> loans = homeService.getLoans();

        return new ModelAndView("/index", "loans", loans);
    }
}
