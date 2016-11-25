package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.RechargeListRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppRechargeListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "个人充值记录列表")
public class MobileAppRechargeListController extends MobileAppBaseController {
    @Autowired
    private MobileAppRechargeListService rechargeListService;

    @RequestMapping(value = "/get/userrecharges", method = RequestMethod.POST)
    @ApiOperation("个人充值记录列表")
    public BaseResponseDto queryList(@RequestBody RechargeListRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return rechargeListService.generateRechargeList(requestDto);
    }
}
