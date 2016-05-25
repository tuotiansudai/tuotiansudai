package com.tuotiansudai.api.service;

import com.tuotiansudai.dto.ArticleStatus;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LiCaiQuanArticleDto;
import com.tuotiansudai.repository.mapper.LicaiquanArticleMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.LicaiquanArticleModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.service.LiCaiQuanArticleService;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class MobileAppMediaCenterServiceTest {

    @Autowired
    private MobileAppMediaCenterService mobileAppAgreementService;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private LicaiquanArticleMapper licaiquanArticleMapper;
    @Autowired
    private UserMapper userMapper;
    @Value("${web.server}")
    private String domainName;

    @Test
    public void shouldObtainCarouselArticleIsSuccess() {
        LicaiquanArticleModel licaiquanArticleModel = fakeLiCaiQuanArticleModel();
        licaiquanArticleMapper.createArticle(licaiquanArticleModel);
        List<LiCaiQuanArticleDto> liCaiQuanArticleDtos = mobileAppAgreementService.obtainCarouselArticle();
        assertEquals(1,liCaiQuanArticleDtos.size());
        assertEquals(licaiquanArticleModel.getTitle(),liCaiQuanArticleDtos.get(0).getTitle());
        assertEquals(licaiquanArticleModel.getSection(),liCaiQuanArticleDtos.get(0).getSection());
        assertEquals(licaiquanArticleModel.getAuthor(),liCaiQuanArticleDtos.get(0).getAuthor());
        assertEquals(domainName + "/" + licaiquanArticleModel.getShowPicture(), liCaiQuanArticleDtos.get(0).getShowPicture());


    }
    @Test
    public void shouldObtainArticleListIsSuccess(){
        LicaiquanArticleModel licaiquanArticleModel = fakeLiCaiQuanArticleModel();
        licaiquanArticleMapper.createArticle(licaiquanArticleModel);
        BaseDto<BasePaginationDataDto> basePaginationDataDto = mobileAppAgreementService.obtainArticleList(ArticleSectionType.INDUSTRY_NEWS,0,10);
        LiCaiQuanArticleDto liCaiQuanArticleDto = (LiCaiQuanArticleDto)basePaginationDataDto.getData().getRecords().get(0);
        assertEquals(1,basePaginationDataDto.getData().getRecords().size());
        assertEquals(licaiquanArticleModel.getAuthor(), liCaiQuanArticleDto.getAuthor());
        assertEquals(ArticleSectionType.INDUSTRY_NEWS,liCaiQuanArticleDto.getSection());
        assertEquals(licaiquanArticleModel.getAuthor(),liCaiQuanArticleDto.getAuthor());
        assertEquals(licaiquanArticleModel.getTitle(),liCaiQuanArticleDto.getTitle());
        assertEquals(licaiquanArticleModel.getContent(),liCaiQuanArticleDto.getContent());
        assertEquals(domainName + "/" + licaiquanArticleModel.getShowPicture(),liCaiQuanArticleDto.getShowPicture());

    }
    @Test
    public void shouldObtainArticleContentIsSuccess(){
        LicaiquanArticleModel licaiquanArticleModel = fakeLiCaiQuanArticleModel();
        licaiquanArticleMapper.createArticle(licaiquanArticleModel);
        LiCaiQuanArticleDto liCaiQuanArticleDto = mobileAppAgreementService.obtainArticleContent(licaiquanArticleModel.getId());

        assertEquals(licaiquanArticleModel.getAuthor(), liCaiQuanArticleDto.getAuthor());
        assertEquals(ArticleSectionType.INDUSTRY_NEWS,liCaiQuanArticleDto.getSection());
        assertEquals(licaiquanArticleModel.getAuthor(),liCaiQuanArticleDto.getAuthor());
        assertEquals(licaiquanArticleModel.getTitle(),liCaiQuanArticleDto.getTitle());
        assertEquals(licaiquanArticleModel.getContent(),liCaiQuanArticleDto.getContent());
        assertEquals(domainName + "/" + licaiquanArticleModel.getShowPicture(), liCaiQuanArticleDto.getShowPicture());

    }


    public LicaiquanArticleModel fakeLiCaiQuanArticleModel() {
        UserModel userModel = getUserModelTest();
        userMapper.create(userModel);
        LicaiquanArticleModel licaiquanArticleModel = new LicaiquanArticleModel();
        licaiquanArticleModel.setId(idGenerator.generate());
        licaiquanArticleModel.setTitle("title");
        licaiquanArticleModel.setAuthor("author");
        licaiquanArticleModel.setCarousel(true);
        licaiquanArticleModel.setDeleted(false);
        licaiquanArticleModel.setSection(ArticleSectionType.INDUSTRY_NEWS);
        licaiquanArticleModel.setShowPicture("showPicture");
        licaiquanArticleModel.setThumb("ThumbPicture");
        licaiquanArticleModel.setContent("content");
        licaiquanArticleModel.setCreatedTime(new Date());
        licaiquanArticleModel.setCreatorLoginName(userModel.getLoginName());
        licaiquanArticleModel.setCheckerLoginName(userModel.getLoginName());
        licaiquanArticleModel.setUpdatedTime(new Date());
        return licaiquanArticleModel;
    }
    private UserModel getUserModelTest() {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("helloworld");
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }
}
