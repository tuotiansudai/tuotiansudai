package com.tuotiansudai.service;

import com.tuotiansudai.util.RedisWrapperClient;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class LiCaiQuanArticleServiceTest {

    @Autowired
    private LiCaiQuanArticleService liCaiQuanArticleService;

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final static String articleCommentRedisKey = "console:article:comment";
    private final static String articleLikeCounterKey = "console:article:likeCounter";
    private final static String articleReadCounterKey = "console:article:readCounter";

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
