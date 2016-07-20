package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.FeedbackRequestDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.repository.model.FeedbackType;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.FeedbackService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Locale;


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

        Source source = Source.valueOf(feedbackRequestDto.getBaseParam().getPlatform().toUpperCase(Locale.ENGLISH));
        String contact = feedbackRequestDto.getContact();
        if (StringUtils.isEmpty(contact)) {
            contact = feedbackRequestDto.getBaseParam().getPhoneNum();
        }

        FeedbackType type;
        if (StringUtils.isEmpty(feedbackRequestDto.getType())) {
            type = FeedbackType.opinion;
        } else {
            type = FeedbackType.valueOf(feedbackRequestDto.getType());
        }

        feedbackService.create(getLoginName(), contact, source, type, feedbackRequestDto.getContent());
        return new BaseResponseDto(ReturnMessage.SUCCESS);
    }
}
