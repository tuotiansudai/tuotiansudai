package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.service.MobileAppMediaCenterService;
import com.tuotiansudai.repository.model.ArticleSectionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@Controller
@RequestMapping(value = "/media-center")
public class MobileAppMediaCenterController {
    @Autowired
    private MobileAppMediaCenterService mobileAppMediaCenterService;

    @RequestMapping(value = "/article-list", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponseDto obtainArticleList( @RequestParam(name = "articleSectionType",defaultValue = "ALL",required = false) ArticleSectionType section,
                                     @Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                     @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize) {

        return mobileAppMediaCenterService.obtainArticleList(section, index, pageSize);

    }

    @RequestMapping(value = "/article-detail/{articleId}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponseDto obtainArticleContent(@PathVariable long articleId) {
        return mobileAppMediaCenterService.obtainArticleContent(articleId);
    }

    @RequestMapping(value = "/banner", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponseDto obtainCarouselArticle() {
        return mobileAppMediaCenterService.obtainCarouselArticle();
    }


}
