package com.tuotiansudai.paywrapper.controller;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.ReferrerRewardDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.paywrapper.service.ReferrerRewardService;
import com.tuotiansudai.paywrapper.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
public class ReferrerRewardController extends BaseController{

    @Autowired
    private ReferrerRewardService referrerRewardService;

    @RequestMapping(value = "/referrer-reward", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto register(@Valid @RequestBody ReferrerRewardDto dto) {
        return referrerRewardService.getReferrerReward(dto);
    }
}
