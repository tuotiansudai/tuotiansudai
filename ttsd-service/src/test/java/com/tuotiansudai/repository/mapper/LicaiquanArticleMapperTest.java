package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.LicaiquanArticleContentModel;
import com.tuotiansudai.repository.model.LicaiquanArticleListItemModel;
import com.tuotiansudai.repository.model.LicaiquanArticleModel;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
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

    private final static int articleId = 1;

    LicaiquanArticleModel createLicaiquanArticleModel() {
        LicaiquanArticleModel licaiquanArticleModel = new LicaiquanArticleModel();
        licaiquanArticleModel.setId(articleId);
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

    LicaiquanArticleModel createModifiedLicaiquanArticleModel(long id)
    {
        LicaiquanArticleModel licaiquanArticleModel = new LicaiquanArticleModel();
        licaiquanArticleModel.setId(id);
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

    @Test
    public void testCreateArticle_findExistedArticleListOrderByUpdateTime_findArticleById()
    {
        LicaiquanArticleModel licaiquanArticleModel = createLicaiquanArticleModel();
        licaiquanArticleMapper.createArticle(licaiquanArticleModel);
        List<LicaiquanArticleListItemModel> licaiquanArticleListItemModels =
                licaiquanArticleMapper.findExistedArticleListOrderByUpdateTime(articleId, 1);
        assertTrue(licaiquanArticleListItemModels.size() > 0);
        final long testId = licaiquanArticleListItemModels.get(0).getId();

        LicaiquanArticleModel testLicaiquanArticleModle = licaiquanArticleMapper.findArticleById(testId);

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
    public void testDeleteArticle_findDeletedArticleListOrderByUpdateTime()
    {
        licaiquanArticleMapper.createArticle(createLicaiquanArticleModel());
        List<LicaiquanArticleListItemModel> existedArticleModels = licaiquanArticleMapper.findExistedArticleListOrderByUpdateTime(articleId, 1);
        final long testId = existedArticleModels.get(0).getId();

        licaiquanArticleMapper.deleteArticle(testId);
        List<LicaiquanArticleListItemModel> licaiquanArticleModels = licaiquanArticleMapper.findDeletedArticleListOrderByUpdateTime(testId, 1);
        assertTrue(licaiquanArticleModels.size() > 0);
        assertEquals(testId, licaiquanArticleModels.get(0).getId());
    }

    @Test
    public void testFindArticleContentById()
    {
        licaiquanArticleMapper.createArticle(createLicaiquanArticleModel());
        List<LicaiquanArticleListItemModel> existedArticleModels = licaiquanArticleMapper.findExistedArticleListOrderByUpdateTime(articleId, 1);
        final long testId = existedArticleModels.get(0).getId();

        LicaiquanArticleContentModel licaiquanArticleContentModel = licaiquanArticleMapper.findArticleContentById(testId);
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
    public void testUpdateArticleContent()
    {
        licaiquanArticleMapper.createArticle(createLicaiquanArticleModel());
        List<LicaiquanArticleListItemModel> existedArticleModels = licaiquanArticleMapper.findExistedArticleListOrderByUpdateTime(articleId, 1);
        final long testId = existedArticleModels.get(0).getId();

        LicaiquanArticleModel licaiquanArticleModel = createModifiedLicaiquanArticleModel(testId);
        licaiquanArticleMapper.updateArticle(licaiquanArticleModel);
        LicaiquanArticleModel modifiedLicaiquanArticleModel = licaiquanArticleMapper.findArticleById(testId);
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
    public void testUpdateLikeCount(){
        licaiquanArticleMapper.createArticle(createLicaiquanArticleModel());
        List<LicaiquanArticleListItemModel> existedArticleModels = licaiquanArticleMapper.findExistedArticleListOrderByUpdateTime(articleId, 1);
        final long testId = existedArticleModels.get(0).getId();

        licaiquanArticleMapper.updateLikeCount(testId, 300);
        LicaiquanArticleModel licaiquanArticleModel = licaiquanArticleMapper.findArticleById(testId);
        assertEquals(300, licaiquanArticleModel.getLikeCount());
    }

    @Test
    public void testUpdateReadCount() {
        licaiquanArticleMapper.createArticle(createLicaiquanArticleModel());
        List<LicaiquanArticleListItemModel> existedArticleModels = licaiquanArticleMapper.findExistedArticleListOrderByUpdateTime(articleId, 1);
        final long testId = existedArticleModels.get(0).getId();

        licaiquanArticleMapper.updateReadCount(testId, 600);
        LicaiquanArticleModel licaiquanArticleModel = licaiquanArticleMapper.findArticleById(testId);
        assertEquals(600, licaiquanArticleModel.getReadCount());
    }
}
