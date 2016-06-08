package com.tuotiansudai.api.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.controller.v1_0.MobileAppAdvertisementController;
import com.tuotiansudai.api.controller.v1_0.MobileAppBannerController;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppAdvertisementServiceImpl;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppBannerServiceImpl;
import com.tuotiansudai.util.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AdvertisementUtils {

    @Value("${web.static.server}")
    private String staticDomainName;

    private static Logger logger = Logger.getLogger(MobileAppAdvertisementServiceImpl.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static Map<String,List<AdvertisementPictureResponseDataDto>> jsonFileMap = new HashMap<>();

    public AdvertisementResponseDataDto getAdvertisementInfo(String jsonName, BaseParamDto requestDto) {
        List<AdvertisementPictureResponseDataDto> advertisements = null;
        String pictureUrl = "";
        if (!jsonName.equals("")) {
            advertisements = loadPictureListFromConfigFile(jsonName);
            int randomInt = (int)(0 + Math.random()*(advertisements.size() - 1 + 1));
            if("iOS".equals(requestDto.getBaseParam().getPlatform())){
                if("640".equals(requestDto.getBaseParam().getScreenW()) && "960".equals(requestDto.getBaseParam().getScreenH())){
                    if (!Strings.isNullOrEmpty(advertisements.get(randomInt).getPicture640960())) {
                        pictureUrl = advertisements.get(randomInt).getPicture640960().replaceFirst("\\{static\\}", staticDomainName);
                    }
                }
                else if("640".equals(requestDto.getBaseParam().getScreenW()) && "1136".equals(requestDto.getBaseParam().getScreenH())){
                    if (!Strings.isNullOrEmpty(advertisements.get(randomInt).getPicture6401136())) {
                        pictureUrl = advertisements.get(randomInt).getPicture6401136().replaceFirst("\\{static\\}", staticDomainName);
                    }
                }
                else if("750".equals(requestDto.getBaseParam().getScreenW()) && "1334".equals(requestDto.getBaseParam().getScreenH())){
                    if (!Strings.isNullOrEmpty(advertisements.get(randomInt).getPicture7501334())) {
                        pictureUrl = advertisements.get(randomInt).getPicture7501334().replaceFirst("\\{static\\}", staticDomainName);
                    }
                }
                else if("1242".equals(requestDto.getBaseParam().getScreenW()) && "2208".equals(requestDto.getBaseParam().getScreenH())){
                    if (!Strings.isNullOrEmpty(advertisements.get(randomInt).getPicture12422208())) {
                        pictureUrl = advertisements.get(randomInt).getPicture12422208().replaceFirst("\\{static\\}", staticDomainName);
                    }
                }
            }
            else if("android".equals(requestDto.getBaseParam().getPlatform())){
                if (!Strings.isNullOrEmpty(advertisements.get(randomInt).getPicture7201280())) {
                    pictureUrl = advertisements.get(randomInt).getPicture7201280().replaceFirst("\\{static\\}", staticDomainName);
                }
            }
        }
        AdvertisementResponseDataDto dataDto = new AdvertisementResponseDataDto();
        dataDto.setUrl(pictureUrl);
        return dataDto;
    }

    private static List<AdvertisementPictureResponseDataDto> loadPictureListFromConfigFile(String jsonName) {
        if(jsonFileMap.get(jsonName) != null){
            return jsonFileMap.get(jsonName);
        }
        try {
            InputStream is = MobileAppAdvertisementController.class.getClassLoader().getResourceAsStream(jsonName);
            List<AdvertisementPictureResponseDataDto> advertisementPictureResponseDataDto =  objectMapper.readValue(is, new TypeReference<List<AdvertisementPictureResponseDataDto>>() {});
            jsonFileMap.put(jsonName, advertisementPictureResponseDataDto);
            return advertisementPictureResponseDataDto;
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return Lists.newArrayList();
    }
}
