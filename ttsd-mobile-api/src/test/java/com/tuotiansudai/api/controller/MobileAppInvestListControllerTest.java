package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.InvestListRequestDto;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MobileAppInvestListControllerTest extends ControllerTestBase<MobileAppInvestListController> {
    @Test
    public void shouldGetInvests() throws Exception {

        InvestListRequestDto requestDto = new InvestListRequestDto();
        requestDto.setIndex(1);
        requestDto.setLoanId("1312312312");
        requestDto.setPageSize(10);

        String requestJson = generateRequestJson(requestDto);

        mockMvc.perform(post("/get/invests").
                contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect((content().contentType(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value(true))
                .andExpect(jsonPath("$.data.code").value("0000"));
    }
}
