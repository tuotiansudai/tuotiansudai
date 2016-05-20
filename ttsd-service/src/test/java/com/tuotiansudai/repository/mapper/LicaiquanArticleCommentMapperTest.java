package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.*;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class LicaiquanArticleCommentMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LicaiquanArticleMapper licaiquanArticleMapper;

    @Autowired
    private LicaiquanArticleCommentMapper licaiquanArticleCommentMapper;

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

    private void prepareArticleData() {
        prepareUsers();
        licaiquanArticleMapper.createArticle(createLicaiquanArticleModel());
    }

    List<LicaiquanArticleCommentModel> createComments(long articleId) {
        List<LicaiquanArticleCommentModel> articleReviewComments = new ArrayList<>();
        LicaiquanArticleCommentModel articleReviewComment = new LicaiquanArticleCommentModel();
        articleReviewComment.setArticleId(articleId);
        articleReviewComment.setComment("testComment1");
        articleReviewComment.setCreateTime(new DateTime().parse("2016-4-6").withTimeAtStartOfDay().toDate());
        articleReviewComments.add(articleReviewComment);

        LicaiquanArticleCommentModel articleReviewComment1 = new LicaiquanArticleCommentModel();
        articleReviewComment1.setArticleId(articleId);
        articleReviewComment1.setComment("testComment2");
        articleReviewComment1.setCreateTime(new DateTime().parse("2016-4-5").withTimeAtStartOfDay().toDate());
        articleReviewComments.add(articleReviewComment1);

        return articleReviewComments;
    }

    @Test
    public void testCreateComment_findCommentByArticleId() {
        prepareArticleData();

        List<LicaiquanArticleCommentModel> articleReviewComments = createComments(articleId);
        for (LicaiquanArticleCommentModel articleReviewComment : articleReviewComments) {
            licaiquanArticleCommentMapper.createComment(articleReviewComment);
        }

        List<LicaiquanArticleCommentModel> testComments = licaiquanArticleCommentMapper.findCommentsByArticleId(articleId);
        assertEquals(articleReviewComments.size(), testComments.size());
        for (int i = 0; i < articleReviewComments.size(); ++i) {
            assertEquals(articleReviewComments.get(i).getArticleId(), testComments.get(i).getArticleId());
            assertEquals(articleReviewComments.get(i).getComment(), testComments.get(i).getComment());
            assertEquals(articleReviewComments.get(i).getCreateTime(), testComments.get(i).getCreateTime());
        }
    }
}
