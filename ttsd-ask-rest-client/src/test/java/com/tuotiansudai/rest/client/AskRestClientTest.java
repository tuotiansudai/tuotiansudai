package com.tuotiansudai.rest.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.ask.dto.QuestionRequestDto;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.etcd.ETCDConfigReader;
import org.apache.log4j.MDC;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class AskRestClientTest {
    private String askRestServerUrl = ETCDConfigReader.getReader().getValue("ask.rest.server");

    @Autowired
    private AskRestClient askRestClient;

    private MockWebServer mockServer;
    private ObjectMapper objectMapper;

    @Before
    public void mockAskRestService() throws IOException {
        this.objectMapper = new ObjectMapper();
        this.mockServer = new MockWebServer();
        URL url = new URL(askRestServerUrl);
        this.mockServer.start(InetAddress.getByName(url.getHost()), url.getPort());
    }

    @Test
    public void shouldCreateQuestion() throws JsonProcessingException {
        String currentUserId = "yyyyyy";
        MDC.put("requestId", "xxxxxxx");
        MDC.put("userId", currentUserId);
        QuestionRequestDto requestDto = new QuestionRequestDto();
        requestDto.setAddition("addition");
        requestDto.setQuestion("question");
        requestDto.setTags(null);

        this.mockServer.enqueue(buildCreateQuestionResponse(requestDto));

        QuestionModel questionModel = askRestClient.createQuestion(requestDto);
        assertEquals(requestDto.getAddition(), questionModel.getAddition());
        assertEquals(currentUserId, questionModel.getLoginName());
    }

    private MockResponse buildCreateQuestionResponse(QuestionRequestDto requestDto) throws JsonProcessingException {
        QuestionModel questionModel = new QuestionModel(String.valueOf(MDC.get("userId")), "123", "123",
                requestDto.getQuestion(), requestDto.getAddition(), requestDto.getTags());
        questionModel.setId(1110001);
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(201);
        mockResponse.setBody(objectMapper.writeValueAsString(questionModel));
        return mockResponse;
    }

    @After
    public void shutdownAskRestServer() throws IOException {
        this.mockServer.shutdown();
    }
}
