package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.service.ConsoleLiCaiQuanArticleService;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.SubArticleSectionType;
import com.tuotiansudai.spring.LoginUserInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping(value = "/announce-manage")
public class LiCaiQuanArticleController {
    static Logger logger = Logger.getLogger(LiCaiQuanArticleController.class);

    @Autowired
    private ConsoleLiCaiQuanArticleService consoleLiCaiQuanArticleService;

    @RequestMapping(value = "/article/create", method = RequestMethod.GET)
    public ModelAndView createArticle() {
        ModelAndView mv = new ModelAndView("/article-edit");
        mv.addObject("sectionList", Lists.newArrayList(ArticleSectionType.values()));
        mv.addObject("subSectionList", SubArticleSectionType.values());
        return mv;
    }

    @RequestMapping(value = "/article/{articleId}/edit", method = RequestMethod.GET)
    public ModelAndView editArticle(@PathVariable long articleId) {
        ModelAndView mv = new ModelAndView("/article-edit");
        LiCaiQuanArticleDto liCaiQuanArticleDto = consoleLiCaiQuanArticleService.obtainEditArticleDto(articleId);
        Map<String,String> comments = consoleLiCaiQuanArticleService.getAllComments(articleId);
        mv.addObject("comments",comments);
        mv.addObject("sectionList", Lists.newArrayList(ArticleSectionType.values()));
        mv.addObject("subSectionList", Arrays.stream(SubArticleSectionType.values()).filter(item->item.getParent().equals(ArticleSectionType.KNOWLEDGE)).collect(Collectors.toList()));
        mv.addObject("dto", liCaiQuanArticleDto);
        return mv;
    }

    @RequestMapping(value = "/article/create", method = RequestMethod.POST)
    public ModelAndView createArticle(@ModelAttribute LiCaiQuanArticleDto liCaiQuanArticleDto) {
        liCaiQuanArticleDto.setCreator(LoginUserInfo.getLoginName());
        consoleLiCaiQuanArticleService.createAndEditArticle(liCaiQuanArticleDto);
        return  new ModelAndView("redirect:/announce-manage/article/list");
    }

    @RequestMapping(value = "/article/{articleId}/retrace", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView retraceArticle(@PathVariable long articleId) {
        consoleLiCaiQuanArticleService.retrace(articleId);
        return new ModelAndView("redirect:/announce-manage/article/list");
    }

    @RequestMapping(value = "/article/list", method = RequestMethod.GET)
    public ModelAndView findArticle(@RequestParam(value = "title", required = false) String title,
                                    @RequestParam(name = "articleSectionType", required = false) ArticleSectionType articleSectionType,
                                    @Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                    @RequestParam(value = "status", required = false) ArticleStatus status) {
        int pageSize = 10;
        ModelAndView mv = new ModelAndView("/article-list");
        ArticlePaginationDataDto dto = consoleLiCaiQuanArticleService.findLiCaiQuanArticleDto(title,articleSectionType,status, pageSize, index);
        mv.addObject("data", dto);
        mv.addObject("title", title);
        mv.addObject("selected", articleSectionType);
        mv.addObject("articleSectionTypeList", ArticleSectionType.values());
        mv.addObject("userName",LoginUserInfo.getLoginName());
        mv.addObject("articleStatus",ArticleStatus.values());
        mv.addObject("status",status);
        return mv;
    }

    @RequestMapping(value = "/article/preview", method = RequestMethod.POST)
    public ModelAndView previewArticle(@ModelAttribute LiCaiQuanArticleDto liCaiQuanArticleDto) {
        if (null == liCaiQuanArticleDto) {
            return new ModelAndView("redirect:/error/404");
        } else {
            ModelAndView modelAndView = new ModelAndView("/article-preview");
            if(liCaiQuanArticleDto.getCreateTime() == null){
                liCaiQuanArticleDto.setCreateTime(new Date());
            }
            modelAndView.addObject("articleContent", liCaiQuanArticleDto);
            return modelAndView;
        }
    }

    @RequestMapping(value = "/article/{articleId}/check", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> checkArticle(@PathVariable long articleId) {
        String loginName = LoginUserInfo.getLoginName();
        return consoleLiCaiQuanArticleService.checkArticleOnStatus(articleId, loginName);
    }

    @RequestMapping(value = "/article/{articleId}/check-view", method = RequestMethod.GET)
    public ModelAndView checkViewArticle(@PathVariable long articleId,
                                         @RequestParam(value = "title", required = false) String title,
                                         @RequestParam(name = "articleSectionType", required = false) ArticleSectionType articleSectionType,
                                         @Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                         @RequestParam(value = "status", required = false) ArticleStatus status) {
        LiCaiQuanArticleDto liCaiQuanArticleDto = consoleLiCaiQuanArticleService.getArticleContent(articleId);
        if (null == liCaiQuanArticleDto) {
            return new ModelAndView("redirect:/error/404");
        } else {
            ModelAndView modelAndView = new ModelAndView("/article-check-view");
            modelAndView.addObject("articleContent", liCaiQuanArticleDto);
            modelAndView.addObject("title", title);
            modelAndView.addObject("articleSectionType", articleSectionType);
            modelAndView.addObject("status",status);
            modelAndView.addObject("index",index);
            return modelAndView;
        }
    }

    @RequestMapping(value = "/article/{articleId}/reject", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> rejectArticle(@PathVariable long articleId, @RequestParam(value = "comment", required = false) String comment) {
        consoleLiCaiQuanArticleService.rejectArticle(articleId, comment);
        return new BaseDto<>();
    }

    @RequestMapping(value = "/article/{articleId}/checkPass", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> checkPass(@PathVariable long articleId) {
        consoleLiCaiQuanArticleService.checkPassAndCreateArticle(articleId, LoginUserInfo.getLoginName());
        return new BaseDto<>();
    }

    @RequestMapping(value = "/article/{articleId}/deleteArticle", method = RequestMethod.GET)
    public ModelAndView deleteArticle(@PathVariable long articleId) {
        ModelAndView mv = new ModelAndView("redirect:/announce-manage/article/list");
        this.consoleLiCaiQuanArticleService.deleteArticle(articleId);
        return mv;
    }

    @RequestMapping(value = "/article/{articleId}/original", method = RequestMethod.GET)
    public ModelAndView articleOriginal(@PathVariable long articleId) {
        LiCaiQuanArticleDto liCaiQuanArticleDto = consoleLiCaiQuanArticleService.getArticleContentByDataBase(articleId);
        ModelAndView modelAndView;
        if (null == liCaiQuanArticleDto) {
            modelAndView = new ModelAndView("redirect:/error/404");
        } else {
            modelAndView = new ModelAndView("/article-preview");
            modelAndView.addObject("articleContent", liCaiQuanArticleDto);
        }
        return modelAndView;
    }
}
