package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BannerPictureResponseDataDto;
import com.tuotiansudai.api.dto.BannerResponseDataDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MobileAppBannerController extends MobileAppBaseController {

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
        List<BannerPictureResponseDataDto> pictureList = new ArrayList<>();
        dataDto.setPictures(pictureList);
        //fakePictureList(pictureList);
        return dataDto;
    }

    private void fakePictureList(List<BannerPictureResponseDataDto> pictureList) {
        BannerPictureResponseDataDto pictureItem = new BannerPictureResponseDataDto();

        pictureItem.setUrl("https://tuotiansudai.com");
        pictureItem.setPictureId("");
        pictureItem.setTitle("title");
        pictureItem.setSeqNum(1);
        pictureItem.setPicture("pictureUrl");
        pictureItem.setNoticeId("noticeId");


        pictureList.add(pictureItem);
    }
}