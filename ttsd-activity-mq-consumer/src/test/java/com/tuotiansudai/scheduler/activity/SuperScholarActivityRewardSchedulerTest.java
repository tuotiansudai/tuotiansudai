package com.tuotiansudai.scheduler.activity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.mapper.ActivityInvestMapper;
import com.tuotiansudai.activity.repository.mapper.SuperScholarRewardMapper;
import com.tuotiansudai.activity.repository.model.ActivityInvestModel;
import com.tuotiansudai.activity.repository.model.SuperScholarRewardModel;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.util.RedisWrapperClient;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class SuperScholarActivityRewardSchedulerTest {

    @InjectMocks
    private SuperScholarActivityRewardScheduler superScholarActivityRewardScheduler;

    @Mock
    private PayWrapperClient payWrapperClient;

    @Mock
    private SuperScholarRewardMapper superScholarRewardMapper;

    @Mock
    private ActivityInvestMapper activityInvestMapper;

    @Mock
    private RedisWrapperClient redisWrapperClient;

    @Mock
    private SmsWrapperClient smsWrapperClient;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        Field redisWrapperClientField = this.superScholarActivityRewardScheduler.getClass().getDeclaredField("redisWrapperClient");
        redisWrapperClientField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(redisWrapperClientField, redisWrapperClientField.getModifiers() & ~Modifier.FINAL);
        redisWrapperClientField.set(this.superScholarActivityRewardScheduler, this.redisWrapperClient);
    }

    @Test
    public void sendCashSuccess(){
        ArgumentCaptor<String> redisDelKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisDelHKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisSetKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisSetValueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<TransferCashDto> transferCashDtoCaptor = ArgumentCaptor.forClass(TransferCashDto.class);

        when(redisWrapperClient.hgetAll("SUPER_SCHOLAR_SEND_CASH_LOAN")).thenReturn(fakeLoanId());
        when(activityInvestMapper.findByLoanIdAndActivity(anyLong(), anyString())).thenReturn(fakeActivityInvestModels());
        when(superScholarRewardMapper.findByLoginNameAndAnswerTime(eq("loginName"), any())).thenReturn(fakeSuperScholarRewardModel("loginName"));
        when(superScholarRewardMapper.findByLoginNameAndAnswerTime(eq("loginName1"), any())).thenReturn(fakeSuperScholarRewardModel("loginName1"));
        when(redisWrapperClient.exists(anyString())).thenReturn(false);
        when(payWrapperClient.transferCash(any(TransferCashDto.class))).thenReturn(new BaseDto(new PayDataDto(true)));

        superScholarActivityRewardScheduler.sendSuperScholarReward();

        verify(this.redisWrapperClient, times(1)).hdel(redisDelKeyCaptor.capture(), redisDelHKeyCaptor.capture());
        verify(this.payWrapperClient, times(3)).transferCash(transferCashDtoCaptor.capture());
        verify(this.redisWrapperClient, times(3)).setex(redisSetKeyCaptor.capture(), anyInt(), redisSetValueCaptor.capture());

        assertThat(redisDelKeyCaptor.getValue(), is("SUPER_SCHOLAR_SEND_CASH_LOAN"));
        assertThat(redisDelHKeyCaptor.getValue(), is("201805"));
        assertThat(transferCashDtoCaptor.getAllValues().get(0).getLoginName(), is("loginName"));
        assertThat(transferCashDtoCaptor.getAllValues().get(0).getAmount(), is("150"));
        assertThat(transferCashDtoCaptor.getAllValues().get(1).getLoginName(), is("loginName1"));
        assertThat(transferCashDtoCaptor.getAllValues().get(1).getAmount(), is("300"));
        assertThat(transferCashDtoCaptor.getAllValues().get(2).getLoginName(), is("loginName1"));
        assertThat(transferCashDtoCaptor.getAllValues().get(2).getAmount(), is("450"));
        assertThat(redisSetKeyCaptor.getAllValues().get(0), is("SUPER_SCHOLAR_SEND_USER_CASH:20180501:loginName"));
        assertThat(redisSetKeyCaptor.getAllValues().get(1), is("SUPER_SCHOLAR_SEND_USER_CASH:20180502:loginName1"));
        assertThat(redisSetKeyCaptor.getAllValues().get(2), is("SUPER_SCHOLAR_SEND_USER_CASH:20180503:loginName1"));
    }

    @Test
    public void sendCashFail(){
        ArgumentCaptor<String> redisDelKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisDelHKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisSetKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisSetValueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<TransferCashDto> transferCashDtoCaptor = ArgumentCaptor.forClass(TransferCashDto.class);

        when(redisWrapperClient.hgetAll("SUPER_SCHOLAR_SEND_CASH_LOAN")).thenReturn(fakeLoanId());
        when(activityInvestMapper.findByLoanIdAndActivity(anyLong(), anyString())).thenReturn(fakeActivityInvestModels());
        when(superScholarRewardMapper.findByLoginNameAndAnswerTime(eq("loginName"), any())).thenReturn(fakeSuperScholarRewardModel("loginName"));
        when(superScholarRewardMapper.findByLoginNameAndAnswerTime(eq("loginName1"), any())).thenReturn(fakeSuperScholarRewardModel("loginName1"));
        when(redisWrapperClient.exists(anyString())).thenReturn(false);
        when(payWrapperClient.transferCash(any(TransferCashDto.class))).thenReturn(new BaseDto(new PayDataDto(false)));

        superScholarActivityRewardScheduler.sendSuperScholarReward();

        verify(this.redisWrapperClient, times(1)).hdel(redisDelKeyCaptor.capture(), redisDelHKeyCaptor.capture());
        verify(this.payWrapperClient, times(3)).transferCash(transferCashDtoCaptor.capture());
        verify(this.redisWrapperClient, times(3)).setex(redisSetKeyCaptor.capture(), anyInt(), redisSetValueCaptor.capture());
        verify(this.smsWrapperClient, times(3)).sendFatalNotify(any(SmsFatalNotifyDto.class));

        assertThat(redisDelKeyCaptor.getValue(), is("SUPER_SCHOLAR_SEND_CASH_LOAN"));
        assertThat(redisDelHKeyCaptor.getValue(), is("201805"));
        assertThat(transferCashDtoCaptor.getAllValues().get(0).getLoginName(), is("loginName"));
        assertThat(transferCashDtoCaptor.getAllValues().get(0).getAmount(), is("150"));
        assertThat(transferCashDtoCaptor.getAllValues().get(1).getLoginName(), is("loginName1"));
        assertThat(transferCashDtoCaptor.getAllValues().get(1).getAmount(), is("300"));
        assertThat(transferCashDtoCaptor.getAllValues().get(2).getLoginName(), is("loginName1"));
        assertThat(transferCashDtoCaptor.getAllValues().get(2).getAmount(), is("450"));
        assertThat(redisSetValueCaptor.getAllValues().get(0), is("fail"));
        assertThat(redisSetValueCaptor.getAllValues().get(1), is("fail"));
        assertThat(redisSetValueCaptor.getAllValues().get(2), is("fail"));
    }

    private Map<String, String> fakeLoanId(){
        return Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("201805", DateTime.now().minusDays(2).toString("yyyy-MM-dd HH:mm:ss"))
                .build());
    }

    public List<ActivityInvestModel> fakeActivityInvestModels(){
        return Lists.newArrayList(new ActivityInvestModel(201805, 20180501, "loginName", "userName", "mobile", 10000, 10000, null),
                new ActivityInvestModel(201805, 20180502, "loginName1", "userName1", "mobile1", 20000, 20000, null),
                new ActivityInvestModel(201805, 20180503, "loginName1", "userName1", "mobile1", 30000, 30000, null));
    }

    public SuperScholarRewardModel fakeSuperScholarRewardModel(String loginName){
        SuperScholarRewardModel superScholarRewardModel = new SuperScholarRewardModel(loginName, "1,2,3,4,5", "A,A,A,A,A");
        superScholarRewardModel.setUserAnswer("A,A,A,A,A");
        superScholarRewardModel.setUserRight(5);
        superScholarRewardModel.setShareHome(true);
        superScholarRewardModel.setShareInvest(true);
        superScholarRewardModel.setShareAccount(true);
        return superScholarRewardModel;
    }

}
