package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.InvestDetailRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppInvestDetailService;
import com.tuotiansudai.api.service.v1_0.MobileAppOperationDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppOperationDataController extends MobileAppBaseController {

    @Autowired
    private MobileAppOperationDataService mobileAppOperationDataService;

    @RequestMapping(value = "/get/operation-data", method = RequestMethod.POST)
    public BaseResponseDto generateOperationData(@RequestBody BaseParamDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppOperationDataService.generatorOperationData(requestDto);
    }
}
