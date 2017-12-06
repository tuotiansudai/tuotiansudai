package com.tuotiansudai.rest.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.dto.request.*;
import com.tuotiansudai.dto.response.UserInfo;
import com.tuotiansudai.dto.response.UserRestPagingResponse;
import com.tuotiansudai.dto.response.UserRestResponseBase;
import com.tuotiansudai.dto.response.UserRestUserInfo;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class UserRestClientTest {
    @Value("${user.rest.server}")
    private String userRestServerUrl;

    @Autowired
    private UserRestClient userRestClient;

    private MockWebServer mockServer;
    private ObjectMapper objectMapper;

    @Before
    public void mockService() throws IOException {
        this.objectMapper = new ObjectMapper();
        this.mockServer = new MockWebServer();
        URL url = new URL(userRestServerUrl);
        this.mockServer.start(InetAddress.getByName(url.getHost()), url.getPort());
    }

    @Test
    public void shouldRegisterUser() throws JsonProcessingException {
        RegisterRequestDto requestDto = new RegisterRequestDto("13800138012", "123abc", null, "Test", Source.WEB);
        UserRestUserInfo mockResp = buildMockUserRestUserInfoResponse(requestDto.getMobile(), requestDto.getChannel(), requestDto.getSource(), null);
        this.mockServer.enqueue(buildCreateQuestionResponse(mockResp, 201));

        UserRestUserInfo responseUserInfo = userRestClient.register(requestDto);
        UserInfo userInfo = responseUserInfo.getUserInfo();
        UserModel userModel = userInfo.toUserModel();
        assertEquals(requestDto.getMobile(), userModel.getMobile());
    }

    @Test
    public void shouldUpdateUser() throws JsonProcessingException {
        UserRestUserInfo mockResp = buildMockUserRestUserInfoResponse("13800000000", "test", Source.WEB, null);
        this.mockServer.enqueue(buildCreateQuestionResponse(mockResp, 200));

        UpdateUserInfoRequestDto requestDto = new UpdateUserInfoRequestDto("xaxwlnqf");
        requestDto.setLastModifiedTime(new Date());
        UserRestUserInfo responseUserInfo = userRestClient.update(requestDto);
        UserInfo userInfo = responseUserInfo.getUserInfo();
        assertEquals(requestDto.getEmail(), userInfo.getEmail());
    }

    @Test
    public void shouldFindUser() throws JsonProcessingException {
        UserRestUserInfo mockResp = buildMockUserRestUserInfoResponse("13800000000", "test", Source.WEB, "test@test.com");
        this.mockServer.enqueue(buildCreateQuestionResponse(mockResp, 200));
        UserRestUserInfo responseUserInfo = userRestClient.findByLoginNameOrMobile("xaxwlnqf");
        UserInfo userInfo = responseUserInfo.getUserInfo();
        assertEquals("test@test.com", userInfo.getEmail());
    }

    @Test
    public void shouldSearchUser() throws JsonProcessingException {
        UserRestPagingResponse mockResp = buildMockUserRestUserInfoPagingResponse("13800000000", "test", Source.WEB, "test@test.com");
        this.mockServer.enqueue(buildCreateQuestionResponse(mockResp, 200));

        UserRestQueryDto queryDto = new UserRestQueryDto();
        queryDto.setFields("login_name", "register_time");
        UserRestPagingResponse<UserInfo> searchResult = userRestClient.search(queryDto);
        List<UserInfo> userModelList = searchResult.getItems();
        assertEquals(1, userModelList.size());
    }

    @Test
    public void shouldChangePassword() throws JsonProcessingException {
        UserRestUserInfo mockResp = buildMockUserRestUserInfoResponse("13800000000", "test", Source.WEB, "test@test.com");
        this.mockServer.enqueue(buildCreateQuestionResponse(mockResp, 200));

        ChangePasswordRequestDto requestDto = new ChangePasswordRequestDto("xaxwlnqf", "123abc", "110abc");
        UserRestUserInfo response = userRestClient.changePassword(requestDto);
        assertTrue(response.isSuccess());
    }

    @Test
    public void shouldResetPassword() throws JsonProcessingException {
        UserRestUserInfo mockResp = buildMockUserRestUserInfoResponse("13800000000", "test", Source.WEB, "test@test.com");
        this.mockServer.enqueue(buildCreateQuestionResponse(mockResp, 200));

        ResetPasswordRequestDto requestDto = new ResetPasswordRequestDto("xaxwlnqf", "123abc");
        UserRestResponseBase response = userRestClient.resetPassword(requestDto);
        assertTrue(response.isSuccess());
    }

    @Test
    public void shouldFindEmptyProvinceUsers() throws JsonProcessingException {
        UserRestPagingResponse mockResp = buildMockUserRestUserInfoPagingResponse("13800000000", "test", Source.WEB, "test@test.com");
        this.mockServer.enqueue(buildCreateQuestionResponse(mockResp, 200));

        UserRestResponseBase response = userRestClient.findEmptyProvinceUsers(2);
        assertTrue(response.isSuccess());
    }

    @After
    public void shutdownMockServer() throws IOException {
        this.mockServer.shutdown();
    }


    private UserRestPagingResponse<UserInfo> buildMockUserRestUserInfoPagingResponse(String mobile, String test, Source source, String email) {
        UserRestPagingResponse<UserInfo> pagingResponse = new UserRestPagingResponse<>();
        pagingResponse.setSuccess(true);
        pagingResponse.setMessage("");
        pagingResponse.setItems(Collections.singletonList(buildMockUserInfo(mobile, test, source, email)));
        return pagingResponse;
    }

    private UserRestUserInfo buildMockUserRestUserInfoResponse(String mobile, String test, Source source, String email) {
        UserInfo mockUserInfo = buildMockUserInfo(mobile, test, source, email);

        UserRestUserInfo mockResp = new UserRestUserInfo();
        mockResp.setSuccess(true);
        mockResp.setMessage("");
        mockResp.setUserInfo(mockUserInfo);
        return mockResp;
    }

    private UserInfo buildMockUserInfo(String mobile, String test, Source source, String email) {
        UserInfo mockUserInfo = new UserInfo();
        mockUserInfo.setMobile(mobile);
        mockUserInfo.setChannel(test);
        mockUserInfo.setSource(String.valueOf(source));
        mockUserInfo.setStatus(String.valueOf(UserStatus.ACTIVE));
        mockUserInfo.setEmail(email);
        return mockUserInfo;
    }

    private MockResponse buildCreateQuestionResponse(Object data, int statusCode) throws JsonProcessingException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(statusCode);
        mockResponse.setBody(objectMapper.writeValueAsString(data));
        return mockResponse;
    }
}
