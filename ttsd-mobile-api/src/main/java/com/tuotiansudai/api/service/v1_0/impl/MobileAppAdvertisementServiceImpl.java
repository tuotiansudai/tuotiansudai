package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.AdvertisementResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppAdvertisementService;
import com.tuotiansudai.api.util.AdvertisementUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppAdvertisementServiceImpl implements MobileAppAdvertisementService {

    private static final String ADVERTISEMENT_CONFIG_FILE = "advertisement.json";
    @Autowired
    private AdvertisementUtils advertisementUtils;

    @Override
    public BaseResponseDto<AdvertisementResponseDataDto> generateAdvertisement(BaseParamDto requestDto) {
        BaseResponseDto<AdvertisementResponseDataDto> baseDto = new BaseResponseDto<>();
        baseDto.setData(advertisementUtils.getAdvertisementInfo(ADVERTISEMENT_CONFIG_FILE, requestDto));
        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseDto;
    }
}
