package com.tuotiansudai.mq.consumer.point;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuotiansudai.message.InvestInfo;
import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.message.LoanDetailInfo;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.point.service.PointService;
import com.tuotiansudai.point.service.PointTaskService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.util.JsonConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class InvestSuccessCompletePointTaskConsumerTest extends PointTaskConsumerTestBase {
    @Autowired
    @Qualifier("investSuccessCompletePointTaskConsumer")
    private MessageConsumer consumer;

    @MockBean
    private PointTaskService pointTaskService;

    @MockBean
    private InvestMapper investMapper;

    @MockBean
    private PointService pointService;

    @Test
    @Transactional
    public void shouldConsume() {
        long investId = 100001;
        String loginName = "helloworld";
        InvestSuccessMessage investSuccessMessage = buildMockedInvestSuccessMessage(investId, loginName);

        InvestModel mockedInvestModel = buildMockInvestModel(investId, loginName);

        final ArgumentCaptor<String> loginNameCaptor = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<PointTask> pointTaskCaptor = ArgumentCaptor.forClass(PointTask.class);
        final ArgumentCaptor<Long> investIdCaptor = ArgumentCaptor.forClass(Long.class);
        doNothing().when(pointTaskService).completeNewbieTask(pointTaskCaptor.capture(), loginNameCaptor.capture());
        doNothing().when(pointTaskService).completeAdvancedTask(pointTaskCaptor.capture(), loginNameCaptor.capture());
        doNothing().when(pointService).obtainPointInvest(investIdCaptor.capture());
        when(investMapper.findById(investId)).thenReturn(mockedInvestModel);

        try {
            consumer.consume(JsonConverter.writeValueAsString(investSuccessMessage));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assertTrue("all loginName is" + loginName,
                loginNameCaptor.getAllValues().stream().allMatch(s -> s.equalsIgnoreCase(loginName)));

        assertEquals(PointTask.FIRST_INVEST, pointTaskCaptor.getAllValues().get(0));
        assertEquals(PointTask.EACH_SUM_INVEST, pointTaskCaptor.getAllValues().get(1));
        assertEquals(PointTask.FIRST_SINGLE_INVEST, pointTaskCaptor.getAllValues().get(2));
        assertEquals(PointTask.EACH_RECOMMEND_INVEST, pointTaskCaptor.getAllValues().get(3));
        assertEquals(PointTask.FIRST_INVEST_180, pointTaskCaptor.getAllValues().get(4));
        assertEquals(PointTask.FIRST_INVEST_360, pointTaskCaptor.getAllValues().get(5));

        assertEquals(mockedInvestModel.getId(), (long) investIdCaptor.getValue());
    }

    private InvestModel buildMockInvestModel(long investId, String loginName) {
        InvestModel investModel = new InvestModel();
        investModel.setId(investId);
        investModel.setLoginName(loginName);
        investModel.setStatus(InvestStatus.SUCCESS);
        return investModel;
    }

    private InvestSuccessMessage buildMockedInvestSuccessMessage(long investId, String loginName) {
        InvestInfo investInfo = new InvestInfo();
        LoanDetailInfo loanDetailInfo = new LoanDetailInfo();

        investInfo.setInvestId(investId);
        investInfo.setLoginName(loginName);
        investInfo.setAmount(4000000);
        investInfo.setStatus("SUCCESS");
        investInfo.setTransferStatus("TRANSFERABLE");

        InvestSuccessMessage investSuccessMessage = new InvestSuccessMessage(investInfo, loanDetailInfo);
        return investSuccessMessage;
    }

}
