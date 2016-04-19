package com.tuotiansudai.api.service.impl;

import com.tuotiansudai.api.dto.BannerResponseDataDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppBannerService;
import com.tuotiansudai.api.util.BannerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppBannerServiceImpl implements MobileAppBannerService {

    private static final String BANNER_CONFIG_FILE = "banner.json";
    @Autowired
    private BannerUtils bannerUtils;

    @Override
    public BaseResponseDto<BannerResponseDataDto> generateBannerList() {
        BaseResponseDto<BannerResponseDataDto> baseDto = new BaseResponseDto<>();
        baseDto.setData(bannerUtils.getLatestBannerInfo(BANNER_CONFIG_FILE));
        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseDto;
    }
}
