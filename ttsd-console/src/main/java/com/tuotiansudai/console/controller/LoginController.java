package com.tuotiansudai.console.controller;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.utils.CaptchaGenerator;
import com.tuotiansudai.utils.CaptchaVerifier;
import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2015/8/14.
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController {

    static Logger logger = Logger.getLogger(LoginController.class);

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private CaptchaVerifier captchaVerifier;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView login() {
        return new ModelAndView("/login");
    }

    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void loginCaptcha(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        int captchaWidth = 80;
        int captchaHeight = 30;
        Captcha captcha = CaptchaGenerator.generate(captchaWidth, captchaHeight);
        CaptchaServletUtil.writeImage(response, captcha.getImage());
        session.setAttribute(session.getId(), captcha.getAnswer());
    }

    @RequestMapping(value = "/captcha/{captcha:^[a-zA-Z0-9]{5}$}/verify", method = RequestMethod.GET,
            consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto captchaVerify(@PathVariable String captcha) {
        boolean result = this.captchaVerifier.loginCaptchaVerify(captcha);
        BaseDto baseDto = new BaseDto();
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(result);
        baseDto.setData(dataDto);

        return baseDto;
    }

}
