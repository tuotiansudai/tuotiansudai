package com.tuotiansudai.web.ask.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.ask.dto.QuestionDto;
import com.tuotiansudai.ask.dto.QuestionResultDataDto;
import com.tuotiansudai.ask.dto.QuestionWithCaptchaRequestDto;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.ask.repository.model.QuestionStatus;
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
    public BaseDto<QuestionResultDataDto> question(@Valid @ModelAttribute QuestionWithCaptchaRequestDto questionRequestDto) {
        return new BaseDto<>(questionService.createQuestion(LoginUserInfo.getMobile(), questionRequestDto));
    }

    @RequestMapping(path = "/{questionId:^\\d+$}", method = RequestMethod.GET)
    public ModelAndView question(@PathVariable long questionId,
                                 @RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        QuestionDto question = questionService.getQuestion(LoginUserInfo.getLoginName(), questionId);
        if (question == null || ((Strings.isNullOrEmpty(LoginUserInfo.getMobile()) || !question.getMobile().equalsIgnoreCase(LoginUserInfo.getMobile())) && Lists.newArrayList(QuestionStatus.REJECTED, QuestionStatus.UNAPPROVED).contains(question.getStatus()))) {
            ModelAndView modelAndView = new ModelAndView("/error/404");
            modelAndView.addObject("errorPage", "true");
            return modelAndView;
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
        return new ModelAndView("/my-questions", "questions", questionService.findMyQuestions(index));
    }

    @RequestMapping(path = "/category/{tag:(?:SECURITIES|BANK|FUTURES|P2P|TRUST|LOAN|FUND|CROWD_FUNDING|INVEST|CREDIT_CARD|FOREX|STOCK|OTHER)}", method = RequestMethod.GET)
    public ModelAndView getQuestionsByCategory(@PathVariable Tag tag,
                                               @RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        ModelAndView modelAndView = new ModelAndView("/question-category");
        modelAndView.addObject("questions", questionService.findByTag(tag, index));
        modelAndView.addObject("tag", tag);
        return modelAndView;
    }

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public ModelAndView getQuestionsByKeyword(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                                              @RequestParam(value = "index", required = false, defaultValue = "1") int index) {
        if (StringUtils.isEmpty(keyword)) {
            return new ModelAndView("redirect:/");
        }
        ModelAndView modelAndView = new ModelAndView("search-data");
        BaseDto<BasePaginationDataDto<QuestionModel>> data = questionService.getQuestionsByKeywords(keyword, index);
        modelAndView.addObject("keywordQuestions", data);
        modelAndView.addObject("keyword", keyword);

        return modelAndView;
    }
}
