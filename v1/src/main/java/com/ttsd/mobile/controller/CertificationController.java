package com.ttsd.mobile.controller;

import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.UserService;
import com.esoft.core.annotations.Logger;
import com.esoft.umpay.user.service.impl.UmPayUserOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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

    private String loginUserId;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView certification() {
        return new ModelAndView("/certification");
    }

    @RequestMapping(value = "/idCard", method = RequestMethod.GET)
    @ResponseBody
    public boolean idCardIsExists(HttpServletRequest request) {
        String idCard = request.getParameter("yourId");
        return !this.userService.idCardIsExists(idCard);
    }

    @RequestMapping(value = "/realName", method = RequestMethod.POST)
    @ResponseBody
    public boolean realNameCertification(HttpServletRequest request) {
        String realName = request.getParameter("yourName");
        String idCard = request.getParameter("yourId");
        User user = new User();
//        if (this.loginUserId == null) {
//            SecurityContextImpl securityContextImpl = (SecurityContextImpl) FacesUtil
//                    .getSessionAttribute("SPRING_SECURITY_CONTEXT");
//            if (securityContextImpl != null) {
//                loginUserId = securityContextImpl.getAuthentication().getName();
//            }
//        }
//        try {
//            user = this.userService.getUserById(this.loginUserId);
//        } catch (UserNotFoundException e) {
//            log.error(e);
//            return false;
//        }
        if (!StringUtils.isNotEmpty(realName) || !StringUtils.isNotEmpty(idCard)){
            return false;
        }
        Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
        Matcher idNumMatcher = idNumPattern.matcher(idCard);
        if (!idNumMatcher.matches() || this.userService.idCardIsExists(idCard)){
            return false;
        }
        user.setRealname(realName);
        user.setIdCard(idCard);
        try {
            this.umPayUserOperation.createOperation(user, null);
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

}
