package com.tuotiansudai.web.ask.controller;

import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.ask.service.QuestionService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
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
                              @RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        ModelAndView modelAndView = new ModelAndView("/home");
        BaseDto<BasePaginationDataDto<QuestionModel>> data = questionService.findAllQuestions(index);
        if (group == QuestionGroup.UNRESOLVED) {
            data = questionService.findAllUnresolvedQuestions(index);
        }

        if (group == QuestionGroup.HOT) {
            data = questionService.findAllHotQuestions(index);
        }

        modelAndView.addObject("questions", data);
        modelAndView.addObject("group", group);

        return modelAndView;
    }

    private enum QuestionGroup {
        ALL, UNRESOLVED, HOT
    }
}
