package com.tuotiansudai.rest.client;

import com.tuotiansudai.ask.dto.QuestionRequestDto;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.rest.FeignClientConfig;
import org.apache.log4j.MDC;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {FeignClientConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AskRestClientTest {
    @Autowired
    private AskRestClient askRestClient;

    @Test
    @Ignore
    public void shouldPostQuestion() {
        MDC.put("requestId", "xxxxxxx");
        MDC.put("userId", "yyyyy");
        QuestionRequestDto requestDto = new QuestionRequestDto();
        requestDto.setAddition("addition");
        requestDto.setQuestion("question");
        requestDto.setCaptcha("11111");
        requestDto.setTags(null);

        QuestionModel questionModel = askRestClient.create(requestDto);
        assertEquals(requestDto.getAddition(), questionModel.getAddition());
    }
}
