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
import com.tuotiansudai.util.RedisWrapperClient;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class MobileAppPromotionServiceTest extends ServiceTestBase {

    @InjectMocks
    private MobileAppPromotionListsV2ServiceImpl mobileAppPromotionListsV2Service;

    @Mock
    private PromotionMapper promotionMapper;

    @Mock
    private RedisWrapperClient redisWrapperClient;

    private static final String PROMOTION_ALERT_KEY = "app:promotion:pop";

    @Before
    public void setUp() throws Exception {
        Field redisWrapperClientField = this.mobileAppPromotionListsV2Service.getClass().getDeclaredField("redisWrapperClient");
        redisWrapperClientField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(redisWrapperClientField, redisWrapperClientField.getModifiers() & ~Modifier.FINAL);
        redisWrapperClientField.set(this.mobileAppPromotionListsV2Service, this.redisWrapperClient);
    }

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

    @Test
    public void shouldWhenMoreThanOnePromotionNoCache() {
        List<PromotionModel> promotionModelList = Lists.newArrayList();
        promotionModelList.add(this.getPromotionModel(1001L, new DateTime().minusDays(2).toDate(), new DateTime().plusDays(3).toDate()));
        promotionModelList.add(this.getPromotionModel(1002L, new DateTime().minusDays(1).toDate(), new DateTime().plusDays(5).toDate()));
        when(promotionMapper.findPromotionList()).thenReturn(promotionModelList);
        when(redisWrapperClient.hexists(anyString(), anyString())).thenReturn(false);
        PromotionRequestDto promotionRequestDto = new PromotionRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setDeviceId("testDeviceId");
        promotionRequestDto.setBaseParam(baseParam);

        BaseResponseDto<PromotionListResponseDataDto> responseDto = mobileAppPromotionListsV2Service.generatePromotionList(promotionRequestDto);
        responseDto.getData();

        assertEquals(2, Integer.parseInt(responseDto.getData().getTotalCount().toString()));
        assertEquals(2, responseDto.getData().getPopList().size());
    }

    @Test
    public void shouldWhenMoreThanOnePromotionExistCache() {
        List<PromotionModel> promotionModelList = Lists.newArrayList();
        promotionModelList.add(this.getPromotionModel(1001L, new DateTime().minusDays(2).toDate(), new DateTime().plusDays(3).toDate()));
        promotionModelList.add(this.getPromotionModel(1002L, new DateTime().minusDays(1).toDate(), new DateTime().plusDays(5).toDate()));
        promotionModelList.add(this.getPromotionModel(1003L, new DateTime().minusDays(1).toDate(), new DateTime().plusDays(1).toDate()));

        final ArgumentCaptor<String> promotionAlertKeyCaptor = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<String> deviceIdPromotionIdKeyCaptor = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<String> promotionIdCaptor = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<Integer> timeoutCaptor = ArgumentCaptor.forClass(Integer.class);

        when(redisWrapperClient.hexists(anyString(), anyString())).thenReturn(true).thenReturn(true).thenReturn(false);

        when(promotionMapper.findPromotionList()).thenReturn(promotionModelList);
        when(redisWrapperClient.hset(promotionAlertKeyCaptor.capture(), deviceIdPromotionIdKeyCaptor.capture(), promotionIdCaptor.capture(), timeoutCaptor.capture())).thenReturn(1L);

        PromotionRequestDto promotionRequestDto = new PromotionRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setDeviceId("testDeviceId");
        promotionRequestDto.setBaseParam(baseParam);
        BaseResponseDto<PromotionListResponseDataDto> responseDto = mobileAppPromotionListsV2Service.generatePromotionList(promotionRequestDto);
        responseDto.getData();

        verify(redisWrapperClient, times(1)).hset(anyString(), anyString(), anyString(), anyInt());

        assertEquals(1, Integer.parseInt(responseDto.getData().getTotalCount().toString()));
        assertEquals(1, responseDto.getData().getPopList().size());
        assertEquals(PROMOTION_ALERT_KEY, promotionAlertKeyCaptor.getValue());
        assertEquals("deviceId:testDeviceId:promotionId:1003", deviceIdPromotionIdKeyCaptor.getValue());
        assertEquals("1003", promotionIdCaptor.getValue());
        assertEquals("172800", String.valueOf(timeoutCaptor.getValue()));

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
