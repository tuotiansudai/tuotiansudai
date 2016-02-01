package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.ResetUmpayPasswordDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.paywrapper.service.ResetUmpayPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
public class ResetUmpayPasswordController extends BaseController{

    @Autowired
    private ResetUmpayPasswordService resetUmpayPasswordService;

    @RequestMapping(value = "/reset-umpay-password", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto resetUmpayPassword(@Valid @RequestBody ResetUmpayPasswordDto resetUmpayPasswordDto) {
        return resetUmpayPasswordService.resetUmpayPassword(resetUmpayPasswordDto);
    }
}
