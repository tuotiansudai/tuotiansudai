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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
            String getMethod = "";
            AdvertisementPictureResponseDataDto advertisementPictureResponseDataDto = advertisements.get(randomInt);
            try {
                Class clazz = advertisementPictureResponseDataDto.getClass();
                getMethod = "getPicture" + requestDto.getBaseParam().getScreenW().trim() + requestDto.getBaseParam().getScreenH().trim().trim();
                Method method = clazz.getDeclaredMethod(getMethod);
                if("iOS".equals(requestDto.getBaseParam().getPlatform())){
                    pictureUrl = method.invoke(advertisementPictureResponseDataDto, new Object[]{}).toString().replaceFirst("\\{static\\}", staticDomainName);
                }
                else if("android".equals(requestDto.getBaseParam().getPlatform())){
                    pictureUrl = advertisementPictureResponseDataDto.getPicture7201280().replaceFirst("\\{static\\}", staticDomainName);;
                }
            }
            catch (NoSuchMethodException e) {
                logger.debug("AdvertisementUtils NoSuchMethod: " + getMethod);
            }
            catch (IllegalAccessException e1) {
                logger.debug("AdvertisementUtils IllegalAccess: " + e1.getMessage());
            }
            catch (InvocationTargetException e2) {
                logger.debug("AdvertisementUtils InvocationTarget: " + e2.getMessage());
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
