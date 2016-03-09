package com.tuotiansudai.api.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.controller.MobileAppBannerController;
import com.tuotiansudai.api.dto.BannerPictureResponseDataDto;
import com.tuotiansudai.api.dto.BannerResponseDataDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppBannerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class MobileAppBannerServiceImpl implements MobileAppBannerService {
    private static final String BANNER_CONFIG_FILE = "banner.json";
    private List<BannerPictureResponseDataDto> banners = null;

    @Value("${web.server}")
    private String domainName;

    @Value("${web.static.server}")
    private String staticDomainName;

    @Override
    public BaseResponseDto generateBannerList() {
        BaseResponseDto baseDto = new BaseResponseDto();

        baseDto.setData(getLastestBannerInfo());
        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseDto;
    }

    private BannerResponseDataDto getLastestBannerInfo() {
        if (banners == null) {
            List rawData = loadPictureListFromConfigFile();
            banners = convertMapToDto(rawData);
        }
        BannerResponseDataDto dataDto = new BannerResponseDataDto();
        dataDto.setPictures(banners);
        return dataDto;
    }

    private List loadPictureListFromConfigFile() {
        try {
            InputStream is = MobileAppBannerController.class.getClassLoader()
                    .getResourceAsStream(BANNER_CONFIG_FILE);
            ObjectMapper objectMapper = new ObjectMapper();
            List rawData = objectMapper.readValue(is, List.class);
            return rawData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<BannerPictureResponseDataDto> convertMapToDto(List rawData) {
        return Lists.transform(rawData, new Function<Object, BannerPictureResponseDataDto>() {
            @Override
            public BannerPictureResponseDataDto apply(Object input) {
                Map<String, String> inputData = (Map<String, String>) input;
                BannerPictureResponseDataDto dataDto = new BannerPictureResponseDataDto();
                dataDto.setNoticeId(inputData.get("noticeId"));
                dataDto.setSeqNum(Integer.valueOf(String.valueOf(inputData.get("seqNum"))));
                dataDto.setPictureId(inputData.get("pictureId"));
                dataDto.setTitle(inputData.get("title"));
                dataDto.setContent(inputData.get("content"));
                String bannerUrl = inputData.get("url");
                if (StringUtils.isNotEmpty(bannerUrl)) {
                    bannerUrl = bannerUrl.replaceFirst("\\{web\\}", domainName);
                    dataDto.setUrl(bannerUrl);
                } else {
                    dataDto.setUrl("");
                }
                String pictureUrl = inputData.get("picture");
                if (StringUtils.isNotEmpty(pictureUrl)) {
                    pictureUrl = pictureUrl.replaceFirst("\\{static\\}", staticDomainName);
                    dataDto.setPicture(pictureUrl);
                } else {
                    dataDto.setPicture("");
                }
                return dataDto;
            }
        });
    }
}
