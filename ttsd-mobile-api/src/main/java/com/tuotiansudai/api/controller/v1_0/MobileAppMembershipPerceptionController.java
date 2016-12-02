package com.tuotiansudai.api.controller.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.MembershipPerceptionRequestDto;
import com.tuotiansudai.api.dto.v1_0.MembershipPerceptionResponseDataDto;
import com.tuotiansudai.api.service.v1_0.MobileAppMembershipPerceptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "会员感知")
public class MobileAppMembershipPerceptionController extends MobileAppBaseController {

    @Autowired
    private MobileAppMembershipPerceptionService mobileAppMembershipPerceptionService;

    @RequestMapping(value = "/membership-perception", method = RequestMethod.POST)
    @ApiOperation("查询会员感知提示")
    public BaseResponseDto<MembershipPerceptionResponseDataDto> getMembershipPerception(@RequestBody MembershipPerceptionRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppMembershipPerceptionService.getMembershipPerception(requestDto);
    }
}
