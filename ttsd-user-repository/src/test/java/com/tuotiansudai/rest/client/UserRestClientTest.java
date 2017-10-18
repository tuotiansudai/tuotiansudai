package com.tuotiansudai.rest.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.ask.dto.QuestionRequestDto;
import com.tuotiansudai.ask.repository.model.QuestionModel;
import com.tuotiansudai.dto.request.*;
import com.tuotiansudai.dto.response.UserInfo;
import com.tuotiansudai.dto.response.UserRestPagingResponse;
import com.tuotiansudai.dto.response.UserRestResponseBase;
import com.tuotiansudai.dto.response.UserRestUserInfo;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.log4j.MDC;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})public class UserRestClientTest {
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
        RegisterRequestDto requestDto = new RegisterRequestDto(
                "13800138012", "123abc", null, "Test", Source.WEB);
//        this.mockServer.enqueue(buildCreateQuestionResponse(requestDto));
        UserRestUserInfo responseUserInfo = userRestClient.register(requestDto);
        UserInfo userInfo = responseUserInfo.getUserInfo();
        UserModel userModel = userInfo.toUserModel();
        assertEquals(requestDto.getMobile(), userInfo.getMobile());
    }

    @Ignore
    @Test
    public void shouldUpdateUser() {
        UpdateUserInfoRequestDto requestDto = new UpdateUserInfoRequestDto("xaxwlnqf");
        requestDto.setLastModifiedTime(new Date());
        UserRestUserInfo responseUserInfo = userRestClient.update(requestDto);
        UserInfo userInfo = responseUserInfo.getUserInfo();
        assertEquals(requestDto.getEmail(), userInfo.getEmail());
    }

    @Ignore
    @Test
    public void shouldFindUser() {
        UserRestUserInfo responseUserInfo = userRestClient.findByLoginNameOrMobile("xaxwlnqf");
        UserInfo userInfo = responseUserInfo.getUserInfo();
        assertEquals("test@test.com", userInfo.getEmail());
    }

    @Ignore
    @Test
    public void shouldSearchUser() {
        UserRestQueryDto queryDto = new UserRestQueryDto(1);
        queryDto.setFields("login_name", "register_time");
        UserRestPagingResponse<UserModel> searchResult = userRestClient.search(queryDto);
        List<UserModel> userModelList = searchResult.getItems();
        assertEquals(0, userModelList.size());
    }

    @Ignore
    @Test
    public void shouldChangePassword() {
        ChangePasswordRequestDto requestDto = new ChangePasswordRequestDto("xaxwlnqf", "123abc", "110abc");
        UserRestResponseBase response = userRestClient.changePassword(requestDto);
        assertTrue(response.isSuccess());
    }

    @Ignore
    @Test
    public void shouldResetPassword() {
        ResetPasswordRequestDto requestDto = new ResetPasswordRequestDto("xaxwlnqf", "123abc");
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
