package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BannerResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppBannerServiceImpl;
import com.tuotiansudai.repository.mapper.BannerMapper;
import com.tuotiansudai.repository.model.BannerModel;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

public class MobileAppBannerServiceTest extends ServiceTestBase{

    @InjectMocks
    private MobileAppBannerServiceImpl mobileAppBannerService;

    @Mock
    private BannerMapper bannerMapper;

    @Test
    public void shouldGetBanner() {
        List<BannerModel> bannerModelList = Lists.newArrayList();
        bannerModelList.add(new BannerModel());
        when(bannerMapper.findBannerIsAuthenticatedOrderByOrder(anyBoolean(),anyString())).thenReturn(bannerModelList);
        BaseResponseDto<BannerResponseDataDto> responseDto = mobileAppBannerService.generateBannerList(new BaseParam());
        BannerResponseDataDto data = responseDto.getData();
        assertTrue(CollectionUtils.isNotEmpty(data.getPictures()));
    }
}
