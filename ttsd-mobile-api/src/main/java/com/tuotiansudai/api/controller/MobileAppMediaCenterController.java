package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.MediaArticleLikeCountResponseDataDto;
import com.tuotiansudai.api.dto.MediaCenterArticleListRequestDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppMediaCenterService;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.service.LiCaiQuanArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;

@Controller
@RequestMapping(value = "/media-center")
public class MobileAppMediaCenterController {
    @Autowired
    private MobileAppMediaCenterService mobileAppMediaCenterService;

    @Autowired
    private LiCaiQuanArticleService liCaiQuanArticleService;

    @RequestMapping(method = RequestMethod.GET)

    public ModelAndView mediaCenter() {
        return new ModelAndView("/media-center");
    }

    @RequestMapping(value = "/article-list", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponseDto obtainArticleList( @RequestParam(name = "section",required = false) ArticleSectionType section,
                                              @Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                              @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return mobileAppMediaCenterService.obtainArticleList(section, index, pageSize);

    }

    @RequestMapping(value = "/article-detail/{articleId}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponseDto obtainArticleContent(@PathVariable long articleId) {
        liCaiQuanArticleService.updateReadCount(articleId);
        return mobileAppMediaCenterService.obtainArticleContent(articleId);
    }

    @RequestMapping(value = "/banner", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponseDto obtainCarouselArticle() {
        return mobileAppMediaCenterService.obtainCarouselArticle();
    }


    @RequestMapping(value = "/{articleId}/like", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponseDto appLikeArticle(@PathVariable long articleId) {
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
