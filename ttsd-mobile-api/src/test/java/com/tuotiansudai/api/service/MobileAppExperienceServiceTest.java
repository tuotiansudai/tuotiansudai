package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppExperienceBillServiceImpl;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.repository.mapper.ExperienceBillMapper;
import com.tuotiansudai.repository.model.ExperienceBillModel;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class MobileAppExperienceServiceTest extends ServiceTestBase {

    @InjectMocks
    private MobileAppExperienceBillServiceImpl mobileAppExperienceBillService;

    @Mock
    private ExperienceBillMapper experienceBillMapper;

    @Mock
    private PageValidUtils pageValidUtils;

    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void shouldQueryExperienceBillsIsOk() {
        ExperienceBillModel experienceBillModel = new ExperienceBillModel();
        experienceBillModel.setId(idGenerator.generate());
        experienceBillModel.setLoginName("admin");
        experienceBillModel.setOperationType(ExperienceBillOperationType.IN);
        experienceBillModel.setBusinessType(ExperienceBillBusinessType.REGISTER);
        experienceBillModel.setAmount(2000);
        experienceBillModel.setNote("新用户注册");

        List<ExperienceBillModel> experienceBillModelList = Lists.newArrayList();
        experienceBillModelList.add(experienceBillModel);
        when(experienceBillMapper.findExperienceBillPagination(anyString(), anyString(), anyInt(), anyInt())).thenReturn(experienceBillModelList);
        when(experienceBillMapper.findCountExperienceBillPagination(anyString(), anyString())).thenReturn(1L);
        when(pageValidUtils.validPageSizeLimit(anyInt())).thenReturn(10);

        ExperienceBillRequestDto experienceBillRequestDto = new ExperienceBillRequestDto();
        experienceBillRequestDto.setIndex(1);
        experienceBillRequestDto.setPageSize(10);
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("admin");
        experienceBillRequestDto.setBaseParam(baseParam);

        BaseResponseDto<ExperienceBillResponseDataDto> baseResponseDto = mobileAppExperienceBillService.queryExperienceBillList(experienceBillRequestDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
        assertEquals(ExperienceBillOperationType.IN.getDescription(), baseResponseDto.getData().getExperienceBills().get(0).getOperationType());
        assertEquals(ExperienceBillBusinessType.REGISTER.getDescription(), baseResponseDto.getData().getExperienceBills().get(0).getBusinessType());
        assertEquals(2000, Long.parseLong(baseResponseDto.getData().getExperienceBills().get(0).getAmount()));
    }
}
