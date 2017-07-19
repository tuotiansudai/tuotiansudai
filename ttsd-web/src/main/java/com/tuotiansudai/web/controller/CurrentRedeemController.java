package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.CurrentRedeemDto;
import com.tuotiansudai.dto.RedeemDataDto;
import com.tuotiansudai.dto.RedeemLimitsDataDto;
import com.tuotiansudai.service.CurrentRedeemService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/current")
public class CurrentRedeemController {

    @Autowired
    private CurrentRedeemService currentRedeemService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView redeem() {
        ModelAndView mv = new ModelAndView("/day-turn-out");
        String loginName = LoginUserInfo.getLoginName();
        BaseDto<RedeemLimitsDataDto> baseDto = currentRedeemService.limits(loginName);
        mv.addObject("remainAmount", AmountConverter.convertCentToString(baseDto.getData().getRemainAmount()));
        mv.addObject("totalAmount",AmountConverter.convertCentToString(baseDto.getData().getTotalAmount()));
        return mv;
    }

    @RequestMapping(path = "/redeem", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<RedeemDataDto> redeem(@Valid @RequestBody CurrentRedeemDto currentRedeemDto) {
        BaseDto<RedeemDataDto> baseDto = currentRedeemService.redeem(currentRedeemDto);
        return baseDto;
    }

}
