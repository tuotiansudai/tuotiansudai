package com.tuotiansudai.api.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppMediaCenterService;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.LicaiquanArticleModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
        MediaArticleResponseDataDto mediaArticleResponseDataDto = new MediaArticleResponseDataDto(liCaiQuanArticleModel);
        mediaArticleResponseDataDto.setReadCount(100000000000000L);
        mediaArticleResponseDataDto.setLikeCount(100000000000001L);
        List<MediaArticleResponseDataDto> mediaArticleResponseDataDtos = Lists.newArrayList(mediaArticleResponseDataDto);
        BaseResponseDto<MediaArticleListResponseDataDto>  baseResponseDto = new BaseResponseDto();
        MediaArticleListResponseDataDto mediaArticleListResponseDataDto = new MediaArticleListResponseDataDto();
        mediaArticleListResponseDataDto.setPageSize(10);
        mediaArticleListResponseDataDto.setIndex(1);
        mediaArticleListResponseDataDto.setTotalCount(10);
        mediaArticleListResponseDataDto.setArticleList(mediaArticleResponseDataDtos);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(mediaArticleListResponseDataDto);

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
                .andExpect(jsonPath("$.data.articleList[0].likeCount").value(mediaArticleResponseDataDto.getLikeCount()))
                .andExpect(jsonPath("$.data.articleList[0].readCount").value(mediaArticleResponseDataDto.getReadCount()));

    }
    @Test
    public void shouldObtainCarouselArticleIsSuccess() throws Exception{
        LicaiquanArticleModel liCaiQuanArticleModel = fakeLiCaiQuanArticleModel();
        MediaArticleResponseDataDto mediaArticleResponseDataDto = new MediaArticleResponseDataDto(liCaiQuanArticleModel);
        mediaArticleResponseDataDto.setReadCount(100000000000000L);
        mediaArticleResponseDataDto.setLikeCount(100000000000001L);
        List<MediaArticleResponseDataDto> mediaArticleResponseDataDtos = Lists.newArrayList(mediaArticleResponseDataDto);
        BaseResponseDto<MediaArticleListResponseDataDto>  baseResponseDto = new BaseResponseDto();
        MediaArticleListResponseDataDto mediaArticleListResponseDataDto = new MediaArticleListResponseDataDto();
        mediaArticleListResponseDataDto.setPageSize(10);
        mediaArticleListResponseDataDto.setIndex(1);
        mediaArticleListResponseDataDto.setTotalCount(10);
        mediaArticleListResponseDataDto.setArticleList(mediaArticleResponseDataDtos);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(mediaArticleListResponseDataDto);

        when(service.obtainCarouselArticle()).thenReturn(baseResponseDto);
        this.mockMvc.perform(get("/media-center/banner"))
                .andExpect(jsonPath("$.code").value("0000"))
                .andExpect(jsonPath("$.data.articleList[0].articleId").value(liCaiQuanArticleModel.getId()))
                .andExpect(jsonPath("$.data.articleList[0].title").value(liCaiQuanArticleModel.getTitle()))
                .andExpect(jsonPath("$.data.articleList[0].author").value(liCaiQuanArticleModel.getAuthor()))
                .andExpect(jsonPath("$.data.articleList[0].thumbPicture").value(liCaiQuanArticleModel.getThumb()))
                .andExpect(jsonPath("$.data.articleList[0].showPicture").value(liCaiQuanArticleModel.getShowPicture()))
                .andExpect(jsonPath("$.data.articleList[0].section").value(liCaiQuanArticleModel.getSection().name()))
                .andExpect(jsonPath("$.data.articleList[0].likeCount").value(mediaArticleResponseDataDto.getLikeCount()))
                .andExpect(jsonPath("$.data.articleList[0].readCount").value(mediaArticleResponseDataDto.getReadCount()));

    }
    @Test
    public void shouldObtainArticleContentIsSuccess() throws Exception{

        LicaiquanArticleModel liCaiQuanArticleModel = fakeLiCaiQuanArticleModel();
        MediaArticleResponseDataDto mediaArticleResponseDataDto = new MediaArticleResponseDataDto(liCaiQuanArticleModel);
        mediaArticleResponseDataDto.setReadCount(100000000000000L);
        mediaArticleResponseDataDto.setLikeCount(100000000000001L);
        BaseResponseDto<MediaArticleResponseDataDto>  baseResponseDto = new BaseResponseDto();

        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(mediaArticleResponseDataDto);

        when(service.obtainArticleContent(anyLong())).thenReturn(baseResponseDto);
        this.mockMvc.perform(get("/media-center/article-detail/111111111111"))
                .andExpect(jsonPath("$.code").value("0000"))
                .andExpect(jsonPath("$.data.articleId").value(liCaiQuanArticleModel.getId()))
                .andExpect(jsonPath("$.data.title").value(liCaiQuanArticleModel.getTitle()))
                .andExpect(jsonPath("$.data.author").value(liCaiQuanArticleModel.getAuthor()))
                .andExpect(jsonPath("$.data.thumbPicture").value(liCaiQuanArticleModel.getThumb()))
                .andExpect(jsonPath("$.data.showPicture").value(liCaiQuanArticleModel.getShowPicture()))
                .andExpect(jsonPath("$.data.section").value(liCaiQuanArticleModel.getSection().name()))
                .andExpect(jsonPath("$.data.likeCount").value(mediaArticleResponseDataDto.getLikeCount()))
                .andExpect(jsonPath("$.data.readCount").value(mediaArticleResponseDataDto.getReadCount()));
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
