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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class MobileAppBannerServiceImpl implements MobileAppBannerService {

    static Logger logger = Logger.getLogger(MobileAppBannerServiceImpl.class);

    private String BANNER_CONFIG_FILE = "banner.json";

    private List<BannerPictureResponseDataDto> banners = null;

    @Value("${web.server}")
    private String domainName;

    @Value("${web.static.server}")
    private String staticDomainName;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public BaseResponseDto<BannerResponseDataDto> generateBannerList() {
        BaseResponseDto<BannerResponseDataDto> baseDto = new BaseResponseDto<>();

        baseDto.setData(getLatestBannerInfo());
        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseDto;
    }

    @Override
    public void setBannerConfigFile(String bannerConfigFile) {
        this.BANNER_CONFIG_FILE = bannerConfigFile;
    }


    private BannerResponseDataDto getLatestBannerInfo() {
        if (banners == null) {
            banners = loadPictureListFromConfigFile();
            for (BannerPictureResponseDataDto banner : banners) {
                if (!Strings.isNullOrEmpty(banner.getUrl())) {
                    banner.setUrl(banner.getUrl().replaceFirst("\\{web\\}", domainName));
                }

                if (!Strings.isNullOrEmpty(banner.getSharedUrl())) {
                    banner.setSharedUrl(banner.getSharedUrl().replaceFirst("\\{web\\}", domainName));
                }

                if (!Strings.isNullOrEmpty(banner.getPicture())) {
                    banner.setPicture(banner.getPicture().replaceFirst("\\{static\\}", staticDomainName));
                }
            }
        }
        BannerResponseDataDto dataDto = new BannerResponseDataDto();
        dataDto.setPictures(banners);
        return dataDto;
    }

    private List<BannerPictureResponseDataDto> loadPictureListFromConfigFile() {
        try {
            InputStream is = MobileAppBannerController.class.getClassLoader()
                    .getResourceAsStream(BANNER_CONFIG_FILE);
            return objectMapper.readValue(is, new TypeReference<List<BannerPictureResponseDataDto>>() {
            });
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return Lists.newArrayList();
    }
}
