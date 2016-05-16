package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.v1_0.BannerResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.service.v1_0.MobileAppBannerService;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MobileAppBannerServiceTest {

    @Autowired
    private MobileAppBannerService mobileAppBannerService;


    @Test
    public void shouldGetBanner() {
        BaseResponseDto<BannerResponseDataDto> responseDto = mobileAppBannerService.generateBannerList();
        BannerResponseDataDto data = responseDto.getData();
        assertTrue(CollectionUtils.isNotEmpty(data.getPictures()));
    }
}
