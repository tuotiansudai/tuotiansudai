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
}
