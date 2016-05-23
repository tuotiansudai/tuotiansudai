package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.ArticleStatus;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LiCaiQuanArticleDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.service.LiCaiQuanArticleService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value = "/announce-manage")
public class LiCaiQuanArticleController {
    static Logger logger = Logger.getLogger(LiCaiQuanArticleController.class);
    @Autowired
    private LiCaiQuanArticleService liCaiQuanArticleService;

    @RequestMapping(value = "/article/create", method = RequestMethod.GET)
    public ModelAndView createArticle() {
        ModelAndView mv = new ModelAndView("/article-edit");
        mv.addObject("sectionList", Lists.newArrayList(ArticleSectionType.values()));
        return mv;
    }
    @RequestMapping(value = "/article/edit/{articleId}",method = RequestMethod.GET)
    public ModelAndView editArticle(@PathVariable long articleId ) {
        ModelAndView mv = new ModelAndView("/article-edit");
        LiCaiQuanArticleDto liCaiQuanArticleDto = liCaiQuanArticleService.obtainEditArticleDto(articleId);
        mv.addObject("sectionList", Lists.newArrayList(ArticleSectionType.values()));
        mv.addObject("dto",liCaiQuanArticleDto);
        return mv;
    }

    @RequestMapping(value = "/article/create", method = RequestMethod.POST)
    public ModelAndView createArticle(@ModelAttribute LiCaiQuanArticleDto liCaiQuanArticleDto) {
        ModelAndView mv = new ModelAndView("/article-edit");
        mv.addObject("sectionList", Lists.newArrayList(ArticleSectionType.values()));
        liCaiQuanArticleService.createAndEditArticle(liCaiQuanArticleDto);
        return mv;
    }

    @RequestMapping(value = "/article/retrace/{articleId}", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> retraceArticle(@PathVariable long articleId) {
        return liCaiQuanArticleService.retrace(articleId);
    }

    @RequestMapping(value = "/article/preview/{articleId}", method = RequestMethod.GET)
    public ModelAndView previewArticle(@PathVariable long articleId) {
        ModelAndView modelAndView = new ModelAndView("/article-preview");
        modelAndView.addObject("articleContent", liCaiQuanArticleService.getArticleContent(articleId));
        return modelAndView;
    }

    @RequestMapping(value = "/article/check/{articleId}", method = RequestMethod.GET)
    public ModelAndView checkArticle(@PathVariable long articleId) {
        ModelAndView modelAndView = new ModelAndView("/article-check");
        liCaiQuanArticleService.changeArticleStatus(articleId, ArticleStatus.APPROVING);
        modelAndView.addObject("articleContent", liCaiQuanArticleService.getArticleContent(articleId));
        return modelAndView;
    }

    @RequestMapping(value = "/article/reject/{articleId}", method = RequestMethod.POST)
    @ResponseBody
    public String rejectArticle(@PathVariable long articleId, @RequestParam(value = "comment", required = false) String comment) {
        liCaiQuanArticleService.changeArticleStatus(articleId, ArticleStatus.TO_APPROVE);
        if (null != comment && !comment.equals("")) {
            liCaiQuanArticleService.rejectArticle(articleId, comment);
        }
        return "";
    }
}
