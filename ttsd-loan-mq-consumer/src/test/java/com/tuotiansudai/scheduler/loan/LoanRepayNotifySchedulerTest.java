package com.tuotiansudai.scheduler.loan;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.sms.SmsDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.LoanRepayNotifyModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class LoanRepayNotifySchedulerTest {

    @InjectMocks
    private LoanRepayNotifyScheduler loanRepayService;

    @Mock
    private LoanRepayMapper loanRepayMapper;

    @Mock
    private PayWrapperClient payWrapperClient;

    @Mock
    private MQWrapperClient mqWrapperClient;

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

        when(payWrapperClient.autoRepay(anyLong())).thenReturn(getAutoRepayReturnDto(false));

        ReflectionTestUtils.setField(loanRepayService, "repayRemindMobileList", Lists.newArrayList("18611445119", "18611112222"));

        loanRepayService.loanRepayNotify();

        verify(mqWrapperClient, times(4)).sendMessage(eq(MessageQueue.UserSms), any(SmsDto.class));

    }

    @Test
    public void shouldSumAmountSendLoanRepayNotifySms() {

        LoanRepayNotifyModel repayNotifyModel1 = this.getFakeLoanRepayNotify(100000, "loginName1", "13911111111");
        LoanRepayNotifyModel repayNotifyModel2 = this.getFakeLoanRepayNotify(200000, "loginName1", "13911111111");
        List<LoanRepayNotifyModel> repayNotifyModelList = Lists.newArrayList(repayNotifyModel1, repayNotifyModel2);

        when(loanRepayMapper.findLoanRepayNotifyToday(any(String.class))).thenReturn(repayNotifyModelList);

        when(payWrapperClient.autoRepay(anyLong())).thenReturn(getAutoRepayReturnDto(false));

        ReflectionTestUtils.setField(loanRepayService, "repayRemindMobileList", Lists.newArrayList("18611445119", "18611112222"));
        loanRepayService.loanRepayNotify();

        verify(mqWrapperClient, times(3)).sendMessage(eq(MessageQueue.UserSms), any(SmsDto.class));

    }

    @Test
    public void shouldNotSendLoanRepayNotifySms() {

        List<LoanRepayNotifyModel> repayNotifyModelList = new ArrayList<>();

        when(loanRepayMapper.findLoanRepayNotifyToday(any(String.class))).thenReturn(repayNotifyModelList);

        when(payWrapperClient.autoRepay(anyLong())).thenReturn(getAutoRepayReturnDto(false));

        ReflectionTestUtils.setField(loanRepayService, "repayRemindMobileList", Lists.newArrayList("18611445119", "18611112222"));
        loanRepayService.loanRepayNotify();

        verify(mqWrapperClient, times(0)).sendMessage(eq(MessageQueue.UserSms), any(SmsDto.class));

    }

    @Test
    public void shouldNotSendLoanRepayNotifySmsAutoRepaySuccess() {

        LoanRepayNotifyModel repayNotifyModel1 = this.getFakeLoanRepayNotify(100000, "loginName1", "13911111111");
        LoanRepayNotifyModel repayNotifyModel2 = this.getFakeLoanRepayNotify(200000, "loginName1", "13911111111");
        List<LoanRepayNotifyModel> repayNotifyModelList = Lists.newArrayList(repayNotifyModel1, repayNotifyModel2);

        when(loanRepayMapper.findLoanRepayNotifyToday(any(String.class))).thenReturn(repayNotifyModelList);

        when(payWrapperClient.autoRepay(anyLong())).thenReturn(getAutoRepayReturnDto(true));

        ReflectionTestUtils.setField(loanRepayService, "repayRemindMobileList", Lists.newArrayList("18611445119", "18611112222"));
        loanRepayService.loanRepayNotify();

        verify(mqWrapperClient, times(0)).sendMessage(eq(MessageQueue.UserSms), any(SmsDto.class));

    }

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
