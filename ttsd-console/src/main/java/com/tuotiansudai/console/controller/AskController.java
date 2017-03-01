package com.tuotiansudai.console.controller;

import com.tuotiansudai.ask.repository.model.AnswerStatus;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.ask.repository.model.QuestionStatus;
import com.tuotiansudai.ask.service.AnswerService;
import com.tuotiansudai.ask.service.EmbodyQuestionService;
import com.tuotiansudai.ask.service.QuestionService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(path = "/ask-manage")
public class AskController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private EmbodyQuestionService embodyQuestionService;

    @Value("${ask.server}")
    private String askServer;

    private static final String PREFIX = "/question";

    @RequestMapping(path = "/questions", method = RequestMethod.GET)
    public ModelAndView getQuestions(@RequestParam(value = "question", required = false) String question,
                                     @RequestParam(value = "mobile", required = false) String mobile,
                                     @RequestParam(value = "status", required = false) QuestionStatus status,
                                     @RequestParam(value = "index", defaultValue = "1", required = false) int index) {

        BaseDto<BasePaginationDataDto<QuestionModel>> questions = questionService.findQuestionsForConsole(question, mobile, status, index);

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
                                   @RequestParam(value = "index", defaultValue = "1", required = false) int index) {

        BaseDto<BasePaginationDataDto> answers = answerService.findAnswersForConsole(question, mobile, status, index, 10);

        ModelAndView modelAndView = new ModelAndView("/answer-list", "answers", answers);
        modelAndView.addObject("question", question);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("status", status);
        modelAndView.addObject("statusOptions", AnswerStatus.values());
        return modelAndView;
    }

    @RequestMapping(path = "/embody-questions", method = RequestMethod.GET)
    public ModelAndView getEmbodyQuestions(@RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        BaseDto<BasePaginationDataDto<QuestionModel>> embodyQuestions = questionService.findEmbodyAllQuestions(index);
        ModelAndView modelAndView = new ModelAndView("/embody-question-list", "embodyQuestions", embodyQuestions);
        return modelAndView;
    }

    @RequestMapping(value = "/import-excel", method = RequestMethod.POST)
    @ResponseBody
    public BaseDataDto importUsers(HttpServletRequest httpServletRequest) throws IOException {
        return embodyQuestionService.createImportEmbodyQuestion(httpServletRequest);
    }


    @RequestMapping(path = "/question/approve", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> approveQuestion(@RequestParam List<Long> ids) {
        questionService.approve(ids);

        return new BaseDto<>(new BaseDataDto(true));
    }

    @RequestMapping(path = "/question/reject", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> rejectQuestion(@RequestParam List<Long> ids) {
        questionService.reject(ids);

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
