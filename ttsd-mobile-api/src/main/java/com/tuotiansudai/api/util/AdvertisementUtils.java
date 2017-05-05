package com.tuotiansudai.api.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.controller.v1_0.MobileAppAdvertisementController;
import com.tuotiansudai.api.dto.v1_0.AdvertisementPictureResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.AdvertisementResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppAdvertisementServiceImpl;
import com.tuotiansudai.repository.model.Source;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AdvertisementUtils {

    @Value("${common.static.server}")
    private String staticDomainName;

    private static Logger logger = Logger.getLogger(MobileAppAdvertisementServiceImpl.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static Map<String,List<AdvertisementPictureResponseDataDto>> jsonFileMap = new HashMap<>();

    public AdvertisementResponseDataDto getAdvertisementInfo(String jsonName, BaseParamDto requestDto) {
        List<AdvertisementPictureResponseDataDto> advertisements = null;
        String pictureUrl = "";
        String title = "";
        String linkedUrl = "";
        String sharedUrl = "";
        String content = "";
        if (!jsonName.equals("")) {
            advertisements = loadPictureListFromConfigFile(jsonName);
            int randomInt = (int)(0 + Math.random()*(advertisements.size() - 1 + 1));
            String getMethod = "";
            AdvertisementPictureResponseDataDto advertisementPictureResponseDataDto = advertisements.get(randomInt);
            if(Source.IOS == Source.valueOf(requestDto.getBaseParam().getPlatform().toUpperCase())) {
                try {
                    Class clazz = advertisementPictureResponseDataDto.getClass();
                    getMethod = "getPicture" + requestDto.getBaseParam().getScreenW().trim() + requestDto.getBaseParam().getScreenH().trim().trim();
                    Method method = clazz.getDeclaredMethod(getMethod);
                    pictureUrl = method.invoke(advertisementPictureResponseDataDto, new Object[]{}).toString().replaceFirst("\\{static\\}", staticDomainName);
                } catch (NoSuchMethodException e) {
                    logger.info("AdvertisementUtils NoSuchMethod: " + getMethod);
                } catch (IllegalAccessException e1) {
                    logger.info("AdvertisementUtils IllegalAccess: " + e1.getMessage());
                } catch (InvocationTargetException e2) {
                    logger.info("AdvertisementUtils InvocationTarget: " + e2.getMessage());
                }
            }
            else if(Source.ANDROID == Source.valueOf(requestDto.getBaseParam().getPlatform().toUpperCase())){
                pictureUrl = advertisementPictureResponseDataDto.getPicture7201280().replaceFirst("\\{static\\}", staticDomainName);;
            }
            title = advertisementPictureResponseDataDto.getTitle();
            linkedUrl = advertisementPictureResponseDataDto.getLinkedUrl();
            sharedUrl = advertisementPictureResponseDataDto.getSharedUrl();
            content = advertisementPictureResponseDataDto.getContent();
        }
        AdvertisementResponseDataDto dataDto = new AdvertisementResponseDataDto();
        dataDto.setUrl(pictureUrl);
        dataDto.setTitle(title);
        dataDto.setLinkedUrl(linkedUrl);
        dataDto.setSharedUrl(sharedUrl);
        dataDto.setContent(content);
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
