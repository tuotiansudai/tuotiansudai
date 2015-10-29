package com.tuotiansudai.api.controller;

import org.junit.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MobileAppBannerControllerTest extends ControllerTestBase<MobileAppBannerController> {
    @Test
    public void shouldGetAppBanner() throws Exception {
        String requestJson = "";
        mockMvc.perform(post("/get/banner").
                contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect((content().contentType(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true))
                .andExpect(jsonPath("$.data.code").value("0000"));
    }
}
