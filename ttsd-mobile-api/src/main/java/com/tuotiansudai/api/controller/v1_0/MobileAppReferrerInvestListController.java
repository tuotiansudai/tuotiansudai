package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReferrerInvestListRequestDto;
import com.tuotiansudai.api.dto.v1_0.ReferrerInvestListResponseDataDto;
import com.tuotiansudai.api.service.v1_0.MobileAppReferrerInvestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "我的推荐人出借列表")
public class MobileAppReferrerInvestListController extends MobileAppBaseController {
    @Autowired
    private MobileAppReferrerInvestService mobileAppReferrerInvestService;

    @RequestMapping(value = "/get/referrerinvests", method = RequestMethod.POST)
    @ApiOperation("我的推荐人出借列表")
    public BaseResponseDto<ReferrerInvestListResponseDataDto> queryInvestList(@RequestBody ReferrerInvestListRequestDto referrerInvestListRequestDto) {
        referrerInvestListRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppReferrerInvestService.generateReferrerInvestList(referrerInvestListRequestDto);
    }

}
