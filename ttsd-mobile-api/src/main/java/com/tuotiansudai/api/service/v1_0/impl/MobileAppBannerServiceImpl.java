package com.tuotiansudai.api.service.v1_0.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.mapper.BannerMapper;
import com.tuotiansudai.activity.repository.model.BannerModel;
import com.tuotiansudai.api.dto.v1_0.BannerPictureResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BannerResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppBannerService;
import com.tuotiansudai.repository.model.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class MobileAppBannerServiceImpl implements MobileAppBannerService {

    private final static String BANNER_URL_TEMPLATE = "{0}?source=app";

    @Autowired
    private BannerMapper bannerMapper;

    @Value("${web.server}")
    private String domainName;

    @Value("${common.static.server}")
    private String bannerServer;

    @Override
    public BaseResponseDto<BannerResponseDataDto> generateBannerList(String loginName, Source source) {
        boolean isAuthenticated = !Strings.isNullOrEmpty(loginName);
        List<BannerModel> bannerModelList = bannerMapper.findBannerIsAuthenticatedOrderByOrder(isAuthenticated, source, new Date());
        BannerResponseDataDto bannerResponseDataDto = new BannerResponseDataDto();
        List<BannerPictureResponseDataDto> pictures = Lists.newArrayList();
        bannerResponseDataDto.setPictures(pictures);
        for (BannerModel bannerModel : bannerModelList) {
            pictures.add(new BannerPictureResponseDataDto(bannerModel.getTitle(), MessageFormat.format(BANNER_URL_TEMPLATE, bannerModel.getAppUrl().equals("") ? bannerModel.getJumpToLink() : bannerModel.getAppUrl()), bannerModel.getSharedUrl(), bannerModel.getOrder(), bannerServer + bannerModel.getAppImageUrl(), bannerModel.getContent(), false));
        }
        BaseResponseDto<BannerResponseDataDto> baseDto = new BaseResponseDto<>();
        baseDto.setData(bannerResponseDataDto);
        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseDto;
    }
}
