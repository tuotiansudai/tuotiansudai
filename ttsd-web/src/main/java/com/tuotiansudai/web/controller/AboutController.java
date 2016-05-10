package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.OperationDataDto;
import com.tuotiansudai.service.impl.OperationDataServiceModel;
import com.tuotiansudai.service.OperationDataService;
import com.tuotiansudai.service.InfoPublishService;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/about")
public class AboutController {

    @Value("${pay.withdraw.fee}")
    private long withdrawFee;


    @Autowired
    private OperationDataService operationDataService;

    @Autowired
    private InfoPublishService infoPublishService;

    @RequestMapping(path = "/{item:^assurance|company|contact|guide|notice|media|notice-detail|service-fee|qa|refer-reward|team|operational$}", method = RequestMethod.GET)
    public ModelAndView about(@PathVariable String item){
        ModelAndView modelAndView =  new ModelAndView("/about/" + item);
        modelAndView.addObject("responsive",true);
        if("service-fee".equals(item)){
            modelAndView.addObject("withdrawFee", AmountConverter.convertCentToString(withdrawFee));
        }
        else if("operation-data".equals(item)) {
        }
        return modelAndView;
    }

    @RequestMapping(path = "/info-publish", method = RequestMethod.GET)
    @ResponseBody
    public OperationDataDto infoPublishChart() {
        OperationDataServiceModel operationDataServiceModel = operationDataService.getOperationDataFromRedis();

        return operationDataServiceModel.getOperationDataDto();
    }

    @RequestMapping(path = "/operation-data", method = RequestMethod.GET)
    public ModelAndView infoPublishTable(){
        ModelAndView modelAndView =  new ModelAndView();

        modelAndView.addObject("investDetailList", infoPublishService.getInvestDetail());
        return modelAndView;
    }
}
