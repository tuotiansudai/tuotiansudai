package com.tuotiansudai.service;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.ArticleStatus;
import com.tuotiansudai.dto.LiCaiQuanArticleDto;
import com.tuotiansudai.repository.mapper.LicaiquanArticleMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.LicaiquanArticleModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class LiCaiQuanArticleServiceTest {

    @Autowired
    private LiCaiQuanArticleService liCaiQuanArticleService;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

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
