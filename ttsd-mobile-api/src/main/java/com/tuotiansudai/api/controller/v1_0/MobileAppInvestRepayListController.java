package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.InvestRepayListRequestDto;
import com.tuotiansudai.api.dto.v1_0.InvestRepayListResponseDataDto;
import com.tuotiansudai.api.service.v1_0.MobileAppInvestRepayListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "出借管理-收款明细")
public class MobileAppInvestRepayListController extends MobileAppBaseController {

    @Autowired
    private MobileAppInvestRepayListService mobileAppInvestRepayListService;

    @RequestMapping(value = "/get/investrepays", method = RequestMethod.POST)
    @ApiOperation("收款明细")
    public BaseResponseDto<InvestRepayListResponseDataDto> queryUserInvestList(@RequestBody InvestRepayListRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppInvestRepayListService.generateUserInvestRepayList(requestDto);
    }
}
