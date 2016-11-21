package com.tuotiansudai.web.ask.controller;

import com.tuotiansudai.ask.dto.QuestionDto;
import com.tuotiansudai.ask.dto.QuestionRequestDto;
import com.tuotiansudai.ask.dto.QuestionResultDataDto;
import com.tuotiansudai.ask.repository.model.Tag;
import com.tuotiansudai.ask.service.AnswerService;
import com.tuotiansudai.ask.service.QuestionService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.spring.LoginUserInfo;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping(path = "/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    final private Map<String, Tag> urlTagMapTag = new HashedMap<String, Tag>() {
        {
            put("securities", Tag.SECURITIES);
            put("bank", Tag.BANK);
            put("futures", Tag.FUTURES);
            put("P2P", Tag.P2P);
            put("trust", Tag.TRUST);
            put("loan", Tag.LOAN);
            put("fund", Tag.FUND);
            put("zhongchou", Tag.CROWD_FUNDING);
            put("licai", Tag.INVEST);
            put("credit_card", Tag.CREDIT_CARD);
            put("forex", Tag.FOREX);
            put("stock", Tag.STOCK);
            put("other", Tag.OTHER);
        }
    };

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
                                 @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                 @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        QuestionDto question = questionService.getQuestion(LoginUserInfo.getLoginName(), questionId);
        if (question == null) {
            return new ModelAndView("/error/404");
        }

        ModelAndView modelAndView = new ModelAndView("/question", "questionId", questionId);
        modelAndView.addObject("isQuestionOwner", StringUtils.isEmpty(question.getMobile()) ? false : question.getMobile().equalsIgnoreCase(LoginUserInfo.getMobile()));
        modelAndView.addObject("question", question);
        modelAndView.addObject("bestAnswer", answerService.getBestAnswer(LoginUserInfo.getLoginName(), questionId));
        modelAndView.addObject("answers", answerService.getNotBestAnswers(LoginUserInfo.getLoginName(), questionId, index, pageSize));

        return modelAndView;
    }

    @RequestMapping(path = "/my-questions", method = RequestMethod.GET)
    public ModelAndView getMyQuestions(@RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                       @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return new ModelAndView("/my-questions", "questions", questionService.findMyQuestions(LoginUserInfo.getLoginName(), index, pageSize));
    }

    @RequestMapping(path = "/{tag}", method = RequestMethod.GET)
    public ModelAndView getQuestionsByCategory(@PathVariable String urlTag,
                                               @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                               @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        Tag tag = urlTagMapTag.get(urlTag);
        if (null == tag) {
            tag = Tag.BANK;
        }
        ModelAndView modelAndView = new ModelAndView("/question-category");
        modelAndView.addObject("questions", questionService.findByTag(LoginUserInfo.getLoginName(), tag, index, pageSize));
        modelAndView.addObject("tag", tag);
        return modelAndView;
    }

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public ModelAndView getQuestionsByKeyword(@RequestParam(value = "keyword") String keyword,
                                              @RequestParam(value = "index") int index) {
        ModelAndView modelAndView = new ModelAndView();
        String loginName = LoginUserInfo.getLoginName();
        BaseDto<BasePaginationDataDto> data = questionService.getQuestionsByKeywords(keyword, loginName, index, 10);
        modelAndView.addObject(data);

        return modelAndView;
    }
}
