package com.tuotiansudai.web.ask.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.ask.repository.model.Tag;
import com.tuotiansudai.ask.service.EmbodyQuestionService;
import com.tuotiansudai.dto.SiteMapDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(path = "/question")
public class EmbodyQuestionController {

    @Autowired
    private EmbodyQuestionService embodyQuestionService;

    @RequestMapping(path = "/sitemap/hot-category-list", method = RequestMethod.GET)
    public ModelAndView getQuestionColumn() {
        ModelAndView modelAndView = new ModelAndView("hot-category-list");
        List<SiteMapDataDto> siteMapDataDtoList = Lists.newArrayList();
        for (Tag tag : Tag.values()) {
            SiteMapDataDto siteMapDataDto = new SiteMapDataDto();
            siteMapDataDto.setName(tag.getDescription());
            siteMapDataDto.setLinkUrl("/question/category/" + tag.name());
            siteMapDataDtoList.add(siteMapDataDto);
        }
        modelAndView.addObject("hotCategoryList", siteMapDataDtoList);
        return modelAndView;
    }

    @RequestMapping(path = "/sitemap/category/{tag:(?:SECURITIES|BANK|FUTURES|P2P|TRUST|LOAN|FUND|CROWD_FUNDING|INVEST|CREDIT_CARD|FOREX|STOCK|OTHER)}", method = RequestMethod.GET)
    public ModelAndView getQuestionsByCategoryList(@PathVariable Tag tag) {
        ModelAndView modelAndView = new ModelAndView("/hot-category-question-list");
        modelAndView.addObject("siteMapDataDtoList", embodyQuestionService.getAskSiteMapData(tag));
        return modelAndView;
    }

}
