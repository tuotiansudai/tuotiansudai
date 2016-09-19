package com.tuotiansudai.api.controller.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.MembershipPerceptionRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppMembershipPerceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppMembershipPerceptionController extends MobileAppBaseController {

    @Autowired
    private MobileAppMembershipPerceptionService mobileAppMembershipPerceptionService;

    @RequestMapping(value = "/membership-perception", method = RequestMethod.POST)
    public BaseResponseDto getMembershipPerception(@RequestBody MembershipPerceptionRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppMembershipPerceptionService.getMembershipPerception(requestDto);
    }
}
