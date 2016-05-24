package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.service.MobileAppMediaCenterService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LiCaiQuanArticleDto;
import com.tuotiansudai.repository.model.ArticleSectionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;
import java.util.List;

@Controller
public class MobileAppMediaCenterController {
    @Autowired
    private MobileAppMediaCenterService mobileAppMediaCenterService;

    @RequestMapping(value = "/media-center", method = RequestMethod.GET)
    public ModelAndView mediaCenter( @RequestParam(name = "articleSectionType",defaultValue = "ALL",required = false) ArticleSectionType articleSectionType,
                                     @Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                     @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize) {
        ModelAndView mv = new ModelAndView("/media-center");
        List<LiCaiQuanArticleDto> carouselArticles = mobileAppMediaCenterService.obtainCarouselArticle();

        BaseDto<BasePaginationDataDto> articleList = mobileAppMediaCenterService.obtainArticleList(articleSectionType, index, pageSize);

        mv.addObject("carouselArticles",carouselArticles);
        mv.addObject("articleList",articleList);

        return mv;
    }

    @RequestMapping(value = "/article/{articleId}", method = RequestMethod.GET)
    public ModelAndView obtainArticle(@PathVariable long articleId) {
        ModelAndView mv = new ModelAndView("/article-detail");
        LiCaiQuanArticleDto liCaiQuanArticleDto = mobileAppMediaCenterService.obtainArticleContent(articleId);

        mv.addObject("liCaiQuanArticleDto",liCaiQuanArticleDto);
        return mv;
    }


}
