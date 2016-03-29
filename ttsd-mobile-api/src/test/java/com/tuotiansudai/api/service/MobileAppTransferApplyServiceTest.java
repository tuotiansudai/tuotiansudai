package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.impl.MobileAppAgreementServiceImpl;
import com.tuotiansudai.api.service.impl.MobileAppTransferApplyServiceImpl;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.transfer.dto.TransferApplicationDto;
import com.tuotiansudai.transfer.service.InvestTransferService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class MobileAppTransferApplyServiceTest extends ServiceTestBase{

    @InjectMocks
    private MobileAppTransferApplyServiceImpl mobileAppTransferApplyService;
    @Mock
    private InvestTransferService investTransferService;

    @Test
    public void shouldTransferApplyIsSuccess() throws Exception {
        doNothing().when(investTransferService).investTransferApply(any(TransferApplicationDto.class));
        TransferApplicationRequestDto transferApplicationRequestDto = new TransferApplicationRequestDto();
        transferApplicationRequestDto.setTransferInvestId("123");
        transferApplicationRequestDto.setTransferAmount("1.00");
        transferApplicationRequestDto.setTransferInterest(true);
        BaseResponseDto baseResponseDto =  mobileAppTransferApplyService.transferApply(transferApplicationRequestDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(),baseResponseDto.getCode());
    }

}
