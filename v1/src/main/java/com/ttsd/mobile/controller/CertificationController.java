package com.ttsd.mobile.controller;

import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.umpay.user.service.impl.UmPayUserOperation;
import com.ttsd.mobile.bean.DataMsg;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/certification")
public class CertificationController {

    @Logger
    static Log log;

    @Autowired
    private UmPayUserOperation umPayUserOperation;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView certification() {
        return new ModelAndView("/certification");
    }

    @RequestMapping(value = "/realName/{realName}/{id}/{idCard}/{mobileNumber}", method = RequestMethod.GET)
    @ResponseBody
    public DataMsg realNameCertification(@PathVariable String realName,@PathVariable String id,@PathVariable String idCard,@PathVariable String mobileNumber,@ModelAttribute DataMsg dataMsg) {
        if (!StringUtils.isNotEmpty(realName) || !StringUtils.isNotEmpty(idCard)){
            dataMsg.setMsg("false");
            return dataMsg;
        }
        Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
        Matcher idNumMatcher = idNumPattern.matcher(idCard);
        if (!idNumMatcher.matches()){
            dataMsg.setMsg("false");
            return dataMsg;
        }
        User user = new User();
        user.setId(id);
        user.setRealname(realName);
        user.setIdCard(idCard);
        user.setMobileNumber(mobileNumber);
        try {
            this.umPayUserOperation.createOperation(user, null);
            dataMsg.setMsg("true");
        } catch (Exception e) {
            dataMsg.setMsg("false");
            log.error(e);
        }
        return dataMsg;
    }

}
