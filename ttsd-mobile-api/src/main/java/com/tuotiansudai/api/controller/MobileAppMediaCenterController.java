package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.MediaArticleLikeCountResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppMediaCenterService;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.SubArticleSectionType;
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

    @Autowired
    private PageValidUtils pageValidUtils;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView mediaCenter() {
        return new ModelAndView("/api-template");
    }

    @RequestMapping(value = "/article-list", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponseDto obtainArticleList( @RequestParam(name = "section",required = false) ArticleSectionType section,
                                              @RequestParam(name = "subSection",required = false) SubArticleSectionType subSection,
                                              @Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                              @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return mobileAppMediaCenterService.obtainArticleList(section, subSection,index, pageValidUtils.validPageSizeLimit(pageSize));

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
