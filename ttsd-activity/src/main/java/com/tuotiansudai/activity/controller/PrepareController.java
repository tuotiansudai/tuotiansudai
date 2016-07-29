package com.tuotiansudai.activity.controller;


import com.tuotiansudai.dto.PrepareRegisterRequestDto;
import com.tuotiansudai.service.PrepareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/prepare")
public class PrepareController {

    @Autowired
    private PrepareService prepareService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView prepareRegister(@Valid @RequestBody PrepareRegisterRequestDto requestDto, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            String errorCode = bindingResult.getFieldError().getDefaultMessage();
            modelAndView.addObject("message", errorCode);
            modelAndView.addObject("status", false);
        } else {
            boolean status = prepareService.prepareRegister(requestDto);
            modelAndView.addObject("status", status);
        }
        return modelAndView;
    }
}
