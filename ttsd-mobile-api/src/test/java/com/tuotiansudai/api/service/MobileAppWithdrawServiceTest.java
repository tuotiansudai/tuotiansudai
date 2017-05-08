package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.BaseParamTest;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppWithdrawServiceImpl;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.WithdrawDto;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.enums.WithdrawStatus;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.mapper.WithdrawMapper;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.BankCardStatus;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.WithdrawModel;
import com.tuotiansudai.service.BlacklistService;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

public class MobileAppWithdrawServiceTest extends ServiceTestBase {
    @InjectMocks
    private MobileAppWithdrawServiceImpl mobileAppWithdrawService;
    @Mock
    private BankCardMapper bankCardMapper;
    @Mock
    private PayWrapperClient payWrapperClient;
    @Mock
    private BlacklistService blacklistService;
    @Mock
    private WithdrawMapper withdrawMapper;
    @Mock
    private PageValidUtils pageValidUtils;

    @Test
    public void shouldGenerateWithdrawRequestIsOk() {
        WithdrawOperateRequestDto withdrawOperateRequestDto = new WithdrawOperateRequestDto();
        withdrawOperateRequestDto.setMoney(10);
        withdrawOperateRequestDto.setBaseParam(BaseParamTest.getInstance());

        BankCardModel bankCardModel = new BankCardModel();
        bankCardModel.setBankCode("ICBC");
        bankCardModel.setStatus(BankCardStatus.PASSED);
        bankCardModel.setCreatedTime(new Date());
        bankCardModel.setLoginName("loginName");
        bankCardModel.setIsFastPayOn(true);
        bankCardModel.setCardNumber("1234567890");

        BaseDto<PayFormDataDto> formDto = new BaseDto<>();
        PayFormDataDto dataDto = new PayFormDataDto();
        dataDto.setUrl("url");
        dataDto.setStatus(true);
        formDto.setData(dataDto);

        when(bankCardMapper.findPassedBankCardByLoginName(anyString())).thenReturn(bankCardModel);

        when(payWrapperClient.withdraw(any(WithdrawDto.class))).thenReturn(formDto);

        when(blacklistService.userIsInBlacklist(anyString())).thenReturn(false);

        BaseResponseDto<WithdrawOperateResponseDataDto> baseResponseDto = mobileAppWithdrawService.generateWithdrawRequest(withdrawOperateRequestDto);

        assertTrue(baseResponseDto.isSuccess());
        assertEquals("url", baseResponseDto.getData().getUrl());

    }

    @Test
    public void shouldQueryUserWithdrawLogsIsOk() {
        WithdrawModel withdrawModel1 = fakeWithDrawModel();
        WithdrawModel withdrawModel2 = fakeWithDrawModel();
        withdrawModel2.setStatus(WithdrawStatus.FAIL);
        List<WithdrawModel> withdrawModels = Lists.newArrayList();
        withdrawModels.add(withdrawModel1);
        withdrawModels.add(withdrawModel2);
        when(withdrawMapper.findWithdrawCount(anyString(), anyString(), any(WithdrawStatus.class), any(Source.class), any(Date.class), any(Date.class), anyString())).thenReturn(2);
        when(withdrawMapper.findWithdrawPagination(anyString(), anyString(),
                any(WithdrawStatus.class), any(Source.class), anyInt(), anyInt(), any(Date.class), any(Date.class), anyString())).thenReturn(withdrawModels);
        when(pageValidUtils.validPageSizeLimit(anyInt())).thenReturn(10);
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("loginName");
        WithdrawListRequestDto withdrawListRequestDto = new WithdrawListRequestDto();
        withdrawListRequestDto.setBaseParam(baseParam);
        BaseResponseDto<WithdrawListResponseDataDto> baseDto = mobileAppWithdrawService.queryUserWithdrawLogs(withdrawListRequestDto);
        assertTrue(baseDto.isSuccess());
        assertEquals("recheck", baseDto.getData().getWithdrawList().get(0).getStatus());
        assertEquals("recheck_fail",baseDto.getData().getWithdrawList().get(1).getStatus());

    }

    private WithdrawModel fakeWithDrawModel() {
        WithdrawModel withdrawModel = new WithdrawModel();
        withdrawModel.setId(IdGenerator.generate());
        withdrawModel.setLoginName("loginName");
        withdrawModel.setAmount(40000L);
        withdrawModel.setApplyNotifyMessage("verify_message");
        withdrawModel.setApplyNotifyTime(new Date());
        withdrawModel.setNotifyMessage("recheck_message");
        withdrawModel.setNotifyTime(new Date());
        withdrawModel.setCreatedTime(new Date());
        withdrawModel.setSource(Source.IOS);
        withdrawModel.setStatus(WithdrawStatus.APPLY_SUCCESS);
        return withdrawModel;
    }

}
