package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v1_0.UserBillDetailListRequestDto;
import com.tuotiansudai.api.dto.v1_0.UserBillDetailListResponseDataDto;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppUserBillListServiceImpl;
import com.tuotiansudai.repository.mapper.UserBillMapper;
import com.tuotiansudai.repository.model.UserBillBusinessType;
import com.tuotiansudai.repository.model.UserBillModel;
import com.tuotiansudai.repository.model.UserBillOperationType;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.when;

public class MobileAppUserBillListServiceServiceTest extends ServiceTestBase{
    @InjectMocks
    private MobileAppUserBillListServiceImpl mobileAppUserBillListService;
    @Mock
    private UserBillMapper userBillMapper;
    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void shouldQueryUserBillListIsOk(){
        UserBillModel userBillModel = new UserBillModel();
        userBillModel.setId(idGenerator.generate());
        userBillModel.setAmount(1000L);
        userBillModel.setBalance(1100L);
        userBillModel.setFreeze(1200l);
        userBillModel.setBusinessType(UserBillBusinessType.RECHARGE_SUCCESS);
        userBillModel.setOperationType(UserBillOperationType.TI_BALANCE);
        userBillModel.setLoginName("admin");
        userBillModel.setOrderId(idGenerator.generate());

        List<UserBillModel> userBillModelList = Lists.newArrayList();
        userBillModelList.add(userBillModel);
        when(userBillMapper.findUserBills(anyMap())).thenReturn(userBillModelList);
        when(userBillMapper.findUserBillsCount(anyMap())).thenReturn(1);
        UserBillDetailListRequestDto userBillDetailListRequestDto = new UserBillDetailListRequestDto();
        userBillDetailListRequestDto.setPageSize(10);
        userBillDetailListRequestDto.setIndex(1);
        userBillDetailListRequestDto.setUserId("admin");
        BaseResponseDto<UserBillDetailListResponseDataDto> baseDto = mobileAppUserBillListService.queryUserBillList(userBillDetailListRequestDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(),baseDto.getCode());
        assertEquals("recharge_success",baseDto.getData().getUserBillList().get(0).getTypeInfo());
        assertEquals(1,baseDto.getData().getTotalCount().intValue());
        assertEquals("+10.00",baseDto.getData().getUserBillList().get(0).getMoney());
    }


}
