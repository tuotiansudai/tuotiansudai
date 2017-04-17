package com.tuotiansudai.api.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.controller.v1_0.MobileAppPrizeImageController;
import com.tuotiansudai.api.dto.v1_0.PrizeImageListResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.PrizeImageResponseDataDto;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppPrizeImageListServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PrizeImageUtils {

    @Value("${common.static.server}")
    private String staticDomainName;

    private static Logger logger = Logger.getLogger(MobileAppPrizeImageListServiceImpl.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static Map<String, List<PrizeImageResponseDataDto>> jsonFileMap = new HashMap<>();

    public PrizeImageListResponseDataDto getPrizeImageInfo(String jsonName) {
        List<PrizeImageResponseDataDto> prizeImages = null;
        if (!jsonName.equals("")) {
            prizeImages = loadPictureListFromConfigFile(jsonName);
            for (PrizeImageResponseDataDto prizeImage : prizeImages) {
                if (!Strings.isNullOrEmpty(prizeImage.getImageUrl())) {
                    prizeImage.setImageUrl(prizeImage.getImageUrl().replaceFirst("\\{static\\}", staticDomainName));
                }
            }
        }
        PrizeImageListResponseDataDto dataDto = new PrizeImageListResponseDataDto();
        dataDto.setPrizeImageList(prizeImages);
        return dataDto;
    }

    private static List<PrizeImageResponseDataDto> loadPictureListFromConfigFile(String jsonName) {
        if (jsonFileMap.get(jsonName) != null) {
            return jsonFileMap.get(jsonName);
        }
        try {
            InputStream is = MobileAppPrizeImageController.class.getClassLoader().getResourceAsStream(jsonName);
            List<PrizeImageResponseDataDto> prizeImageResponseDataDtoList = objectMapper.readValue(is, new TypeReference<List<PrizeImageResponseDataDto>>() {
            });
            jsonFileMap.put(jsonName, prizeImageResponseDataDtoList);
            return prizeImageResponseDataDtoList;
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return Lists.newArrayList();
    }
}
