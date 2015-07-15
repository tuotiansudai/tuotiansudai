package com.ttsd.mobile.controller;

import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.UserService;
import com.esoft.umpay.user.service.impl.UmPayUserOperation;
import com.ttsd.mobile.Util.MobileUtil;
import com.ttsd.mobile.model.ModelJson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/certification")
public class CertificationController {

    @Autowired
    private UmPayUserOperation umPayUserOperation;

    @Autowired
    private UserService userService;

    @Autowired
    private MobileUtil mobileUtil;

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
    public ModelJson realNameCertification(@RequestParam String realName,@RequestParam String idCard,@ModelAttribute ModelJson modelJson) {
        if (StringUtils.isEmpty(realName) || StringUtils.isEmpty(idCard) || StringUtils.isEmpty(mobileUtil.getLoginUserId())){
            modelJson.setSuccess("false");
            return modelJson;
        }
        User user = null;
        try {
            user = userService.getUserById(mobileUtil.getLoginUserId());
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            modelJson.setSuccess("false");
            return modelJson;
        }
        Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
        Matcher idNumMatcher = idNumPattern.matcher(idCard);
        if (!idNumMatcher.matches() || this.userService.idCardIsExists(idCard)){
            modelJson.setSuccess("false");
            return modelJson;
        }
        user.setRealname(realName);
        user.setIdCard(idCard);
        try {
            this.umPayUserOperation.createOperation(user, null);
            modelJson.setSuccess("true");
            return modelJson;
        } catch (Exception e) {
            e.printStackTrace();
            modelJson.setSuccess("false");
            return modelJson;
        }
    }

}
