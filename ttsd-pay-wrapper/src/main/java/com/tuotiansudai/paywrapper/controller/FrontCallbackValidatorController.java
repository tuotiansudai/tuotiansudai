package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.umpay.api.exception.VerifyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

@Controller
public class FrontCallbackValidatorController {

    private final PayAsyncClient payAsyncClient;

    @Autowired
    public FrontCallbackValidatorController(PayAsyncClient payAsyncClient) {
        this.payAsyncClient = payAsyncClient;
    }

    @RequestMapping(value = "/validate-front-callback", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> validate(@RequestBody Map<String, String> params) {
        PayDataDto payDataDto = new PayDataDto();
        BaseDto<PayDataDto> baseDto = new BaseDto<>(payDataDto);

        try {
            BaseCallbackRequestModel model = payAsyncClient.parseParamsToModel(params, BaseCallbackRequestModel.class);
            payDataDto.setStatus(model.isSuccess());
            payDataDto.setCode(model.getRetCode());
            payDataDto.setMessage(model.getRetMsg());
        } catch (VerifyException | IOException e) {
            payDataDto.setMessage(e.getLocalizedMessage());
        }

        return baseDto;
    }
}
