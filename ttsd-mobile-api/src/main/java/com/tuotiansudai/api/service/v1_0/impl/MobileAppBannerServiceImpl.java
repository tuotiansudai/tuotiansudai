package com.tuotiansudai.api.service.v1_0.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppBannerService;
import com.tuotiansudai.api.util.BannerUtils;
import com.tuotiansudai.repository.mapper.BannerMapper;
import com.tuotiansudai.repository.model.BannerModel;
import com.tuotiansudai.repository.model.Source;
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

    @Value("${web.banner.server}")
    private String bannerName;

    @Override
    public BaseResponseDto<BannerResponseDataDto> generateBannerList(BaseParam baseParam) {
        boolean s = Strings.isNullOrEmpty(baseParam.getUserId()) ? false : true;
        Source sdd = baseParam.getPlatform().toUpperCase().equals(Source.ANDROID.name()) ? Source.ANDROID : Source.IOS;
        List<BannerModel> bannerModelList = bannerMapper.findBannerIsAuthenticatedOrderByOrder(Strings.isNullOrEmpty(baseParam.getUserId()) ? false : true,baseParam.getPlatform().toUpperCase().equals(Source.ANDROID.name()) ? Source.ANDROID : Source.IOS);
        BannerResponseDataDto bannerResponseDataDto = new BannerResponseDataDto();
        List<BannerPictureResponseDataDto> pictures = Lists.newArrayList();
        bannerResponseDataDto.setPictures(pictures);
        for(BannerModel bannerModel : bannerModelList){
                pictures.add(new BannerPictureResponseDataDto(bannerModel.getTitle(),bannerModel.getUrl(),bannerModel.getSharedUrl(),bannerModel.getOrder(),bannerName + "/" + bannerModel.getAppImageUrl(),bannerModel.getContent(),false));
        }
        BaseResponseDto<BannerResponseDataDto> baseDto = new BaseResponseDto<>();
        baseDto.setData(bannerResponseDataDto);
        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseDto;
    }
}
