package com.tuotiansudai.api.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.service.MobileAppMediaCenterService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LiCaiQuanArticleDto;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.LicaiquanArticleModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml","classpath:dispatcher-servlet.xml"})
@WebAppConfiguration
@Transactional
public class MobileAppMediaCenterControllerTest {

    @InjectMocks
    private MobileAppMediaCenterController controller;

    @Mock
    private MobileAppMediaCenterService service;

    private MockMvc mockMvc;

    @Before
    public void baseSetup() {
        MockitoAnnotations.initMocks(this);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".ftl");

        this.mockMvc = MockMvcBuilders.standaloneSetup(this.controller)
                .setViewResolvers(viewResolver)
                .build();
    }
    @Test
    public void shouldMediaCenterIsSuccess() throws Exception{
        LiCaiQuanArticleDto liCaiQuanArticleDto = new LiCaiQuanArticleDto(fakeLiCaiQuanArticleModel());
        List<LiCaiQuanArticleDto> liCaiQuanArticleDtos = Lists.newArrayList(liCaiQuanArticleDto);
        BasePaginationDataDto<LiCaiQuanArticleDto> dataDto = new BasePaginationDataDto<>(1, 10, 1, liCaiQuanArticleDtos);
        BaseDto<BasePaginationDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);
        baseDto.setSuccess(true);
        when(service.obtainCarouselArticle()).thenReturn(liCaiQuanArticleDtos);
        when(service.obtainArticleList(any(ArticleSectionType.class), anyInt(), anyInt())).thenReturn(baseDto);
        this.mockMvc.perform(get("/media-center")
                .param("articleSectionType", "ALL").param("index","1").param("pageSize","10"))
                .andExpect(view().name("/media-center"))
                .andExpect(model().attributeExists("carouselArticles"))
                .andExpect(model().attributeExists("articleList"));
    }
    @Test
    public void shouldObtainArticleIsSuccess() throws Exception{
        LiCaiQuanArticleDto liCaiQuanArticleDto = new LiCaiQuanArticleDto(fakeLiCaiQuanArticleModel());
        when(service.obtainArticleContent(anyLong())).thenReturn(liCaiQuanArticleDto);
        this.mockMvc.perform(get("/article/1111"))
                .andExpect(view().name("/article-detail"))
                .andExpect(model().attributeExists("liCaiQuanArticleDto"));
    }

    public LicaiquanArticleModel fakeLiCaiQuanArticleModel() {
        LicaiquanArticleModel licaiquanArticleModel = new LicaiquanArticleModel();
        licaiquanArticleModel.setId(1111L);
        licaiquanArticleModel.setTitle("title");
        licaiquanArticleModel.setAuthor("author");
        licaiquanArticleModel.setCarousel(true);
        licaiquanArticleModel.setIsDeleted(false);
        licaiquanArticleModel.setArticleSection(ArticleSectionType.INDUSTRY_NEWS);
        licaiquanArticleModel.setShowPicture("showPicture");
        licaiquanArticleModel.setThumb("ThumbPicture");
        licaiquanArticleModel.setContent("content");
        licaiquanArticleModel.setCreateTime(new Date());
        licaiquanArticleModel.setCreator("loginName");
        licaiquanArticleModel.setChecker("loginName");
        licaiquanArticleModel.setUpdateTime(new Date());
        return licaiquanArticleModel;
    }


}
