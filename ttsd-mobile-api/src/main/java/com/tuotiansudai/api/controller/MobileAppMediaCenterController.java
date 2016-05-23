package com.tuotiansudai.api.controller;

import com.tuotiansudai.service.LiCaiQuanArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MobileAppMediaCenterController {
    @Autowired
    private LiCaiQuanArticleService liCaiQuanArticleService;

    @RequestMapping(value = "/media-center", method = RequestMethod.GET)
    public ModelAndView mediaCenter() {
        ModelAndView mv = new ModelAndView("/media-center");
        return mv;
    }

    @RequestMapping(value = "/article/{articleId}", method = RequestMethod.GET)
    public ModelAndView obtainArticle(@PathVariable long articleId) {
        ModelAndView mv = new ModelAndView("/article-detail");

        return mv;
    }


}
