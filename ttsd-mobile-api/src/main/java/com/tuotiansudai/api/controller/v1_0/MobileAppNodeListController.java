package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NodeListRequestDto;
import com.tuotiansudai.api.dto.v1_0.NodeListResponseDataDto;
import com.tuotiansudai.api.service.v1_0.MobileAppNodeListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "公告-列表")
public class MobileAppNodeListController extends MobileAppBaseController {
    @Autowired
    private MobileAppNodeListService mobileAppNodeListService;

    @RequestMapping(value = "/get/nodes", method = RequestMethod.POST)
    @ApiOperation("列表")
    public BaseResponseDto<NodeListResponseDataDto> queryLoanList(@RequestBody NodeListRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppNodeListService.generateNodeList(requestDto);
    }
}
