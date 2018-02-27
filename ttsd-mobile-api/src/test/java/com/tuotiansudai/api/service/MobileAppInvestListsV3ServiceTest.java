package com.tuotiansudai.api.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v3_0.UserInvestListRequestDto;
import com.tuotiansudai.api.dto.v3_0.UserInvestListResponseDataDto;
import com.tuotiansudai.api.service.v3_0.impl.MobileAppInvestListsV3ServiceImpl;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.UserInvestRecordDataDto;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.service.InvestService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

public class MobileAppInvestListsV3ServiceTest extends ServiceTestBase {

    @InjectMocks
    private MobileAppInvestListsV3ServiceImpl mobileAppInvestListsV3Service;

    @Mock
    private InvestService investService;

    @Test
    public void shouldGenerateUserInvestList() {
        List<UserInvestRecordDataDto> dataDtoList = Lists.newArrayList();
        UserInvestRecordDataDto userInvestRecordDataDto = getFakeUserInvestRecordDataDto();
        dataDtoList.add(userInvestRecordDataDto);

        BasePaginationDataDto<UserInvestRecordDataDto> basePaginationDataDto = new BasePaginationDataDto<>(1, 10, 1, dataDtoList);

        when(investService.generateUserInvestList(anyString(), anyInt(), anyInt(), any(LoanStatus.class))).thenReturn(basePaginationDataDto);


        UserInvestListRequestDto investListRequestDto = new UserInvestListRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("investor");
        investListRequestDto.setBaseParam(baseParam);
        investListRequestDto.setIndex(1);
        investListRequestDto.setPageSize(10);
        investListRequestDto.setStatus(LoanStatus.COMPLETE);

        BaseResponseDto<UserInvestListResponseDataDto> baseResponseDto = mobileAppInvestListsV3Service.generateUserInvestList(investListRequestDto);

        assertEquals(baseResponseDto.getData().getInvestList().get(0).getLoanId(), userInvestRecordDataDto.getLoanId());
        assertEquals(baseResponseDto.getData().getInvestList().get(0).getInvestId(), userInvestRecordDataDto.getInvestId());
        assertEquals(baseResponseDto.getData().getInvestList().get(0).getExpectedInterest(), userInvestRecordDataDto.getExpectedInterest());
        assertEquals(baseResponseDto.getData().getInvestList().get(0).getActualInterest(), userInvestRecordDataDto.getActualInterest());
        assertEquals(baseResponseDto.getData().getInvestList().get(0).isUsedCoupon(), userInvestRecordDataDto.isUsedCoupon());
        assertEquals(baseResponseDto.getData().getInvestList().get(0).isUsedRedEnvelope(), userInvestRecordDataDto.isUsedRedEnvelope());
        assertEquals(baseResponseDto.getData().getInvestList().get(0).getProductNewType(), userInvestRecordDataDto.getProductNewType());
        assertEquals(baseResponseDto.getData().getInvestList().get(0).getRepayProgress(), userInvestRecordDataDto.getRepayProgress());
    }

    public UserInvestRecordDataDto getFakeUserInvestRecordDataDto() {
        UserInvestRecordDataDto userInvestRecordDataDto = new UserInvestRecordDataDto();
        userInvestRecordDataDto.setLoanId("1234567890");
        userInvestRecordDataDto.setLoanName("loanName");
        userInvestRecordDataDto.setInvestId("111111111");
        userInvestRecordDataDto.setExpectedInterest("100");
        userInvestRecordDataDto.setActualInterest("1100");
        userInvestRecordDataDto.setUsedCoupon(false);
        userInvestRecordDataDto.setUsedRedEnvelope(false);
        userInvestRecordDataDto.setProductNewType("_30");
        userInvestRecordDataDto.setRepayProgress(100);
        return userInvestRecordDataDto;
    }
}
