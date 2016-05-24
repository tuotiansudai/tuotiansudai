package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.service.LiCaiQuanArticleService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;
import java.text.ParseException;
import java.util.Date;


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

    @RequestMapping(value = "/article/{articleId}/edit", method = RequestMethod.GET)
    public ModelAndView editArticle(@PathVariable long articleId) {
        ModelAndView mv = new ModelAndView("/article-edit");
        LiCaiQuanArticleDto liCaiQuanArticleDto = liCaiQuanArticleService.obtainEditArticleDto(articleId);
        mv.addObject("sectionList", Lists.newArrayList(ArticleSectionType.values()));
        mv.addObject("dto", liCaiQuanArticleDto);
        return mv;
    }

    @RequestMapping(value = "/article/create", method = RequestMethod.POST)
    public ModelAndView createArticle(@ModelAttribute LiCaiQuanArticleDto liCaiQuanArticleDto,
                                      @RequestParam(name = "beginTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date beginTime) throws ParseException {
        ModelAndView mv = new ModelAndView("/article-edit");
        mv.addObject("sectionList", Lists.newArrayList(ArticleSectionType.values()));
        if (beginTime != null) {
            liCaiQuanArticleDto.setCreateTime(beginTime);
        }
        liCaiQuanArticleDto.setCreator(LoginUserInfo.getLoginName());
        liCaiQuanArticleService.createAndEditArticle(liCaiQuanArticleDto);
        return mv;
    }

    @RequestMapping(value = "/article/{articleId}/retrace/", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> retraceArticle(@PathVariable long articleId) {
        return liCaiQuanArticleService.retrace(articleId);
    }

    @RequestMapping(value = "/article/list", method = RequestMethod.GET)
    public ModelAndView findArticle(@RequestParam(value = "title", required = false) String title,
                                    @RequestParam(name = "articleSectionType", required = false) ArticleSectionType articleSectionType,
                                    @Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                    @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        ModelAndView mv = new ModelAndView("/article-list");
        ArticlePaginationDataDto dto = liCaiQuanArticleService.findLiCaiQuanArticleDto(title, articleSectionType != null && articleSectionType.equals(ArticleSectionType.ALL) ? null : articleSectionType, pageSize, index);
        mv.addObject("data", dto);
        mv.addObject("title", title);
        mv.addObject("selected", articleSectionType != null ? articleSectionType.getArticleSectionTypeName() : "");
        mv.addObject("articleSectionTypeList", ArticleSectionType.values());
        return mv;
    }

    @RequestMapping(value = "/article/{articleId}/preview", method = RequestMethod.GET)
    public ModelAndView previewArticle(@PathVariable long articleId) {
        LiCaiQuanArticleDto liCaiQuanArticleDto = liCaiQuanArticleService.getArticleContent(articleId);
        if (null == liCaiQuanArticleDto) {
            return new ModelAndView("redirect:/error/404");
        } else {
            ModelAndView modelAndView = new ModelAndView("/article-preview");
            modelAndView.addObject("articleContent", liCaiQuanArticleDto);
            return modelAndView;
        }
    }

    @RequestMapping(value = "/article/{articleId}/check", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> checkArticle(@PathVariable long articleId) {
        return liCaiQuanArticleService.checkArticleOnStatus(articleId);
    }

    @RequestMapping(value = "/article/{articleId}/check-view", method = RequestMethod.GET)
    public ModelAndView checkViewArticle(@PathVariable long articleId) {
        LiCaiQuanArticleDto liCaiQuanArticleDto = liCaiQuanArticleService.getArticleContent(articleId);
        if (null == liCaiQuanArticleDto) {
            return new ModelAndView("redirect:/error/404");
        } else {
            ModelAndView modelAndView = new ModelAndView("/article-check-view");
            modelAndView.addObject("articleContent", liCaiQuanArticleDto);
            return modelAndView;
        }
    }

    @RequestMapping(value = "/article/{articleId}/reject", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> rejectArticle(@PathVariable long articleId, @RequestParam(value = "comment", required = false) String comment) {
        liCaiQuanArticleService.rejectArticle(articleId, comment);
        return new BaseDto<>();
    }

    @RequestMapping(value = "/article/{articleId}/checkPass/", method = RequestMethod.GET)
    public ModelAndView checkPass(@PathVariable long articleId) {
        liCaiQuanArticleService.checkPassAndCreateArticle(articleId, LoginUserInfo.getLoginName());
        ModelAndView mv = new ModelAndView("redirect:/announce-manage/article/list");
        return mv;
    }

    @RequestMapping(value = "/article/{articleId}/deleteArticle/", method = RequestMethod.GET)
    public ModelAndView deleteArticle(@PathVariable long articleId) {
        ModelAndView mv = new ModelAndView("redirect:/announce-manage/article/list");
        this.liCaiQuanArticleService.deleteArticle(articleId);
        return mv;
    }

    @RequestMapping(value = "article/{articleId}/appRead", method = RequestMethod.GET)
    public BaseDto<BaseDataDto> appReadArticle(@PathVariable long articleId) {
        liCaiQuanArticleService.updateReadCount(articleId);
        return new BaseDto<>();
    }

    @RequestMapping(value = "article/{articleId}/appLike", method = RequestMethod.GET)
    public BaseDto<BaseDataDto> appLikeArticle(@PathVariable long articleId) {
        liCaiQuanArticleService.updateLikeCount(articleId);
        return new BaseDto<>();
    }


    @RequestMapping(value = "/article/{articleId}/original", method = RequestMethod.GET)
    public ModelAndView articleOriginal(@PathVariable long articleId) {
        LiCaiQuanArticleDto liCaiQuanArticleDto = liCaiQuanArticleService.getArticleContentByDataBase(articleId);
        ModelAndView modelAndView;
        if (null == liCaiQuanArticleDto) {
            modelAndView = new ModelAndView("redirect:/error/404");
        } else {
            modelAndView = new ModelAndView("/article-check-view");
            modelAndView.addObject("articleContent", liCaiQuanArticleDto);
        }
        return modelAndView;
    }
}
