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
@RequestMapping(path = "/about")
public class AboutController {

    @Autowired
    private OperationDataService operationDataService;

    @RequestMapping(path = "/{item:^assurance|risk-flow|company|contact|guide|notice|media|notice-detail|service-fee|qa|refer-reward|team|operational|knowledge|audit-report$}", method = RequestMethod.GET)
    public ModelAndView about(@PathVariable String item) {
        ModelAndView modelAndView = new ModelAndView("/about/" + item);
        modelAndView.addObject("responsive", true);
        if ("operational".equals(item)) {
            modelAndView.addObject("investDetailList", operationDataService.getInvestDetail(new Date()));
        }
        return modelAndView;
    }

    @RequestMapping(path = "/operation-data/chart", method = RequestMethod.GET)
    @ResponseBody
    public OperationDataDto infoPublishChart() {
        return operationDataService.getOperationDataFromRedis(new Date());
    }

}
