package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.impl.MobileAppReferrerStatisticsServiceImpl;
import com.tuotiansudai.repository.mapper.ReferrerManageMapper;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.model.ReferrerManageView;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
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
    private MobileAppReferrerBannerService mobileAppReferrerBannerService;

    @Test
    public void getReferrerStatistics() throws Exception {

        Long referInvestTotalAmount = 0L;
        int referrerCountByReferrer = 0;
        List<ReferrerManageView> referInvestSumAmountList = new ArrayList<>();
        BaseResponseDto dto = new BaseResponseDto();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        ReferrerStatisticsResponseDataDto referrerStatisticsResponseDataDto = new ReferrerStatisticsResponseDataDto();
        referrerStatisticsResponseDataDto.setReferrersInvestAmount(AmountConverter.convertCentToString(referInvestTotalAmount));
        referrerStatisticsResponseDataDto.setReferrersSum(String.valueOf(referInvestSumAmountList.size()));
        referrerStatisticsResponseDataDto.setRewardAmount(CollectionUtils.isNotEmpty(referInvestSumAmountList) ? AmountConverter.convertCentToString(referInvestSumAmountList.get(0).getInvestAmount()) : "0.00");
        BannerPictureResponseDataDto bannerPictureResponseDataDto = new BannerPictureResponseDataDto();
        bannerPictureResponseDataDto.setTitle("拓天速贷");
        bannerPictureResponseDataDto.setContent("0元投资赚收益，呼朋唤友抢佣金");
        bannerPictureResponseDataDto.setPicture("{static}/images/app-banner/app-banner-referrer.jpg");
        bannerPictureResponseDataDto.setUrl("{web}/activity/rank-list-app?source=app");
        bannerPictureResponseDataDto.setSharedUrl("{web}/activity/rank-list");
        referrerStatisticsResponseDataDto.setBanner(bannerPictureResponseDataDto);
        dto.setData(referrerStatisticsResponseDataDto);
        BaseResponseDto<BannerResponseDataDto> bannerResponseDataDtoList = new BaseResponseDto();
        BannerResponseDataDto bannerResponseDataDto = new BannerResponseDataDto();
        List<BannerPictureResponseDataDto> bannerPictureResponseDataDtos = new ArrayList<>();
        bannerPictureResponseDataDtos.add(bannerPictureResponseDataDto);
        bannerResponseDataDto.setPictures(bannerPictureResponseDataDtos);
        bannerResponseDataDtoList.setData(bannerResponseDataDto);

        when(referrerManageMapper.findReferInvestTotalAmount(anyString(), anyString(), any(Date.class), any(Date.class), anyString())).thenReturn(referInvestTotalAmount);
        when(referrerRelationMapper.findReferrerCountByReferrerLoginName(anyString())).thenReturn(referrerCountByReferrer);
        when(referrerManageMapper.findReferInvestSumAmount(anyString())).thenReturn(referInvestSumAmountList);
        when(mobileAppReferrerBannerService.generateReferrerBannerList()).thenReturn(bannerResponseDataDtoList);

        assertThat(AmountConverter.convertCentToString(referInvestTotalAmount), is(referrerStatisticsResponseDataDto.getReferrersInvestAmount()));
        assertThat(String.valueOf(referrerCountByReferrer), is(referrerStatisticsResponseDataDto.getReferrersSum()));
        assertThat(CollectionUtils.isNotEmpty(referInvestSumAmountList) ? AmountConverter.convertCentToString(referInvestSumAmountList.get(0).getInvestAmount()) : "0.00", is(referrerStatisticsResponseDataDto.getRewardAmount()));
        assertNotNull(bannerResponseDataDto.getPictures().get(0).getTitle());
        assertNotNull(bannerResponseDataDto.getPictures().get(0).getContent());
        assertNotNull(bannerResponseDataDto.getPictures().get(0).getPicture());
        assertNotNull(bannerResponseDataDto.getPictures().get(0).getUrl());
        assertNotNull(bannerResponseDataDto.getPictures().get(0).getSharedUrl());

    }

}
