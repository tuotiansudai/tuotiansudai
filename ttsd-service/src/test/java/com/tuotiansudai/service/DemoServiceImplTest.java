package com.tuotiansudai.service;

import com.tuotiansudai.repository.mapper.DemoMapper;
import com.tuotiansudai.repository.model.DemoModel;
import com.tuotiansudai.service.impl.DemoServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class DemoServiceImplTest {

    @InjectMocks
    private DemoServiceImpl demoService;

    @Mock
    private DemoMapper demoMapper;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldGetDemoById() throws Exception {
        String demoId = "demoId";
        DemoModel demoModel = new DemoModel();
        demoModel.setId(demoId);
        when(demoMapper.getDemoById(anyString())).thenReturn(demoModel);

        DemoModel actualDemoModel = demoService.getDemoById(demoId);
        assertThat(actualDemoModel.getId(), is(demoId));
    }
}