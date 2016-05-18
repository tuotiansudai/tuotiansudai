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
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LicaiquanArticleMapper licaiquanArticleMapper;

    private final static String articleRedisKey = "console:article:key";

    @Test
    public void shouldRetraceIsSuccess(){
        LiCaiQuanArticleDto liCaiQuanArticleDto = fakeLiCaiQuanArticleDto();
        liCaiQuanArticleService.createAndEditArticle(liCaiQuanArticleDto);

        liCaiQuanArticleService.retrace(liCaiQuanArticleDto.getArticleId());
        LiCaiQuanArticleDto liCaiQuanArticleDtoNew = (LiCaiQuanArticleDto)redisWrapperClient.hgetSeri(articleRedisKey, String.valueOf(liCaiQuanArticleDto.getArticleId()));
        redisWrapperClient.hdelSeri(articleRedisKey,String.valueOf(liCaiQuanArticleDto.getArticleId()));
        assertEquals(ArticleStatus.RETRACED,liCaiQuanArticleDtoNew.getArticleStatus());
        assertNotEquals(liCaiQuanArticleDto.getArticleStatus(),liCaiQuanArticleDtoNew.getArticleStatus());
    }

    @Test
    public void shouldCreateIsSuccess(){
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
        licaiquanArticleModel.setThumb("testThumb");

        return licaiquanArticleModel;
    }

    private LiCaiQuanArticleDto fakeLiCaiQuanArticleDto(){
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
}
