package com.tuotiansudai.web.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.WeChatUserModel;
import com.tuotiansudai.service.WeChatService;
import com.tuotiansudai.spring.security.MyAuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
        String sessionId = httpServletRequest.getSession().getId();
        return new ModelAndView(MessageFormat.format("redirect:{0}", weChatService.generateAuthorizeURL(sessionId, redirect)));
    }

    @RequestMapping(path = "/authorize-callback", method = RequestMethod.GET)
    public ModelAndView openid(HttpServletRequest httpServletRequest,
                               @RequestParam(name = "code") String code,
                               @RequestParam(name = "state") String state,
                               @RequestParam(name = "redirect", required = false) String redirect) {
        httpServletRequest.getSession().removeAttribute("weChatUserLoginName");

        WeChatUserModel weChatUserModel = weChatService.parseWeChatUserStatus(httpServletRequest.getSession().getId(), state, code);
        if (weChatUserModel == null) {
            return new ModelAndView("/404");
        }

        if (weChatUserModel.isBound()) {
            myAuthenticationUtil.createAuthentication(weChatUserModel.getLoginName(), Source.WE_CHAT);
        } else {
            httpServletRequest.getSession().setAttribute("weChatUserLoginName", weChatUserModel.getLoginName());
        }

        return new ModelAndView(Strings.isNullOrEmpty(redirect) ? "redirect:/" : MessageFormat.format("redirect:{0}", redirect));
    }

    @RequestMapping(path = "/entry-point/{item}", method = RequestMethod.GET)
    public ModelAndView entryPoint(@PathVariable String item) {
        return new ModelAndView(MessageFormat.format("/weChat/{0}", item));
    }
}
