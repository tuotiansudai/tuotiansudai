package com.ttsd.mobile.controller;

import com.esoft.archer.common.CommonConstants;
import com.esoft.archer.user.model.User;
import com.esoft.core.jsf.util.FacesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
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

    @Resource
    UserDetailsService userDetailsService;
    @Autowired
    SessionRegistry sessionRegistry;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView register() {
        return new ModelAndView("/register");
    }

    @RequestMapping(value = "/mobileRegister",method = RequestMethod.POST)
    public ModelAndView mobileRegister(HttpServletRequest request,HttpServletResponse response){
        String userName =  request.getParameter("username");
        String passWord = request.getParameter("password");
        String phoneNumber = request.getParameter("phoneNumber");
        String vCode = request.getParameter("vCode");
        String operationType = "1";//表示注册
        boolean responseResult = mobileRegisterService.mobileRegister(userName,passWord,phoneNumber,vCode,operationType);
        /**
         * 用户注册成功之后，登录
         */
        if ("1".equals(operationType) && responseResult){
            HttpSession session = request.getSession();
            User user = new User();
            user.setUsername(userName);
            user.setMobileNumber(phoneNumber);
            UserDetails userDetails = userDetailsService
                    .loadUserByUsername(user.getUsername());
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    user.getUsername(), userDetails.getPassword(),
                    userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(token);
            session.setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());
            sessionRegistry.registerNewSession(session.getId(), userDetails);
            return new ModelAndView("/certification");
        }
        return new ModelAndView("/register");
    }

    @RequestMapping(value = "/mobileRegisterValidationCode",method = RequestMethod.POST)
    @ResponseBody
    public boolean mobileRegisterValidationCode(HttpServletRequest request,HttpServletResponse response){
        String userName =  request.getParameter("username");
        String passWord = request.getParameter("password");
        String phoneNumber = request.getParameter("phoneNumber");
        String operationType = "0";//表示获取注册授权码
        boolean responseResult = mobileRegisterService.mobileRegister(userName,passWord,phoneNumber,null,operationType);
        return responseResult;
    }

    @RequestMapping(value = "/userNameValidation", method = RequestMethod.GET)
    @ResponseBody
    public boolean validateUserName(HttpServletRequest request,HttpServletResponse response){
        String userName = request.getParameter("username");
        response.setContentType("text/json; charset=utf-8");
        return mobileRegisterService.validateUserName(userName);
    }

    @RequestMapping(value = "/mobilePhoneNumValidation", method = RequestMethod.GET)
    @ResponseBody
    public boolean validatemobilePhoneNum(HttpServletRequest request,HttpServletResponse response){
        String phoneNumber = request.getParameter("phoneNumber");
        response.setContentType("text/json; charset=utf-8");
        return mobileRegisterService.validateMobilePhoneNum(phoneNumber);
    }

    @RequestMapping(value = "/vCodeValidation", method = RequestMethod.GET)
    @ResponseBody
    public boolean validateVCode(HttpServletRequest request,HttpServletResponse response){
        String phoneNum = request.getParameter("phoneNumber");
        String vCode = request.getParameter("vCode");
        response.setContentType("text/json; charset=utf-8");
        return mobileRegisterService.validateVCode(phoneNum,vCode);
    }
    /***************************setter注入方法****************************/
    public void setMobileRegisterService(IMobileRegisterService mobileRegisterService) {
        this.mobileRegisterService = mobileRegisterService;
    }
}
