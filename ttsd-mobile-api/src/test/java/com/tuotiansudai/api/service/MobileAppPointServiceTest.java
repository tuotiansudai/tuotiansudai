package com.tuotiansudai.api.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppPointServiceImpl;
import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.point.repository.mapper.PointTaskMapper;
import com.tuotiansudai.point.repository.mapper.UserPointTaskMapper;
import com.tuotiansudai.point.repository.model.*;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

public class MobileAppPointServiceTest extends ServiceTestBase {

    @InjectMocks
    private MobileAppPointServiceImpl mobileAppPointService;

    @Mock
    private PointBillMapper pointBillMapper;

    @Mock
    private PointTaskMapper pointTaskMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private UserPointTaskMapper userPointTaskMapper;

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
        when(accountMapper.findByLoginName(anyString())).thenReturn(new AccountModel());
        when(pointBillMapper.findPointBillPagination(anyString(), anyInt(), anyInt(), any(Date.class), any(Date.class), any(PointBusinessType.class))).thenReturn(pointBillModelList);
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

    @Test
    public void shouldQueryPointTaskIsOk(){
        PointTaskModel pointTaskModel = new PointTaskModel();
        pointTaskModel.setPoint(60);
        pointTaskModel.setName(PointTask.BIND_BANK_CARD);
        pointTaskModel.setCreatedTime(new Date());
        pointTaskModel.setId(111);

        List<PointTaskModel> pointTaskModels =  Lists.newArrayList();
        pointTaskModels.add(pointTaskModel);

        UserPointTaskModel userPointTaskModel = new UserPointTaskModel();
        userPointTaskModel.setLoginName("loginName");
        userPointTaskModel.setCreatedTime(new Date());
        userPointTaskModel.setPointTask(pointTaskModel);

        when(pointTaskMapper.findCountPointTaskPagination()).thenReturn(1);
        when(pointTaskMapper.findPointTaskPagination(anyInt(), anyInt())).thenReturn(pointTaskModels);
        when(userPointTaskMapper.findByLoginNameAndId(anyLong(),anyString())).thenReturn(userPointTaskModel);
        PointTaskRequestDto pointTaskRequestDto = new PointTaskRequestDto();
        pointTaskRequestDto.setIndex(1);
        pointTaskRequestDto.setPageSize(10);
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("admin");
        pointTaskRequestDto.setBaseParam(baseParam);

        BaseResponseDto<PointTaskListResponseDataDto> baseResponseDto = mobileAppPointService.queryPointTaskList(pointTaskRequestDto);

        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
        assertEquals(PointTask.BIND_BANK_CARD, baseResponseDto.getData().getPointTasks().get(0).getPointTaskType());
        assertEquals(60, Long.parseLong(baseResponseDto.getData().getPointTasks().get(0).getPoint()));

    }


}
