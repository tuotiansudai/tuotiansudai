package com.tuotiansudai.api.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.impl.MobileAppPointServiceImpl;
import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.point.repository.model.PointBillModel;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class MobileAppPointServiceTest extends ServiceTestBase {

    @InjectMocks
    private MobileAppPointServiceImpl mobileAppPointService;

    @Mock
    private PointBillMapper pointBillMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void shouldQueryPointBillsIsOk() {
        PointBillModel pointBillModel = new PointBillModel();
        pointBillModel.setId(idGenerator.generate());
        pointBillModel.setLoginName("admin");
        pointBillModel.setBusinessType(PointBusinessType.TASK);
        pointBillModel.setCreatedTime(new Date());
        pointBillModel.setPoint(1000);
        pointBillModel.setOrderId(idGenerator.generate());
        pointBillModel.setNote("11111");

        List<PointBillModel> pointBillModelList = Lists.newArrayList();
        pointBillModelList.add(pointBillModel);
        when(pointBillMapper.findPointBillPagination(anyString(),anyInt(),anyInt(),any(Date.class), any(Date.class), any(PointBusinessType.class))).thenReturn(pointBillModelList);
        when(pointBillMapper.findCountPointBillPagination(anyString(), any(Date.class), any(Date.class), any(PointBusinessType.class))).thenReturn(1L);

        PointBillRequestDto pointBillRequestDto = new PointBillRequestDto();
        pointBillRequestDto.setIndex(1);
        pointBillRequestDto.setPageSize(10);
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("admin");
        pointBillRequestDto.setBaseParam(baseParam);

        BaseResponseDto<PointBillResponseDataDto> baseResponseDto = mobileAppPointService.queryPointBillList(pointBillRequestDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
        assertEquals("TASK", baseResponseDto.getData().getPointBills().get(0).getBusinessType());
        assertEquals(1000, Long.parseLong(baseResponseDto.getData().getPointBills().get(0).getPoint()));
    }

}
