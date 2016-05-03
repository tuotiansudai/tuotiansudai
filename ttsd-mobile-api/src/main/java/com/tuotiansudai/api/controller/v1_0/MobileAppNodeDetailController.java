package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NodeDetailRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppNodeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppNodeDetailController extends MobileAppBaseController {
    @Autowired
    private MobileAppNodeDetailService mobileAppNodeDetailService;
    @RequestMapping(value="/get/node",method = RequestMethod.POST)
    public BaseResponseDto queryLoanDetail(@RequestBody NodeDetailRequestDto requestDto){
        return mobileAppNodeDetailService.generateNodeDetail(requestDto);
    }
}
