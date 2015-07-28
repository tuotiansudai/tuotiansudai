package com.ttsd.mobile.controller;

import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.UserService;
import com.esoft.umpay.trusteeship.exception.UmPayOperationException;
import com.esoft.umpay.user.service.impl.UmPayUserOperation;
import com.ttsd.mobile.Util.MobileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
        if (this.userService.hasRole(mobileUtil.getLoginUserId(),"INVESTOR")){
            return new ModelAndView("redirect:/user/get_investor_permission");
        }
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
    public ModelAndView realNameCertification(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/certification");
        String realName = request.getParameter("yourName");
        String idCard = request.getParameter("yourId");
        modelAndView.addObject("yourName",realName);
        modelAndView.addObject("yourId",idCard);
        User user = null;
        try {
            user = userService.getUserById(mobileUtil.getLoginUserId());
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            modelAndView.addObject("message","用户未登录！");
            return modelAndView;
        }
        user.setRealname(realName);
        user.setIdCard(idCard);
        try {
            this.umPayUserOperation.createOperation(user, null);
            return new ModelAndView("redirect:/user/center");
        } catch (IOException e) {
            e.printStackTrace();
            modelAndView.addObject("message","由于网络传输原因，您实名认证失败！");
            return modelAndView;
        } catch (UmPayOperationException e) {
            e.printStackTrace();
            modelAndView.addObject("message","实名认证失败！");
            return modelAndView;
        }
    }

}
