package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.UserTransferInvestRepayRequestDto;
import com.tuotiansudai.api.dto.v1_0.UserTransferInvestRepayResponseDataDto;
import com.tuotiansudai.api.service.v1_0.MobileAppUserTransferInvestRepayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(description = "债券转让用户回款计划")
public class MobileAppUserTransferInvestRepayController extends MobileAppBaseController {
    @Autowired
    private MobileAppUserTransferInvestRepayService mobileAppUserTransferInvestRepayService;

    @RequestMapping(value = "/get/transfer-invest-repay", method = RequestMethod.POST)
    @ApiOperation("债券转让用户回款计划")
    public BaseResponseDto<UserTransferInvestRepayResponseDataDto> userTransferInvestRepay(@RequestBody UserTransferInvestRepayRequestDto userTransferInvestRepayRequestDto) {

        return mobileAppUserTransferInvestRepayService.userTransferInvestRepay(userTransferInvestRepayRequestDto);
    }
}
