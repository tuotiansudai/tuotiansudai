package com.tuotiansudai.service;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.ArticleStatus;
import com.tuotiansudai.dto.LiCaiQuanArticleDto;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.util.IdGenerator;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

import static org.junit.Assert.*;

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
    private final static String articleCommentRedisKey = "console:article:comment";
    private final static String articleLikeCounterKey = "console:article:likeCounter";
    private final static String articleReadCounterKey = "console:article:readCounter";

    @Test
    public void shouldRetraceIsSuccess() {
        LiCaiQuanArticleDto liCaiQuanArticleDto = fakeLiCaiQuanArticleDto();
        liCaiQuanArticleService.createArticle(liCaiQuanArticleDto);

        liCaiQuanArticleService.retrace(liCaiQuanArticleDto.getId());
        LiCaiQuanArticleDto liCaiQuanArticleDtoNew = (LiCaiQuanArticleDto) redisWrapperClient.hgetSeri(articleRedisKey, String.valueOf(liCaiQuanArticleDto.getId()));
        redisWrapperClient.hdelSeri(articleRedisKey, String.valueOf(liCaiQuanArticleDto.getId()));
        assertEquals(ArticleStatus.RETRACED, liCaiQuanArticleDtoNew.getArticleStatus());
        assertNotEquals(liCaiQuanArticleDto.getArticleStatus(), liCaiQuanArticleDtoNew.getArticleStatus());
    }

    @Test
    public void shouldCreateIsSuccess() {
        LiCaiQuanArticleDto liCaiQuanArticleDto = fakeLiCaiQuanArticleDto();
        liCaiQuanArticleService.createArticle(liCaiQuanArticleDto);
        LiCaiQuanArticleDto liCaiQuanArticleDtoNew = (LiCaiQuanArticleDto) redisWrapperClient.hgetSeri(articleRedisKey, String.valueOf(liCaiQuanArticleDto.getId()));
        redisWrapperClient.hdelSeri(articleRedisKey, String.valueOf(liCaiQuanArticleDto.getId()));
        assertEquals(liCaiQuanArticleDto.getId(), liCaiQuanArticleDtoNew.getId());
        assertEquals(liCaiQuanArticleDto.getTitle(), liCaiQuanArticleDtoNew.getTitle());
        assertEquals(liCaiQuanArticleDto.getArticleStatus(), liCaiQuanArticleDtoNew.getArticleStatus());
        assertEquals(liCaiQuanArticleDto.isCarousel(), liCaiQuanArticleDtoNew.isCarousel());
    }

    private LiCaiQuanArticleDto fakeLiCaiQuanArticleDto() {
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

    private void prepareArticleData(long articleId, ArticleStatus articleStatus)
    {
        LiCaiQuanArticleDto liCaiQuanArticleDto = new LiCaiQuanArticleDto();
        liCaiQuanArticleDto.setId(articleId);
        liCaiQuanArticleDto.setTitle("testTitle");
        liCaiQuanArticleDto.setAuthor("testAuthor");
        liCaiQuanArticleDto.setCarousel(true);
        liCaiQuanArticleDto.setSection(ArticleSectionType.INDUSTRY_NEWS);
        liCaiQuanArticleDto.setArticleStatus(articleStatus);
        liCaiQuanArticleDto.setShowPicture("testShowPicture");
        liCaiQuanArticleDto.setThumbPicture("testThumbPicture");
        liCaiQuanArticleDto.setCreateTime(new Date());
        redisWrapperClient.hsetSeri(articleRedisKey, String.valueOf(articleId), liCaiQuanArticleDto);
    }

    @Test
    public void testRejectArticle_getAllComments() throws InterruptedException {
        prepareArticleData(0, ArticleStatus.TO_APPROVE);
        prepareArticleData(1, ArticleStatus.TO_APPROVE);
        prepareArticleData(2, ArticleStatus.TO_APPROVE);

        liCaiQuanArticleService.rejectArticle(0, "testComment0-1");
        Thread.currentThread().sleep(1000);
        liCaiQuanArticleService.rejectArticle(0, "testComment0-2");
        Thread.currentThread().sleep(1000);
        liCaiQuanArticleService.rejectArticle(1, "testComment1-0");
        Thread.currentThread().sleep(1000);
        liCaiQuanArticleService.rejectArticle(2, "testComment2-0");

        Map<String, String> comments = liCaiQuanArticleService.getAllComments(0);
        assertEquals(2, comments.size());
        assertTrue(comments.containsValue("testComment0-1"));
        assertTrue(comments.containsValue("testComment0-2"));

        comments = liCaiQuanArticleService.getAllComments(1);
        assertEquals(1, comments.size());
        assertTrue(comments.containsValue("testComment1-0"));

        comments = liCaiQuanArticleService.getAllComments(2);
        assertEquals(1, comments.size());
        assertTrue(comments.containsValue("testComment2-0"));
    }

    @Test
    public void testGetLikeCount_getReadCount_updateLikeCount_updateReadCount() throws InterruptedException {
        long article0 = 0;
        long article1 = 1;
        for (int i = 0; i < 10; ++i) {
            liCaiQuanArticleService.updateReadCount(article0);
        }
        for (int i = 0; i < 5; ++i) {
            liCaiQuanArticleService.updateLikeCount(article0);
        }
        assertEquals(10, liCaiQuanArticleService.getReadCount(article0));
        assertEquals(5, liCaiQuanArticleService.getLikeCount(article0));

        assertEquals(0, liCaiQuanArticleService.getReadCount(article1));
        assertEquals(0, liCaiQuanArticleService.getLikeCount(article1));
        liCaiQuanArticleService.updateLikeCount(article1);
        liCaiQuanArticleService.updateReadCount(article1);
        assertEquals(1, liCaiQuanArticleService.getLikeCount(article1));
        assertEquals(1, liCaiQuanArticleService.getReadCount(article1));
    }

    @After
    public void clearRedis() {
        redisWrapperClient.del(articleCommentRedisKey);
        redisWrapperClient.del(articleLikeCounterKey);
        redisWrapperClient.del(articleReadCounterKey);
    }
}
