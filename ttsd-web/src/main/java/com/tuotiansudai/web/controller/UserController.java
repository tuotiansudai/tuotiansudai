package com.tuotiansudai.web.controller;

import com.tuotiansudai.service.UserService;
import com.tuotiansudai.utils.LoginUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/personal-info")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView mv = new ModelAndView("/personal-info");
        mv.addObject("loginName", LoginUserInfo.getLoginName());
        return mv;
    }

    @RequestMapping(path = "/password", method = RequestMethod.GET)
    public ModelAndView changePassword() {
        ModelAndView mv = new ModelAndView("/change-password");
        mv.addObject("loginName", LoginUserInfo.getLoginName());
        return mv;
    }

    @RequestMapping(value = "/password", method = RequestMethod.POST)
    public String doChangePassword(String oldPassword, String newPassword, String newPasswordCheck) {
        String loginName = LoginUserInfo.getLoginName();
        if(StringUtils.isBlank(loginName)){
            return "redirect:/";
        }
        if(!StringUtils.equals(newPassword, newPasswordCheck)){
            return "redirect:/personal-info/password";
        }
        boolean result = userService.changePassword(loginName, oldPassword, newPassword);
        if(result) {
            return "redirect:/personal-info";
        }else{
            return "redirect:/personal-info/password";
        }
    }
}
