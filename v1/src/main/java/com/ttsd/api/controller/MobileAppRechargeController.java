package com.ttsd.api.controller;

import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.jdp2p.loan.model.Recharge;
import com.esoft.umpay.recharge.service.impl.UmPayRechargeOteration;
import com.ttsd.api.dto.BankCardRequestDto;
import com.ttsd.api.dto.BankCardResponseDto;
import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.ReturnMessage;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by tuotian on 15/8/7.
 */
@Controller
public class MobileAppRechargeController {
    @Resource
    private UmPayRechargeOteration umPayRechargeOteration;

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
        try {
            Recharge recharge = new Recharge();
            recharge.setActualMoney(new Double(rechargeAmount));
            User user = new User();
            user.setId(userId);
            recharge.setUser(user);
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
