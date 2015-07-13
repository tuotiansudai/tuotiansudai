package com.ttsd.mobile.controller;

import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.UserService;
import com.esoft.core.annotations.Logger;
import com.esoft.umpay.user.service.impl.UmPayUserOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/certification")
public class CertificationController {

    @Logger
    static Log log;

    @Autowired
    private UmPayUserOperation umPayUserOperation;

    @Resource
    private LoginUserInfo loginUserInfo;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView certification() {
        return new ModelAndView("/certification");
    }

    @RequestMapping(value = "/idCard/{idCard}", method = RequestMethod.GET)
    @ResponseBody
    public String idCardIsExists(@PathVariable String idCard) {
        return judgeIdCardIsExists(idCard);
    }

    private String judgeIdCardIsExists(String idCard) {
        if (this.userService.idCardIsExists(idCard)) {
            return "false";
        } else {
            return "true";
        }
    }

    @RequestMapping(value = "/realName", method = RequestMethod.POST)
    @ResponseBody
    public String realNameCertification(HttpServletRequest request) {
        String realName = request.getParameter("realName");
        String idCard = request.getParameter("idCard");
        User user = new User();
        try {
            user = this.userService.getUserById(this.loginUserInfo
                    .getLoginUserId());
        } catch (UserNotFoundException e) {
            log.error(e);
            return "false";
        }
        if (!StringUtils.isNotEmpty(realName) || !StringUtils.isNotEmpty(idCard)){
            return "false";
        }
        Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
        Matcher idNumMatcher = idNumPattern.matcher(idCard);
        if (!idNumMatcher.matches() || this.userService.idCardIsExists(idCard)){
            return "false";
        }
        user.setRealname(realName);
        user.setIdCard(idCard);
        try {
            this.umPayUserOperation.createOperation(user, null);
            return "true";
        } catch (Exception e) {
            log.error(e);
            return "false";
        }
    }

}
