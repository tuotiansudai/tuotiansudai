package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppActivityService;
import com.tuotiansudai.enums.riskestimation.Estimate;
import com.tuotiansudai.service.RiskEstimateService;
import com.tuotiansudai.spring.LoginUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(description = "用户出借偏好测评")
public class MobileAppRiskEstimateController extends MobileAppBaseController {

    private final RiskEstimateService riskEstimateService;

    @Autowired
    public MobileAppRiskEstimateController(RiskEstimateService riskEstimateService) {
        this.riskEstimateService = riskEstimateService;
    }

    @RequestMapping(value = "/risk-estimate", method = RequestMethod.POST)
    @ApiOperation("提交测评")
    public BaseResponseDto<RiskEstimateResponseDto> estimate(@Valid @RequestBody RiskEstimateRequestDto requestDto) {
        Estimate estimate = riskEstimateService.estimate(LoginUserInfo.getLoginName(), requestDto.getAnswers());

        if (estimate == null) {
            return new BaseResponseDto<>(ReturnMessage.RISK_ESTIMATE_FAILED);
        }

        BaseResponseDto<RiskEstimateResponseDto> responseDto = new BaseResponseDto<>(ReturnMessage.SUCCESS);
        responseDto.setData(new RiskEstimateResponseDto(estimate));
        return responseDto;
    }
}
