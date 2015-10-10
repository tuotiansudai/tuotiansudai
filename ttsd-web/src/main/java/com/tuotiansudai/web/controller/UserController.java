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
@RequestMapping("/my")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView mv = new ModelAndView("/user-home");
        mv.addObject("loginName", LoginUserInfo.getLoginName());
        return mv;
    }

    @RequestMapping(value = "/password", method = RequestMethod.GET)
    public ModelAndView changePassword() {
        ModelAndView mv = new ModelAndView("/user-changepassword");
        mv.addObject("loginName", LoginUserInfo.getLoginName());
        return mv;
    }

    @RequestMapping(value = "/password", method = RequestMethod.POST)
    public String doChangePassword(String oldpwd, String newpwd, String newpwdcheck) {
        String loginName = LoginUserInfo.getLoginName();
        if(StringUtils.isBlank(loginName)){
            return "redirect:/";
        }
        if(!StringUtils.equals(newpwd, newpwdcheck)){
            return "redirect:/my/password";
        }
        boolean result = userService.changePassword(loginName, oldpwd, newpwd);
        if(result) {
            return "redirect:/my";
        }else{
            return "redirect:/my/password";
        }
    }
}
