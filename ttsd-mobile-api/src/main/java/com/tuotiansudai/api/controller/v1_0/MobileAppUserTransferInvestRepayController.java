package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.UserInvestRepayRequestDto;
import com.tuotiansudai.api.dto.v1_0.UserTransferInvestRepayRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppUserInvestRepayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MobileAppUserTransferInvestRepayController extends MobileAppBaseController {
    @Autowired
    private MobileAppUserInvestRepayService mobileAppUserInvestRepayService;

    @RequestMapping(value = "/get/transfer-invest-repay", method = RequestMethod.POST)
    public BaseResponseDto userTransferInvestRepay(@RequestBody UserTransferInvestRepayRequestDto userTransferInvestRepayRequestDto) {

        return mobileAppUserInvestRepayService.userInvestRepay(userTransferInvestRepayRequestDto);
    }
}
