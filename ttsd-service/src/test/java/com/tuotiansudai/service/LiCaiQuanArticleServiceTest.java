package com.tuotiansudai.service;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.ArticlePaginationDataDto;
import com.tuotiansudai.dto.ArticleStatus;
import com.tuotiansudai.dto.LiCaiQuanArticleDto;
import com.tuotiansudai.repository.mapper.LicaiquanArticleMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.LicaiquanArticleModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.util.IdGenerator;
import org.junit.After;
import org.joda.time.DateTime;
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
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LicaiquanArticleMapper licaiquanArticleMapper;

    private final static String articleRedisKey = "console:article:key";
    private final static String articleCommentRedisKey = "console:article:comment";
    private final static String articleCounterKey = "console:article:counter";

    private final static String likeCounterKey = "likeCounter";
    private final static String readCounterKey = "readCounter";

    @Test
    public void shouldRetraceIsSuccess() {
        LiCaiQuanArticleDto liCaiQuanArticleDto = fakeLiCaiQuanArticleDto();
        liCaiQuanArticleService.createAndEditArticle(liCaiQuanArticleDto);

        liCaiQuanArticleService.retrace(liCaiQuanArticleDto.getArticleId());
        LiCaiQuanArticleDto liCaiQuanArticleDtoNew = (LiCaiQuanArticleDto)redisWrapperClient.hgetSeri(articleRedisKey, String.valueOf(liCaiQuanArticleDto.getArticleId()));
        redisWrapperClient.hdelSeri(articleRedisKey,String.valueOf(liCaiQuanArticleDto.getArticleId()));
        assertEquals(ArticleStatus.RETRACED,liCaiQuanArticleDtoNew.getArticleStatus());
        assertNotEquals(liCaiQuanArticleDto.getArticleStatus(),liCaiQuanArticleDtoNew.getArticleStatus());
    }

    @Test
    public void shouldCreateIsSuccess() {
        LiCaiQuanArticleDto liCaiQuanArticleDto = fakeLiCaiQuanArticleDto();
        liCaiQuanArticleService.createAndEditArticle(liCaiQuanArticleDto);
        LiCaiQuanArticleDto liCaiQuanArticleDtoNew = (LiCaiQuanArticleDto)redisWrapperClient.hgetSeri(articleRedisKey, String.valueOf(liCaiQuanArticleDto.getArticleId()));
        redisWrapperClient.hdelSeri(articleRedisKey, String.valueOf(liCaiQuanArticleDto.getArticleId()));
        assertEquals(liCaiQuanArticleDto.getArticleId(), liCaiQuanArticleDtoNew.getArticleId());
        assertEquals(liCaiQuanArticleDto.getTitle(), liCaiQuanArticleDtoNew.getTitle());
        assertEquals(liCaiQuanArticleDto.getArticleStatus(),liCaiQuanArticleDtoNew.getArticleStatus());
        assertEquals(liCaiQuanArticleDto.isCarousel(), liCaiQuanArticleDtoNew.isCarousel());
    }
    @Test
    public void shouldObtainEditArticleDtoFromRedisIsSuccess(){
        prepareUsers();
        LiCaiQuanArticleDto liCaiQuanArticleDto = fakeLiCaiQuanArticleDto();
        liCaiQuanArticleService.createAndEditArticle(liCaiQuanArticleDto);
        licaiquanArticleMapper.createArticle(createLicaiquanArticleModel(liCaiQuanArticleDto.getArticleId()));

        LiCaiQuanArticleDto liCaiQuanArticleDtoReturn =  liCaiQuanArticleService.obtainEditArticleDto(liCaiQuanArticleDto.getArticleId());
        LicaiquanArticleModel licaiquanArticleModel = licaiquanArticleMapper.findArticleById(liCaiQuanArticleDto.getArticleId());
        redisWrapperClient.hdelSeri(articleRedisKey, String.valueOf(liCaiQuanArticleDto.getArticleId()));
        assertEquals(liCaiQuanArticleDto.getTitle(), liCaiQuanArticleDtoReturn.getTitle());
        assertNotEquals(licaiquanArticleModel.getTitle(), liCaiQuanArticleDtoReturn.getTitle());

    }

    @Test
    public void shouldObtainEditArticleDtoFromDbIsSuccess(){
        prepareUsers();
        long articleId = idGenerator.generate();
        LicaiquanArticleModel licaiquanArticleModel = createLicaiquanArticleModel(articleId);

        licaiquanArticleMapper.createArticle(licaiquanArticleModel);
        LiCaiQuanArticleDto liCaiQuanArticleDtoReturn =  liCaiQuanArticleService.obtainEditArticleDto(articleId);
        LicaiquanArticleModel licaiquanArticleModelReturn = licaiquanArticleMapper.findArticleById(articleId);
        assertEquals(licaiquanArticleModel.getTitle(), licaiquanArticleModelReturn.getTitle());

    }

    private void prepareUsers() {
        UserModel userModel = new UserModel();
        userModel.setLoginName("testCreator");
        userModel.setPassword("e8ba3a39cef651c08fbd7f8df591760f6b7412a4");
        userModel.setMobile("13812340000");
        userModel.setRegisterTime(new Date());
        userModel.setStatus(UserStatus.ACTIVE);
        userModel.setSalt("083e54eaef1f42afaec76a077f571693");
        userMapper.create(userModel);

        userModel.setLoginName("testChecker");
        userModel.setMobile("13812340001");
        userMapper.create(userModel);

        userModel.setLoginName("modifyCreator");
        userModel.setMobile("13812340002");
        userMapper.create(userModel);

        userModel.setLoginName("modifyChecker");
        userModel.setMobile("13812340003");
        userMapper.create(userModel);
    }

    private LicaiquanArticleModel createLicaiquanArticleModel(long articleId) {
        LicaiquanArticleModel licaiquanArticleModel = new LicaiquanArticleModel();
        licaiquanArticleModel.setId(articleId);
        licaiquanArticleModel.setArticleSection(ArticleSectionType.INDUSTRY_NEWS);
        licaiquanArticleModel.setAuthor("testAuthor");
        licaiquanArticleModel.setCarousel(false);
        licaiquanArticleModel.setChecker("testChecker");
        licaiquanArticleModel.setContent("testContent");
        licaiquanArticleModel.setCreator("testCreator");
        licaiquanArticleModel.setCreateTime(new DateTime().parse("2015-12-12").withTimeAtStartOfDay().toDate());
        licaiquanArticleModel.setSource("testSource");
        licaiquanArticleModel.setTitle("testTitle");
        licaiquanArticleModel.setUpdateTime(new DateTime().parse("2016-1-1").withTimeAtStartOfDay().toDate());
        licaiquanArticleModel.setShowPicture("testShowPicture");
        licaiquanArticleModel.setThumbnail("testThumb");

        return licaiquanArticleModel;
    }

    private LiCaiQuanArticleDto fakeLiCaiQuanArticleDto() {
        LiCaiQuanArticleDto liCaiQuanArticleDto = new LiCaiQuanArticleDto();
        liCaiQuanArticleDto.setArticleId(idGenerator.generate());
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
    public void shouldFindLiCaiQuanArticleDtoIsOk() throws InterruptedException {
        liCaiQuanArticleService.createAndEditArticle(fakeLiCaiQuanArticleDto());
        ArticlePaginationDataDto dto = liCaiQuanArticleService.findLiCaiQuanArticleDto("tile",ArticleSectionType.INDUSTRY_NEWS,10,1);
        assertNotNull(dto);
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
    public void testGetLikeAndReadCount_updateLikeCount_updateReadCount() throws InterruptedException {
        long article0 = 0;
        long article1 = 1;
        for (int i = 0; i < 10; ++i) {
            liCaiQuanArticleService.updateReadCount(article0);
        }
        for (int i = 0; i < 5; ++i) {
            liCaiQuanArticleService.updateLikeCount(article0);
        }
        Map<String, Integer> counter = liCaiQuanArticleService.getLikeAndReadCount(article0);
        assertEquals(10, counter.get(readCounterKey).intValue());
        assertEquals(5, counter.get(likeCounterKey).intValue());

        counter = liCaiQuanArticleService.getLikeAndReadCount(article1);
        assertEquals(0, counter.get(readCounterKey).intValue());
        assertEquals(0, counter.get(likeCounterKey).intValue());
        liCaiQuanArticleService.updateLikeCount(article1);
        liCaiQuanArticleService.updateReadCount(article1);
        counter = liCaiQuanArticleService.getLikeAndReadCount(article1);
        assertEquals(1, counter.get(readCounterKey).intValue());
        assertEquals(1, counter.get(likeCounterKey).intValue());
    }

    @After
    public void clearRedis() {
        redisWrapperClient.del(articleCommentRedisKey);
        redisWrapperClient.del(articleCounterKey);
    }
}
