package com.tuotiansudai.api.controller.v1_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.MembershipRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppMembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppMembershipController extends MobileAppBaseController {

    @Autowired
    private MobileAppMembershipService mobileAppMembershipService;

    @RequestMapping(value = "/get/membership-experience-bill", method = RequestMethod.POST)
    public BaseResponseDto getMembershipExperienceBill(@RequestBody MembershipRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppMembershipService.getMembershipExperienceBill(requestDto);
    }
}
