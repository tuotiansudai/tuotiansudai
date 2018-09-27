package com.tuotiansudai.api.service.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.MediaArticleListResponseDataDto;
import com.tuotiansudai.api.dto.MediaArticleResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppMediaCenterService;
import com.tuotiansudai.repository.mapper.LicaiquanArticleMapper;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.LicaiquanArticleModel;
import com.tuotiansudai.repository.model.SubArticleSectionType;
import com.tuotiansudai.service.LiCaiQuanArticleService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileAppMediaCenterServiceImpl implements MobileAppMediaCenterService {

    @Value("${common.static.server}")
    private String domainName;

    @Autowired
    private LicaiquanArticleMapper licaiquanArticleMapper;

    @Autowired
    private LiCaiQuanArticleService liCaiQuanArticleService;


    @Override
    public BaseResponseDto<MediaArticleListResponseDataDto> obtainCarouselArticle() {
        List<LicaiquanArticleModel> liCaiQuanArticleModels = licaiquanArticleMapper.findCarouselArticle();
        List<MediaArticleResponseDataDto> records = Lists.newArrayList();
        MediaArticleListResponseDataDto mediaArticleListResponseDataDto = new MediaArticleListResponseDataDto();
        if (CollectionUtils.isNotEmpty(liCaiQuanArticleModels)) {
            records = convertModel2Dto(liCaiQuanArticleModels);
        }

        mediaArticleListResponseDataDto.setTotalCount(liCaiQuanArticleModels == null ? 0 : liCaiQuanArticleModels.size());
        mediaArticleListResponseDataDto.setIndex(1);
        mediaArticleListResponseDataDto.setPageSize(10);
        mediaArticleListResponseDataDto.setArticleList(records);

        BaseResponseDto<MediaArticleListResponseDataDto> baseResponseDto = new BaseResponseDto<>();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(mediaArticleListResponseDataDto);
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto<MediaArticleListResponseDataDto> obtainArticleList(ArticleSectionType articleSectionType, SubArticleSectionType subSection,int index, int pageSize) {
        int count = licaiquanArticleMapper.findCountArticleByArticleSectionType(articleSectionType,subSection);
        List<MediaArticleResponseDataDto> records = Lists.newArrayList();
        List<LicaiquanArticleModel> liCaiQuanArticleModels = licaiquanArticleMapper.findArticleByArticleSectionType(articleSectionType, subSection,(index - 1) * pageSize, pageSize);
        if (CollectionUtils.isNotEmpty(liCaiQuanArticleModels)) {
            records = convertModel2Dto(liCaiQuanArticleModels);
        }

        MediaArticleListResponseDataDto mediaArticleListResponseDataDto = new MediaArticleListResponseDataDto();
        mediaArticleListResponseDataDto.setTotalCount(count);
        mediaArticleListResponseDataDto.setIndex(index);
        mediaArticleListResponseDataDto.setPageSize(pageSize);
        mediaArticleListResponseDataDto.setArticleList(records);
        BaseResponseDto<MediaArticleListResponseDataDto> baseResponseDto = new BaseResponseDto();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(mediaArticleListResponseDataDto);
        return baseResponseDto;
    }

    public List<MediaArticleResponseDataDto> convertModel2Dto(List<LicaiquanArticleModel> liCaiQuanArticleModels) {

        return Lists.transform(liCaiQuanArticleModels, new Function<LicaiquanArticleModel, MediaArticleResponseDataDto>() {
            @Override
            public MediaArticleResponseDataDto apply(LicaiquanArticleModel liCaiQuanArticleModel) {

                MediaArticleResponseDataDto mediaArticleResponseDataDto = new MediaArticleResponseDataDto(liCaiQuanArticleModel);
                mediaArticleResponseDataDto.setShowPicture(domainName + liCaiQuanArticleModel.getShowPicture());
                mediaArticleResponseDataDto.setThumbPicture(domainName + liCaiQuanArticleModel.getThumb());
                mediaArticleResponseDataDto.setLikeCount(liCaiQuanArticleService.getLikeCount(liCaiQuanArticleModel.getId()));
                mediaArticleResponseDataDto.setReadCount(liCaiQuanArticleService.getReadCount(liCaiQuanArticleModel.getId()));
                return mediaArticleResponseDataDto;
            }
        });
    }

    @Override
    public BaseResponseDto<MediaArticleResponseDataDto> obtainArticleContent(long articleId) {
        LicaiquanArticleModel liCaiQuanArticleModel = licaiquanArticleMapper.findArticleById(articleId);
        MediaArticleResponseDataDto mediaArticleResponseDataDto = new MediaArticleResponseDataDto(liCaiQuanArticleModel);
        mediaArticleResponseDataDto.setShowPicture(domainName + liCaiQuanArticleModel.getShowPicture());
        mediaArticleResponseDataDto.setThumbPicture(domainName + liCaiQuanArticleModel.getThumb());
        mediaArticleResponseDataDto.setLikeCount(liCaiQuanArticleService.getLikeCount(liCaiQuanArticleModel.getId()));
        mediaArticleResponseDataDto.setReadCount(liCaiQuanArticleService.getReadCount(liCaiQuanArticleModel.getId()));
        BaseResponseDto<MediaArticleResponseDataDto> baseResponseDto = new BaseResponseDto<>();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(mediaArticleResponseDataDto);
        return baseResponseDto;
    }

}
