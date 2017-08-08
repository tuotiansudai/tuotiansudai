package com.tuotiansudai.web.controller;

import com.tuotiansudai.current.client.CurrentRestClient;
import com.tuotiansudai.current.dto.AccountResponseDto;
import com.tuotiansudai.current.dto.RedeemRequestDto;
import com.tuotiansudai.current.dto.RedeemResponseDto;
import com.tuotiansudai.dto.CurrentRedeemDto;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/current")
public class CurrentRedeemController {

    @Autowired
    private CurrentRestClient currentRestClient;

    @RequestMapping(path = "/redeem", method = RequestMethod.GET)
    public ModelAndView redeem() {
        ModelAndView mv = new ModelAndView("/current-redeem");
        String loginName = LoginUserInfo.getLoginName();
        AccountResponseDto baseDto = currentRestClient.getAccount(loginName);
        mv.addObject("availableRedeemAmount", AmountConverter.convertCentToString(baseDto.getPersonalAvailableRedeem()));
        mv.addObject("maxRedeemAmount", AmountConverter.convertCentToString(baseDto.getPersonalMaxRedeem()));
        return mv;
    }

    @RequestMapping(path = "/redeem", method = RequestMethod.POST)
    public ModelAndView redeem(@Valid @ModelAttribute CurrentRedeemDto currentRedeemDto) {
        RedeemRequestDto redeemRequestDto = new RedeemRequestDto(LoginUserInfo.getLoginName(), AmountConverter.convertStringToCent(currentRedeemDto.getAmount()), Source.WEB);
        RedeemResponseDto baseDto = currentRestClient.redeem(redeemRequestDto);
        ModelAndView mv = new ModelAndView("/current-redeem-success");
        mv.addObject("amount",AmountConverter.convertCentToString(baseDto.getAmount()));
        return mv;
    }

}
