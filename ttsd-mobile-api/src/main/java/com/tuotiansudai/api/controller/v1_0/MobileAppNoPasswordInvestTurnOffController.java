package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NoPasswordInvestTurnOffRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppNoPasswordInvestTurnOffService;
import com.tuotiansudai.util.RequestIPParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(description = "关闭免密出借功能")
public class MobileAppNoPasswordInvestTurnOffController extends MobileAppBaseController {
    @Autowired
    private MobileAppNoPasswordInvestTurnOffService mobileAppNoPasswordInvestTurnOffService;

    static Logger logger = Logger.getLogger(MobileAppNoPasswordInvestTurnOffController.class);

    @RequestMapping(value = "/no-password-invest/turn-off", method = RequestMethod.POST)
    @ApiOperation("关闭免密出借功能")
    public BaseResponseDto noPasswordInvestTurnOff(@RequestBody NoPasswordInvestTurnOffRequestDto noPasswordInvestTurnOffRequestDto, HttpServletRequest request) {
        String ip = RequestIPParser.parse(request);
        noPasswordInvestTurnOffRequestDto.getBaseParam().setUserId(getLoginName());

        return mobileAppNoPasswordInvestTurnOffService.noPasswordInvestTurnOff(noPasswordInvestTurnOffRequestDto, ip);
    }

}
