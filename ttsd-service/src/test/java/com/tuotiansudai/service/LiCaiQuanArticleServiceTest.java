package com.tuotiansudai.service;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.ArticleStatus;
import com.tuotiansudai.dto.LiCaiQuanArticleDto;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class LiCaiQuanArticleServiceTest {
    @Autowired
    private LiCaiQuanArticleService liCaiQuanArticleService;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private final static String articleRedisKey = "console:article:key";

    @Test
    public void shouldRetraceIsSuccess(){
        LiCaiQuanArticleDto liCaiQuanArticleDto = fakeLiCaiQuanArticleDto();
        liCaiQuanArticleService.createArticle(liCaiQuanArticleDto);

        liCaiQuanArticleService.retrace(liCaiQuanArticleDto.getId());
        LiCaiQuanArticleDto liCaiQuanArticleDtoNew = (LiCaiQuanArticleDto)redisWrapperClient.hgetSeri(articleRedisKey, String.valueOf(liCaiQuanArticleDto.getId()));
        redisWrapperClient.hdelSeri(articleRedisKey,String.valueOf(liCaiQuanArticleDto.getId()));
        assertEquals(ArticleStatus.RETRACED,liCaiQuanArticleDtoNew.getArticleStatus());
        assertNotEquals(liCaiQuanArticleDto.getArticleStatus(),liCaiQuanArticleDtoNew.getArticleStatus());
    }

    @Test
    public void shouldCreateIsSuccess(){
        LiCaiQuanArticleDto liCaiQuanArticleDto = fakeLiCaiQuanArticleDto();
        liCaiQuanArticleService.createArticle(liCaiQuanArticleDto);
        LiCaiQuanArticleDto liCaiQuanArticleDtoNew = (LiCaiQuanArticleDto)redisWrapperClient.hgetSeri(articleRedisKey, String.valueOf(liCaiQuanArticleDto.getId()));
        redisWrapperClient.hdelSeri(articleRedisKey,String.valueOf(liCaiQuanArticleDto.getId()));
        assertEquals(liCaiQuanArticleDto.getId(), liCaiQuanArticleDtoNew.getId());
        assertEquals(liCaiQuanArticleDto.getTitle(),liCaiQuanArticleDtoNew.getTitle());
        assertEquals(liCaiQuanArticleDto.getArticleStatus(),liCaiQuanArticleDtoNew.getArticleStatus());
        assertEquals(liCaiQuanArticleDto.isCarousel(),liCaiQuanArticleDtoNew.isCarousel());
    }

    private LiCaiQuanArticleDto fakeLiCaiQuanArticleDto(){
        LiCaiQuanArticleDto liCaiQuanArticleDto = new LiCaiQuanArticleDto();
        liCaiQuanArticleDto.setId(idGenerator.generate());
        liCaiQuanArticleDto.setTitle("tile");
        liCaiQuanArticleDto.setAuthor("author");
        liCaiQuanArticleDto.setCarousel(true);
        liCaiQuanArticleDto.setSection(ArticleSectionType.INDUSTRY_NEWS);
        liCaiQuanArticleDto.setArticleStatus(ArticleStatus.TO_APPROVE);
        liCaiQuanArticleDto.setShowPicture("showPicture");
        liCaiQuanArticleDto.setThumbPicture("ThumbPicture");
        liCaiQuanArticleDto.setCreateTime(new Date());
        return liCaiQuanArticleDto;
    }

    @Test
    public void shouldFindLiCaiQuanArticleDtoIsOk(){
        LiCaiQuanArticleDto liCaiQuanArticleDto = fakeLiCaiQuanArticleDto();
        liCaiQuanArticleService.createArticle(liCaiQuanArticleDto);
        liCaiQuanArticleService.createArticle(liCaiQuanArticleDto);
        liCaiQuanArticleService.createArticle(liCaiQuanArticleDto);

        List<LiCaiQuanArticleDto> list =  liCaiQuanArticleService.findLiCaiQuanArticleDto("",null,0,1000);


    }
}
