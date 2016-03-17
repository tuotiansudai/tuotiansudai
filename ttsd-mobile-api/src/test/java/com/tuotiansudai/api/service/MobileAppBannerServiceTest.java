package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BannerResponseDataDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MobileAppBannerServiceTest {

    @Autowired
    private MobileAppBannerService mobileAppBannerService;


    @Test
    public void shouldGetBanner() {
        BaseResponseDto<BannerResponseDataDto> responseDto = mobileAppBannerService.generateBannerList();
        BannerResponseDataDto data = responseDto.getData();
        assertThat(data.getPictures().size(), is(4));
    }
}
