package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.UserBillDetailListRequestDto;
import com.tuotiansudai.api.dto.v1_0.UserBillDetailListResponseDataDto;
import com.tuotiansudai.api.service.v1_0.MobileAppUserBillListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "资金管理-明细")
public class MobileAppUserBillListController extends MobileAppBaseController {
    @Autowired
    private MobileAppUserBillListService mobileAppUserBillListService;

    @RequestMapping(value = "/get/userbills", method = RequestMethod.POST)
    @ApiOperation("资金管理-明细")
    public BaseResponseDto<UserBillDetailListResponseDataDto> queryFundManagement(@RequestBody UserBillDetailListRequestDto userBillDetailListRequestDto) {
        userBillDetailListRequestDto.setUserId(getLoginName());
        userBillDetailListRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppUserBillListService.queryUserBillList(userBillDetailListRequestDto);
    }

}
