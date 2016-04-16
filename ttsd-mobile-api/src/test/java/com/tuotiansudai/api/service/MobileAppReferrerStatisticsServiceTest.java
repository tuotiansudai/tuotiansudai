package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.impl.MobileAppReferrerStatisticsServiceImpl;
import com.tuotiansudai.repository.mapper.ReferrerManageMapper;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;


public class MobileAppReferrerStatisticsServiceTest  extends ServiceTestBase {

    @InjectMocks
    private MobileAppReferrerStatisticsServiceImpl mobileAppReferrerStatisticsServiceImpl;
    @Mock
    private ReferrerManageMapper referrerManageMapper;
    @Mock
    private ReferrerRelationMapper referrerRelationMapper;
    @Mock
    private MobileAppBannerService mobileAppBannerService;

    @Test
    public void getReferrerStatistics() throws Exception {
        BaseResponseDto dto = new BaseResponseDto();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        ReferrerStatisticsResponseDataDto referrerStatisticsResponseDataDto = new ReferrerStatisticsResponseDataDto();
        referrerStatisticsResponseDataDto.setReferrersInvestAmount("0.00");
        referrerStatisticsResponseDataDto.setReferrersSum("0");
        referrerStatisticsResponseDataDto.setRewardAmount("0.00");
        BannerPictureResponseDataDto bannerPictureResponseDataDto = new BannerPictureResponseDataDto();
        bannerPictureResponseDataDto.setTitle("拓天速贷");
        bannerPictureResponseDataDto.setContent("0元投资赚收益，呼朋唤友抢佣金");
        bannerPictureResponseDataDto.setPicture("{static}/images/app-banner/app-banner-referrer.jpg");
        bannerPictureResponseDataDto.setUrl("{web}/activity/rank-list-app?source=app");
        bannerPictureResponseDataDto.setSharedUrl("{web}/activity/rank-list");
        referrerStatisticsResponseDataDto.setBanner(bannerPictureResponseDataDto);
        dto.setData(referrerStatisticsResponseDataDto);

        BannerResponseDataDto bannerResponseDataDto = new BannerResponseDataDto();
        List<BannerPictureResponseDataDto> bannerPictureResponseDataDtos = new ArrayList<>();
        bannerPictureResponseDataDtos.add(bannerPictureResponseDataDto);
        bannerResponseDataDto.setPictures(bannerPictureResponseDataDtos);

        when(mobileAppBannerService.getLatestBannerInfo(anyString())).thenReturn(bannerResponseDataDto);

        BaseParamDto baseParamDto = new BaseParamDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("meiyouzhegeren");
        baseParamDto.setBaseParam(baseParam);
        BaseResponseDto baseResponseDto = mobileAppReferrerStatisticsServiceImpl.getReferrerStatistics(baseParamDto);
        assertThat(((ReferrerStatisticsResponseDataDto) baseResponseDto.getData()).getReferrersInvestAmount(), is(referrerStatisticsResponseDataDto.getReferrersInvestAmount()));
        assertThat(((ReferrerStatisticsResponseDataDto) baseResponseDto.getData()).getReferrersSum(), is(referrerStatisticsResponseDataDto.getReferrersSum()));
        assertThat(((ReferrerStatisticsResponseDataDto) baseResponseDto.getData()).getRewardAmount(), is(referrerStatisticsResponseDataDto.getRewardAmount()));
        assertNotNull(bannerResponseDataDto.getPictures().get(0).getTitle());
        assertNotNull(bannerResponseDataDto.getPictures().get(0).getContent());
        assertNotNull(bannerResponseDataDto.getPictures().get(0).getPicture());
        assertNotNull(bannerResponseDataDto.getPictures().get(0).getUrl());
        assertNotNull(bannerResponseDataDto.getPictures().get(0).getSharedUrl());

    }

}
