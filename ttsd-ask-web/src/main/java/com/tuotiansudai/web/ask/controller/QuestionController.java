package com.tuotiansudai.web.ask.controller;

import com.tuotiansudai.ask.dto.QuestionRequestDto;
import com.tuotiansudai.ask.service.QuestionService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.web.config.security.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.web.http.HttpSessionManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping(path = "/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView ask(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        HttpSessionManager sessionManager = (HttpSessionManager) httpServletRequest.getAttribute(HttpSessionManager.class.getName());

        ModelAndView modelAndView = new ModelAndView("/question");
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST)
    public BaseDto<BaseDataDto> ask(@Valid @ModelAttribute QuestionRequestDto questionRequestDto) {
        return questionService.createQuestion(LoginUserInfo.getLoginName(), questionRequestDto);
    }
}
