package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppBankCardService;
import com.tuotiansudai.util.RequestIPParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(description = "绑卡")
public class MobileAppBankCardController extends MobileAppBaseController {

    @Autowired
    private MobileAppBankCardService mobileAppBankCardService;//查询签约／绑卡结果

    /**
     * @param bankCardRequestDto 绑卡请求参数
     * @return BaseResponseDto
     * @function 绑卡
     */
    @RequestMapping(value = "/bankcard/bind", method = RequestMethod.POST)
    @ApiOperation("绑卡")
    public BaseResponseDto<BankCardResponseDto> bankCardBind(@RequestBody BankCardRequestDto bankCardRequestDto, HttpServletRequest request) {
        if (!StringUtils.isNumeric(bankCardRequestDto.getCardNo())) {
            return new BaseResponseDto<>(ReturnMessage.BIND_CARD_FAIL);
        }
        bankCardRequestDto.setUserId(getLoginName());
        bankCardRequestDto.getBaseParam().setUserId(getLoginName());
        bankCardRequestDto.setIp(RequestIPParser.parse(request));
        return mobileAppBankCardService.bindBankCard(bankCardRequestDto);
    }

    /**
     * @param requestDto 换卡请求参数
     * @return BaseResponseDto
     * @function 换卡
     */
    @RequestMapping(value = "/bankcard/replace", method = RequestMethod.POST)
    @ApiOperation("换卡")
    public BaseResponseDto<BankCardReplaceResponseDataDto> bankCardReplace(@RequestBody BankCardReplaceRequestDto requestDto, HttpServletRequest request) {
        requestDto.setIp(RequestIPParser.parse(request));
        return mobileAppBankCardService.replaceBankCard(requestDto);
    }

    /**
     * @param bankCardRequestDto
     * @return BaseResponseDto
     * @function 签约
     */
    @RequestMapping(value = "/bankcard/sign", method = RequestMethod.POST)
    @ApiOperation("签约")
    public BaseResponseDto<BankCardResponseDto> bankCardSign(@RequestBody BankCardRequestDto bankCardRequestDto) {
        bankCardRequestDto.setUserId(getLoginName());
        bankCardRequestDto.setIsOpenFastPayment(true);
        bankCardRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppBankCardService.openFastPay(bankCardRequestDto);
    }

    @RequestMapping(value = "/bankcard/query", method = RequestMethod.POST)
    @ApiOperation("绑卡／签约结果查询")
    public BaseResponseDto queryBindAndSginStatus(@RequestBody BankCardRequestDto bankCardRequestDto) {
        bankCardRequestDto.setUserId(getLoginName());
        bankCardRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppBankCardService.queryStatus(bankCardRequestDto);
    }

    @RequestMapping(value = "/bankcard/is-replacing", method = RequestMethod.POST)
    @ApiOperation("是否可以换卡")
    public BaseResponseDto<BankCardIsReplacingResponseDto> queryIsReplacing(@Valid @RequestBody BaseParamDto baseParamDto) {
        baseParamDto.getBaseParam().setUserId(getLoginName());
        return mobileAppBankCardService.isReplacing(baseParamDto);
    }
}
