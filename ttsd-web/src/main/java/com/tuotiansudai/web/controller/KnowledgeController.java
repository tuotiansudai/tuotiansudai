package com.tuotiansudai.web.controller;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.LicaiquanArticleMapper;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.LicaiquanArticleModel;
import com.tuotiansudai.repository.model.SubArticleSectionType;
import com.tuotiansudai.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Controller
@RequestMapping(value = "/knowledge")
public class KnowledgeController {

    private final LicaiquanArticleMapper licaiquanArticleMapper;

    @Autowired
    public KnowledgeController(LicaiquanArticleMapper licaiquanArticleMapper) {
        this.licaiquanArticleMapper = licaiquanArticleMapper;
    }

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseDto<BasePaginationDataDto> getKnowledge(@RequestParam(value = "subSection",required = false) SubArticleSectionType subSection,
            @RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        int pageSize = 10;
        int count = licaiquanArticleMapper.findCountArticleByArticleSectionType(ArticleSectionType.KNOWLEDGE,subSection);

        List<LicaiquanArticleModel> list = licaiquanArticleMapper.findArticleByArticleSectionType(ArticleSectionType.KNOWLEDGE, subSection,PaginationUtil.calculateOffset(index, pageSize, count), pageSize);

        BasePaginationDataDto<LicaiquanArticleModel> dataDto = new BasePaginationDataDto<>(index, pageSize, count, list);
        dataDto.setStatus(true);
        return new BaseDto<>(dataDto);
    }

    @RequestMapping(value = "/{knowledge:^\\d+$}", method = RequestMethod.GET)
    public ModelAndView getAnnounceDetail(@PathVariable long knowledge, HttpServletResponse response) {
        LicaiquanArticleModel model = licaiquanArticleMapper.findArticleById(knowledge);

        if (model == null || model.getSection() != ArticleSectionType.KNOWLEDGE) {
            response.setStatus(404);
            return new ModelAndView("/error/404");
        }

        return new ModelAndView("/about/knowledge_detail", "knowledge", model);
    }
}
