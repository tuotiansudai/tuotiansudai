package com.tuotiansudai.web.ask.controller;

import com.tuotiansudai.ask.dto.*;
import com.tuotiansudai.ask.repository.model.Tag;
import com.tuotiansudai.ask.service.AnswerService;
import com.tuotiansudai.ask.service.QuestionService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.web.http.HttpSessionManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(path = "/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView question() {
        return new ModelAndView("/create-question");
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> question(@Valid @ModelAttribute QuestionRequestDto questionRequestDto) {
        return questionService.createQuestion(LoginUserInfo.getLoginName(), questionRequestDto);
    }

    @RequestMapping(path = "/{questionId:^\\d+$}", method = RequestMethod.GET)
    public ModelAndView question(@PathVariable long questionId) {
        QuestionDto question = questionService.getQuestion(questionId);
        if (question == null) {
            return new ModelAndView("/error/404");
        }

        AnswerDto bestAnswer = answerService.getBestAnswer(LoginUserInfo.getLoginName(), questionId);
        List<AnswerDto> answers = answerService.getAnswers(LoginUserInfo.getLoginName(), questionId);

        ModelAndView modelAndView = new ModelAndView("/question");
        modelAndView.addObject("question", question);
        modelAndView.addObject("bestAnswer", bestAnswer);
        modelAndView.addObject("answers", answers);

        return modelAndView;
    }

    @RequestMapping(path = "/my-questions", method = RequestMethod.GET)
    public ModelAndView getMyQuestions(@RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                       @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return new ModelAndView("/my-questions", "questions", questionService.findMyQuestions(LoginUserInfo.getLoginName(), index, pageSize));
    }

    @RequestMapping(path = "/category", method = RequestMethod.GET)
    public ModelAndView getQuestionsByCategory(@RequestParam(value = "tag", required = true) Tag tag,
                                               @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                               @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/question-category");
        modelAndView.addObject("questions", questionService.findByTag(LoginUserInfo.getLoginName(), tag, index, pageSize));
        modelAndView.addObject("tag", tag);
        return modelAndView;
    }
}
