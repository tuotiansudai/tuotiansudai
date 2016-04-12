package com.ttsd.api.service.impl;

import com.esoft.archer.banner.model.BannerPicture;
import com.ttsd.api.dao.MobileAppBannerDao;
import com.ttsd.api.dto.BannerPictureResponseDataDto;
import com.ttsd.api.dto.BannerResponseDataDto;
import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.ReturnMessage;
import com.ttsd.api.service.MobileAppBannerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service(value = "MobileAppBannerServiceImpl")
public class MobileAppBannerServiceImpl implements MobileAppBannerService {

    @Resource(name = "MobileAppBannerDaoImpl")
    private MobileAppBannerDao mobileAppBannerDao;

    @Value("${domain}")
    private String domainName;

    @Override
    public BaseResponseDto getAppBanner() {

        List<BannerPicture> bannerPictureList = mobileAppBannerDao.getAppBanner();
        List<BannerPictureResponseDataDto> bannerPictureDtoList = new ArrayList<>();
        for(BannerPicture bannerPicture : bannerPictureList) {
            bannerPictureDtoList.add(convertPicture2DTO(bannerPicture));
        }

        BannerResponseDataDto bannerResponseDataDto = new BannerResponseDataDto();
        bannerResponseDataDto.setPictures(bannerPictureDtoList);

        BaseResponseDto baseDto = new BaseResponseDto();
        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage( ReturnMessage.SUCCESS.getMsg());
        baseDto.setData(bannerResponseDataDto);
        return baseDto;
    }


    private BannerPictureResponseDataDto convertPicture2DTO(BannerPicture bannerPicture){
        BannerPictureResponseDataDto pictureDto = new BannerPictureResponseDataDto();

        pictureDto.setPictureId(bannerPicture.getId());
        pictureDto.setPicture(domainName + bannerPicture.getPicture());
        pictureDto.setSeqNum(bannerPicture.getSeqNum());
        pictureDto.setTitle(bannerPicture.getTitle());
        pictureDto.setUrl(bannerPicture.getUrl());
        pictureDto.setNoticeId(bannerPicture.getUrl());

        return pictureDto;

    }

}
