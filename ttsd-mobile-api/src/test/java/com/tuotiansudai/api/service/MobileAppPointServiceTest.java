package com.tuotiansudai.api.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppPointServiceImpl;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.point.repository.dto.SignInPointDto;
import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.point.repository.model.PointBillModel;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.service.SignInService;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

public class MobileAppPointServiceTest extends ServiceTestBase {

    @InjectMocks
    private MobileAppPointServiceImpl mobileAppPointService;

    @Mock
    private PointBillMapper pointBillMapper;

    @Mock
    private BankAccountMapper bankAccountMapper;

    @Mock
    private SignInService signInService;

    @Mock
    private PageValidUtils pageValidUtils;

    @Test
    public void shouldQueryPointBillsIsOk() {
        PointBillModel pointBillModel = new PointBillModel();
        pointBillModel.setId(IdGenerator.generate());
        pointBillModel.setLoginName("admin");
        pointBillModel.setBusinessType(PointBusinessType.TASK);
        pointBillModel.setCreatedTime(new Date());
        pointBillModel.setPoint(1000);
        pointBillModel.setOrderId(IdGenerator.generate());
        pointBillModel.setNote("11111");

        List<PointBillModel> pointBillModelList = Lists.newArrayList();
        pointBillModelList.add(pointBillModel);
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), any())).thenReturn(new BankAccountModel());
        when(pointBillMapper.findPointBillPagination(anyString(), anyString(), anyInt(), anyInt(), any(Date.class), any(Date.class), any(ArrayList.class))).thenReturn(pointBillModelList);
        when(pointBillMapper.findCountPointBillPagination(anyString(), anyString(), any(Date.class), any(Date.class), any(ArrayList.class))).thenReturn(1L);
        when(pageValidUtils.validPageSizeLimit(anyInt())).thenReturn(10);

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
    public void shouldGetLastSignInTimeIsOk() {
        BankAccountModel bankAccountModel = new BankAccountModel();
        SignInPointDto signInPointDto = new SignInPointDto(1, DateUtils.addDays(new DateTime().withTimeAtStartOfDay().toDate(), -1), 0, 10, false);
        when(bankAccountMapper.findByLoginNameAndRole(anyString(), any())).thenReturn(bankAccountModel);
        when(signInService.getLastSignIn(anyString())).thenReturn(signInPointDto);
        when(signInService.signInIsSuccess(anyString())).thenReturn(true);
        when(signInService.getNextSignInPoint(anyString())).thenReturn(10);
        BaseParamDto baseParamDto = new BaseParamDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("signTest");
        baseParamDto.setBaseParam(baseParam);
        BaseResponseDto baseResponseDto = mobileAppPointService.getLastSignInTime(baseParamDto);
        assertNotNull(baseResponseDto);
        LastSignInTimeResponseDataDto lstSignInTimeResponseDataDto = (LastSignInTimeResponseDataDto) baseResponseDto.getData();
        assertEquals(lstSignInTimeResponseDataDto.getSignInTimes(), 1);
        assertEquals(lstSignInTimeResponseDataDto.getNextSignInPoint(), 10);

        signInPointDto.setSignInDate(DateUtils.addDays(new DateTime().withTimeAtStartOfDay().toDate(), -2));
        signInPointDto.setNextSignInPoint(10);
        signInPointDto.setSignInCount(0);
        baseResponseDto = mobileAppPointService.getLastSignInTime(baseParamDto);
        lstSignInTimeResponseDataDto = (LastSignInTimeResponseDataDto) baseResponseDto.getData();
        assertNotNull(baseResponseDto);
        assertEquals(lstSignInTimeResponseDataDto.getSignInTimes(), 0);
        assertEquals(lstSignInTimeResponseDataDto.getNextSignInPoint(), 10);

        signInPointDto.setSignInDate(new DateTime().withTimeAtStartOfDay().toDate());
        signInPointDto.setNextSignInPoint(10);
        signInPointDto.setSignInCount(0);
        baseResponseDto = mobileAppPointService.getLastSignInTime(baseParamDto);
        lstSignInTimeResponseDataDto = (LastSignInTimeResponseDataDto) baseResponseDto.getData();
        assertNotNull(baseResponseDto);
        assertEquals(lstSignInTimeResponseDataDto.getSignInTimes(), 0);
        assertEquals(lstSignInTimeResponseDataDto.getNextSignInPoint(), 10);
    }
}
