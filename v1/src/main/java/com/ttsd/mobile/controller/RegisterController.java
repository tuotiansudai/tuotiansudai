package com.ttsd.mobile.controller;

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

    @Resource(name = "com.ttsd.mobile.service.impl.MobileRegisterImpl")
    private IMobileRegisterService mobileRegister;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView register() {
        return new ModelAndView("/register");
    }

    @RequestMapping(value = "mobileRegister",method = RequestMethod.GET)
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
        HttpSession sesion = request.getSession();
        return "";
    }

    /***************************setter注入方法****************************/
    public void setMobileRegister(IMobileRegisterService mobileRegister) {
        this.mobileRegister = mobileRegister;
    }
}
