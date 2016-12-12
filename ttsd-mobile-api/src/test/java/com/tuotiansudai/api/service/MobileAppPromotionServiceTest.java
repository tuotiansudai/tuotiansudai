package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.PromotionStatus;
import com.tuotiansudai.activity.repository.mapper.PromotionMapper;
import com.tuotiansudai.activity.repository.model.PromotionModel;
import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.PromotionListResponseDataDto;
import com.tuotiansudai.api.dto.v2_0.PromotionRequestDto;
import com.tuotiansudai.api.service.v2_0.impl.MobileAppPromotionListsV2ServiceImpl;
import com.tuotiansudai.client.RedisWrapperClient;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class MobileAppPromotionServiceTest extends ServiceTestBase {

    @InjectMocks
    private MobileAppPromotionListsV2ServiceImpl mobileAppPromotionListsV2Service;

    @Mock
    private PromotionMapper promotionMapper;

    @Mock
    private RedisWrapperClient redisWrapperClient;

    @Test
    public void shouldWhenOnlyOnePromotionNotCache() {
        List<PromotionModel> promotionModelList = Lists.newArrayList();
        promotionModelList.add(this.getPromotionModel(1001L, new DateTime().minusDays(2).toDate(), new DateTime().plusDays(3).toDate()));
        when(promotionMapper.findPromotionList()).thenReturn(promotionModelList);
        when(redisWrapperClient.hexists(anyString(), anyString())).thenReturn(false);
        PromotionRequestDto promotionRequestDto = new PromotionRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setDeviceId("testDeviceId");
        promotionRequestDto.setBaseParam(baseParam);

        BaseResponseDto<PromotionListResponseDataDto> responseDto = mobileAppPromotionListsV2Service.generatePromotionList(promotionRequestDto);
        responseDto.getData();

        assertEquals(1, Integer.parseInt(responseDto.getData().getTotalCount().toString()));
        assertEquals(1, responseDto.getData().getPopList().size());
    }

    @Test
    public void shouldWhenOnlyOnePromotionExistCache() {
        List<PromotionModel> promotionModelList = Lists.newArrayList();
        promotionModelList.add(this.getPromotionModel(1001L, new DateTime().minusDays(2).toDate(), new DateTime().plusDays(3).toDate()));
        when(promotionMapper.findPromotionList()).thenReturn(promotionModelList);
        when(redisWrapperClient.hexists(anyString(), anyString())).thenReturn(true);
        PromotionRequestDto promotionRequestDto = new PromotionRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setDeviceId("testDeviceId");
        promotionRequestDto.setBaseParam(baseParam);

        BaseResponseDto<PromotionListResponseDataDto> responseDto = mobileAppPromotionListsV2Service.generatePromotionList(promotionRequestDto);
        responseDto.getData();

        assertEquals(0, Integer.parseInt(responseDto.getData().getTotalCount().toString()));
        assertEquals(0, responseDto.getData().getPopList().size());
    }

    private PromotionModel getPromotionModel(long id, Date startTime, Date endTime) {
        PromotionModel promotionModel = new PromotionModel();
        promotionModel.setId(id);
        promotionModel.setName("testName");
        promotionModel.setStartTime(startTime);
        promotionModel.setEndTime(endTime);
        promotionModel.setStatus(PromotionStatus.APPROVED);
        return promotionModel;

    }
}
