package com.tuotiansudai.api.service.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.service.MobileAppMediaCenterService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LiCaiQuanArticleDto;
import com.tuotiansudai.repository.mapper.LicaiquanArticleMapper;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.LicaiquanArticleModel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileAppMediaCenterServiceImpl implements MobileAppMediaCenterService{
    @Autowired
    private LicaiquanArticleMapper licaiquanArticleMapper;
    @Value("${web.server}")
    private String domainName;


    @Override
    public List<LiCaiQuanArticleDto> obtainCarouselArticle() {
        List<LicaiquanArticleModel> liCaiQuanArticleModels = licaiquanArticleMapper.findCarouselArticle();
        if(CollectionUtils.isEmpty(liCaiQuanArticleModels)){
            return null;
        }

        return Lists.transform(liCaiQuanArticleModels, new Function<LicaiquanArticleModel, LiCaiQuanArticleDto>() {
            @Override
            public LiCaiQuanArticleDto apply(LicaiquanArticleModel licaiquanArticleModel) {
                LiCaiQuanArticleDto liCaiQuanArticleDto = new LiCaiQuanArticleDto(licaiquanArticleModel);
                liCaiQuanArticleDto.setShowPicture(domainName + "/" + liCaiQuanArticleDto.getShowPicture());
                liCaiQuanArticleDto.setThumbPicture(domainName + "/" + liCaiQuanArticleDto.getThumbPicture());
                return liCaiQuanArticleDto;
            }
        });
    }

    @Override
    public BaseDto<BasePaginationDataDto> obtainArticleList(ArticleSectionType articleSectionType,int index,int pageSize) {
        int count = licaiquanArticleMapper.findCountArticleByArticleSectionType(articleSectionType);
        List<LiCaiQuanArticleDto> records = Lists.newArrayList();
        List<LicaiquanArticleModel> liCaiQuanArticleModels  = licaiquanArticleMapper.findArticleByArticleSectionType(articleSectionType, index, pageSize);
        if(CollectionUtils.isNotEmpty(liCaiQuanArticleModels)){
            records = Lists.transform(liCaiQuanArticleModels, new Function<LicaiquanArticleModel, LiCaiQuanArticleDto>() {
                @Override
                public LiCaiQuanArticleDto apply(LicaiquanArticleModel liCaiQuanArticleModel) {
                    LiCaiQuanArticleDto liCaiQuanArticleDto = new LiCaiQuanArticleDto(liCaiQuanArticleModel);
                    liCaiQuanArticleDto.setShowPicture(domainName + "/" + liCaiQuanArticleDto.getShowPicture());
                    liCaiQuanArticleDto.setThumbPicture(domainName + "/" + liCaiQuanArticleDto.getThumbPicture());
                    return liCaiQuanArticleDto;
                }
            });

        }
        BaseDto<BasePaginationDataDto> baseDto = new BaseDto<>();
        BasePaginationDataDto<LiCaiQuanArticleDto> dataDto = new BasePaginationDataDto<>(index, pageSize, count, records);
        baseDto.setData(dataDto);
        dataDto.setStatus(true);
        return baseDto;
    }

    @Override
    public LiCaiQuanArticleDto obtainArticleContent(long articleId) {
        LicaiquanArticleModel liCaiQuanArticleModel = licaiquanArticleMapper.findArticleById(articleId);
        LiCaiQuanArticleDto liCaiQuanArticleDto = new LiCaiQuanArticleDto(liCaiQuanArticleModel);
        liCaiQuanArticleDto.setShowPicture(domainName + "/" + liCaiQuanArticleDto.getShowPicture());
        liCaiQuanArticleDto.setThumbPicture(domainName + "/" + liCaiQuanArticleDto.getThumbPicture());
        return liCaiQuanArticleDto;
    }


}
