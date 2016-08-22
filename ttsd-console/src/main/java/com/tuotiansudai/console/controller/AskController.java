package com.tuotiansudai.console.controller;

import com.tuotiansudai.ask.dto.BaseDataDto;
import com.tuotiansudai.ask.dto.BaseDto;
import com.tuotiansudai.ask.dto.BasePaginationDataDto;
import com.tuotiansudai.ask.repository.model.AnswerStatus;
import com.tuotiansudai.ask.repository.model.QuestionStatus;
import com.tuotiansudai.ask.service.AnswerService;
import com.tuotiansudai.ask.service.QuestionService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(path = "/ask-manage")
public class AskController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    @RequestMapping(path = "/questions", method = RequestMethod.GET)
    public ModelAndView getQuestions(@RequestParam(value = "question", required = false) String question,
                                     @RequestParam(value = "mobile", required = false) String mobile,
                                     @RequestParam(value = "status", required = false) QuestionStatus status,
                                     @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                     @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

        BaseDto<BasePaginationDataDto> questions = questionService.findQuestionsForConsole(question, mobile, status, index, pageSize);

        ModelAndView modelAndView = new ModelAndView("/question-list", "questions", questions);
        modelAndView.addObject("question", question);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("status", status);
        modelAndView.addObject("statusOptions", QuestionStatus.values());
        return modelAndView;
    }

    @RequestMapping(path = "/answers", method = RequestMethod.GET)
    public ModelAndView getAnswers(@RequestParam(value = "question", required = false) String question,
                                   @RequestParam(value = "mobile", required = false) String mobile,
                                   @RequestParam(value = "status", required = false) AnswerStatus status,
                                   @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                   @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

        BaseDto<BasePaginationDataDto> answers = answerService.findAnswersForConsole(question, mobile, status, index, pageSize);

        ModelAndView modelAndView = new ModelAndView("/answer-list", "answers", answers);
        modelAndView.addObject("question", question);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("status", status);
        modelAndView.addObject("statusOptions", AnswerStatus.values());
        return modelAndView;
    }

    @RequestMapping(path = "/question/approve", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> approveQuestion(@RequestParam List<Long> ids) {
        questionService.approve(LoginUserInfo.getLoginName(), ids);

        return new BaseDto<>(new BaseDataDto(true));
    }

    @RequestMapping(path = "/question/reject", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> rejectQuestion(@RequestParam List<Long> ids) {
        questionService.reject(LoginUserInfo.getLoginName(), ids);

        return new BaseDto<>(new BaseDataDto(true));
    }

    @RequestMapping(path = "/answer/approve", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> approveAnswer(@RequestParam List<Long> ids) {
        answerService.approve(LoginUserInfo.getLoginName(), ids);

        return new BaseDto<>(new BaseDataDto(true));
    }

    @RequestMapping(path = "/answer/reject", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> rejectAnswer(@RequestParam List<Long> ids) {
        answerService.reject(LoginUserInfo.getLoginName(), ids);

        return new BaseDto<>(new BaseDataDto(true));
    }
}
