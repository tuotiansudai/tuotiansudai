package com.tuotiansudai.rest.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.ask.dto.QuestionRequestDto;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.dto.request.*;
import com.tuotiansudai.rest.dto.response.UserRestPagingResponse;
import com.tuotiansudai.rest.dto.response.UserRestResponseBase;
import com.tuotiansudai.rest.dto.response.UserRestUserInfo;
import org.apache.log4j.MDC;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class UserRestClientTest {
    @Value("${ask.rest.server}")
    private String askRestServerUrl;

    @Autowired
    private UserRestClient userRestClient;

    private MockWebServer mockServer;
    private ObjectMapper objectMapper;

    @Before
    public void mockService() throws IOException {
        this.objectMapper = new ObjectMapper();
//        this.mockServer = new MockWebServer();
//        URL url = new URL(askRestServerUrl);
//        this.mockServer.start(InetAddress.getByName(url.getHost()), url.getPort());
    }

    @Test
    @Ignore
    public void shouldRegisterUser() throws JsonProcessingException {
//        String currentUserId = "yyyyyy";
//        MDC.put("requestId", "xxxxxxx");
//        MDC.put("userId", currentUserId);
        UserRestRegisterRequestDto requestDto = new UserRestRegisterRequestDto(
                "13800138012", "123abc", null, "Test", Source.WEB);
//        this.mockServer.enqueue(buildCreateQuestionResponse(requestDto));
        UserRestUserInfo responseUserInfo = userRestClient.register(requestDto);
        UserModel userModel = responseUserInfo.getUserInfo();
        assertEquals(requestDto.getMobile(), userModel.getMobile());
    }

    @Ignore
    @Test
    public void shouldUpdateUser() {
        UserRestUpdateUserInfoRequestDto requestDto = new UserRestUpdateUserInfoRequestDto("xaxwlnqf");
        requestDto.setLastModifiedTime(new Date());
        UserRestUserInfo responseUserInfo = userRestClient.update(requestDto);
        UserModel userModel = responseUserInfo.getUserInfo();
        assertEquals(requestDto.getEmail(), userModel.getEmail());
    }

    @Ignore
    @Test
    public void shouldFindUser() {
        UserRestUserInfo responseUserInfo = userRestClient.findByLoginNameOrMobile("xaxwlnqf");
        UserModel userModel = responseUserInfo.getUserInfo();
        assertEquals("test@test.com", userModel.getEmail());
    }

    @Ignore
    @Test
    public void shouldSearchUser() {
        UserRestQueryDto queryDto = new UserRestQueryDto(1);
        queryDto.setFields(new String[]{"login_name", "register_time"});
        UserRestPagingResponse<UserModel> searchResult = userRestClient.search(queryDto);
        List<UserModel> userModelList = searchResult.getItems();
        assertEquals(0, userModelList.size());
    }

    @Ignore
    @Test
    public void shouldChangePassword() {
        UserRestChangePasswordRequestDto requestDto = new UserRestChangePasswordRequestDto("xaxwlnqf", "123abc", "110abc");
        UserRestResponseBase response = userRestClient.changePassword(requestDto);
        assertTrue(response.isSuccess());
    }

    @Ignore
    @Test
    public void shouldResetPassword() {
        UserRestResetPasswordRequestDto requestDto = new UserRestResetPasswordRequestDto("xaxwlnqf", "123abc");
        UserRestResponseBase response = userRestClient.resetPassword(requestDto);
        assertTrue(response.isSuccess());
    }
//    UserRestChangePasswordRequestDto requestDto = new UserRestChangePasswordRequestDto()

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
    public void shutdownMockServer() throws IOException {
//        this.mockServer.shutdown();
    }
}
