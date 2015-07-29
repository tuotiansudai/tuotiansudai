package com.ttsd.api.controller;

import com.ttsd.api.service.RetrievePasswordService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * Created by tuotian on 15/7/29.
 */
@Controller
public class RetrievePasswordController {
    @Resource(name = "RetrievePasswordServiceImpl")
    private RetrievePasswordService retrievePasswordService;

    @RequestMapping(value = "/retrievepassword",method = RequestMethod.POST)
    public String retrievePassword(){

        return null;
    }

    @RequestMapping(value = "/validatecaptcha",method = RequestMethod.POST)
    public String validateAuthCode(){

        return null;
    }
}
