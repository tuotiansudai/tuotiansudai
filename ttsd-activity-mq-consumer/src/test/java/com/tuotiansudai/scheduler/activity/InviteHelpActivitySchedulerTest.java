package com.tuotiansudai.scheduler.activity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.dto.InviteHelpActivityPayCashDto;
import com.tuotiansudai.activity.repository.mapper.WeChatHelpInfoMapper;
import com.tuotiansudai.activity.repository.mapper.WeChatHelpMapper;
import com.tuotiansudai.activity.repository.model.*;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class InviteHelpActivitySchedulerTest {

    @InjectMocks
    private InviteHelpActivityScheduler inviteHelpActivityScheduler;

    @Mock
    private PayWrapperClient payWrapperClient;

    @Mock
    private SmsWrapperClient smsWrapperClient;

    @Mock
    private WeChatHelpMapper weChatHelpMapper;

    @Mock
    private WeChatHelpInfoMapper weChatHelpInfoMapper;

    @Mock
    private RedisWrapperClient redisWrapperClient;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        Field redisWrapperClientField = this.inviteHelpActivityScheduler.getClass().getDeclaredField("redisWrapperClient");
        redisWrapperClientField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(redisWrapperClientField, redisWrapperClientField.getModifiers() & ~Modifier.FINAL);
        redisWrapperClientField.set(this.inviteHelpActivityScheduler, this.redisWrapperClient);
    }

    @Test
    public void sendInvestHelpAllSuccess(){
        ArgumentCaptor<String> redisDelKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisDelHKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<TransferCashDto> transferCashDtoCaptor = ArgumentCaptor.forClass(TransferCashDto.class);
        ArgumentCaptor<InviteHelpActivityPayCashDto> inviteHelpActivityPayCashDtoCaptor = ArgumentCaptor.forClass(InviteHelpActivityPayCashDto.class);
        ArgumentCaptor<WeChatHelpInfoModel> weChatHelpInfoModelCaptor = ArgumentCaptor.forClass(WeChatHelpInfoModel.class);

        when(redisWrapperClient.hgetAll("INVEST_HELP_WAIT_SEND_CASH")).thenReturn(fakeWaitInvestHelp());
        when(redisWrapperClient.hgetAll("EVERYONE_HELP_WAIT_SEND_CASH")).thenReturn(new HashMap<String, String>());
        when(weChatHelpMapper.findByLoanId(anyLong())).thenReturn(fakeWeChatHelpModelList());
        when(weChatHelpMapper.findByLoanId(anyLong())).thenReturn(fakeWeChatHelpModelList());
        when(weChatHelpInfoMapper.findByHelpId(anyLong())).thenReturn(fakeWeChatHelpInfoModelList());
        when(redisWrapperClient.exists(anyString())).thenReturn(false);
        when(payWrapperClient.transferCash(any(TransferCashDto.class))).thenReturn(new BaseDto(new PayDataDto(true)));
        when(payWrapperClient.InviteHelpActivityTransferCash(any(InviteHelpActivityPayCashDto.class))).thenReturn(new BaseDto(new PayDataDto(true)));

        inviteHelpActivityScheduler.sendCash();

        verify(this.redisWrapperClient, times(1)).hdel(redisDelKeyCaptor.capture(), redisDelHKeyCaptor.capture());
        verify(this.payWrapperClient, times(2)).transferCash(transferCashDtoCaptor.capture());
        verify(this.payWrapperClient, times(4)).InviteHelpActivityTransferCash(inviteHelpActivityPayCashDtoCaptor.capture());
        verify(this.weChatHelpInfoMapper, times(4)).update(weChatHelpInfoModelCaptor.capture());

        assertThat(redisDelKeyCaptor.getValue(), is("INVEST_HELP_WAIT_SEND_CASH"));
        assertThat(redisDelHKeyCaptor.getValue(), is("20181"));
        assertThat(transferCashDtoCaptor.getAllValues().get(0).getLoginName(), is("loginName1"));
        assertThat(transferCashDtoCaptor.getAllValues().get(1).getLoginName(), is("loginName2"));
        assertThat(inviteHelpActivityPayCashDtoCaptor.getAllValues().get(0).getOpenid(), is("openId1"));
        assertThat(inviteHelpActivityPayCashDtoCaptor.getAllValues().get(1).getOpenid(), is("openId2"));
        assertThat(weChatHelpInfoModelCaptor.getAllValues().get(0).getStatus(), is(WeChatHelpUserStatus.SUCCESS));
        assertThat(weChatHelpInfoModelCaptor.getAllValues().get(1).getStatus(), is(WeChatHelpUserStatus.SUCCESS));
    }

    @Test
    public void sendInvestHelpCreatorSuccess(){
        ArgumentCaptor<String> redisDelKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisDelHKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<TransferCashDto> transferCashDtoCaptor = ArgumentCaptor.forClass(TransferCashDto.class);
        ArgumentCaptor<InviteHelpActivityPayCashDto> inviteHelpActivityPayCashDtoCaptor = ArgumentCaptor.forClass(InviteHelpActivityPayCashDto.class);
        ArgumentCaptor<WeChatHelpInfoModel> weChatHelpInfoModelCaptor = ArgumentCaptor.forClass(WeChatHelpInfoModel.class);

        when(redisWrapperClient.hgetAll("INVEST_HELP_WAIT_SEND_CASH")).thenReturn(fakeWaitInvestHelp());
        when(redisWrapperClient.hgetAll("EVERYONE_HELP_WAIT_SEND_CASH")).thenReturn(new HashMap<String, String>());
        when(weChatHelpMapper.findByLoanId(anyLong())).thenReturn(fakeWeChatHelpModelList());
        when(weChatHelpMapper.findByLoanId(anyLong())).thenReturn(fakeWeChatHelpModelList());
        when(weChatHelpInfoMapper.findByHelpId(anyLong())).thenReturn(fakeWeChatHelpInfoModelList());
        when(redisWrapperClient.exists(anyString())).thenReturn(false);
        when(payWrapperClient.transferCash(any(TransferCashDto.class))).thenReturn(new BaseDto(new PayDataDto(true)));
        PayDataDto payDataDto = new PayDataDto();
        payDataDto.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
        payDataDto.setMessage("用户未注册");
        when(payWrapperClient.InviteHelpActivityTransferCash(any(InviteHelpActivityPayCashDto.class))).thenReturn(new BaseDto(payDataDto));

        inviteHelpActivityScheduler.sendCash();

        verify(this.redisWrapperClient, times(1)).hdel(redisDelKeyCaptor.capture(), redisDelHKeyCaptor.capture());
        verify(this.payWrapperClient, times(2)).transferCash(transferCashDtoCaptor.capture());
        verify(this.payWrapperClient, times(4)).InviteHelpActivityTransferCash(inviteHelpActivityPayCashDtoCaptor.capture());
        verify(this.weChatHelpInfoMapper, times(4)).update(weChatHelpInfoModelCaptor.capture());
        verify(this.smsWrapperClient, times(4)).sendFatalNotify(any(SmsFatalNotifyDto.class));

        assertThat(redisDelKeyCaptor.getValue(), is("INVEST_HELP_WAIT_SEND_CASH"));
        assertThat(redisDelHKeyCaptor.getValue(), is("20181"));
        assertThat(transferCashDtoCaptor.getAllValues().get(0).getLoginName(), is("loginName1"));
        assertThat(transferCashDtoCaptor.getAllValues().get(1).getLoginName(), is("loginName2"));
        assertThat(inviteHelpActivityPayCashDtoCaptor.getAllValues().get(0).getOpenid(), is("openId1"));
        assertThat(inviteHelpActivityPayCashDtoCaptor.getAllValues().get(1).getOpenid(), is("openId2"));
        assertThat(weChatHelpInfoModelCaptor.getAllValues().get(0).getStatus(), is(WeChatHelpUserStatus.FAIL));
        assertThat(weChatHelpInfoModelCaptor.getAllValues().get(1).getStatus(), is(WeChatHelpUserStatus.FAIL));
        assertThat(weChatHelpInfoModelCaptor.getAllValues().get(0).getRemark(), is("用户未注册"));
        assertThat(weChatHelpInfoModelCaptor.getAllValues().get(1).getRemark(), is("用户未注册"));
    }

    public Map<String, String> fakeWaitInvestHelp(){
        return Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("20181", DateTime.now().minusDays(2).toString("yyyy-MM-dd HH:mm:ss"))
                .build());
    }

    public List<WeChatHelpModel> fakeWeChatHelpModelList(){
        return Lists.newArrayList(
                fakeWeChatHelpModelByInvest(20181, "loginName1"),
                fakeWeChatHelpModelByInvest(20181, "loginName2")
        );
    }

    public List<WeChatHelpInfoModel> fakeWeChatHelpInfoModelList(){
        return Lists.newArrayList(
                fakeWeChatHelpInfoModel(1, "openId1"),
                fakeWeChatHelpInfoModel(2, "openId2")
        );
    }

    public Map<String, String> fakeWaitEveryoneHelp(){
        return Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("1", DateTime.now().minusDays(2).toString("yyyy-MM-dd HH:mm:SS"))
                .build());
    }

    public WeChatHelpModel fakeWeChatHelpModelByInvest(long loanId, String loginName){
        WeChatHelpModel weChatHelpModel = new WeChatHelpModel(loanId, 11111, 1000000, 1000000, loginName, "userName1", "mobile", "openId",
                WeChatHelpType.INVEST_HELP, DateTime.now().minusDays(3).toDate(), DateTime.now().minusDays(2).toDate());
        weChatHelpModel.setHelpUserCount(2);
        weChatHelpModel.setReward(200);
        return weChatHelpModel;
    }

    public WeChatHelpInfoModel fakeWeChatHelpInfoModel(long helpId, String openId){
        return new WeChatHelpInfoModel(openId, helpId, WeChatHelpUserStatus.WAITING);
    }
}

