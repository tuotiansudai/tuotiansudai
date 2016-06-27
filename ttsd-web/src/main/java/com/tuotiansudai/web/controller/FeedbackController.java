package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.exception.CaptchaNotMatchException;
import com.tuotiansudai.repository.model.FeedbackType;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.FeedbackService;
import com.tuotiansudai.util.CaptchaGenerator;
import com.tuotiansudai.util.CaptchaHelper;
import com.tuotiansudai.web.util.LoginUserInfo;
import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    public BaseDto feedback(String contact, FeedbackType type, String content, String captcha) {

        BaseDto baseDto = new BaseDto();
        BaseDataDto baseDataDto = new BaseDataDto();
        baseDto.setData(baseDataDto);
        baseDto.setSuccess(true);

        boolean result = this.captchaHelper.captchaVerify(CaptchaHelper.FEEDBACK_CAPTCHA, captcha);

        if (!result) {
            logger.debug("submit feedback failed: captcha does not match actual value");
            baseDataDto.setMessage("验证码错误！");
            baseDataDto.setStatus(false);
            baseDto.setSuccess(false);
            return baseDto;
        }

        String loginName = LoginUserInfo.getLoginName();
        if (LoginUserInfo.getMobile() != null) {
            contact = LoginUserInfo.getMobile();
        }

        feedbackService.create(loginName, contact, Source.WEB, type, content);

        return baseDto;
    }

    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void feedbackCaptcha(HttpServletResponse response) {
        int captchaWidth = 80;
        int captchaHeight = 30;
        Captcha captcha = CaptchaGenerator.generate(captchaWidth, captchaHeight);
        CaptchaServletUtil.writeImage(response, captcha.getImage());

        this.captchaHelper.storeCaptcha(CaptchaHelper.FEEDBACK_CAPTCHA, captcha.getAnswer());
    }
}
