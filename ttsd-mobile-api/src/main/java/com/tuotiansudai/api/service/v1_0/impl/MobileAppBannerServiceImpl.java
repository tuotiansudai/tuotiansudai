package com.tuotiansudai.api.service.v1_0.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppBannerService;
import com.tuotiansudai.api.util.BannerUtils;
import com.tuotiansudai.repository.mapper.BannerMapper;
import com.tuotiansudai.repository.model.BannerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileAppBannerServiceImpl implements MobileAppBannerService {

    @Autowired
    private BannerMapper bannerMapper;

    @Value("${web.server}")
    private String domainName;

    @Value("${web.static.server}")
    private String staticDomainName;

    @Override
    public BaseResponseDto<BannerResponseDataDto> generateBannerList(BaseParam baseParam) {
        List<BannerModel> bannerModelList = bannerMapper.findBannerIsAuthenticatedOrderByOrder(Strings.isNullOrEmpty(baseParam.getUserId()) ? false : true,baseParam.getPlatform().toUpperCase());
        BannerResponseDataDto bannerResponseDataDto = new BannerResponseDataDto();
        List<BannerPictureResponseDataDto> pictures = Lists.newArrayList();
        bannerResponseDataDto.setPictures(pictures);
        for(BannerModel bannerModel : bannerModelList){
                pictures.add(new BannerPictureResponseDataDto("",bannerModel.getTitle(),domainName + bannerModel.getUrl(),domainName + bannerModel.getSharedUrl(),bannerModel.getOrder(),staticDomainName + "/" + bannerModel.getAppImageUrl(),"",bannerModel.getContent(),false));
        }
        BaseResponseDto<BannerResponseDataDto> baseDto = new BaseResponseDto<>();
        baseDto.setData(bannerResponseDataDto);
        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseDto;
    }
}
