package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.InvestDetailRequestDto;
import com.tuotiansudai.api.dto.v1_0.OperationDataResponseDataDto;
import com.tuotiansudai.api.service.v1_0.MobileAppInvestDetailService;
import com.tuotiansudai.api.service.v1_0.MobileAppOperationDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "运营数据")
public class MobileAppOperationDataController extends MobileAppBaseController {

    @Autowired
    private MobileAppOperationDataService mobileAppOperationDataService;

    @RequestMapping(value = "/get/operation-data", method = RequestMethod.POST)
    @ApiOperation("运营数据")
    public BaseResponseDto<OperationDataResponseDataDto> generateOperationData(@RequestBody BaseParamDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppOperationDataService.generatorOperationData(requestDto);
    }
}
