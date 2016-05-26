package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppRechargeListServiceImpl;
import com.tuotiansudai.repository.mapper.RechargeMapper;
import com.tuotiansudai.repository.model.RechargeModel;
import com.tuotiansudai.repository.model.RechargeSource;
import com.tuotiansudai.repository.model.RechargeStatus;
import com.tuotiansudai.repository.model.Source;
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

public class MobileAppRechargeListServiceTest extends ServiceTestBase {
    @InjectMocks
    private MobileAppRechargeListServiceImpl mobileAppRechargeListService;
    @Mock
    private RechargeMapper rechargeMapper;
    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void shouldGenerateRechargeListIsOk() {
        RechargeModel rechargeModel1 = fakeRechargeModel("loginName");
        RechargeModel rechargeModel2 = fakeRechargeModel("loginName");
        List<RechargeModel> rechargeModels = Lists.newArrayList();
        rechargeModels.add(rechargeModel1);
        rechargeModels.add(rechargeModel2);
        when(rechargeMapper.findRechargePagination(anyString(), anyString(), any(RechargeSource.class),
                any(RechargeStatus.class), anyString(), anyInt(), anyInt(), any(Date.class), any(Date.class))).thenReturn(rechargeModels);

        when(rechargeMapper.findRechargeCount(anyString(), anyString(), any(RechargeSource.class),
                any(RechargeStatus.class), anyString(), any(Date.class), any(Date.class))).thenReturn(2);
        RechargeListRequestDto rechargeListRequestDto = new RechargeListRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("loginName");
        rechargeListRequestDto.setIndex(1);
        rechargeListRequestDto.setPageSize(1);
        rechargeListRequestDto.setBaseParam(baseParam);
        BaseResponseDto<RechargeListResponseDataDto> dto = mobileAppRechargeListService.generateRechargeList(rechargeListRequestDto);

        assertEquals(ReturnMessage.SUCCESS.getCode(),dto.getCode());
        assertEquals("12.00",dto.getData().getRechargeList().get(0).getActualMoney());


    }

    private RechargeModel fakeRechargeModel(String loginName) {
        RechargeModel rechargeModel = new RechargeModel();
        rechargeModel.setId(idGenerator.generate());
        rechargeModel.setLoginName("loginName");
        rechargeModel.setAmount(1200l);
        rechargeModel.setBankCode("CMB");
        rechargeModel.setStatus(RechargeStatus.SUCCESS);
        rechargeModel.setSource(Source.WEB);
        rechargeModel.setFastPay(true);
        rechargeModel.setCreatedTime(new Date());
        return rechargeModel;

    }
}
