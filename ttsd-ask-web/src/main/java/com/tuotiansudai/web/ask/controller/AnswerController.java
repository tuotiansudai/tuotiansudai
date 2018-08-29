package com.tuotiansudai.web.ask.controller;

import com.tuotiansudai.ask.dto.AnswerRequestDto;
import com.tuotiansudai.ask.dto.AnswerResultDataDto;
import com.tuotiansudai.ask.service.AnswerService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.RedisWrapperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/answer")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<AnswerResultDataDto> answer(@Valid @ModelAttribute AnswerRequestDto answerRequestDto) {
        return new BaseDto<>(answerService.createAnswer(LoginUserInfo.getLoginName(), answerRequestDto));
    }

    @RequestMapping(path = "/{answerId:^\\d+$}/best", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> markBestAnswer(@PathVariable long answerId) {
        BaseDataDto dataDto = new BaseDataDto(answerService.makeBestAnswer(answerId));

        return new BaseDto<>(dataDto);
    }

    @RequestMapping(path = "/{answerId:^\\d+$}/favor", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> favor(@PathVariable long answerId) {
        BaseDataDto dataDto = new BaseDataDto(answerService.favor(LoginUserInfo.getLoginName(), answerId));

        return new BaseDto<>(dataDto);
    }

    @RequestMapping(path = "/my-answers", method = RequestMethod.GET)
    public ModelAndView getMyAnswers(@RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        return new ModelAndView("/my-answers", "answers", answerService.findMyAnswers(LoginUserInfo.getLoginName(), index, 10));
    }
}
