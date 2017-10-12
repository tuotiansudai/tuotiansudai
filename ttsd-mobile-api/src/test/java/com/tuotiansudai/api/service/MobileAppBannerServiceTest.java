package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.mapper.BannerMapper;
import com.tuotiansudai.activity.repository.model.BannerModel;
import com.tuotiansudai.api.dto.v1_0.BannerResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppBannerServiceImpl;
import com.tuotiansudai.repository.model.Source;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.when;

public class MobileAppBannerServiceTest extends ServiceTestBase {

    @InjectMocks
    private MobileAppBannerServiceImpl mobileAppBannerService;

    @Mock
    private BannerMapper bannerMapper;

    @Test
    public void shouldGetBanner() {
        List<BannerModel> bannerModelList = Lists.newArrayList();
        bannerModelList.add(getBannerModel());
        when(bannerMapper.findBannerIsAuthenticatedOrderByOrder(anyBoolean(), any(Source.class),any())).thenReturn(bannerModelList);
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("ceshi1");
        baseParam.setPlatform("android");
        BaseResponseDto<BannerResponseDataDto> responseDto = mobileAppBannerService.generateBannerList(baseParam.getUserId(), Source.ANDROID);
        BannerResponseDataDto data = responseDto.getData();
        assertTrue(CollectionUtils.isNotEmpty(data.getPictures()));
    }
    private BannerModel getBannerModel(){
        BannerModel bannerModel = new BannerModel();
        bannerModel.setId(111111L);
        bannerModel.setName("testName");
        bannerModel.setWebImageUrl("/upload/test.pg");
        bannerModel.setTitle("test");
        bannerModel.setAppUrl("http://app.com");
        bannerModel.setJumpToLink("http://test1.com");
        return bannerModel;

    }
}
