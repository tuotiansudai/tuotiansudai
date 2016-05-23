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

    private LicaiquanArticleModel createModifiedLicaiquanArticleModel(long id) {
        LicaiquanArticleModel licaiquanArticleModel = new LicaiquanArticleModel();
        licaiquanArticleModel.setId(id);
        licaiquanArticleModel.setCheckerLoginName("modifyChecker");
        licaiquanArticleModel.setCreatorLoginName("modifyCreator");
        licaiquanArticleModel.setTitle("modifyTitle");
        licaiquanArticleModel.setThumb("modifyThumb");
        licaiquanArticleModel.setSource("modifySource");
        licaiquanArticleModel.setSection(ArticleSectionType.PLATFORM_ACTIVITY);
        licaiquanArticleModel.setAuthor("modifyAuthor");
        licaiquanArticleModel.setCarousel(true);
        licaiquanArticleModel.setShowPicture("modifyShowPicture");
        licaiquanArticleModel.setContent("modifyContent");
        licaiquanArticleModel.setCreatedTime(new DateTime().parse("2016-4-3").withTimeAtStartOfDay().toDate());
        licaiquanArticleModel.setUpdatedTime(new DateTime().parse("2016-5-5").withTimeAtStartOfDay().toDate());

        return licaiquanArticleModel;
    }

    private void prepareArticleData() {
        prepareUsers();
        licaiquanArticleMapper.createArticle(createLicaiquanArticleModel());
    }

    @Test
    public void testCreateArticle_findArticleByArticleId() {
        prepareUsers();
        LicaiquanArticleModel licaiquanArticleModel = createLicaiquanArticleModel();
        licaiquanArticleMapper.createArticle(licaiquanArticleModel);

        LicaiquanArticleModel testLicaiquanArticleModle = licaiquanArticleMapper.findArticleById(articleId);

        assertEquals(licaiquanArticleModel.getSection(), testLicaiquanArticleModle.getSection());
        assertEquals(licaiquanArticleModel.getAuthor(), testLicaiquanArticleModle.getAuthor());
        assertEquals(licaiquanArticleModel.isCarousel(), testLicaiquanArticleModle.isCarousel());
        assertEquals(licaiquanArticleModel.getCheckerLoginName(), testLicaiquanArticleModle.getCheckerLoginName());
        assertEquals(licaiquanArticleModel.getContent(), testLicaiquanArticleModle.getContent());
        assertEquals(licaiquanArticleModel.getCreatorLoginName(), testLicaiquanArticleModle.getCreatorLoginName());
        assertEquals(licaiquanArticleModel.getCreatedTime(), testLicaiquanArticleModle.getCreatedTime());
        assertEquals(licaiquanArticleModel.getSource(), testLicaiquanArticleModle.getSource());
        assertEquals(licaiquanArticleModel.getTitle(), testLicaiquanArticleModle.getTitle());
        assertEquals(licaiquanArticleModel.getUpdatedTime(), testLicaiquanArticleModle.getUpdatedTime());
        assertEquals(licaiquanArticleModel.getShowPicture(), licaiquanArticleModel.getShowPicture());
        assertEquals(licaiquanArticleModel.getThumb(), licaiquanArticleModel.getThumb());
    }

    @Test
    public void testDeleteArticle() {
        prepareArticleData();
        assertFalse(licaiquanArticleMapper.findArticleById(articleId).isDeleted());
        licaiquanArticleMapper.deleteArticle(articleId);
        assertTrue(licaiquanArticleMapper.findArticleById(articleId).isDeleted());
    }

    @Test
    public void testFindExistedArticleListOrderByCreateTime() {
        prepareArticleData();
        List<LicaiquanArticleModel> licaiquanArticleListItemModels = licaiquanArticleMapper.findExistedArticleListOrderByCreateTime(null, null, articleId, 1);
        assertTrue(licaiquanArticleListItemModels.size() > 0);
        licaiquanArticleListItemModels = licaiquanArticleMapper.findExistedArticleListOrderByCreateTime("testTitle", ArticleSectionType.PLATFORM_ACTIVITY, articleId, 1);
        assertTrue(licaiquanArticleListItemModels.size() == 0);
        licaiquanArticleListItemModels = licaiquanArticleMapper.findExistedArticleListOrderByCreateTime("", ArticleSectionType.INDUSTRY_NEWS, articleId, 1);
        assertTrue(licaiquanArticleListItemModels.size() == 0);
        licaiquanArticleListItemModels = licaiquanArticleMapper.findExistedArticleListOrderByCreateTime("testTitle", ArticleSectionType.INDUSTRY_NEWS, articleId, 1);
        assertTrue(licaiquanArticleListItemModels.size() > 0);
    }

    @Test
    public void testFindDeletedArticleListOrderByCreateTime() {
        prepareArticleData();
        licaiquanArticleMapper.deleteArticle(articleId);
        List<LicaiquanArticleModel> licaiquanArticleListItemModels = licaiquanArticleMapper.findDeletedArticleListOrderByCreateTime(null, null, articleId, 1);
        assertTrue(licaiquanArticleListItemModels.size() > 0);
        licaiquanArticleListItemModels = licaiquanArticleMapper.findDeletedArticleListOrderByCreateTime("testTitle", ArticleSectionType.PLATFORM_ACTIVITY, articleId, 1);
        assertTrue(licaiquanArticleListItemModels.size() == 0);
        licaiquanArticleListItemModels = licaiquanArticleMapper.findDeletedArticleListOrderByCreateTime("", ArticleSectionType.INDUSTRY_NEWS, articleId, 1);
        assertTrue(licaiquanArticleListItemModels.size() == 0);
        licaiquanArticleListItemModels = licaiquanArticleMapper.findDeletedArticleListOrderByCreateTime("testTitle", ArticleSectionType.INDUSTRY_NEWS, articleId, 1);
        assertTrue(licaiquanArticleListItemModels.size() > 0);
    }

    @Test
    public void testUpdateArticle() {
        prepareArticleData();

        LicaiquanArticleModel licaiquanArticleModel = createModifiedLicaiquanArticleModel(articleId);
        licaiquanArticleMapper.updateArticle(licaiquanArticleModel);
        LicaiquanArticleModel modifiedLicaiquanArticleModel = licaiquanArticleMapper.findArticleById(articleId);
        assertEquals(licaiquanArticleModel.getCheckerLoginName(), modifiedLicaiquanArticleModel.getCheckerLoginName());
        assertEquals(licaiquanArticleModel.getCreatorLoginName(), modifiedLicaiquanArticleModel.getCreatorLoginName());
        assertEquals(licaiquanArticleModel.getTitle(), modifiedLicaiquanArticleModel.getTitle());
        assertEquals(licaiquanArticleModel.getThumb(), modifiedLicaiquanArticleModel.getThumb());
        assertEquals(licaiquanArticleModel.getSource(), modifiedLicaiquanArticleModel.getSource());
        assertEquals(licaiquanArticleModel.getSection(), modifiedLicaiquanArticleModel.getSection());
        assertEquals(licaiquanArticleModel.getAuthor(), modifiedLicaiquanArticleModel.getAuthor());
        assertEquals(licaiquanArticleModel.isCarousel(), modifiedLicaiquanArticleModel.isCarousel());
        assertEquals(licaiquanArticleModel.getShowPicture(), modifiedLicaiquanArticleModel.getShowPicture());
        assertEquals(licaiquanArticleModel.getContent(), modifiedLicaiquanArticleModel.getContent());
        assertEquals(licaiquanArticleModel.getCreatorLoginName(), modifiedLicaiquanArticleModel.getCreatorLoginName());
        assertEquals(licaiquanArticleModel.getUpdatedTime(), modifiedLicaiquanArticleModel.getUpdatedTime());
    }
}
