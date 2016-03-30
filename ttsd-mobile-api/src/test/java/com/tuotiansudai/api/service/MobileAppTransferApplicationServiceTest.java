package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.impl.MobileAppTransferApplicationServiceImpl;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationRecordDto;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;


public class MobileAppTransferApplicationServiceTest extends ServiceTestBase{

    @InjectMocks
    private MobileAppTransferApplicationServiceImpl mobileAppTransferApplicationService;

    @Mock
    private TransferApplicationMapper transferApplicationMapper;
    @Test
    public void shouldGenerateTransferApplicationIsSuccess() {
        TransferApplicationRecordDto transferApplicationRecordDto = createTransferApplicationRecordDto();
        TransferApplicationRequestDto transferApplicationRequestDto = new TransferApplicationRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("test");
        transferApplicationRequestDto.setBaseParam(baseParam);
        transferApplicationRequestDto.setPageSize(10);
        transferApplicationRequestDto.setIndex(1);
        transferApplicationRequestDto.setTransferStatus(Lists.newArrayList(TransferStatus.TRANSFERRING));
        List<TransferApplicationRecordDto> transferApplicationRecordDtos = Lists.newArrayList(transferApplicationRecordDto);

        when(transferApplicationMapper.findTransferApplicationPaginationByLoginName(anyString(),any(List.class),anyInt(),anyInt())).thenReturn(transferApplicationRecordDtos);
        when(transferApplicationMapper.findCountTransferApplicationPaginationByLoginName(anyString(), any(List.class))).thenReturn(1);

        BaseResponseDto<TransferApplicationResponseDataDto> baseResponseDto =  mobileAppTransferApplicationService.generateTransferApplication(transferApplicationRequestDto);
        assertEquals(TransferStatus.TRANSFERRING,baseResponseDto.getData().getTransferApplication().get(0).getTransferStatus());
        assertEquals("17",baseResponseDto.getData().getTransferApplication().get(0).getActivityRate());
        assertEquals("16",baseResponseDto.getData().getTransferApplication().get(0).getBaseRate());
        assertEquals("name",baseResponseDto.getData().getTransferApplication().get(0).getName());
        assertEquals("10.00",baseResponseDto.getData().getTransferApplication().get(0).getTransferAmount());
        assertEquals("12.00",baseResponseDto.getData().getTransferApplication().get(0).getInvestAmount());
        assertEquals("2016-02-09 00:00:00",baseResponseDto.getData().getTransferApplication().get(0).getTransferTime());
        assertEquals("25",baseResponseDto.getData().getTransferApplication().get(0).getTransferInterestDays());

    }

    private TransferApplicationRecordDto createTransferApplicationRecordDto(){
        TransferApplicationRecordDto transferApplicationRecordDto = new TransferApplicationRecordDto();
        transferApplicationRecordDto.setName("name");
        transferApplicationRecordDto.setTransferAmount(1000);
        transferApplicationRecordDto.setInvestAmount(1200);
        transferApplicationRecordDto.setTransferTime(new DateTime("2016-02-09").toDate());
        transferApplicationRecordDto.setBaseRate(0.16);
        transferApplicationRecordDto.setActivityRate(0.17);
        transferApplicationRecordDto.setTransferInterestDays(25);
        transferApplicationRecordDto.setTransferStatus(TransferStatus.TRANSFERRING);
        return transferApplicationRecordDto;

    }

}
