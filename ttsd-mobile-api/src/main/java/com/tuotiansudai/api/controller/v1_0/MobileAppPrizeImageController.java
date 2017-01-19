package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BannerResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.PrizeImageListResponseDataDto;
import com.tuotiansudai.api.service.v1_0.MobileAppBannerService;
import com.tuotiansudai.api.service.v1_0.MobileAppPrizeImageListService;
import com.tuotiansudai.repository.model.Source;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "抽奖转盘图片列表")
public class MobileAppPrizeImageController extends MobileAppBaseController {

    @Autowired
    private MobileAppPrizeImageListService mobileAppPrizeImageListService;

    @RequestMapping(value = "/get/prize-images", method = RequestMethod.POST)
    @ApiOperation("获取抽奖转盘图片列表")
    public BaseResponseDto<PrizeImageListResponseDataDto> getAppPrizeImageList(@RequestBody BaseParamDto baseParamDto) {
        return mobileAppPrizeImageListService.generatePrizeImageList();
    }

}