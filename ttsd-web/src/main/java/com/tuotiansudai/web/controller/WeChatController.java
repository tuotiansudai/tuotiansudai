package com.tuotiansudai.web.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.WeChatService;
import com.tuotiansudai.spring.security.MyAuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

@Controller
@RequestMapping(path = "/we-chat")
public class WeChatController {

    private final WeChatService weChatService;

    private final MyAuthenticationUtil myAuthenticationUtil;

    @Autowired
    public WeChatController(WeChatService weChatService, MyAuthenticationUtil myAuthenticationUtil) {
        this.weChatService = weChatService;
        this.myAuthenticationUtil = myAuthenticationUtil;
    }

    @RequestMapping(path = "/authorize", method = RequestMethod.GET)
    public ModelAndView authorize(HttpServletRequest httpServletRequest,
                                  @RequestParam(name = "redirect", required = false) String redirect) {
        return new ModelAndView(MessageFormat.format("redirect:{0}", weChatService.generateAuthorizeURL(httpServletRequest.getSession().getId(), redirect)));
    }

    @RequestMapping(path = "/openid", method = RequestMethod.GET)
    public ModelAndView openid(HttpServletRequest httpServletRequest,
                               @RequestParam(name = "redirect", required = false) String redirect,
                               @RequestParam(name = "code") String code,
                               @RequestParam(name = "state") String state) {
        String openid = weChatService.fetchOpenid(httpServletRequest.getSession().getId(), state, code);
        if (Strings.isNullOrEmpty(openid)) {
            new ModelAndView("/404");
        }
        boolean isBound = weChatService.isWeChatUserBound(openid);
        String loginName = weChatService.findByOpenid(openid);
        if (isBound) {
            myAuthenticationUtil.createAuthentication(loginName, Source.WE_CHAT);
            httpServletRequest.getSession().removeAttribute("weChatUserLoginName");
        } else {
            httpServletRequest.getSession().setAttribute("weChatUserLoginName", loginName);
        }

        return new ModelAndView(Strings.isNullOrEmpty(redirect) ? "redirect:/" : MessageFormat.format("redirect:{0}", redirect));
    }
}
