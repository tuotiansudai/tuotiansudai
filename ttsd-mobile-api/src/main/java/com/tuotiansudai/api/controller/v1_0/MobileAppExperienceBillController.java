package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ExperienceBillRequestDto;
import com.tuotiansudai.api.dto.v1_0.ExperienceBillResponseDataDto;
import com.tuotiansudai.api.service.v1_0.MobileAppExperienceBillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "体验金明细")
public class MobileAppExperienceBillController extends MobileAppBaseController {

    @Autowired
    private MobileAppExperienceBillService mobileAppExperienceBillService;

    @RequestMapping(value = "/get/experience-bill", method = RequestMethod.POST)
    @ApiOperation("体验金明细")
    public BaseResponseDto<ExperienceBillResponseDataDto> getExperienceBillData(@RequestBody ExperienceBillRequestDto experienceBillRequestDto) {
        experienceBillRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppExperienceBillService.queryExperienceBillList(experienceBillRequestDto);
    }

}
