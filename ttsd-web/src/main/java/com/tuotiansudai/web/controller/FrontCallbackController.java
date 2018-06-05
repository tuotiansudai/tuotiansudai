package com.tuotiansudai.web.controller;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.enums.BankCallbackType;
import com.tuotiansudai.fudian.message.BankReturnCallbackMessage;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

@Controller
@RequestMapping(path = "/callback")
public class FrontCallbackController {

    private static Logger logger = Logger.getLogger(FrontCallbackController.class);

    private final BankWrapperClient bankWrapperClient = new BankWrapperClient();

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(BankCallbackType.class, new BankCallbackTypePropertyEditor());
    }

    @RequestMapping(value = "/return-url/{bankCallbackType}", method = RequestMethod.POST)
    public ModelAndView bankReturnCallback(HttpServletRequest request, @PathVariable BankCallbackType bankCallbackType) {
        String reqData = request.getParameter("reqData");

        logger.info(MessageFormat.format("front callback url: {0}, data: {1}", request.getRequestURI(), reqData));

        BankReturnCallbackMessage bankReturnCallbackMessage = this.bankWrapperClient.checkBankReturnUrl(request.getRequestURI(), reqData);

        if (bankReturnCallbackMessage == null) {
            return new ModelAndView("/error/404");
        }

        if (bankReturnCallbackMessage.isStatus()) {
            return new ModelAndView("/pay-in-progress", "orderNo", bankReturnCallbackMessage.getBankOrderNo())
                    .addObject("bankCallbackType", bankCallbackType);
        }

        return new ModelAndView("/bank-result-failure", "message", bankReturnCallbackMessage.getMessage())
                .addObject("bankCallbackType", bankCallbackType);
    }

    @RequestMapping(value = "/{bankCallbackType}/order-no/{orderNo}/in-progress", method = RequestMethod.POST)
    public ModelAndView bankInProgress(@PathVariable BankCallbackType bankCallbackType,
                                       @PathVariable String orderNo) {
        return new ModelAndView("/pay-in-progress", "orderNo", orderNo)
                .addObject("bankCallbackType", bankCallbackType);
    }

    @RequestMapping(value = "/{bankCallbackType}/order-no/{orderNo}/is-success", method = RequestMethod.POST)
    public ModelAndView callbackSuccess(@PathVariable BankCallbackType bankCallbackType,
                                        @PathVariable String orderNo) {
        Boolean isSuccess = this.bankWrapperClient.isCallbackSuccess(bankCallbackType, orderNo);

        if (isSuccess == null || isSuccess) {
            return new ModelAndView("/bank-result-success", "bankCallbackType", bankCallbackType)
                    .addObject("isInProgress", isSuccess == null);
        }

        return new ModelAndView("/bank-result-failure", "bankCallbackType", bankCallbackType);
    }
}
