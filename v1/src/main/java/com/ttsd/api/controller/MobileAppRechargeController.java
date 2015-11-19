package com.ttsd.api.controller;

import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.jdp2p.loan.model.Recharge;
import com.esoft.umpay.recharge.service.impl.UmPayRechargeOteration;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.MobileAppChannelService;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by tuotian on 15/8/7.
 */
@Controller
public class MobileAppRechargeController {
    @Resource
    private UmPayRechargeOteration umPayRechargeOteration;

    @Autowired
    private MobileAppChannelService mobileAppChannelService;

    @Logger
    private Log log;

    /**
     * @function
     * @param bankCardRequestDto
     * @return
     */
    @RequestMapping(value = "/recharge",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto recharge(@RequestBody BankCardRequestDto bankCardRequestDto,
                                    HttpServletRequest request){
        String rechargeAmount = bankCardRequestDto.getRechargeAmount();
        String userId = bankCardRequestDto.getUserId();
        String platform = bankCardRequestDto.getBaseParam().getPlatform();
        try {
            Recharge recharge = new Recharge();
            recharge.setActualMoney(new Double(rechargeAmount));
            User user = new User();
            user.setId(userId);
            recharge.setUser(user);
            recharge.setSource(AccessSource.valueOf(platform.toUpperCase(Locale.ENGLISH)).name());
            recharge.setChannel(mobileAppChannelService.obtainChannelBySource(bankCardRequestDto.getBaseParam()));
            return umPayRechargeOteration.createOperation(recharge, request);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(),e);
            BaseResponseDto<BankCardResponseDto> baseResponseDto = new BaseResponseDto<BankCardResponseDto>();
            baseResponseDto.setCode(ReturnMessage.NETWORK_EXCEPTION.getCode());
            baseResponseDto.setMessage(ReturnMessage.NETWORK_EXCEPTION.getMsg());
            return baseResponseDto;
        }
    }
}
