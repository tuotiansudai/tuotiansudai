package com.tuotiansudai.fudian.controller;

import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.service.AsyncCallbackInterface;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path = "/callback")
public class CallbackController {

    private static Logger logger = LoggerFactory.getLogger(CallbackController.class);

    private final SignatureHelper signatureHelper;

    private final ApplicationContext context;

    @Autowired
    public CallbackController(ApplicationContext context, SignatureHelper signatureHelper) {
        this.context = context;
        this.signatureHelper = signatureHelper;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(ApiType.class, new CallbackTypePropertyEditor());
    }

    @RequestMapping(value = "/notify-url/{apiType}", method = RequestMethod.POST)
    public ResponseEntity<String> notifyCallback(HttpServletRequest request, @PathVariable ApiType apiType) {
        String reqData = request.getParameter("reqData");
        boolean isCorrect = signatureHelper.verifySign(reqData);

        logger.info("[notify callback] type: {}, sign: {}, data: {}, ", apiType.name(), isCorrect, reqData);

        if (isCorrect) {
            AsyncCallbackInterface asyncCallback = (AsyncCallbackInterface) this.context.getBean(apiType.getCallbackHandlerClass());
            asyncCallback.callback(reqData);
        } else {
            logger.error("[notify callback] sign is incorrect, type: {}, data: {}, ", apiType.name(), reqData);
        }

        return ResponseEntity.ok("success");
    }

    @RequestMapping(value = "/return-url/{apiType}", method = RequestMethod.POST)
    public ResponseEntity<ResponseDto> returnCallback(HttpServletRequest request, @PathVariable ApiType apiType) {
        String reqData = request.getParameter("reqData");

        boolean isCorrect = signatureHelper.verifySign(reqData);

        logger.info("return callback type: {}, sign: {}, data: {}, ", apiType.name(), isCorrect, reqData);

        ResponseDto responseDto = apiType.getParser().parse(reqData);
        responseDto.setReqData(reqData);

        return ResponseEntity.ok(responseDto);
    }
}
