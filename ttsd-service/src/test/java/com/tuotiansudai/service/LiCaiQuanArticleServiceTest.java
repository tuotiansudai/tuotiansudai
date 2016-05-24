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
import java.util.UUID;

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
    private final static String articleLikeCounterKey = "console:article:likeCounter";
    private final static String articleReadCounterKey = "console:article:readCounter";

    @Test
    public void shouldRetraceIsSuccess() {
        LiCaiQuanArticleDto liCaiQuanArticleDto = fakeLiCaiQuanArticleDto();
        liCaiQuanArticleService.createAndEditArticle(liCaiQuanArticleDto, "test");

        liCaiQuanArticleService.retrace(liCaiQuanArticleDto.getArticleId());
        LiCaiQuanArticleDto liCaiQuanArticleDtoNew = (LiCaiQuanArticleDto)redisWrapperClient.hgetSeri(articleRedisKey, String.valueOf(liCaiQuanArticleDto.getArticleId()));
        redisWrapperClient.hdelSeri(articleRedisKey, String.valueOf(liCaiQuanArticleDto.getArticleId()));
        assertEquals(ArticleStatus.RETRACED, liCaiQuanArticleDtoNew.getArticleStatus());
        assertNotEquals(liCaiQuanArticleDto.getArticleStatus(), liCaiQuanArticleDtoNew.getArticleStatus());
    }

    @Test
    public void shouldCreateIsSuccess() {
        LiCaiQuanArticleDto liCaiQuanArticleDto = fakeLiCaiQuanArticleDto();
        liCaiQuanArticleService.createAndEditArticle(liCaiQuanArticleDto, "test");
        LiCaiQuanArticleDto liCaiQuanArticleDtoNew = (LiCaiQuanArticleDto)redisWrapperClient.hgetSeri(articleRedisKey, String.valueOf(liCaiQuanArticleDto.getArticleId()));
        redisWrapperClient.hdelSeri(articleRedisKey, String.valueOf(liCaiQuanArticleDto.getArticleId()));
        assertEquals(liCaiQuanArticleDto.getArticleId(), liCaiQuanArticleDtoNew.getArticleId());
        assertEquals(liCaiQuanArticleDto.getTitle(), liCaiQuanArticleDtoNew.getTitle());
        assertEquals(liCaiQuanArticleDto.getArticleStatus(), liCaiQuanArticleDtoNew.getArticleStatus());
        assertEquals(liCaiQuanArticleDto.isCarousel(), liCaiQuanArticleDtoNew.isCarousel());
    }
    @Test
    public void shouldObtainEditArticleDtoFromRedisIsSuccess(){
        prepareUsers();
        LiCaiQuanArticleDto liCaiQuanArticleDto = fakeLiCaiQuanArticleDto();
        liCaiQuanArticleService.createAndEditArticle(liCaiQuanArticleDto, "test");
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
        licaiquanArticleModel.setSection(ArticleSectionType.INDUSTRY_NEWS);
        licaiquanArticleModel.setAuthor("testAuthor");
        licaiquanArticleModel.setCarousel(false);
        licaiquanArticleModel.setCheckerLoginName("testChecker");
        licaiquanArticleModel.setContent("testContent");
        licaiquanArticleModel.setCreatorLoginName("testCreator");
        licaiquanArticleModel.setCreatedTime(new DateTime().parse("2015-12-12").withTimeAtStartOfDay().toDate());
        licaiquanArticleModel.setSource("testSource");
        licaiquanArticleModel.setTitle("testTitle");
        licaiquanArticleModel.setUpdatedTime(new DateTime().parse("2016-1-1").withTimeAtStartOfDay().toDate());
        licaiquanArticleModel.setShowPicture("testShowPicture");
        licaiquanArticleModel.setThumb("testThumb");

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

    private void prepareArticleData(long articleId, ArticleStatus articleStatus)
    {
        LiCaiQuanArticleDto liCaiQuanArticleDto = new LiCaiQuanArticleDto();
        liCaiQuanArticleDto.setArticleId(articleId);
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

    @Test
    public void shouldDeleteArticleIsOk(){
        UserModel user = createUserByUserId("ceshi1");
        LiCaiQuanArticleDto liCaiQuanArticleDto = fakeLiCaiQuanArticleDto();
        liCaiQuanArticleService.createAndEditArticle(liCaiQuanArticleDto, "test");
        liCaiQuanArticleService.checkPassAndCreateArticle(liCaiQuanArticleDto.getArticleId(), user.getLoginName());
        liCaiQuanArticleService.deleteArticle(liCaiQuanArticleDto.getArticleId());
        assertNotNull(licaiquanArticleMapper.findArticleById(liCaiQuanArticleDto.getArticleId()));
    }

    @Test
    public void shouldcheckPassAndCreateArticleIsOk(){
        UserModel user = createUserByUserId("ceshi1");
        LiCaiQuanArticleDto liCaiQuanArticleDto = fakeLiCaiQuanArticleDto();
        liCaiQuanArticleService.createAndEditArticle(liCaiQuanArticleDto, "test");
        liCaiQuanArticleService.checkPassAndCreateArticle(liCaiQuanArticleDto.getArticleId(), user.getLoginName());
        assertNotNull(licaiquanArticleMapper.findArticleById(liCaiQuanArticleDto.getArticleId()));
    }

    @After
    public void clearRedis() {
        redisWrapperClient.del(articleCommentRedisKey);
        redisWrapperClient.del(articleLikeCounterKey);
        redisWrapperClient.del(articleReadCounterKey);
    }

    private UserModel createUserByUserId(String userId) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(userId);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("1" + RandomStringUtils.randomNumeric(10));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
        return userModelTest;
    }

    @Test
    public void testCheckArticleOnStatus() {
        prepareArticleData(0, ArticleStatus.TO_APPROVE);
        assertTrue(liCaiQuanArticleService.checkArticleOnStatus(0).getData().getStatus());
        assertEquals(ArticleStatus.APPROVING, liCaiQuanArticleService.getArticleContent(0).getArticleStatus());
        assertFalse(liCaiQuanArticleService.checkArticleOnStatus(0).getData().getStatus());
    }

    @Test
    public void testGetArticleContent() {
        prepareArticleData(0, ArticleStatus.TO_APPROVE);
        LiCaiQuanArticleDto liCaiQuanArticleDto = liCaiQuanArticleService.getArticleContent(0);
        assertEquals(liCaiQuanArticleDto.getArticleId().longValue(), 0L);
        assertEquals("testTitle", liCaiQuanArticleDto.getTitle());
        assertEquals("testAuthor", liCaiQuanArticleDto.getAuthor());
        assertEquals(true, liCaiQuanArticleDto.isCarousel());
        assertEquals(ArticleSectionType.INDUSTRY_NEWS, liCaiQuanArticleDto.getSection());
        assertEquals(ArticleStatus.TO_APPROVE, liCaiQuanArticleDto.getArticleStatus());
        assertEquals("testShowPicture", liCaiQuanArticleDto.getShowPicture());
        assertEquals("testThumbPicture", liCaiQuanArticleDto.getThumbPicture());
    }
}
