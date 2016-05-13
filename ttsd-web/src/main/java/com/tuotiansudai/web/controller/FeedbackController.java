package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.repository.model.FeedbackType;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.FeedbackService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @RequestMapping(value = "/feedback", params = {"source", "contact", "type", "content"}, method = RequestMethod.POST)
    public BaseDto feedback(Source source, String contact, FeedbackType type, String content) {
        String loginName = LoginUserInfo.getLoginName();
        if (LoginUserInfo.getMobile() != null) {
            contact = LoginUserInfo.getMobile();
        }

        feedbackService.create(loginName, contact, source, type, content);

        BaseDto baseDto = new BaseDto();
        baseDto.setSuccess(true);
        return baseDto;
    }
}
