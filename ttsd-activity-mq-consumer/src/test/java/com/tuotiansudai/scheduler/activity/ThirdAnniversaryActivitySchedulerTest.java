package com.tuotiansudai.scheduler.activity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.mapper.ActivityInvestMapper;
import com.tuotiansudai.activity.repository.mapper.ThirdAnniversaryDrawMapper;
import com.tuotiansudai.activity.repository.mapper.WeChatHelpInfoMapper;
import com.tuotiansudai.activity.repository.mapper.WeChatHelpMapper;
import com.tuotiansudai.activity.repository.model.*;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.TransferCashDto;
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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class ThirdAnniversaryActivitySchedulerTest {

    @InjectMocks
    private ThirdAnniversaryActivityScheduler thirdAnniversaryActivityScheduler;

    @Mock
    private PayWrapperClient payWrapperClient;

    @Mock
    private RedisWrapperClient redisWrapperClient;

    @Mock
    private WeChatHelpMapper weChatHelpMapper;

    @Mock
    private WeChatHelpInfoMapper weChatHelpInfoMapper;

    @Mock
    private ActivityInvestMapper activityInvestMapper;

    @Mock
    private ThirdAnniversaryDrawMapper thirdAnniversaryDrawMapper;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        Field redisWrapperClientField = this.thirdAnniversaryActivityScheduler.getClass().getDeclaredField("redisWrapperClient");
        redisWrapperClientField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(redisWrapperClientField, redisWrapperClientField.getModifiers() & ~Modifier.FINAL);
        redisWrapperClientField.set(this.thirdAnniversaryActivityScheduler, this.redisWrapperClient);
    }

    @Test
    public void shouldSendHelpCashSuccess(){
        ArgumentCaptor<String> redisDelKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisDelHKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisSetKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisSetValueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<TransferCashDto> transferCashDtoCaptor = ArgumentCaptor.forClass(TransferCashDto.class);

        when(redisWrapperClient.hgetAll("THIRD_ANNIVERSARY_WAIT_SEND_REWARD")).
                thenReturn(Maps.newHashMap(ImmutableMap.<String, String>builder()
                        .put("1", DateTime.now().minusDays(1).toString("yyyy-MM-dd HH:mm:ss"))
                        .build()));

        when(weChatHelpMapper.findById(1)).thenReturn(new WeChatHelpModel("loginName1", "userName1", "mobile1", WeChatHelpType.THIRD_ANNIVERSARY_HELP, DateTime.now().minusDays(4).toDate(), DateTime.now().minusDays(1).toDate()));
        when(weChatHelpInfoMapper.findByHelpId(1)).thenReturn(Lists.newArrayList(new WeChatHelpInfoModel("friend1", "friendMobile1", "userName1", 1, WeChatHelpUserStatus.WAITING),
                new WeChatHelpInfoModel("friend2", "friendMobile2", "userName2", 1, WeChatHelpUserStatus.WAITING),
                new WeChatHelpInfoModel("friend3", "friendMobile3", "userName3", 1, WeChatHelpUserStatus.WAITING)));

        List<ActivityInvestModel> activityInvestModelList = Lists.newArrayList(new ActivityInvestModel(1l,1l, "loginName1", "userName1", "mobile1", 100000l, 100000l, ActivityCategory.THIRD_ANNIVERSARY.name()),
                new ActivityInvestModel(1l,2l, "loginName1", "userName1", "mobile1", 100000l, 100000l, ActivityCategory.THIRD_ANNIVERSARY.name()),
                new ActivityInvestModel(1l,3l, "loginName1", "userName1", "mobile1", 100000l, 100000l, ActivityCategory.THIRD_ANNIVERSARY.name()));
        when(activityInvestMapper.findAllByActivityLoginNameAndTime(eq("loginName1"), any(), any(), any())).thenReturn(activityInvestModelList);

        thirdAnniversaryActivityScheduler.sendHelpCash();

        verify(this.redisWrapperClient, times(1)).hdel(redisDelKeyCaptor.capture(), redisDelHKeyCaptor.capture());
        verify(this.payWrapperClient, times(4)).transferCash(transferCashDtoCaptor.capture());
        verify(this.redisWrapperClient, times(4)).setex(redisSetKeyCaptor.capture(), anyInt(), redisSetValueCaptor.capture());

        assertThat(redisDelKeyCaptor.getValue(), is("THIRD_ANNIVERSARY_WAIT_SEND_REWARD"));
        assertThat(redisDelHKeyCaptor.getValue(), is("1"));
        assertThat(transferCashDtoCaptor.getAllValues().get(0).getLoginName(), is("loginName1"));
        assertThat(transferCashDtoCaptor.getAllValues().get(0).getAmount(), is("1500"));
        assertThat(transferCashDtoCaptor.getAllValues().get(1).getLoginName(), is("friend1"));
        assertThat(transferCashDtoCaptor.getAllValues().get(1).getAmount(), is("500"));
        assertThat(transferCashDtoCaptor.getAllValues().get(2).getLoginName(), is("friend2"));
        assertThat(transferCashDtoCaptor.getAllValues().get(2).getAmount(), is("500"));
        assertThat(transferCashDtoCaptor.getAllValues().get(3).getLoginName(), is("friend3"));
        assertThat(transferCashDtoCaptor.getAllValues().get(3).getAmount(), is("500"));
        assertThat(redisSetKeyCaptor.getAllValues().get(0), is("THIRD_ANNIVERSARY_SEND_HELP_CASH_SUCCESS:loginName1"));
        assertThat(redisSetKeyCaptor.getAllValues().get(1), is("THIRD_ANNIVERSARY_SEND_HELP_CASH_SUCCESS:friend1"));
        assertThat(redisSetKeyCaptor.getAllValues().get(2), is("THIRD_ANNIVERSARY_SEND_HELP_CASH_SUCCESS:friend2"));
        assertThat(redisSetKeyCaptor.getAllValues().get(3), is("THIRD_ANNIVERSARY_SEND_HELP_CASH_SUCCESS:friend3"));
    }

    @Test
    public void shouldSendTopFourCash(){

        ArgumentCaptor<String> redisSetKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisSetValueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<TransferCashDto> transferCashDtoCaptor = ArgumentCaptor.forClass(TransferCashDto.class);

        String topFourTeams = "team1,team2,team3,team4";
        when(redisWrapperClient.exists("THIRD_ANNIVERSARY_TOP_FOUR_TEAM")).thenReturn(true);
        when(redisWrapperClient.get("THIRD_ANNIVERSARY_TOP_FOUR_TEAM")).thenReturn(topFourTeams);
        when(thirdAnniversaryDrawMapper.findLoginNameByCollectTopFour(any())).thenReturn(Lists.newArrayList("loginName1", "loginName2", "loginName3"));

        thirdAnniversaryActivityScheduler.sendTopFourCash();

        verify(this.payWrapperClient, times(3)).transferCash(transferCashDtoCaptor.capture());
        verify(this.redisWrapperClient, times(3)).setex(redisSetKeyCaptor.capture(), anyInt(), redisSetValueCaptor.capture());

        assertThat(transferCashDtoCaptor.getAllValues().get(0).getLoginName(), is("loginName1"));
        assertThat(transferCashDtoCaptor.getAllValues().get(0).getAmount(), is("296266"));
        assertThat(transferCashDtoCaptor.getAllValues().get(1).getLoginName(), is("loginName2"));
        assertThat(transferCashDtoCaptor.getAllValues().get(1).getAmount(), is("296266"));
        assertThat(transferCashDtoCaptor.getAllValues().get(2).getLoginName(), is("loginName3"));
        assertThat(transferCashDtoCaptor.getAllValues().get(2).getAmount(), is("296266"));
        assertThat(redisSetKeyCaptor.getAllValues().get(0), is("THIRD_ANNIVERSARY_SEND_TEAM_CASH_SUCCESS:loginName1"));
        assertThat(redisSetKeyCaptor.getAllValues().get(1), is("THIRD_ANNIVERSARY_SEND_TEAM_CASH_SUCCESS:loginName2"));
        assertThat(redisSetKeyCaptor.getAllValues().get(2), is("THIRD_ANNIVERSARY_SEND_TEAM_CASH_SUCCESS:loginName3"));
    }
}
