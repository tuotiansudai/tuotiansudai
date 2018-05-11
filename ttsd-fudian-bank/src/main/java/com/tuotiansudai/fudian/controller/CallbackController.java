package com.tuotiansudai.fudian.controller;

import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
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
import java.text.MessageFormat;

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
    public ResponseEntity<String> returnCallback(HttpServletRequest request, @PathVariable ApiType apiType) {
        String reqData = request.getParameter("reqData");

        try {
            if (Strings.isNullOrEmpty(reqData) && !signatureHelper.verifySign(reqData)) {
                logger.error(MessageFormat.format("verify sign failed, return callback type: {0}, data: {1} ", apiType.name(), reqData));
                return ResponseEntity.badRequest().build();
            }
        } catch (SecurityException e) {
            logger.error(MessageFormat.format("verify sign exception, return callback type: {0}, data: {1} ", apiType.name(), reqData), e);
            return ResponseEntity.badRequest().build();
        }

        ResponseDto responseDto = apiType.getParser().parse(reqData);
        if (responseDto == null) {
            logger.error("parse data error, return callback type: {}, data: {} ", apiType.name(), reqData);
            return ResponseEntity.badRequest().build();
        }

        return this.generateResponseJson(responseDto);
    }

    @RequestMapping(value = "/{apiType}/order-no/{orderNo}/is-success", method = RequestMethod.GET)
    public ResponseEntity isCallbackSuccess(@PathVariable ApiType apiType, @PathVariable String orderNo) {
        AsyncCallbackInterface asyncCallback = (AsyncCallbackInterface) this.context.getBean(apiType.getCallbackHandlerClass());
        Boolean isSuccess = asyncCallback.isSuccess(orderNo);

        if (isSuccess == null) {
            return ResponseEntity.noContent().build();
        }

        return isSuccess ? ResponseEntity.ok().build() : ResponseEntity.status(-1).build();
    }

    private ResponseEntity<String> generateResponseJson(ResponseDto responseDto) {
        JsonObject extraValues = new JsonObject();
        extraValues.addProperty("orderNo", responseDto.getContent().getOrderNo());

        JsonObject payData = new JsonObject();
        payData.addProperty("code", responseDto.getRetCode());
        payData.addProperty("message", responseDto.getRetMsg());
        payData.addProperty("status", responseDto.isSuccess());
        payData.add("extraValues", extraValues);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", true);
        jsonObject.add("data", payData);

        return ResponseEntity.ok(new GsonBuilder().create().toJson(jsonObject));
    }
}
