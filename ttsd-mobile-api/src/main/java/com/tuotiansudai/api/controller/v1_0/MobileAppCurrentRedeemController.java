package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.current.client.CurrentRestClient;
import com.tuotiansudai.current.dto.AccountResponseDto;
import com.tuotiansudai.current.dto.RedeemRequestDto;
import com.tuotiansudai.current.dto.RedeemResponseDto;
import com.tuotiansudai.enums.AsyncUmPayService;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.AmountConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

@RestController
@Api(description = "活期转出申请")
public class MobileAppCurrentRedeemController extends MobileAppBaseController {

    @Autowired
    private CurrentRestClient currentRestClient;

    @RequestMapping(value = "/get/rxb/redeem", method = RequestMethod.POST)
    @ApiOperation("转出申请")
    public BaseResponseDto<CurrentRedeemResponseDataDto> redeem(@RequestBody CurrentRedeemRequestDto currentRedeemRequestDto) {
        String loginName = getLoginName();
        AccountResponseDto accountResponseDto = currentRestClient.getAccount(loginName);

        String source = currentRedeemRequestDto.getBaseParam().getPlatform().toUpperCase();
        long amount = AmountConverter.convertStringToCent(currentRedeemRequestDto.getAmount());
        RedeemRequestDto redeemRequestDto = new RedeemRequestDto(loginName, amount, Source.valueOf(source));

        BaseResponseDto baseResponseDto = new BaseResponseDto();
        CurrentRedeemResponseDataDto currentRedeemResponseDataDto = new CurrentRedeemResponseDataDto();
        if (accountResponseDto.getPersonalAvailableRedeem() < amount) {
            baseResponseDto.setCode(ReturnMessage.REDEEM_NO_AMOUNT.getCode());
            baseResponseDto.setMessage(ReturnMessage.REDEEM_NO_AMOUNT.getMsg());
            return baseResponseDto;
        }

        RedeemResponseDto baseDto = currentRestClient.redeem(redeemRequestDto);
        baseResponseDto.setCode("");
        baseResponseDto.setMessage("");
        currentRedeemResponseDataDto.setUrl(MessageFormat.format("/callback/{0}", AsyncUmPayService.CURRENT_REDEEM_APPLY));
        baseResponseDto.setData(currentRedeemResponseDataDto);

        return baseResponseDto;

    }

    @RequestMapping(value = "/get/rxb/redeem/limits", method = RequestMethod.GET)
    @ApiOperation("当日可转出金额和最大转出金额")
    public BaseResponseDto<CurrentRedeemLimitResponseDataDto> limitRedeem() {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        CurrentRedeemLimitResponseDataDto currentRedeemLimitResponseDataDto = new CurrentRedeemLimitResponseDataDto();

        AccountResponseDto baseDto = currentRestClient.getAccount(getLoginName());
        baseResponseDto.setCode("");
        baseResponseDto.setMessage("");
        currentRedeemLimitResponseDataDto.setAvliableRedeemAmount(baseDto.getPersonalAvailableRedeem());
        currentRedeemLimitResponseDataDto.setMaxRedeemAmount(baseDto.getPersonalMaxRedeem());
        baseResponseDto.setData(currentRedeemLimitResponseDataDto);
        return baseResponseDto;
    }

}
