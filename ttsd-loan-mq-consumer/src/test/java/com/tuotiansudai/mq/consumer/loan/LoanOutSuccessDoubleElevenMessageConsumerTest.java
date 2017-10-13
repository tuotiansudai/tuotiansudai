package com.tuotiansudai.mq.consumer.loan;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.dto.sms.RepayNotifyDto;
import com.tuotiansudai.message.LoanOutSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanDetailsMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanerEnterpriseInfoMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.tuotiansudai.mq.consumer.loan.LoanOutSuccessNationalMidAutumnMessageConsumer.NATIONAL_MID_AUTUMN_CASH_KEY;
import static com.tuotiansudai.mq.consumer.loan.LoanOutSuccessNationalMidAutumnMessageConsumer.NATIONAL_MID_AUTUMN_SUM_CASH_KEY;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class LoanOutSuccessDoubleElevenMessageConsumerTest {
    @Autowired
    @Qualifier("loanOutSuccessDoubleElevenMessageConsumer")
    private MessageConsumer consumer;

    @MockBean
    private LoanDetailsMapper loanDetailsMapper;

    @MockBean
    private InvestMapper investMapper;

    @MockBean
    private LoanMapper loanMapper;

    @MockBean
    private MQWrapperClient mqWrapperClient;

    @Test
    @Transactional
    public void shouldConsumeIsOk() throws JsonProcessingException {

        LoanOutSuccessMessage loanOutSuccessMessage = buildMockedLoanOutSuccessMessage();
        LoanModel loanModel = getMockedLoanModel(loanOutSuccessMessage.getLoanId());
        LoanDetailsModel loanDetailsModel = getMockedLoanDetailsModel(loanOutSuccessMessage.getLoanId());
        List<InvestModel> investModels = getMockedInvestModels();

        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(loanDetailsMapper.getByLoanId(anyLong())).thenReturn(loanDetailsModel);
        when(investMapper.findSuccessDoubleElevenActivityByTime(anyLong(), any(), any())).thenReturn(investModels);

        consumer.consume(JsonConverter.writeValueAsString(loanOutSuccessMessage));

        verify(mqWrapperClient, times(2)).sendMessage(any(MessageQueue.class),any());
    }

    private LoanOutSuccessMessage buildMockedLoanOutSuccessMessage() {
        return new LoanOutSuccessMessage(123l);
    }

    private LoanModel getMockedLoanModel(long loanId) {
        LoanModel loanModel = new LoanModel();
        loanModel.setId(loanId);
        loanModel.setName("loanName");
        loanModel.setActivityType(ActivityType.NORMAL);
        return loanModel;
    }

    private LoanDetailsModel getMockedLoanDetailsModel(long loanId) {
        LoanDetailsModel loanDetailsModel = new LoanDetailsModel();
        loanDetailsModel.setNonTransferable(false);
        loanDetailsModel.setDeclaration("declaration");
        loanDetailsModel.setActivity(true);
        loanDetailsModel.setLoanId(loanId);
        loanDetailsMapper.create(loanDetailsModel);
        return loanDetailsModel;
    }

    private List<InvestModel> getMockedInvestModels() {
        InvestModel investModel = new InvestModel();
        investModel.setAmount(5000);
        investModel.setLoginName("test1");
        investModel.setTradingTime(new DateTime(new Date()).withTimeAtStartOfDay().plusHours(2).toDate());

        InvestModel investModel2 = new InvestModel();
        investModel2.setAmount(6000);
        investModel2.setLoginName("test1");
        investModel2.setTradingTime(new DateTime(new Date()).withTimeAtStartOfDay().plusHours(3).toDate());

        InvestModel investModel3 = new InvestModel();
        investModel3.setAmount(6000);
        investModel3.setLoginName("test1");
        investModel3.setTradingTime(new DateTime(new Date()).withTimeAtStartOfDay().plusHours(4).toDate());

        return Arrays.asList(investModel, investModel2, investModel3);
    }

}
