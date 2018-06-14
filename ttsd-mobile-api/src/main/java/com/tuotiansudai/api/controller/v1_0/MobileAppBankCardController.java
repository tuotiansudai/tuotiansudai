package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.UserBindBankCardService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.RequestIPParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(description = "解绑卡")
public class MobileAppBankCardController extends MobileAppBaseController {

    @Autowired
    private UserBindBankCardService userBindBankCardService;

    @RequestMapping(path = "/bankcard/bind/source/{source}", method = RequestMethod.POST)
    @ApiOperation("绑卡")
    public BankAsyncMessage bindBankCard(HttpServletRequest request, @PathVariable(value = "source") Source source) {
        return userBindBankCardService.bind(LoginUserInfo.getLoginName(), source, RequestIPParser.parse(request), null);
    }


    @RequestMapping(path = "/bankcard/unbind/source/{source}", method = RequestMethod.POST)
    @ApiOperation("解绑")
    public BankAsyncMessage unbindBankCard(HttpServletRequest request, @PathVariable(value = "source") Source source) {
        return userBindBankCardService.unbind(LoginUserInfo.getLoginName(), source, RequestIPParser.parse(request), null);
    }
}
