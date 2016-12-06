package com.tuotiansudai.api.controller.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.MembershipRequestDto;
import com.tuotiansudai.api.dto.v1_0.MembershipResponseDataDto;
import com.tuotiansudai.api.service.v1_0.MobileAppMembershipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "会员成长值列表")
public class MobileAppMembershipController extends MobileAppBaseController {

    @Autowired
    private MobileAppMembershipService mobileAppMembershipService;

    @RequestMapping(value = "/get/membership-experience-bill", method = RequestMethod.POST)
    @ApiOperation("会员成长值列表")
    public BaseResponseDto<MembershipResponseDataDto> getMembershipExperienceBill(@RequestBody MembershipRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppMembershipService.getMembershipExperienceBill(requestDto);
    }
}
