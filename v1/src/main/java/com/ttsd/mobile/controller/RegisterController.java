package com.ttsd.mobile.controller;

import com.esoft.archer.common.CommonConstants;
import com.esoft.archer.user.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ttsd.mobile.service.IMobileRegisterService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/register")
public class RegisterController {

    @Resource(name = "mobileRegisterServiceImpl")
    private IMobileRegisterService mobileRegisterService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView register() {
        return new ModelAndView("/register");
    }

    @RequestMapping(value = "/mobileRegister",method = RequestMethod.GET)
    @ResponseBody
    public String mobileRegister(HttpServletRequest request,HttpServletResponse response){
        /**
         * 注：operationType＝0，表示获取验证码
         * operationType＝1，表示正常的注册操作
         */
        String userName =  request.getParameter("userName");
        String passWord = request.getParameter("passWord");
        String phoneNumber = request.getParameter("phoneNumber");
        String vCode = request.getParameter("vCode");
        String operationType = request.getParameter("operationType");
        String responseResult = mobileRegisterService.mobileRegister(userName,passWord,phoneNumber,vCode,operationType);
        if (operationType.equals("1") && responseResult.equals("ture")){
            HttpSession session = request.getSession();
            User user = new User();
            user.setUsername(userName);
            user.setMobileNumber(phoneNumber);
            session.setAttribute("userInfo",user);
        }
        return responseResult;
    }

    /***************************setter注入方法****************************/
    public void setMobileRegisterService(IMobileRegisterService mobileRegisterService) {
        this.mobileRegisterService = mobileRegisterService;
    }
}
