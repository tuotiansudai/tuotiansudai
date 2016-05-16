package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.UserBillDetailListRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppUserBillListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppUserBillListController extends MobileAppBaseController {
    @Autowired
    private MobileAppUserBillListService mobileAppUserBillListService;

    @RequestMapping(value = "/get/userbills", method = RequestMethod.POST)
    public BaseResponseDto queryFundManagement(@RequestBody UserBillDetailListRequestDto userBillDetailListRequestDto) {
        userBillDetailListRequestDto.setUserId(getLoginName());
        userBillDetailListRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppUserBillListService.queryUserBillList(userBillDetailListRequestDto);
    }

}
