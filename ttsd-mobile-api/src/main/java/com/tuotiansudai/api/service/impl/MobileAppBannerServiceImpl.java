package com.tuotiansudai.api.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.controller.MobileAppBannerController;
import com.tuotiansudai.api.dto.BannerPictureResponseDataDto;
import com.tuotiansudai.api.dto.BannerResponseDataDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppBannerService;
import com.tuotiansudai.api.util.BannerUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MobileAppBannerServiceImpl implements MobileAppBannerService {

    static Logger logger = Logger.getLogger(MobileAppBannerServiceImpl.class);

    private static final String BANNER_CONFIG_FILE = "banner.json";

    @Value("${web.server}")
    private String domainName;

    @Value("${web.static.server}")
    private String staticDomainName;

    @Override
    public BaseResponseDto<BannerResponseDataDto> generateBannerList() {
        BaseResponseDto<BannerResponseDataDto> baseDto = new BaseResponseDto<>();
        baseDto.setData(BannerUtils.getLatestBannerInfo(BANNER_CONFIG_FILE, domainName, staticDomainName));
        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseDto;
    }
}
