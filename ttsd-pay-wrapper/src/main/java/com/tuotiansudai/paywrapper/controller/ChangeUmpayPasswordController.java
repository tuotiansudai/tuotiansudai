package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChangeUmpayPasswordController extends BaseController{

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto changeUmpayPassword() {

    }
}
