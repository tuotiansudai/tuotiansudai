package com.tuotiansudai.point.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.point.controller.PointShopController;
import com.tuotiansudai.point.repository.dto.PrizeResponseDataDto;
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

    @Value("${point.static.server}")
    private String staticDomainName;

    private static Logger logger = Logger.getLogger(PrizeImageUtils.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static Map<String, List<PrizeResponseDataDto>> jsonFileMap = new HashMap<>();

    public List<PrizeResponseDataDto> getPrizeImageInfo(String jsonName) {
        List<PrizeResponseDataDto> prizeImages = null;
        if (!jsonName.equals("")) {
            prizeImages = loadPictureListFromConfigFile(jsonName);
            for (PrizeResponseDataDto prizeImage : prizeImages) {
                if (!Strings.isNullOrEmpty(prizeImage.getImageUrl())) {
                    prizeImage.setImageUrl(prizeImage.getImageUrl().replaceFirst("\\{static\\}", staticDomainName));
                }
            }
        }
        return prizeImages;
    }

    private static List<PrizeResponseDataDto> loadPictureListFromConfigFile(String jsonName) {
        if (jsonFileMap.get(jsonName) != null) {
            return jsonFileMap.get(jsonName);
        }
        try {
            InputStream is = PointShopController.class.getClassLoader().getResourceAsStream(jsonName);
            List<PrizeResponseDataDto> prizeResponseDataDtoList = objectMapper.readValue(is, new TypeReference<List<PrizeResponseDataDto>>() {
            });
            jsonFileMap.put(jsonName, prizeResponseDataDtoList);
            return prizeResponseDataDtoList;
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return Lists.newArrayList();
    }
}
