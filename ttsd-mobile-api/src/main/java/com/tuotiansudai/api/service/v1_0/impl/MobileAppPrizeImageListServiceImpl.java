package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppAdvertisementService;
import com.tuotiansudai.api.service.v1_0.MobileAppPrizeImageListService;
import com.tuotiansudai.api.util.AdvertisementUtils;
import com.tuotiansudai.api.util.PrizeImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppPrizeImageListServiceImpl implements MobileAppPrizeImageListService {

    private static final String PRIZEIMAGE_CONFIG_FILE = "pointLotteryImages.json";
    @Autowired
    private PrizeImageUtils prizeImageUtils;

    @Override
    public BaseResponseDto<PrizeImageListResponseDataDto> generatePrizeImageList() {
        BaseResponseDto<PrizeImageListResponseDataDto> baseDto = new BaseResponseDto<>();
        baseDto.setData(prizeImageUtils.getPrizeImageInfo(PRIZEIMAGE_CONFIG_FILE));
        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseDto;
    }
}
