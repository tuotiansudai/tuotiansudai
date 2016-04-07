package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanRepayNotifyDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.LoanRepayNotifyModel;
import com.tuotiansudai.service.impl.LoanRepayServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class LoanRepayServiceTest {

    //TODO: 合并自动还款的代码后，将本测试类中的代码解注即可

    @InjectMocks
    private LoanRepayServiceImpl loanRepayService;

    @Mock
    private LoanRepayMapper loanRepayMapper;

    @Mock
    private PayWrapperClient payWrapperClient;

    @Mock
    private SmsWrapperClient smsWrapperClient;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldSendLoanRepayNotifySms() {

        LoanRepayNotifyModel repayNotifyModel1 = this.getFakeLoanRepayNotify(100000, "loginName1", "13911111111");
        LoanRepayNotifyModel repayNotifyModel2 = this.getFakeLoanRepayNotify(200000, "loginName2", "13922222222");
        List<LoanRepayNotifyModel> repayNotifyModelList = Lists.newArrayList(repayNotifyModel1, repayNotifyModel2);

        when(loanRepayMapper.findLoanRepayNotifyToday(any(String.class))).thenReturn(repayNotifyModelList);

        when(smsWrapperClient.sendLoanRepayNotify(any(LoanRepayNotifyDto.class))).thenReturn(new BaseDto<SmsDataDto>());

        when(payWrapperClient.autoRepay(anyLong())).thenReturn(getAutoRepayReturnDto(false));

        ReflectionTestUtils.setField(loanRepayService, "repayRemindMobileList", Lists.newArrayList("18611445119", "18611112222"));

        loanRepayService.loanRepayNotify();

        verify(smsWrapperClient, times(4)).sendLoanRepayNotify(any(LoanRepayNotifyDto.class));

    }

    @Test
    public void shouldSumAmountSendLoanRepayNotifySms() {

        LoanRepayNotifyModel repayNotifyModel1 = this.getFakeLoanRepayNotify(100000, "loginName1", "13911111111");
        LoanRepayNotifyModel repayNotifyModel2 = this.getFakeLoanRepayNotify(200000, "loginName1", "13911111111");
        List<LoanRepayNotifyModel> repayNotifyModelList = Lists.newArrayList(repayNotifyModel1, repayNotifyModel2);

        when(loanRepayMapper.findLoanRepayNotifyToday(any(String.class))).thenReturn(repayNotifyModelList);

        when(smsWrapperClient.sendLoanRepayNotify(any(LoanRepayNotifyDto.class))).thenReturn(new BaseDto<SmsDataDto>());

        when(payWrapperClient.autoRepay(anyLong())).thenReturn(getAutoRepayReturnDto(false));

        ReflectionTestUtils.setField(loanRepayService, "repayRemindMobileList", Lists.newArrayList("18611445119", "18611112222"));
        loanRepayService.loanRepayNotify();

        verify(smsWrapperClient, times(3)).sendLoanRepayNotify(any(LoanRepayNotifyDto.class));

    }

    @Test
    public void shouldNotSendLoanRepayNotifySms() {

        List<LoanRepayNotifyModel> repayNotifyModelList = new ArrayList<>();

        when(loanRepayMapper.findLoanRepayNotifyToday(any(String.class))).thenReturn(repayNotifyModelList);

        when(smsWrapperClient.sendLoanRepayNotify(any(LoanRepayNotifyDto.class))).thenReturn(new BaseDto<SmsDataDto>());

        when(payWrapperClient.autoRepay(anyLong())).thenReturn(getAutoRepayReturnDto(false));

        ReflectionTestUtils.setField(loanRepayService, "repayRemindMobileList", Lists.newArrayList("18611445119", "18611112222"));
        loanRepayService.loanRepayNotify();

        verify(smsWrapperClient, times(0)).sendLoanRepayNotify(any(LoanRepayNotifyDto.class));

    }

//    @Test
//    public void shouldNotSendLoanRepayNotifySmsAutoRepaySuccess() {
//
//        LoanRepayNotifyModel repayNotifyModel1 = this.getFakeLoanRepayNotify(100000, "loginName1", "13911111111", 111111111);
//        LoanRepayNotifyModel repayNotifyModel2 = this.getFakeLoanRepayNotify(200000, "loginName1", "13911111111", 222222222);
//        List<LoanRepayNotifyModel> repayNotifyModelList = Lists.newArrayList(repayNotifyModel1, repayNotifyModel2);
//
//        when(loanRepayMapper.findLoanRepayNotifyToday(any(String.class))).thenReturn(repayNotifyModelList);
//
//        when(smsWrapperClient.sendLoanRepayNotify(any(LoanRepayNotifyDto.class))).thenReturn(new BaseDto<SmsDataDto>());
//
//        when(payWrapperClient.autoRepay(anyLong())).thenReturn(getAutoRepayReturnDto(true));
//
//        ReflectionTestUtils.setField(loanRepayService, "repayRemindMobileList", Lists.newArrayList("18611445119", "18611112222"));
//        loanRepayService.loanRepayNotify();
//
//        verify(smsWrapperClient, times(0)).sendLoanRepayNotify(any(LoanRepayNotifyDto.class));
//
//    }

    private BaseDto<PayDataDto> getAutoRepayReturnDto(boolean status) {
        BaseDto<PayDataDto> autoRepayReturn = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        autoRepayReturn.setData(payDataDto);
        payDataDto.setStatus(status);
        autoRepayReturn.setSuccess(status);
        return autoRepayReturn;
    }


    private LoanRepayNotifyModel getFakeLoanRepayNotify(long repayAmount, String loanName, String mobile) {
        LoanRepayNotifyModel repayNotify = new LoanRepayNotifyModel();
        repayNotify.setMobile(mobile);
        repayNotify.setLoanName(loanName);
        repayNotify.setRepayAmount(repayAmount);
        return repayNotify;
    }

}
