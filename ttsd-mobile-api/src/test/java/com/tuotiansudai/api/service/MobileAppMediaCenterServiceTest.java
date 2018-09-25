package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.MediaArticleListResponseDataDto;
import com.tuotiansudai.api.dto.MediaArticleResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.mapper.LicaiquanArticleMapper;
import com.tuotiansudai.repository.model.ArticleSectionType;
import com.tuotiansudai.repository.model.LicaiquanArticleModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class MobileAppMediaCenterServiceTest extends ServiceTestBase {

    @Autowired
    private MobileAppMediaCenterService mobileAppAgreementService;
    @Autowired
    private LicaiquanArticleMapper licaiquanArticleMapper;
    @Autowired
    private FakeUserHelper userMapper;
    @Value("${common.static.server}")
    private String domainName;

    @Test
    public void shouldObtainCarouselArticleIsSuccess() {
        LicaiquanArticleModel licaiquanArticleModel = fakeLiCaiQuanArticleModel();
        licaiquanArticleMapper.createArticle(licaiquanArticleModel);
        BaseResponseDto<MediaArticleListResponseDataDto> baseResponseDto = mobileAppAgreementService.obtainCarouselArticle();
        assertEquals(1, baseResponseDto.getData().getArticleList().size());
        assertEquals(licaiquanArticleModel.getTitle(), baseResponseDto.getData().getArticleList().get(0).getTitle());
        assertEquals(licaiquanArticleModel.getSection(), baseResponseDto.getData().getArticleList().get(0).getSection());
        assertEquals(licaiquanArticleModel.getAuthor(), baseResponseDto.getData().getArticleList().get(0).getAuthor());
        assertEquals(domainName + licaiquanArticleModel.getShowPicture(), baseResponseDto.getData().getArticleList().get(0).getShowPicture());


    }

    @Test
    public void shouldObtainArticleListIsSuccess() {
        LicaiquanArticleModel licaiquanArticleModel = fakeLiCaiQuanArticleModel();
        licaiquanArticleMapper.createArticle(licaiquanArticleModel);
        BaseResponseDto<MediaArticleListResponseDataDto> baseResponseDto = mobileAppAgreementService.obtainArticleList(ArticleSectionType.INDUSTRY_NEWS,null, 1, 10);
        assertEquals(1, baseResponseDto.getData().getArticleList().size());
        assertEquals(licaiquanArticleModel.getAuthor(), baseResponseDto.getData().getArticleList().get(0).getAuthor());
        assertEquals(ArticleSectionType.INDUSTRY_NEWS, baseResponseDto.getData().getArticleList().get(0).getSection());
        assertEquals(licaiquanArticleModel.getAuthor(), baseResponseDto.getData().getArticleList().get(0).getAuthor());
        assertEquals(licaiquanArticleModel.getTitle(), baseResponseDto.getData().getArticleList().get(0).getTitle());
        assertEquals(licaiquanArticleModel.getContent(), baseResponseDto.getData().getArticleList().get(0).getContent());
        assertEquals(domainName + licaiquanArticleModel.getShowPicture(), baseResponseDto.getData().getArticleList().get(0).getShowPicture());

    }

    @Test
    public void shouldObtainArticleContentIsSuccess() {
        LicaiquanArticleModel licaiquanArticleModel = fakeLiCaiQuanArticleModel();
        licaiquanArticleMapper.createArticle(licaiquanArticleModel);
        BaseResponseDto<MediaArticleResponseDataDto> baseResponseDto = mobileAppAgreementService.obtainArticleContent(licaiquanArticleModel.getId());

        assertEquals(licaiquanArticleModel.getAuthor(), baseResponseDto.getData().getAuthor());
        assertEquals(ArticleSectionType.INDUSTRY_NEWS, baseResponseDto.getData().getSection());
        assertEquals(licaiquanArticleModel.getAuthor(), baseResponseDto.getData().getAuthor());
        assertEquals(licaiquanArticleModel.getTitle(), baseResponseDto.getData().getTitle());
        assertEquals(licaiquanArticleModel.getContent(), baseResponseDto.getData().getContent());
        assertEquals(domainName + licaiquanArticleModel.getShowPicture(), baseResponseDto.getData().getShowPicture());

    }


    public LicaiquanArticleModel fakeLiCaiQuanArticleModel() {
        UserModel userModel = getUserModelTest();
        userMapper.create(userModel);
        LicaiquanArticleModel licaiquanArticleModel = new LicaiquanArticleModel();
        licaiquanArticleModel.setId(IdGenerator.generate());
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
