package com.tuotiansudai.api.service.impl;


import com.tuotiansudai.api.dto.BannerResponseDataDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppReferrerBannerService;
import com.tuotiansudai.api.util.BannerUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MobileAppReferrerBannerServiceImpl implements MobileAppReferrerBannerService {

    @Value("${web.server}")
    private String domainName;

    @Value("${web.static.server}")
    private String staticDomainName;

    private static final String REFERRER_STATISTICS_FILE = "referrer.json";

    @Override
    public BaseResponseDto<BannerResponseDataDto> generateReferrerBannerList() {
        BaseResponseDto<BannerResponseDataDto> baseDto = new BaseResponseDto<>();
        baseDto.setData(BannerUtils.getLatestBannerInfo(REFERRER_STATISTICS_FILE, domainName, staticDomainName));
        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseDto;
    }
}
