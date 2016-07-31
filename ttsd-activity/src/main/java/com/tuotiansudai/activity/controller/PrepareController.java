package com.tuotiansudai.activity.controller;


import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.PrepareRegisterRequestDto;
import com.tuotiansudai.service.PrepareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/prepare")
public class PrepareController {

    @Autowired
    private PrepareService prepareService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public BaseDataDto prepareRegister(@Valid @ModelAttribute PrepareRegisterRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldError().getDefaultMessage();
            return new BaseDataDto(false, message);
        }
        return prepareService.prepareRegister(requestDto);
    }
}
