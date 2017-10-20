package com.tuotiansudai.activity.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.model.BannerModel;
import com.tuotiansudai.repository.model.Source;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class BannerMapperTest {

    @Autowired
    private BannerMapper bannerMapper;

    @Test
    public void shouldFindBannerIsAuthenticatedOrderByOrderIsOk() {
        BannerModel bannerModel = new BannerModel();
        bannerModel.setWebImageUrl("11");
        bannerModel.setAppImageUrl("11");
        bannerModel.setCreatedTime(new Date());
        bannerModel.setActivatedBy("1");
        bannerModel.setActivatedTime(new Date());
        bannerModel.setDeactivatedTime(new DateTime(new Date()).plusDays(3).toDate());
        bannerModel.setAuthenticated(true);
        bannerModel.setContent("11");
        bannerModel.setCreatedBy("1");
        bannerModel.setDeleted(false);
        bannerModel.setTitle("1");
        bannerModel.setName("1");
        bannerModel.setUrl("1");
        bannerModel.setSharedUrl("11");
        bannerModel.setSource(Lists.newArrayList(Source.WEB, Source.ANDROID));
        bannerMapper.create(bannerModel);
        List<BannerModel> bannerModelList = bannerMapper.findBannerIsAuthenticatedOrderByOrder(true, Source.WEB, new Date());
        assertNotNull(bannerModelList);
    }
}
