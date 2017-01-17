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
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {FeignClientConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AskRestClientTest {
    @Autowired
    private AskRestClient askRestClient;

    @Test
    public void shouldLoadApplicationContext() {
        assertNotNull(askRestClient);
    }

    @Ignore
    @Test
    public void shouldCreateQuestion() {
        MDC.put("requestId", "xxxxxxx");
        MDC.put("userId", "yyyy");
        QuestionRequestDto requestDto = new QuestionRequestDto();
        requestDto.setAddition("addition");
        requestDto.setQuestion("question");
        requestDto.setTags(null);

        QuestionModel questionModel = askRestClient.createQuestion(requestDto);
        assertEquals(requestDto.getAddition(), questionModel.getAddition());
    }
}
