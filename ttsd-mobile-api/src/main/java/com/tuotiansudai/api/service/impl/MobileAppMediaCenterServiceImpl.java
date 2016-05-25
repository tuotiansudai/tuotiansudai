package com.tuotiansudai.api.service.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.ArticleListResponseDataDto;
import com.tuotiansudai.api.dto.ArticleResponseDataDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppMediaCenterService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LiCaiQuanArticleDto;
import com.tuotiansudai.repository.mapper.LicaiquanArticleMapper;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.LicaiquanArticleModel;
import com.tuotiansudai.service.LiCaiQuanArticleService;
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
    @Autowired
    private LiCaiQuanArticleService liCaiQuanArticleService;


    @Override
    public BaseResponseDto<ArticleListResponseDataDto> obtainCarouselArticle() {
        List<LicaiquanArticleModel> liCaiQuanArticleModels = licaiquanArticleMapper.findCarouselArticle();
        List<ArticleResponseDataDto> records = Lists.newArrayList();
        ArticleListResponseDataDto articleListResponseDataDto = new ArticleListResponseDataDto();
        if(CollectionUtils.isNotEmpty(liCaiQuanArticleModels)){
            records = convertModel2Dto(liCaiQuanArticleModels);
            articleListResponseDataDto.setTotalCount(liCaiQuanArticleModels.size());
            articleListResponseDataDto.setIndex(1);
            articleListResponseDataDto.setPageSize(10);
            articleListResponseDataDto.setArticleList(records);
        }

        BaseResponseDto<ArticleListResponseDataDto> baseResponseDto = new BaseResponseDto();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(articleListResponseDataDto);
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto<ArticleListResponseDataDto> obtainArticleList(ArticleSectionType articleSectionType,int index,int pageSize) {
        int count = licaiquanArticleMapper.findCountArticleByArticleSectionType(articleSectionType);
        List<ArticleResponseDataDto> records = Lists.newArrayList();
        List<LicaiquanArticleModel> liCaiQuanArticleModels  = licaiquanArticleMapper.findArticleByArticleSectionType(articleSectionType, index, pageSize);
        if(CollectionUtils.isNotEmpty(liCaiQuanArticleModels)){
            records = convertModel2Dto(liCaiQuanArticleModels);
        }

        ArticleListResponseDataDto articleListResponseDataDto = new ArticleListResponseDataDto();
        articleListResponseDataDto.setTotalCount(count);
        articleListResponseDataDto.setIndex(index);
        articleListResponseDataDto.setPageSize(pageSize);
        articleListResponseDataDto.setArticleList(records);
        BaseResponseDto<ArticleListResponseDataDto> baseResponseDto = new BaseResponseDto();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(articleListResponseDataDto);
        return baseResponseDto;
    }
    public List<ArticleResponseDataDto> convertModel2Dto(List<LicaiquanArticleModel> liCaiQuanArticleModels){

        return Lists.transform(liCaiQuanArticleModels, new Function<LicaiquanArticleModel, ArticleResponseDataDto>() {
            @Override
            public ArticleResponseDataDto apply(LicaiquanArticleModel liCaiQuanArticleModel) {

                ArticleResponseDataDto articleResponseDataDto = new ArticleResponseDataDto(liCaiQuanArticleModel);
                articleResponseDataDto.setShowPicture(domainName + "/" + liCaiQuanArticleModel.getShowPicture());
                articleResponseDataDto.setThumbPicture(domainName + "/" + liCaiQuanArticleModel.getThumb());
                articleResponseDataDto.setLikeCount(liCaiQuanArticleService.getLikeCount(liCaiQuanArticleModel.getId()));
                articleResponseDataDto.setReadCount(liCaiQuanArticleService.getReadCount(liCaiQuanArticleModel.getId()));
                return articleResponseDataDto;
            }
        });
    }

    @Override
    public BaseResponseDto<ArticleResponseDataDto> obtainArticleContent(long articleId) {
        LicaiquanArticleModel liCaiQuanArticleModel = licaiquanArticleMapper.findArticleById(articleId);
        ArticleResponseDataDto articleResponseDataDto = new ArticleResponseDataDto(liCaiQuanArticleModel);
        articleResponseDataDto.setShowPicture(domainName + "/" + liCaiQuanArticleModel.getShowPicture());
        articleResponseDataDto.setThumbPicture(domainName + "/" + liCaiQuanArticleModel.getThumb());
        articleResponseDataDto.setLikeCount(liCaiQuanArticleService.getLikeCount(liCaiQuanArticleModel.getId()));
        articleResponseDataDto.setReadCount(liCaiQuanArticleService.getReadCount(liCaiQuanArticleModel.getId()));
        BaseResponseDto<ArticleResponseDataDto> baseResponseDto = new BaseResponseDto<>();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(articleResponseDataDto);
        return baseResponseDto;
    }


}
