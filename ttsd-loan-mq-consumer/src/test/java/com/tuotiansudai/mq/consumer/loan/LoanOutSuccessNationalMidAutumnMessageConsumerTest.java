package com.tuotiansudai.mq.consumer.loan;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.message.LoanOutSuccessMessage;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanDetailsMapper;
import com.tuotiansudai.repository.model.InvestAchievementView;
import com.tuotiansudai.repository.model.LoanDetailsModel;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static com.tuotiansudai.mq.consumer.loan.LoanOutSuccessNationalMidAutumnMessageConsumer.NATIONAL_MID_AUTUMN_CASH_KEY;
import static com.tuotiansudai.mq.consumer.loan.LoanOutSuccessNationalMidAutumnMessageConsumer.NATIONAL_MID_AUTUMN_SUM_CASH_KEY;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class LoanOutSuccessNationalMidAutumnMessageConsumerTest {
    @Autowired
    @Qualifier("loanOutSuccessNationalMidAutumnMessageConsumer")
    private MessageConsumer consumer;

    @MockBean
    private PayWrapperClient payWrapperClient;

    @MockBean
    private SmsWrapperClient smsWrapperClient;

    @MockBean
    private LoanDetailsMapper loanDetailsMapper;

    @MockBean
    private InvestMapper investMapper;

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Test
    @Transactional
    public void shouldConsumeIsOk() throws JsonProcessingException {

        LoanOutSuccessMessage loanOutSuccessMessage = buildMockedLoanOutSuccessMessage();
        clearRedisClient(loanOutSuccessMessage.getLoanId());
        LoanDetailsModel loanDetailsModel = getMockedLoanDetailsModel(loanOutSuccessMessage.getLoanId());
        List<InvestAchievementView> investAchievementViews = getMockedInvestAchievementViews();

        when(investMapper.findAmountOrderByLoanId(anyLong(), any(), any(), any())).thenReturn(investAchievementViews);
        when(loanDetailsMapper.getByLoanId(anyLong())).thenReturn(loanDetailsModel);

        BaseDto<PayDataDto> baseDto = new BaseDto();
        baseDto.setSuccess(true);
        PayDataDto payDataDto = new PayDataDto();
        payDataDto.setMessage("message");
        baseDto.setData(payDataDto);
        when(payWrapperClient.transferCash(any(TransferCashDto.class))).thenReturn(baseDto);

        consumer.consume(JsonConverter.writeValueAsString(loanOutSuccessMessage));
        assertEquals("10000", getRedisClientAmount("test1"));
        assertEquals("640000", getRedisClientAmount("test2"));
        loanOutSuccessMessage.setLoanId(234l);
        loanDetailsModel.setLoanId(loanOutSuccessMessage.getLoanId());
        consumer.consume(JsonConverter.writeValueAsString(loanOutSuccessMessage));
        assertEquals("20000", getRedisClientAmount("test1"));
        assertEquals("1000000", getRedisClientAmount("test2"));
        clearRedisClient(loanOutSuccessMessage.getLoanId());
    }

    private LoanOutSuccessMessage buildMockedLoanOutSuccessMessage() {
        return new LoanOutSuccessMessage(123l);
    }

    private LoanDetailsModel getMockedLoanDetailsModel(long loanID) {
        LoanDetailsModel loanDetailsModel = new LoanDetailsModel();
        loanDetailsModel.setNonTransferable(false);
        loanDetailsModel.setDeclaration("declaration");
        loanDetailsModel.setActivity(true);
        loanDetailsModel.setActivityDesc("逢万返百");
        loanDetailsModel.setLoanId(loanID);
        loanDetailsMapper.create(loanDetailsModel);
        return loanDetailsModel;
    }

    private List<InvestAchievementView> getMockedInvestAchievementViews() {
        InvestAchievementView investAchievementView1 = new InvestAchievementView();
        investAchievementView1.setAmount(1800000);
        investAchievementView1.setLoginName("test1");

        InvestAchievementView investAchievementView2 = new InvestAchievementView();
        investAchievementView2.setAmount(64000000);
        investAchievementView2.setLoginName("test2");

        InvestAchievementView investAchievementView3 = new InvestAchievementView();
        investAchievementView3.setAmount(100000);
        investAchievementView3.setLoginName("test3");

        return Arrays.asList(investAchievementView1, investAchievementView2, investAchievementView3);
    }

    private void clearRedisClient(long loanId) {
        redisWrapperClient.del(NATIONAL_MID_AUTUMN_CASH_KEY, String.valueOf(loanId) + "test1");
        redisWrapperClient.del(NATIONAL_MID_AUTUMN_CASH_KEY, String.valueOf(loanId) + "test2");
        redisWrapperClient.del(NATIONAL_MID_AUTUMN_SUM_CASH_KEY, "test1");
        redisWrapperClient.del(NATIONAL_MID_AUTUMN_SUM_CASH_KEY, "test2");
    }

    private String getRedisClientAmount(String key) {
        return redisWrapperClient.hget(NATIONAL_MID_AUTUMN_SUM_CASH_KEY, key);
    }
}
