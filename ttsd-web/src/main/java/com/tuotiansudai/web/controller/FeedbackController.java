package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.repository.model.FeedbackType;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.FeedbackService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.util.CaptchaGenerator;
import com.tuotiansudai.spring.security.CaptchaHelper;
import com.tuotiansudai.spring.LoginUserInfo;
import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/feedback")
public class FeedbackController {

    static Logger logger = Logger.getLogger(FeedbackController.class);

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private CaptchaHelper captchaHelper;

    @ResponseBody
    @RequestMapping(value = "/submit", params = {"contact", "type", "content", "captcha"}, method = RequestMethod.POST)
    public BaseDto feedback(HttpServletRequest httpServletRequest,
                            String contact,
                            FeedbackType type,
                            String content,
                            String captcha) {

        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto baseDataDto = new BaseDataDto();
        baseDto.setData(baseDataDto);
        baseDto.setSuccess(true);

        boolean result = this.captchaHelper.captchaVerify(captcha, httpServletRequest.getSession(false).getId(), httpServletRequest.getRemoteAddr());

        if (!result) {
            logger.info("submit feedback failed: captcha does not match actual value");
            baseDataDto.setMessage("验证码错误！");
            baseDataDto.setStatus(false);
            baseDto.setSuccess(false);
            return baseDto;
        }

        feedbackService.create(LoginUserInfo.getLoginName(), Source.WEB, type, content,contact);

        return baseDto;
    }

    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void feedbackCaptcha(HttpServletRequest request, HttpServletResponse response) {
        int captchaWidth = 80;
        int captchaHeight = 33;
        Captcha captcha = this.captchaHelper.getCaptcha(request.getSession().getId(), captchaHeight, captchaWidth, true);
        CaptchaServletUtil.writeImage(response, captcha.getImage());
    }
}
