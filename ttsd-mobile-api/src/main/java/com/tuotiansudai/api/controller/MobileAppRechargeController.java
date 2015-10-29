package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BankCardRequestDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MobileAppRechargeController {

    /**
     * @param bankCardRequestDto
     * @return
     * @function
     */
    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    public BaseResponseDto recharge(@RequestBody BankCardRequestDto bankCardRequestDto,
                                    HttpServletRequest request) {
        throw new NotImplementedException(getClass().getName());
    }
}
