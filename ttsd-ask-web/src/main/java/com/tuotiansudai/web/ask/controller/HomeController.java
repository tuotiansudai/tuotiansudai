package com.tuotiansudai.web.ask.controller;

import com.tuotiansudai.ask.dto.BaseDto;
import com.tuotiansudai.ask.dto.BasePaginationDataDto;
import com.tuotiansudai.ask.service.QuestionService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/")
public class HomeController {

    @Autowired
    private QuestionService questionService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index(@RequestParam(value = "group", defaultValue = "ALL", required = false) QuestionGroup group,
                              @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                              @RequestParam(value = "page-size", defaultValue = "10", required = false) int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/home");
        BaseDto<BasePaginationDataDto> data = questionService.findAllQuestions(LoginUserInfo.getLoginName(), index, pageSize);
        if (group == QuestionGroup.UNRESOLVED) {
            data = questionService.findAllUnresolvedQuestions(LoginUserInfo.getLoginName(), index, pageSize);
        }

        if (group == QuestionGroup.HOT) {
            data = questionService.findAllHotQuestions(LoginUserInfo.getLoginName(), index, pageSize);
        }

        modelAndView.addObject("questions", data);
        modelAndView.addObject("group", group);
        return modelAndView;
    }

    private enum QuestionGroup {
        ALL, UNRESOLVED, HOT
    }
}
