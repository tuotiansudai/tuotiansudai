package com.ttsd.mobile.controller;

import com.esoft.archer.common.exception.AuthInfoAlreadyActivedException;
import com.esoft.archer.common.exception.AuthInfoOutOfDateException;
import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.ttsd.mobile.service.IMobileRegisterService;
import com.ttsd.util.CommonUtils;
import org.apache.commons.logging.Log;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/register")
public class RegisterController {

    @Logger
    static Log log;

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
     * @param password 密码
     * @param phoneNumber 手机号
     * @param vCode 验证码
     * @param referrer 推荐人
     * @return ModelAndView
     */
    @RequestMapping(value = "/mobileRegister",method = RequestMethod.POST)
    public ModelAndView mobileRegister(HttpServletRequest request,
                                       @RequestParam(value = "username")String userName,
                                       @RequestParam(value = "password")String password,
                                       @RequestParam(value = "phoneNumber")String phoneNumber,
                                       @RequestParam(value = "vCode")String vCode,
                                       @RequestParam(value = "referrer")String referrer){
        boolean responseResult = false;
        try {
            responseResult = mobileRegisterService.mobileRegister(userName,password,phoneNumber,vCode,referrer);
        } catch (AuthInfoOutOfDateException e) {
            log.error("用户名为："+userName+",手机号为："+phoneNumber+"的用户信息持久化失败！");
            log.error(e.getLocalizedMessage(), e);
        } catch (AuthInfoAlreadyActivedException e) {
            log.error("用户名为："+userName+",手机号为："+phoneNumber+"的用户信息持久化失败！");
            log.error(e.getLocalizedMessage(), e);
        } catch (NoMatchingObjectsException e) {
            log.error("用户名为："+userName+",手机号为："+phoneNumber+"的用户信息持久化失败！");
            log.error(e.getLocalizedMessage(), e);
        }
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
    public boolean mobileRegisterValidationCode(HttpServletRequest request,@RequestParam(value = "phoneNumber")String phoneNumber){
        boolean responseResult = false;
        try {
            responseResult = mobileRegisterService.getCreatedValidateCode(phoneNumber, CommonUtils.getRemoteHost(request));
            return responseResult;
        }catch (Exception e){
            log.error("手机授权码发送异常！");
            return false;
        }
    }


    /**
     * @function 用户名校验
     * @param userName 用户名
     * @return boolean 校验通过，返回true，否则返回false
     */
    @RequestMapping(value = "/userNameValidation", method = RequestMethod.GET)
    @ResponseBody
    public boolean validateUserName(@RequestParam(value = "username")String userName){
        return mobileRegisterService.validateUserName(userName);
    }

    /**
     * @function 密码校验
     * @param password 密码
     * @return boolean 校验通过，返回true，否则返回false
     */
    @RequestMapping(value = "/passwordValidation", method = RequestMethod.GET)
    @ResponseBody
    public boolean validatePassword(@RequestParam(value = "password")String password){
        return mobileRegisterService.validatePassword(password);
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
     * @function 推荐人校验
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
