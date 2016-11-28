package com.tuotiansudai.web.ask.controller;

import com.tuotiansudai.ask.repository.dto.QuestionDto;
import com.tuotiansudai.ask.repository.dto.QuestionRequestDto;
import com.tuotiansudai.ask.repository.dto.QuestionResultDataDto;
import com.tuotiansudai.ask.repository.model.Tag;
import com.tuotiansudai.ask.service.AnswerService;
import com.tuotiansudai.ask.service.QuestionService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.spring.LoginUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

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
    public BaseDto<QuestionResultDataDto> question(@Valid @ModelAttribute QuestionRequestDto questionRequestDto) {
        return new BaseDto<>(questionService.createQuestion(LoginUserInfo.getLoginName(), questionRequestDto));
    }

    @RequestMapping(path = "/{questionId:^\\d+$}", method = RequestMethod.GET)
    public ModelAndView question(@PathVariable long questionId,
                                 @RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        QuestionDto question = questionService.getQuestion(LoginUserInfo.getLoginName(), questionId);
        if (question == null) {
            return new ModelAndView("/error/404");
        }

        ModelAndView modelAndView = new ModelAndView("/question", "questionId", questionId);
        modelAndView.addObject("isQuestionOwner", !StringUtils.isEmpty(question.getMobile()) && question.getMobile().equalsIgnoreCase(LoginUserInfo.getMobile()));
        modelAndView.addObject("question", question);
        modelAndView.addObject("bestAnswer", answerService.getBestAnswer(LoginUserInfo.getLoginName(), questionId));
        modelAndView.addObject("answers", answerService.getNotBestAnswers(LoginUserInfo.getLoginName(), questionId, index, 10));

        return modelAndView;
    }

    @RequestMapping(path = "/my-questions", method = RequestMethod.GET)
    public ModelAndView getMyQuestions(@RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        return new ModelAndView("/my-questions", "questions", questionService.findMyQuestions(LoginUserInfo.getLoginName(), index, 10));
    }

    @RequestMapping(path = "/category/{urlTag}", method = RequestMethod.GET)
    public ModelAndView getQuestionsByCategory(@PathVariable String urlTag,
                                               @RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        Tag tag;
        try {
            tag = Tag.valueOf(urlTag.toUpperCase());
        } catch (Exception e) {
            return new ModelAndView("error/404");
        }
        ModelAndView modelAndView = new ModelAndView("/question-category");
        modelAndView.addObject("questions", questionService.findByTag(LoginUserInfo.getLoginName(), tag, index, 10));
        modelAndView.addObject("tag", tag);
        return modelAndView;
    }

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public ModelAndView getQuestionsByKeyword(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                                              @RequestParam(value = "index", required = false, defaultValue = "1") int index) {
        if(StringUtils.isEmpty(keyword)) {
            return new ModelAndView("redirect:/?group=HOT&index=1");
        }
        ModelAndView modelAndView = new ModelAndView("search-data");
        String loginName = LoginUserInfo.getLoginName();
        BaseDto<BasePaginationDataDto> data = questionService.getQuestionsByKeywords(keyword, loginName, index, 10);
        modelAndView.addObject("keywordQuestions", data);
        modelAndView.addObject("keyword", keyword);

        return modelAndView;
    }
}
