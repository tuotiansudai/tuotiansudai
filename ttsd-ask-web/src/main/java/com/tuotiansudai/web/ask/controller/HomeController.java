package com.tuotiansudai.web.ask.controller;

import com.tuotiansudai.ask.service.QuestionService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.web.config.security.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping
public class HomeController {

    @Autowired
    private QuestionService questionService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        return new ModelAndView("/index");
    }

    @RequestMapping(path = "/all-questions", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BasePaginationDataDto> getAllQuestions(@RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                                          @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return questionService.findAllQuestions(LoginUserInfo.getLoginName(), index, pageSize);
    }

    @RequestMapping(path = "/all-unresolved-questions", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BasePaginationDataDto> findAllUnresolvedQuestions(@RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                                                     @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return questionService.findAllUnresolvedQuestions(LoginUserInfo.getLoginName(), index, pageSize);
    }

    @RequestMapping(path = "/all-hot-questions", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BasePaginationDataDto> findAllHotQuestions(@RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                                              @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return questionService.findAllHotQuestions(LoginUserInfo.getLoginName(), index, pageSize);
    }
}
