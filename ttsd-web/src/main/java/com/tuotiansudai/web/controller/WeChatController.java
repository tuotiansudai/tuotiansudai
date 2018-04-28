package com.tuotiansudai.web.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.WeChatUserModel;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.service.WeChatService;
import com.tuotiansudai.spring.security.MyAuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.regex.Pattern;

@Controller
@RequestMapping(path = "/we-chat")
public class WeChatController {

    private final WeChatService weChatService;

    private final MyAuthenticationUtil myAuthenticationUtil;

    private final UserService userService;

    @Autowired
    public WeChatController(WeChatService weChatService, MyAuthenticationUtil myAuthenticationUtil, UserService userService) {
        this.weChatService = weChatService;
        this.myAuthenticationUtil = myAuthenticationUtil;
        this.userService = userService;
    }

    @RequestMapping(path = "/authorize", method = RequestMethod.GET)
    public ModelAndView authorize(HttpServletRequest httpServletRequest,
                                  @RequestParam(name = "redirect", required = false) String redirect) {
        String sessionId = httpServletRequest.getSession().getId();
        return new ModelAndView(MessageFormat.format("redirect:{0}", weChatService.generateMuteAuthorizeURL(sessionId, redirect)));
    }

    @RequestMapping(path = "/active/authorize", method = RequestMethod.GET)
    public ModelAndView userInfoAuthorize(HttpServletRequest httpServletRequest,
                                  @RequestParam(name = "redirect", required = false) String redirect) {
        String sessionId = httpServletRequest.getSession().getId();
        return new ModelAndView(MessageFormat.format("redirect:{0}", weChatService.generateActiveAuthorizeURL(sessionId, redirect)));
    }

    @RequestMapping(path = "/authorize-callback", method = RequestMethod.GET)
    public ModelAndView openid(HttpServletRequest httpServletRequest,
                               @RequestParam(name = "code") String code,
                               @RequestParam(name = "state") String state,
                               @RequestParam(name = "redirect", required = false) String redirect) {
        httpServletRequest.getSession().removeAttribute("weChatUserLoginName");
        httpServletRequest.getSession().removeAttribute("weChatUserOpenid");

        WeChatUserModel weChatUserModel = weChatService.parseWeChatUserStatus(httpServletRequest.getSession().getId(), state, code);

        if (weChatUserModel == null) {
            myAuthenticationUtil.removeAuthentication();
            return new ModelAndView("/error/404");
        }

        httpServletRequest.getSession().setAttribute("weChatUserOpenid", weChatUserModel.getOpenid());

        if (weChatUserModel.isBound()) {
            myAuthenticationUtil.createAuthentication(weChatUserModel.getLoginName(), Source.WE_CHAT);
        } else {
            myAuthenticationUtil.removeAuthentication();
            httpServletRequest.getSession().setAttribute("weChatUserLoginName", weChatUserModel.getLoginName());
        }

        return new ModelAndView(Strings.isNullOrEmpty(redirect) ? "redirect:/" : MessageFormat.format("redirect:{0}", redirect));
    }

    @RequestMapping(path = "/entry-point", method = RequestMethod.GET)
    public ModelAndView entryPoint(@RequestParam(name = "redirect", required = false, defaultValue = "/") String redirect) {
        return new ModelAndView("/wechat/wechat-entry-point", "redirect", redirect);
    }

    @RequestMapping(path = "/entry-point", method = RequestMethod.POST)
    public ModelAndView entryPoint(HttpServletRequest httpServletRequest,
                                   @RequestParam(name = "redirect", required = false, defaultValue = "/") String redirect,
                                   @ModelAttribute(name = "mobile") String mobile) {
        if (Strings.isNullOrEmpty(mobile) || !Pattern.matches("^1\\d{10}$", mobile)) {
            ModelAndView modelAndView = new ModelAndView("/wechat/wechat-entry-point");
            modelAndView.addObject("mobile", mobile);
            modelAndView.addObject("error", true);
            return modelAndView;
        }

        ModelAndView modelAndView = new ModelAndView(userService.mobileIsExist(mobile) ? "/wechat/wechat-login" : "/wechat/wechat-register");
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("openid", httpServletRequest.getSession().getAttribute("weChatUserOpenid"));
        modelAndView.addObject("redirect", redirect);
        return modelAndView;
    }

    @RequestMapping(path = "/bind-success", method = RequestMethod.GET)
    public ModelAndView bindSuccess(HttpServletRequest httpServletRequest,
                                    @RequestParam(name = "redirect", required = false, defaultValue = "/") String redirect) {
        httpServletRequest.getSession().removeAttribute("weChatUserLoginName");

        return new ModelAndView("/wechat/wechat-bind-success", "redirect", redirect);
    }
}
