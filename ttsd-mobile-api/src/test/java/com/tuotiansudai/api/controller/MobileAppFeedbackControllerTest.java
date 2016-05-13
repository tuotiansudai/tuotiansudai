package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.FeedbackRequestDto;
import com.tuotiansudai.repository.model.FeedbackType;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.FeedbackService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class MobileAppFeedbackControllerTest extends ControllerTestBase {

    @InjectMocks
    private MobileAppFeedbackController controller;

    @Mock
    private FeedbackService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void createFeedbackOK() throws Exception {
        when(service.create(anyString(), anyString(), any(Source.class), any(FeedbackType.class), anyString())).thenReturn(null);
        FeedbackRequestDto requestDto = new FeedbackRequestDto();
        requestDto.setContent("hello");
        doRequestWithServiceMockedTest("/feedback", requestDto);
    }

    @Test
    public void createFeedbackFail() throws Exception {
        when(service.create(anyString(), anyString(), any(Source.class), any(FeedbackType.class), anyString())).thenReturn(null);
        doRequestWithServiceIsOkMockedTest("/feedback", new FeedbackRequestDto())
                .andExpect(jsonPath("$.code").value("0080"));
    }
}
