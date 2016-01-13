package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.client.RedisWrapperClient;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        when(redisWrapperClient.get(eq("app:version:info"))).thenReturn("{\n" +
                "    \"android\": {\n" +
                "        \"version\": \"1.5\",\n" +
                "        \"versionCode\": 11,\n" +
                "        \"forceUpgrade\": false,\n" +
                "        \"url\": \"https://tuotiansudai.com/app/tuotiansudai.apk\",\n" +
                "        \"message\": \"1，拓天助手修改为个人中心；\\n2，新增账户信息模块；\\n3，新增服务费用、常见问题、意见反馈；\\n4，合并密码修改，优化修改流程；\\n5，交互优化；\\n6，新增8家银行快捷支付；\\n7，推荐人二维码扫描注册。\"\n" +
                "    },\n" +
                "    \"ios\": {\n" +
                "        \"version\": \"1.5.5\",\n" +
                "        \"forceUpgrade\": false,\n" +
                "        \"message\": \"1，拓天助手修改为个人中心；\\n2，新增账户信息模块；\\n3，新增服务费用、常见问题、意见反馈；\\n4，合并密码修改，优化修改流程；\\n5，交互优化；\\n6，新增8家银行快捷支付；\\n7，推荐人二维码扫描注册。\"\n" +
                "    }\n" +
                "}");

        mockMvc.perform(post("/v1.0/get/version").
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

        mockMvc.perform(post("/v1.0/get/version").
                contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

    }

}
