package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.FeedbackRequestDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
public class MobileAppFeedbackController extends MobileAppBaseController {
    @Autowired
    private FeedbackService feedbackService;

    @RequestMapping(value = "/feedback", method = RequestMethod.POST)
    public BaseResponseDto feedback(@Valid @RequestBody FeedbackRequestDto feedbackRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            String errorCode = fieldError.getDefaultMessage();
            String errorMessage = ReturnMessage.getErrorMsgByCode(errorCode);
            return new BaseResponseDto(errorCode, errorMessage);
        }
        feedbackService.create(getLoginName(), feedbackRequestDto.getContent());
        return new BaseResponseDto(ReturnMessage.SUCCESS);
    }
}
