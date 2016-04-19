package com.tuotiansudai.api.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.controller.MobileAppBannerController;
import com.tuotiansudai.api.dto.BannerPictureResponseDataDto;
import com.tuotiansudai.api.dto.BannerResponseDataDto;
import com.tuotiansudai.api.service.impl.MobileAppBannerServiceImpl;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BannerUtils {

    private static Logger logger = Logger.getLogger(MobileAppBannerServiceImpl.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static List<BannerPictureResponseDataDto> banners = null;

    private static Map<String,List<BannerPictureResponseDataDto>> jsonFileMap = new HashMap<>();

    public static BannerResponseDataDto getLatestBannerInfo(String jsonName,String domainName,String staticDomainName) {
        if (!jsonName.equals("")) {
            banners = loadPictureListFromConfigFile(jsonName);
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

    private static List<BannerPictureResponseDataDto> loadPictureListFromConfigFile(String jsonName) {
        if(jsonFileMap.get(jsonName) != null){
            return jsonFileMap.get(jsonName);
        }

        try {
            InputStream is = MobileAppBannerController.class.getClassLoader()
                    .getResourceAsStream(jsonName);

            List<BannerPictureResponseDataDto> bannerPictureResponseDataDto =  objectMapper.readValue(is, new TypeReference<List<BannerPictureResponseDataDto>>() {});
            jsonFileMap.put(jsonName,bannerPictureResponseDataDto);
            return bannerPictureResponseDataDto;
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return Lists.newArrayList();
    }
}
