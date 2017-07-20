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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/current")
public class CurrentRedeemController {

    @Autowired
    private CurrentRedeemService currentRedeemService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView redeem() {
        ModelAndView mv = new ModelAndView("/current-redeem");
        String loginName = LoginUserInfo.getLoginName();
        BaseDto<RedeemLimitsDataDto> baseDto = currentRedeemService.limits(loginName);
        mv.addObject("redeemRemainAmount", AmountConverter.convertCentToString(baseDto.getData().getRemainAmount()));
        mv.addObject("redeemMaxAmount", AmountConverter.convertCentToString(baseDto.getData().getTotalAmount()));
        return mv;
    }

    @RequestMapping(path = "/redeem", method = RequestMethod.POST)
    public ModelAndView redeem(@Valid @ModelAttribute CurrentRedeemDto currentRedeemDto) {
        BaseDto<RedeemDataDto> baseDto = currentRedeemService.redeem(currentRedeemDto, LoginUserInfo.getLoginName());
        ModelAndView mv = new ModelAndView("/current-redeem-success");
        if (baseDto.isSuccess()){
            mv.addObject("result","success");
            mv.addObject("amount",AmountConverter.convertCentToString(baseDto.getData().getAmount()));
        }
        else{
            mv.addObject("result","fail");
        }
        return mv;
    }

}
