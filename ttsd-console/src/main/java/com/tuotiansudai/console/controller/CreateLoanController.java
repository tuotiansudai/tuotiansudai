package com.tuotiansudai.console.controller;

import com.tuotiansudai.repository.model.TitleModel;
import com.tuotiansudai.service.CreateLoanBidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class CreateLoanController {

    @Autowired
    private CreateLoanBidService createLoanBidService;

    @RequestMapping(value = "/get/loginNames", method = RequestMethod.GET)
    public List<String> index(@RequestParam(value = "loginName")String loginName) {
        return createLoanBidService.getLoginNames(loginName);
    }

    @RequestMapping(value = "/get/allTitles", method = RequestMethod.GET)
    public List<TitleModel> getAllTitles(@RequestBody TitleModel titleModel){
        return createLoanBidService.findAllTitles();
    }
}
