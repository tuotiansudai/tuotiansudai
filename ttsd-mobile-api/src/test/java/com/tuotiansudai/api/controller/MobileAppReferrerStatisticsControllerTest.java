package com.tuotiansudai.api.controller;


import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppReferrerStatisticsService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class MobileAppReferrerStatisticsControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppReferrerStatisticsController controller;
    @Mock
    private MobileAppReferrerStatisticsService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldGetReferrerStatisticsSuccess() throws Exception {
        BaseResponseDto dto = new BaseResponseDto();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        ReferrerStatisticsResponseDataDto referrerStatisticsResponseDataDto = new ReferrerStatisticsResponseDataDto();
        referrerStatisticsResponseDataDto.setReferrersInvestAmount("1");
        referrerStatisticsResponseDataDto.setReferrersSum("1");
        referrerStatisticsResponseDataDto.setRewardAmount("1");
        BannerPictureResponseDataDto bannerPictureResponseDataDto = new BannerPictureResponseDataDto();
        bannerPictureResponseDataDto.setTitle("拓天速贷");
        bannerPictureResponseDataDto.setContent("0元投资赚收益，呼朋唤友抢佣金");
        bannerPictureResponseDataDto.setPicture("{static}/images/app-banner/app-banner-referrer.jpg");
        bannerPictureResponseDataDto.setUrl("{web}/activity/rank-list-app?source=app");
        bannerPictureResponseDataDto.setSharedUrl("{web}/activity/rank-list");
        referrerStatisticsResponseDataDto.setBanner(bannerPictureResponseDataDto);
        dto.setData(referrerStatisticsResponseDataDto);

        when(service.getReferrerStatistics(any(BaseParamDto.class))).thenReturn(dto);

        doRequestWithServiceMockedTest("/get/referrer-statistics", new BaseParamDto())
                .andExpect(jsonPath("$.code").value("0000"))
                .andExpect(jsonPath("$.data.referrersInvestAmount").value(referrerStatisticsResponseDataDto.getReferrersInvestAmount()))
                .andExpect(jsonPath("$.data.referrersSum").value(referrerStatisticsResponseDataDto.getReferrersSum()))
                .andExpect(jsonPath("$.data.banner.title").value(referrerStatisticsResponseDataDto.getBanner().getTitle()))
                .andExpect(jsonPath("$.data.banner.content").value(referrerStatisticsResponseDataDto.getBanner().getContent()))
                .andExpect(jsonPath("$.data.banner.picture").value(referrerStatisticsResponseDataDto.getBanner().getPicture()))
                .andExpect(jsonPath("$.data.banner.url").value(referrerStatisticsResponseDataDto.getBanner().getUrl()))
                .andExpect(jsonPath("$.data.banner.sharedUrl").value(referrerStatisticsResponseDataDto.getBanner().getSharedUrl()));
    }

}
