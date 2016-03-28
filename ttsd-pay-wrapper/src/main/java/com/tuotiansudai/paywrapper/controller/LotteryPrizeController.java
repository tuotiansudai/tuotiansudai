package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.paywrapper.service.impl.LotteryPrizeServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/activity")
public class LotteryPrizeController {

    static Logger logger = Logger.getLogger(LotteryPrizeController.class);

    @Autowired
    LotteryPrizeServiceImpl lotteryPrizeService;

    @ResponseBody
    @RequestMapping(value = "/send-tiandou-lottery-prize-20", method = RequestMethod.POST)
    public void sendTianDouLotteryPrize20(@RequestBody String loginName) {
        logger.debug("into sendTianDouLotteryPrize20, loginName:" + loginName);
        lotteryPrizeService.sendTianDouLotteryPrize20(loginName);
    }

}
