package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.MediaArticleLikeCountResponseDataDto;
import com.tuotiansudai.api.dto.MediaArticleListResponseDataDto;
import com.tuotiansudai.api.dto.MediaArticleResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppMediaCenterService;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.service.LiCaiQuanArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;

@Controller
@RequestMapping(value = "/media-center")
@Api(description = "媒体中心")
public class MobileAppMediaCenterController {
    @Autowired
    private MobileAppMediaCenterService mobileAppMediaCenterService;

    @Autowired
    private LiCaiQuanArticleService liCaiQuanArticleService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView mediaCenter() {
        return new ModelAndView("/api-template");
    }

    @RequestMapping(value = "/article-list", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("理财圈首页文章列表")
    public BaseResponseDto<MediaArticleListResponseDataDto> obtainArticleList( @RequestParam(name = "section",required = false) ArticleSectionType section,
                                              @Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                              @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return mobileAppMediaCenterService.obtainArticleList(section, index, pageSize);

    }

    @RequestMapping(value = "/article-detail/{articleId}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("理财圈首页文章详细")
    public BaseResponseDto<MediaArticleResponseDataDto> obtainArticleContent(@PathVariable long articleId) {
        liCaiQuanArticleService.updateReadCount(articleId);
        return mobileAppMediaCenterService.obtainArticleContent(articleId);
    }

    @RequestMapping(value = "/banner", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("理财圈首页banner")
    public BaseResponseDto<MediaArticleListResponseDataDto> obtainCarouselArticle() {
        return mobileAppMediaCenterService.obtainCarouselArticle();
    }


    @RequestMapping(value = "/{articleId}/like", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("点赞")
    public BaseResponseDto<MediaArticleLikeCountResponseDataDto> appLikeArticle(@PathVariable long articleId) {
        liCaiQuanArticleService.updateLikeCount(articleId);
        BaseResponseDto<MediaArticleLikeCountResponseDataDto> baseResponseDto = new BaseResponseDto();
        MediaArticleLikeCountResponseDataDto mediaArticleLikeCountResponseDataDto = new MediaArticleLikeCountResponseDataDto();
        mediaArticleLikeCountResponseDataDto.setLikeCount(liCaiQuanArticleService.getLikeCount(articleId));
        mediaArticleLikeCountResponseDataDto.setArticleId(articleId);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(mediaArticleLikeCountResponseDataDto);
        return baseResponseDto;
    }


}
