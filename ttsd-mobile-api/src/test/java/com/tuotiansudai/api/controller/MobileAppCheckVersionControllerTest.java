package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.RegisterRequestDto;
import com.tuotiansudai.api.dto.SendSmsRequestDto;
import com.tuotiansudai.api.service.MobileAppRegisterService;
import com.tuotiansudai.client.RedisWrapperClient;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MobileAppCheckVersionControllerTest extends ControllerTestBase {
    @InjectMocks
    private MobileAppCheckVersionController controller;
    @Mock
    private RedisWrapperClient redisWrapperClient;

    @Override
    protected Object getControllerObject() {
        return controller;
    }


    @Test
    public void shouldGetAndroidVersionFromRedis() throws Exception {

        String requestJson = generateRequestJson(new BaseParamDto());
        when(redisWrapperClient.get("app:version:info")).thenReturn("{" +
                "  \"version\": \"1.0\"," +
                "  \"versionCode\": 1," +
                "  \"forceUpgrade\": false," +
                "  \"url\": \"https://tuotiansudai.com/app/tuotiansudai.apk\"," +
                "  \"message\": \"修复用户名包含下划线不能登陆问题\"" +
                "}");

        mockMvc.perform(post("/get/version").
                contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.code").value("0000"))
                .andExpect(jsonPath("$.data.version").exists());

    }

    @Test
    public void shouldGetAndroidVersionFromOSS() throws Exception {

        String requestJson = generateRequestJson(new BaseParamDto());
        when(redisWrapperClient.get("app:version:info")).thenReturn(null);

        mockMvc.perform(post("/get/version").
                contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.code").value("0000"))
                .andExpect(jsonPath("$.data.version").exists());

    }

}
