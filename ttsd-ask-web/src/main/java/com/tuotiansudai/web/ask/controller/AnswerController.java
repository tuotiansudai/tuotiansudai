package com.tuotiansudai.web.ask.controller;

import com.tuotiansudai.ask.dto.AnswerRequestDto;
import com.tuotiansudai.ask.dto.BaseDataDto;
import com.tuotiansudai.ask.dto.BaseDto;
import com.tuotiansudai.ask.service.AnswerService;
import com.tuotiansudai.spring.LoginUserInfo;
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
    public BaseDto<BaseDataDto> answer(@Valid @ModelAttribute AnswerRequestDto answerRequestDto) {
        return new BaseDto<>(new BaseDataDto(answerService.createAnswer(LoginUserInfo.getLoginName(), answerRequestDto)));
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
    public ModelAndView getMyAnswers(@RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                     @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return new ModelAndView("/my-answers", "answers", answerService.findMyAnswers(LoginUserInfo.getLoginName(), index, pageSize));
    }
}
