package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.ghb.service.GHBQueryOGW00043;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/ghb/job-query")
public class GHBJobQueryController {

    private final static Logger logger = Logger.getLogger(GHBJobQueryController.class);

    @Autowired
    private GHBQueryOGW00043 ghbQueryOGW00043;

    public GHBJobQueryController(GHBQueryOGW00043 ghbQueryOGW00043) {
        this.ghbQueryOGW00043 = ghbQueryOGW00043;
    }

    @RequestMapping(path = "/OGW00043", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<PayDataDto> queryOGW00043() {
        logger.info("[GHB] OGW00043 is called");
        this.ghbQueryOGW00043.query();
        return new BaseDto<>(new PayDataDto());
    }
}
