package com.tuotiansudai.pointsystem.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/pointsystem")
public class PointSystemController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView pointSystemHome() {
        ModelAndView modelAndView = new ModelAndView("pointsystem-index");

        modelAndView.addObject("responsive",true);
        return modelAndView;
    }

 @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ModelAndView pointSystemDetail() {
        ModelAndView modelAndView = new ModelAndView("/pointsystem-detail");

        modelAndView.addObject("responsive",true);
        return modelAndView;
    }

    @RequestMapping(value = "/order",method = RequestMethod.GET)
    public ModelAndView pointSystemOrder() {
        ModelAndView modelAndView = new ModelAndView("/pointsystem-order");

        modelAndView.addObject("responsive",true);
        return modelAndView;
    }

    @RequestMapping(value = "/task",method = RequestMethod.GET)
    public ModelAndView pointSystemTask() {
        ModelAndView modelAndView = new ModelAndView("/pointsystem-task");

        modelAndView.addObject("responsive",true);
        return modelAndView;
    }

    @RequestMapping(value = "/record",method = RequestMethod.GET)
    public ModelAndView pointSystemRecord() {
        ModelAndView modelAndView = new ModelAndView("/pointsystem-order");

        modelAndView.addObject("responsive",true);
        return modelAndView;
    }


}
