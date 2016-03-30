package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.impl.MobileAppTransferApplyServiceImpl;
import com.tuotiansudai.transfer.dto.TransferApplicationDto;
import com.tuotiansudai.transfer.service.InvestTransferService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;

public class MobileAppTransferApplyServiceTest extends ServiceTestBase{

    @InjectMocks
    private MobileAppTransferApplyServiceImpl mobileAppTransferApplyService;
    @Mock
    private InvestTransferService investTransferService;

    @Test
    public void shouldTransferApplyIsSuccess() throws Exception {
        doNothing().when(investTransferService).investTransferApply(any(TransferApplicationDto.class));
        TransferApplyRequestDto transferApplyRequestDto = new TransferApplyRequestDto();
        transferApplyRequestDto.setTransferInvestId("123");
        transferApplyRequestDto.setTransferAmount("1.00");
        transferApplyRequestDto.setTransferInterest(true);
        BaseResponseDto baseResponseDto =  mobileAppTransferApplyService.transferApply(transferApplyRequestDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(),baseResponseDto.getCode());
    }

}
