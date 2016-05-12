package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NodeListRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppNodeListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppNodeListController extends MobileAppBaseController {
    @Autowired
    private MobileAppNodeListService mobileAppNodeListService;

    @RequestMapping(value = "/get/nodes", method = RequestMethod.POST)
    public BaseResponseDto queryLoanList(@RequestBody NodeListRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppNodeListService.generateNodeList(requestDto);
    }
}
