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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.ttsd.mobile.service.IMobileRegisterService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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


    /**
     * @function 手机端注册页面访问接口
     * @return ModelAndView
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView register() {
        return new ModelAndView("/register");
    }


    /**
     * @function 手机端注册
     * @param request
     * @param userName 用户名
     * @param passWord 密码
     * @param phoneNumber 手机号
     * @param vCode 验证码
     * @param referrer 推荐人
     * @return ModelAndView
     */
    @RequestMapping(value = "/mobileRegister",method = RequestMethod.POST)
    public ModelAndView mobileRegister(HttpServletRequest request,
                                       @RequestParam(value = "username")String userName,
                                       @RequestParam(value = "password")String passWord,
                                       @RequestParam(value = "phoneNumber")String phoneNumber,
                                       @RequestParam(value = "vCode")String vCode,
                                       @RequestParam(value = "referrer")String referrer){
        boolean responseResult = mobileRegisterService.mobileRegister(userName,passWord,phoneNumber,vCode,referrer);
        /**
         * 用户注册成功之后，登录
         */
        if (responseResult){
            HttpSession session = request.getSession();
            User user = new User();
            user.setUsername(userName);
            user.setMobileNumber(phoneNumber);
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    userDetails.getPassword(),
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


    /**
     * @function 发送手机授权码
     * @param phoneNumber 手机号
     * @return boolean 手机授权码发送成功，返回true，否则返回false
     */
    @RequestMapping(value = "/mobileRegisterValidationCode",method = RequestMethod.POST)
    @ResponseBody
    public boolean mobileRegisterValidationCode(@RequestParam(value = "phoneNumber")String phoneNumber){
        boolean responseResult = mobileRegisterService.getCreatedValidateCode(phoneNumber);
        return responseResult;
    }


    /**
     * @function 用户名校验
     * @param userName 用户名
     * @return boolean 校验通过，返回true，否则返回false
     */
    @RequestMapping(value = "/userNameValidation", method = RequestMethod.GET)
    @ResponseBody
    public boolean validateUserName(@RequestParam(value = "username")String userName){
//        String userName = request.getParameter("username");
        return mobileRegisterService.validateUserName(userName);
    }


    /**
     * @function 手机号校验
     * @param phoneNumber 手机号
     * @return boolean 校验通过，返回true，否则返回false
     */
    @RequestMapping(value = "/mobilePhoneNumValidation", method = RequestMethod.GET)
    @ResponseBody
    public boolean validatemobilePhoneNum(@RequestParam(value = "phoneNumber")String phoneNumber){
        return mobileRegisterService.validateMobilePhoneNum(phoneNumber);
    }


    /**
     * @function 授权码校验
     * @param phoneNumber 手机号
     * @param vCode 授权码
     * @return boolean 校验通过，返回true，否则返回false
     */
    @RequestMapping(value = "/vCodeValidation", method = RequestMethod.GET)
    @ResponseBody
    public boolean validateVCode(@RequestParam(value = "phoneNumber")String phoneNumber,
                                 @RequestParam(value = "vCode")String vCode){
        return mobileRegisterService.validateVCode(phoneNumber, vCode);
    }


    /**
     * @function 用户名校验
     * @param referrer 推荐人
     * @return boolean 校验通过，返回true，否则返回false
     */
    @RequestMapping(value = "/referrerValidation", method = RequestMethod.GET)
    @ResponseBody
    public boolean validateReferrer(@RequestParam(value = "referrer")String referrer){
        return mobileRegisterService.validateReferrer(referrer);
    }
    /***************************setter注入方法****************************/
    public void setMobileRegisterService(IMobileRegisterService mobileRegisterService) {
        this.mobileRegisterService = mobileRegisterService;
    }
}
