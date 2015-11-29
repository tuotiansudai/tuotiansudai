package com.tuotiansudai.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.api.dto.BannerPictureResponseDataDto;
import com.tuotiansudai.api.dto.BannerResponseDataDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MobileAppBannerController extends MobileAppBaseController {
    private static final String BANNER_CONFIG_FILE = "banner.json";
    private static final List<BannerPictureResponseDataDto> BANNERS;

    static {
        BANNERS = loadPictureList();
    }

    @RequestMapping(value = "/get/banner", method = RequestMethod.POST)
    public BaseResponseDto getAppBanner() {
        BaseResponseDto baseDto = new BaseResponseDto();
        baseDto.setData(getLastestBannerInfo());
        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseDto;
    }

    private BannerResponseDataDto getLastestBannerInfo() {
        BannerResponseDataDto dataDto = new BannerResponseDataDto();
        dataDto.setPictures(BANNERS);
        return dataDto;
    }

    private static List<BannerPictureResponseDataDto> loadPictureList() {
        List<BannerPictureResponseDataDto> list = new ArrayList<>();
        try {
            InputStream is = MobileAppBannerController.class.getClassLoader()
                    .getResourceAsStream(BANNER_CONFIG_FILE);
            ObjectMapper objectMapper = new ObjectMapper();
            list = objectMapper.readValue(is, list.getClass());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}