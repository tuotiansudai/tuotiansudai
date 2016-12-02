package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NodeDetailRequestDto;
import com.tuotiansudai.api.dto.v1_0.NodeDetailResponseDataDto;
import com.tuotiansudai.api.service.v1_0.MobileAppNodeDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "公告-详情")
public class MobileAppNodeDetailController extends MobileAppBaseController {

    @Autowired
    private MobileAppNodeDetailService mobileAppNodeDetailService;


    @RequestMapping(value="/get/node",method = RequestMethod.POST)
    @ApiOperation("详情")
    public BaseResponseDto<NodeDetailResponseDataDto> queryLoanDetail(@RequestBody NodeDetailRequestDto requestDto){
        return mobileAppNodeDetailService.generateNodeDetail(requestDto);
    }
}
