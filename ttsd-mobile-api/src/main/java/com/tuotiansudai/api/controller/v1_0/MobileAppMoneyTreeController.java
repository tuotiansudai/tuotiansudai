package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.MoneyTreeLeftCountResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.MoneyTreePrizeResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.MoneyTreeResultListResponseDataDto;
import com.tuotiansudai.api.service.v1_0.MobileAppMoneyTreeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "摇钱树")
public class MobileAppMoneyTreeController extends MobileAppBaseController {

    @Autowired
    private MobileAppMoneyTreeService mobileAppMoneyTreeService;

    @RequestMapping(value = "/get/money-tree-left-chance", method = RequestMethod.POST)
    @ApiOperation("剩余摇奖次数")
    public BaseResponseDto<MoneyTreeLeftCountResponseDataDto> getMoneyTreeLeftCount() {
        return mobileAppMoneyTreeService.generateLeftCount(getLoginName());
    }

    @RequestMapping(value = "/get/money-tree-all-prize-list", method = RequestMethod.POST)
    @ApiOperation("运气榜")
    public BaseResponseDto<MoneyTreeResultListResponseDataDto> getMoneyTreeAllPrizeList() {
        return mobileAppMoneyTreeService.generatePrizeListTop10();
    }

    @RequestMapping(value = "/get/money-tree-my-prize-list", method = RequestMethod.POST)
    @ApiOperation("我的中奖记录")
    public BaseResponseDto<MoneyTreeResultListResponseDataDto> getMoneyTreeMyPrizeList() {
        return mobileAppMoneyTreeService.generateMyPrizeList(getLoginName());
    }

    @RequestMapping(value = "/get/money-tree-prize", method = RequestMethod.POST)
    @ApiOperation("摇奖结果")
    public BaseResponseDto<MoneyTreePrizeResponseDataDto> getMoneyTreePrize() {
        return mobileAppMoneyTreeService.generatePrize(getLoginName());
    }

}