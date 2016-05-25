package com.tuotiansudai.api.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppMediaCenterService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LiCaiQuanArticleDto;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.LicaiquanArticleModel;
import org.joda.time.DateTime;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml","classpath:dispatcher-servlet.xml"})
@WebAppConfiguration
@Transactional
public class MobileAppMediaCenterControllerTest extends ControllerTestBase{

    @InjectMocks
    private MobileAppMediaCenterController controller;

    @Mock
    private MobileAppMediaCenterService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }
    @Test
    public void shouldObtainArticleListIsSuccess() throws Exception{
        LicaiquanArticleModel liCaiQuanArticleModel = fakeLiCaiQuanArticleModel();
        ArticleResponseDataDto articleResponseDataDto = new ArticleResponseDataDto(liCaiQuanArticleModel);
        articleResponseDataDto.setReadCount(100000000000000L);
        articleResponseDataDto.setLikeCount(100000000000001L);
        List<ArticleResponseDataDto> articleResponseDataDtos = Lists.newArrayList(articleResponseDataDto);
        BaseResponseDto<ArticleListResponseDataDto>  baseResponseDto = new BaseResponseDto();
        ArticleListResponseDataDto articleListResponseDataDto = new ArticleListResponseDataDto();
        articleListResponseDataDto.setPageSize(10);
        articleListResponseDataDto.setIndex(1);
        articleListResponseDataDto.setTotalCount(10);
        articleListResponseDataDto.setArticleList(articleResponseDataDtos);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(articleListResponseDataDto);

        when(service.obtainArticleList(any(ArticleSectionType.class), anyInt(), anyInt())).thenReturn(baseResponseDto);
        this.mockMvc.perform(get("/media-center/article-list")
                .param("articleSectionType", "ALL").param("index", "1").param("pageSize", "10"))
                .andExpect(jsonPath("$.code").value("0000"))
                .andExpect(jsonPath("$.data.articleList[0].articleId").value(liCaiQuanArticleModel.getId()))
                .andExpect(jsonPath("$.data.articleList[0].title").value(liCaiQuanArticleModel.getTitle()))
                .andExpect(jsonPath("$.data.articleList[0].author").value(liCaiQuanArticleModel.getAuthor()))
                .andExpect(jsonPath("$.data.articleList[0].thumbPicture").value(liCaiQuanArticleModel.getThumb()))
                .andExpect(jsonPath("$.data.articleList[0].showPicture").value(liCaiQuanArticleModel.getShowPicture()))
                .andExpect(jsonPath("$.data.articleList[0].section").value(liCaiQuanArticleModel.getSection().name()))
                .andExpect(jsonPath("$.data.articleList[0].likeCount").value(articleResponseDataDto.getLikeCount()))
                .andExpect(jsonPath("$.data.articleList[0].readCount").value(articleResponseDataDto.getReadCount()));

    }
    @Test
    public void shouldObtainCarouselArticleIsSuccess() throws Exception{
        LicaiquanArticleModel liCaiQuanArticleModel = fakeLiCaiQuanArticleModel();
        ArticleResponseDataDto articleResponseDataDto = new ArticleResponseDataDto(liCaiQuanArticleModel);
        articleResponseDataDto.setReadCount(100000000000000L);
        articleResponseDataDto.setLikeCount(100000000000001L);
        List<ArticleResponseDataDto> articleResponseDataDtos = Lists.newArrayList(articleResponseDataDto);
        BaseResponseDto<ArticleListResponseDataDto>  baseResponseDto = new BaseResponseDto();
        ArticleListResponseDataDto articleListResponseDataDto = new ArticleListResponseDataDto();
        articleListResponseDataDto.setPageSize(10);
        articleListResponseDataDto.setIndex(1);
        articleListResponseDataDto.setTotalCount(10);
        articleListResponseDataDto.setArticleList(articleResponseDataDtos);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(articleListResponseDataDto);

        when(service.obtainCarouselArticle()).thenReturn(baseResponseDto);
        this.mockMvc.perform(get("/media-center/banner"))
                .andExpect(jsonPath("$.code").value("0000"))
                .andExpect(jsonPath("$.data.articleList[0].articleId").value(liCaiQuanArticleModel.getId()))
                .andExpect(jsonPath("$.data.articleList[0].title").value(liCaiQuanArticleModel.getTitle()))
                .andExpect(jsonPath("$.data.articleList[0].author").value(liCaiQuanArticleModel.getAuthor()))
                .andExpect(jsonPath("$.data.articleList[0].thumbPicture").value(liCaiQuanArticleModel.getThumb()))
                .andExpect(jsonPath("$.data.articleList[0].showPicture").value(liCaiQuanArticleModel.getShowPicture()))
                .andExpect(jsonPath("$.data.articleList[0].section").value(liCaiQuanArticleModel.getSection().name()))
                .andExpect(jsonPath("$.data.articleList[0].likeCount").value(articleResponseDataDto.getLikeCount()))
                .andExpect(jsonPath("$.data.articleList[0].readCount").value(articleResponseDataDto.getReadCount()));

    }
    @Test
    public void shouldObtainArticleContentIsSuccess() throws Exception{

        LicaiquanArticleModel liCaiQuanArticleModel = fakeLiCaiQuanArticleModel();
        ArticleResponseDataDto articleResponseDataDto = new ArticleResponseDataDto(liCaiQuanArticleModel);
        articleResponseDataDto.setReadCount(100000000000000L);
        articleResponseDataDto.setLikeCount(100000000000001L);
        BaseResponseDto<ArticleResponseDataDto>  baseResponseDto = new BaseResponseDto();

        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(articleResponseDataDto);

        when(service.obtainArticleContent(anyLong())).thenReturn(baseResponseDto);
        this.mockMvc.perform(get("/media-center/article-detail/111111111111"))
                .andExpect(jsonPath("$.code").value("0000"))
                .andExpect(jsonPath("$.data.articleId").value(liCaiQuanArticleModel.getId()))
                .andExpect(jsonPath("$.data.title").value(liCaiQuanArticleModel.getTitle()))
                .andExpect(jsonPath("$.data.author").value(liCaiQuanArticleModel.getAuthor()))
                .andExpect(jsonPath("$.data.thumbPicture").value(liCaiQuanArticleModel.getThumb()))
                .andExpect(jsonPath("$.data.showPicture").value(liCaiQuanArticleModel.getShowPicture()))
                .andExpect(jsonPath("$.data.section").value(liCaiQuanArticleModel.getSection().name()))
                .andExpect(jsonPath("$.data.likeCount").value(articleResponseDataDto.getLikeCount()))
                .andExpect(jsonPath("$.data.readCount").value(articleResponseDataDto.getReadCount()));
    }

    public LicaiquanArticleModel fakeLiCaiQuanArticleModel() {
        LicaiquanArticleModel licaiquanArticleModel = new LicaiquanArticleModel();
        licaiquanArticleModel.setId(111111111111111L);
        licaiquanArticleModel.setTitle("title");
        licaiquanArticleModel.setAuthor("author");
        licaiquanArticleModel.setCarousel(true);
        licaiquanArticleModel.setDeleted(false);
        licaiquanArticleModel.setSection(ArticleSectionType.INDUSTRY_NEWS);
        licaiquanArticleModel.setShowPicture("showPicture");
        licaiquanArticleModel.setThumb("ThumbPicture");
        licaiquanArticleModel.setContent("content");
        licaiquanArticleModel.setCreatedTime(new Date());
        licaiquanArticleModel.setCheckerLoginName("loginName");
        licaiquanArticleModel.setCheckerLoginName("loginName");
        licaiquanArticleModel.setUpdatedTime(new Date());
        return licaiquanArticleModel;
    }


}
