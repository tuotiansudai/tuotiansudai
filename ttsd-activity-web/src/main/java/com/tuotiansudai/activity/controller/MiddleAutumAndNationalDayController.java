package com.tuotiansudai.activity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/activity/middleautum-nationalday")
public class MiddleAutumAndNationalDayController {
    @RequestMapping()
    public ModelAndView getView(){
        ModelAndView modelAndView=new ModelAndView("/activities/2018/mid-autumn-festival");
        return modelAndView;
    }

}
