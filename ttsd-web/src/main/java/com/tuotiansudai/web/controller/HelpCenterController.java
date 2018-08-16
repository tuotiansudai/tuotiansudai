package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.OperationDataDto;
import com.tuotiansudai.service.OperationDataService;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Controller
@RequestMapping(path = "/help")
public class HelpCenterController {

    @Value("${pay.withdraw.fee}")
    private long withdrawFee;


    @Autowired
    private OperationDataService operationDataService;

    @RequestMapping(path = "/{item:^help-center|account|user|money|product|help-content|other$}", method = RequestMethod.GET)
    public ModelAndView about(@PathVariable String item) {
        ModelAndView modelAndView = new ModelAndView("/helpCenter/" + item);
        modelAndView.addObject("responsive", true);

        return modelAndView;
    }

    @RequestMapping(path = "/operation-data/chart", method = RequestMethod.GET)
    @ResponseBody
    public OperationDataDto infoPublishChart() {
        return operationDataService.getOperationDataFromRedis(new Date());
    }

}
