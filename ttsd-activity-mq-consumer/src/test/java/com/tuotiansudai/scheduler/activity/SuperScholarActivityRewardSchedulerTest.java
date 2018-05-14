package com.tuotiansudai.scheduler.activity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.mapper.ActivityInvestMapper;
import com.tuotiansudai.activity.repository.mapper.SuperScholarRewardMapper;
import com.tuotiansudai.activity.repository.mapper.WeChatHelpInfoMapper;
import com.tuotiansudai.activity.repository.model.ActivityInvestModel;
import com.tuotiansudai.activity.repository.model.SuperScholarRewardModel;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.util.RedisWrapperClient;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

    }

    private Map<String, String> fakeLoanId(){
        return Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("201805", DateTime.now().minusDays(2).toString("yyyy-MM-dd HH:mm:ss"))
                .build());
    }

    public List<ActivityInvestModel> fakeActivityInvestModels(){
        return Lists.newArrayList(new ActivityInvestModel(201805, 20180501, "loginName", "userName", "mobile", 100, 100, null),
                new ActivityInvestModel(201805, 20180502, "loginName", "userName", "mobile", 200, 200, null),
                new ActivityInvestModel(201805, 20180503, "loginName", "userName", "mobile", 300, 300, null));
    }

    public SuperScholarRewardModel fakeSuperScholarRewardModel(){
        SuperScholarRewardModel superScholarRewardModel = new SuperScholarRewardModel("loginName", "1,2,3,4,5", "A,A,A,A,A");
        superScholarRewardModel.setUserAnswer("A,A,A,A,A");
        superScholarRewardModel.setUserRight(5);
        superScholarRewardModel.setShareHome(true);
        superScholarRewardModel.setShareInvest(true);
        superScholarRewardModel.setShareAccount(true);
        return superScholarRewardModel;
    }

}
