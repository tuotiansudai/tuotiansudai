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

    private Map<String,BannerResponseDataDto> jsonFileMap = new HashMap<>();

    @Override
    public BaseResponseDto<BannerResponseDataDto> generateReferrerBannerList() {
        BaseResponseDto<BannerResponseDataDto> baseDto = new BaseResponseDto<>();

        if(jsonFileMap.get(REFERRER_STATISTICS_FILE) == null)
            jsonFileMap.put(REFERRER_STATISTICS_FILE, BannerUtils.getLatestBannerInfo(REFERRER_STATISTICS_FILE, domainName, staticDomainName));

        baseDto.setData(jsonFileMap.get(REFERRER_STATISTICS_FILE) );
        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseDto;
    }
}
