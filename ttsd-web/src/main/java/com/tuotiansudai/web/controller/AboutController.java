package com.tuotiansudai.web.controller;

import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/about")
public class AboutController {

    @Value("${pay.withdraw.fee}")
    private long withdrawFee;

    @RequestMapping(path = "/{item:^assurance|company|contact|guide|notice|media|notice-detail|service-fee|qa|refer-reward|team|operational$}", method = RequestMethod.GET)
    public ModelAndView about(@PathVariable String item){
        ModelAndView modelAndView =  new ModelAndView("/about/" + item);
        modelAndView.addObject("responsive",true);
        if("service-fee".equals(item)){
            modelAndView.addObject("withdrawFee", AmountConverter.convertCentToString(withdrawFee));

        }
        return modelAndView;
    }
}
