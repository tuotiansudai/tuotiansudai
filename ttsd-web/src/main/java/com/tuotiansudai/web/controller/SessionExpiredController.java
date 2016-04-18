package com.tuotiansudai.web.controller;

import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/checkLogin")
public class SessionExpiredController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public boolean checkLogin(){
        return LoginUserInfo.getLoginName() != null;
    }
}
