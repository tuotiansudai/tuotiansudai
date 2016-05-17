package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.*;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by huoxuanbo on 16/5/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class LicaiquanArticleMapperTest {
    @Autowired
    LicaiquanArticleMapper licaiquanArticleMapper;
    @Autowired
    UserMapper userMapper;

    private final static int articleId = 1;

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

    private LicaiquanArticleModel createLicaiquanArticleModel() {
        LicaiquanArticleModel licaiquanArticleModel = new LicaiquanArticleModel();
        licaiquanArticleModel.setArticleId(articleId);
        licaiquanArticleModel.setArticleSection(ArticleSectionType.INDUSTRY_NEWS);
        licaiquanArticleModel.setAuthor("testAuthor");
        licaiquanArticleModel.setCarousel(false);
        licaiquanArticleModel.setChecker("testChecker");
        licaiquanArticleModel.setContent("testContent");
        licaiquanArticleModel.setCreator("testCreator");
        licaiquanArticleModel.setCreateTime(new DateTime().parse("2015-12-12").withTimeAtStartOfDay().toDate());
        licaiquanArticleModel.setLikeCount(10);
        licaiquanArticleModel.setReadCount(100);
        licaiquanArticleModel.setSource("testSource");
        licaiquanArticleModel.setTitle("testTitle");
        licaiquanArticleModel.setUpdateTime(new DateTime().parse("2016-1-1").withTimeAtStartOfDay().toDate());
        licaiquanArticleModel.setShowPicture("testShowPicture");
        licaiquanArticleModel.setThumbnail("testThumbnail");

        return licaiquanArticleModel;
    }

    private LicaiquanArticleModel createModifiedLicaiquanArticleModel(long id) {
        LicaiquanArticleModel licaiquanArticleModel = new LicaiquanArticleModel();
        licaiquanArticleModel.setArticleId(id);
        licaiquanArticleModel.setChecker("modifyChecker");
        licaiquanArticleModel.setCreator("modifyCreator");
        licaiquanArticleModel.setLikeCount(20);
        licaiquanArticleModel.setReadCount(200);
        licaiquanArticleModel.setTitle("modifyTitle");
        licaiquanArticleModel.setThumbnail("modifyThumbnail");
        licaiquanArticleModel.setSource("modifySource");
        licaiquanArticleModel.setArticleSection(ArticleSectionType.PLATFORM_ACTIVITY);
        licaiquanArticleModel.setAuthor("modifyAuthor");
        licaiquanArticleModel.setCarousel(true);
        licaiquanArticleModel.setShowPicture("modifyShowPicture");
        licaiquanArticleModel.setContent("modifyContent");
        licaiquanArticleModel.setCreateTime(new DateTime().parse("2016-4-3").withTimeAtStartOfDay().toDate());
        licaiquanArticleModel.setUpdateTime(new DateTime().parse("2016-5-5").withTimeAtStartOfDay().toDate());

        return licaiquanArticleModel;
    }

    private long prepareArticleData() {
        prepareUsers();
        licaiquanArticleMapper.createArticle(createLicaiquanArticleModel());
        List<LicaiquanArticleListItemModel> existedArticleModels = licaiquanArticleMapper.findExistedArticleListOrderByCreateTime(null, null, articleId, 1);
        return existedArticleModels.get(0).getId();
    }

    List<ArticleReviewComment> createComments(long articleId) {
        List<ArticleReviewComment> articleReviewComments = new ArrayList<>();
        ArticleReviewComment articleReviewComment = new ArticleReviewComment();
        articleReviewComment.setArticleId(articleId);
        articleReviewComment.setComment("testComment1");
        articleReviewComment.setCreateTime(new DateTime().parse("2016-4-6").withTimeAtStartOfDay().toDate());
        articleReviewComments.add(articleReviewComment);

        ArticleReviewComment articleReviewComment1 = new ArticleReviewComment();
        articleReviewComment1.setArticleId(articleId);
        articleReviewComment1.setComment("testComment2");
        articleReviewComment1.setCreateTime(new DateTime().parse("2016-4-5").withTimeAtStartOfDay().toDate());
        articleReviewComments.add(articleReviewComment1);

        return articleReviewComments;
    }

    private List<ArticleReviewComment> prepareCommentData(long articleId) {
        List<ArticleReviewComment> articleReviewComments = createComments(articleId);
        for (ArticleReviewComment articleReviewComment : articleReviewComments) {
            licaiquanArticleMapper.createComment(articleReviewComment);
        }

        return articleReviewComments;
    }

    @Test
    public void testCreateArticle_findArticleByArticleId() {
        prepareUsers();
        LicaiquanArticleModel licaiquanArticleModel = createLicaiquanArticleModel();
        licaiquanArticleMapper.createArticle(licaiquanArticleModel);

        LicaiquanArticleModel testLicaiquanArticleModle = licaiquanArticleMapper.findArticleByArticleId(articleId);

        assertEquals(licaiquanArticleModel.getArticleSection(), testLicaiquanArticleModle.getArticleSection());
        assertEquals(licaiquanArticleModel.getAuthor(), testLicaiquanArticleModle.getAuthor());
        assertEquals(licaiquanArticleModel.isCarousel(), testLicaiquanArticleModle.isCarousel());
        assertEquals(licaiquanArticleModel.getChecker(), testLicaiquanArticleModle.getChecker());
        assertEquals(licaiquanArticleModel.getContent(), testLicaiquanArticleModle.getContent());
        assertEquals(licaiquanArticleModel.getCreator(), testLicaiquanArticleModle.getCreator());
        assertEquals(licaiquanArticleModel.getCreateTime(), testLicaiquanArticleModle.getCreateTime());
        assertEquals(licaiquanArticleModel.getLikeCount(), testLicaiquanArticleModle.getLikeCount());
        assertEquals(licaiquanArticleModel.getReadCount(), testLicaiquanArticleModle.getReadCount());
        assertEquals(licaiquanArticleModel.getSource(), testLicaiquanArticleModle.getSource());
        assertEquals(licaiquanArticleModel.getTitle(), testLicaiquanArticleModle.getTitle());
        assertEquals(licaiquanArticleModel.getUpdateTime(), testLicaiquanArticleModle.getUpdateTime());
        assertEquals(licaiquanArticleModel.getShowPicture(), licaiquanArticleModel.getShowPicture());
        assertEquals(licaiquanArticleModel.getThumbnail(), licaiquanArticleModel.getThumbnail());
    }

    @Test
    public void testDeleteArticle() {
        prepareArticleData();
        assertFalse(licaiquanArticleMapper.findArticleByArticleId(articleId).isDeleted());
        licaiquanArticleMapper.deleteArticle(articleId);
        assertTrue(licaiquanArticleMapper.findArticleByArticleId(articleId).isDeleted());
    }

    @Test
    public void testFindExistedArticleListOrderByCreateTime() {
        long testId = prepareArticleData();
        List<LicaiquanArticleListItemModel> licaiquanArticleListItemModels = licaiquanArticleMapper.findExistedArticleListOrderByCreateTime(null, null, testId, 1);
        assertTrue(licaiquanArticleListItemModels.size() > 0);
        licaiquanArticleListItemModels = licaiquanArticleMapper.findExistedArticleListOrderByCreateTime("testTitle", ArticleSectionType.PLATFORM_ACTIVITY, testId, 1);
        assertTrue(licaiquanArticleListItemModels.size() == 0);
        licaiquanArticleListItemModels = licaiquanArticleMapper.findExistedArticleListOrderByCreateTime("", ArticleSectionType.INDUSTRY_NEWS, testId, 1);
        assertTrue(licaiquanArticleListItemModels.size() == 0);
        licaiquanArticleListItemModels = licaiquanArticleMapper.findExistedArticleListOrderByCreateTime("testTitle", ArticleSectionType.INDUSTRY_NEWS, testId, 1);
        assertTrue(licaiquanArticleListItemModels.size() > 0);
    }

    @Test
    public void testFindDeletedArticleListOrderByCreateTime() {
        long testId = prepareArticleData();
        licaiquanArticleMapper.deleteArticle(articleId);
        List<LicaiquanArticleListItemModel> licaiquanArticleListItemModels = licaiquanArticleMapper.findDeletedArticleListOrderByCreateTime(null, null, testId, 1);
        assertTrue(licaiquanArticleListItemModels.size() > 0);
        licaiquanArticleListItemModels = licaiquanArticleMapper.findDeletedArticleListOrderByCreateTime("testTitle", ArticleSectionType.PLATFORM_ACTIVITY, testId, 1);
        assertTrue(licaiquanArticleListItemModels.size() == 0);
        licaiquanArticleListItemModels = licaiquanArticleMapper.findDeletedArticleListOrderByCreateTime("", ArticleSectionType.INDUSTRY_NEWS, testId, 1);
        assertTrue(licaiquanArticleListItemModels.size() == 0);
        licaiquanArticleListItemModels = licaiquanArticleMapper.findDeletedArticleListOrderByCreateTime("testTitle", ArticleSectionType.INDUSTRY_NEWS, testId, 1);
        assertTrue(licaiquanArticleListItemModels.size() > 0);
    }

    @Test
    public void testFindArticleContentById() {
        prepareArticleData();

        LicaiquanArticleContentModel licaiquanArticleContentModel = licaiquanArticleMapper.findArticleContentByArticleId(articleId);
        assertEquals(ArticleSectionType.INDUSTRY_NEWS, licaiquanArticleContentModel.getArticleSection());
        assertEquals(false, licaiquanArticleContentModel.isCarousel());
        assertEquals("testContent", licaiquanArticleContentModel.getContent());
        assertEquals("testAuthor", licaiquanArticleContentModel.getAuthor());
        assertEquals("testSource", licaiquanArticleContentModel.getSource());
        assertEquals("testTitle", licaiquanArticleContentModel.getTitle());
        assertEquals("testShowPicture", licaiquanArticleContentModel.getShowPicture());
        assertEquals("testThumbnail", licaiquanArticleContentModel.getThumbnail());
        assertEquals(new DateTime().parse("2015-12-12").withTimeAtStartOfDay().toDate(), licaiquanArticleContentModel.getCreateTime());
        assertEquals(new DateTime().parse("2016-1-1").withTimeAtStartOfDay().toDate(), licaiquanArticleContentModel.getUpdateTime());
    }

    @Test
    public void testUpdateArticleContent() {
        prepareArticleData();

        LicaiquanArticleModel licaiquanArticleModel = createModifiedLicaiquanArticleModel(articleId);
        licaiquanArticleMapper.updateArticle(licaiquanArticleModel);
        LicaiquanArticleModel modifiedLicaiquanArticleModel = licaiquanArticleMapper.findArticleByArticleId(articleId);
        assertEquals(licaiquanArticleModel.getChecker(), modifiedLicaiquanArticleModel.getChecker());
        assertEquals(licaiquanArticleModel.getCreator(), modifiedLicaiquanArticleModel.getCreator());
        assertEquals(licaiquanArticleModel.getLikeCount(), modifiedLicaiquanArticleModel.getLikeCount());
        assertEquals(licaiquanArticleModel.getReadCount(), modifiedLicaiquanArticleModel.getReadCount());
        assertEquals(licaiquanArticleModel.getTitle(), modifiedLicaiquanArticleModel.getTitle());
        assertEquals(licaiquanArticleModel.getThumbnail(), modifiedLicaiquanArticleModel.getThumbnail());
        assertEquals(licaiquanArticleModel.getSource(), modifiedLicaiquanArticleModel.getSource());
        assertEquals(licaiquanArticleModel.getArticleSection(), modifiedLicaiquanArticleModel.getArticleSection());
        assertEquals(licaiquanArticleModel.getAuthor(), modifiedLicaiquanArticleModel.getAuthor());
        assertEquals(licaiquanArticleModel.isCarousel(), modifiedLicaiquanArticleModel.isCarousel());
        assertEquals(licaiquanArticleModel.getShowPicture(), modifiedLicaiquanArticleModel.getShowPicture());
        assertEquals(licaiquanArticleModel.getContent(), modifiedLicaiquanArticleModel.getContent());
        assertEquals(licaiquanArticleModel.getCreator(), modifiedLicaiquanArticleModel.getCreator());
        assertEquals(licaiquanArticleModel.getUpdateTime(), modifiedLicaiquanArticleModel.getUpdateTime());
    }

    @Test
    public void testUpdateLikeCount() {
        prepareArticleData();

        licaiquanArticleMapper.updateLikeCount(articleId, 300);
        LicaiquanArticleModel licaiquanArticleModel = licaiquanArticleMapper.findArticleByArticleId(articleId);
        assertEquals(300, licaiquanArticleModel.getLikeCount());
    }

    @Test
    public void testUpdateReadCount() {
        prepareArticleData();

        licaiquanArticleMapper.updateReadCount(articleId, 600);
        LicaiquanArticleModel licaiquanArticleModel = licaiquanArticleMapper.findArticleByArticleId(articleId);
        assertEquals(600, licaiquanArticleModel.getReadCount());
    }

    @Test
    public void testCreateComment_findCommentByArticleId() {
        prepareArticleData();

        List<ArticleReviewComment> articleReviewComments = createComments(articleId);
        for (ArticleReviewComment articleReviewComment : articleReviewComments) {
            licaiquanArticleMapper.createComment(articleReviewComment);
        }

        List<ArticleReviewComment> testComments = licaiquanArticleMapper.findCommentsByArticleId(articleId);
        assertEquals(articleReviewComments.size(), testComments.size());
        for (int i = 0; i < articleReviewComments.size(); ++i) {
            assertEquals(articleReviewComments.get(i).getArticleId(), testComments.get(i).getArticleId());
            assertEquals(articleReviewComments.get(i).getComment(), testComments.get(i).getComment());
            assertEquals(articleReviewComments.get(i).getCreateTime(), testComments.get(i).getCreateTime());
        }
    }

    @Test
    public void testDeleteCommentById() {
        prepareArticleData();
        List<ArticleReviewComment> articleReviewComments = prepareCommentData(articleId);

        List<ArticleReviewComment> testComments = licaiquanArticleMapper.findCommentsByArticleId(articleId);
        final long testCommentId = testComments.get(0).getId();
        licaiquanArticleMapper.deleteCommentById(testCommentId);
        testComments = licaiquanArticleMapper.findCommentsByArticleId(articleId);
        assertEquals(articleReviewComments.size() - 1, testComments.size());
    }

    @Test
    public void testDeleteCommentsByArticleId() {
        prepareArticleData();
        prepareCommentData(articleId);

        licaiquanArticleMapper.deleteCommentsByArticleId(articleId);
        List<ArticleReviewComment> testComments = licaiquanArticleMapper.findCommentsByArticleId(articleId);
        assertEquals(0, testComments.size());
    }
}
