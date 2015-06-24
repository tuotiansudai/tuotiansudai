package com.tuotiansudai.web.controller;

import com.tuotiansudai.service.UserService;
import com.tuotiansudai.web.common.CommonConstants;
import com.tuotiansudai.web.dto.UserInteractiveDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dispatcher-servlet.xml", "classpath:applicationContext.xml"})
public class UserControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.userController).build();
    }

    @Test
    public void shouldUserEmailIsExisted() throws Exception {

        when(userService.userEmailIsExisted(anyString())).thenReturn(true);

        MvcResult result = this.mockMvc.perform(get("/register/email/123@abc.com/verify")).
                andExpect(status().isOk()).
                andExpect(model().attributeExists("userInteractiveDto")).
                andDo(MockMvcResultHandlers.print()).andReturn();

        UserInteractiveDto UserInteractiveDto = ((UserInteractiveDto)result.getModelAndView().getModel().get("userInteractiveDto"));
        String status = UserInteractiveDto.getStatus();
        boolean exist = UserInteractiveDto.getData().getExist();

        assertEquals(CommonConstants.SUCCESS_STATUS, status);
        assertTrue(exist);
    }

    @Test
    public void shouldUserEmailIsNotExisted() throws Exception {

        when(userService.userEmailIsExisted(anyString())).thenReturn(false);

        MvcResult result = this.mockMvc.perform(get("/register/email/123@abc.com/verify")).
                andExpect(status().isOk()).
                andExpect(model().attributeExists("userInteractiveDto")).
                andDo(MockMvcResultHandlers.print()).andReturn();

        UserInteractiveDto UserInteractiveDto = ((UserInteractiveDto)result.getModelAndView().getModel().get("userInteractiveDto"));
        String status = UserInteractiveDto.getStatus();
        boolean exist = UserInteractiveDto.getData().getExist();

        assertEquals(CommonConstants.FAIL_STATUS, status);
        assertFalse(exist);
    }

    @Test
    public void shouldMobileNumberIsExisted() throws Exception {

        when(userService.userMobileNumberIsExisted(anyString())).thenReturn(true);

        MvcResult result = this.mockMvc.perform(get("/register/mobileNumber/13900000000/verify")).
                andExpect(status().isOk()).
                andExpect(model().attributeExists("userInteractiveDto")).
                andDo(MockMvcResultHandlers.print()).andReturn();

        UserInteractiveDto UserInteractiveDto = ((UserInteractiveDto)result.getModelAndView().getModel().get("userInteractiveDto"));
        String status = UserInteractiveDto.getStatus();
        boolean exist = UserInteractiveDto.getData().getExist();

        assertEquals(CommonConstants.SUCCESS_STATUS, status);
        assertTrue(exist);
    }

    @Test
    public void shouldMobileNumberIsNotExisted() throws Exception {

        when(userService.userEmailIsExisted(anyString())).thenReturn(false);

        MvcResult result = this.mockMvc.perform(get("/register/mobileNumber/13900000000/verify")).
                andExpect(status().isOk()).
                andExpect(model().attributeExists("userInteractiveDto")).
                andDo(MockMvcResultHandlers.print()).andReturn();

        UserInteractiveDto UserInteractiveDto = ((UserInteractiveDto)result.getModelAndView().getModel().get("userInteractiveDto"));
        String status = UserInteractiveDto.getStatus();
        boolean exist = UserInteractiveDto.getData().getExist();

        assertEquals(CommonConstants.FAIL_STATUS, status);
        assertFalse(exist);
    }

    @Test
    public void shouldReferrerIsExisted() throws Exception {

        when(userService.referrerIsExisted(anyString())).thenReturn(true);

        MvcResult result = this.mockMvc.perform(get("/register/referrer/hourglass/verify")).
                andExpect(status().isOk()).
                andExpect(model().attributeExists("userInteractiveDto")).
                andDo(MockMvcResultHandlers.print()).andReturn();

        UserInteractiveDto UserInteractiveDto = ((UserInteractiveDto)result.getModelAndView().getModel().get("userInteractiveDto"));
        String status = UserInteractiveDto.getStatus();
        boolean exist = UserInteractiveDto.getData().getExist();

        assertEquals(CommonConstants.SUCCESS_STATUS, status);
        assertTrue(exist);
    }

    @Test
    public void shouldReferrerIsNotExisted() throws Exception {

        when(userService.referrerIsExisted(anyString())).thenReturn(false);

        MvcResult result = this.mockMvc.perform(get("/register/referrer/hourglass/verify")).
                andExpect(status().isOk()).
                andExpect(model().attributeExists("userInteractiveDto")).
                andDo(MockMvcResultHandlers.print()).andReturn();

        UserInteractiveDto UserInteractiveDto = ((UserInteractiveDto)result.getModelAndView().getModel().get("userInteractiveDto"));
        String status = UserInteractiveDto.getStatus();
        boolean exist = UserInteractiveDto.getData().getExist();

        assertEquals(CommonConstants.FAIL_STATUS, status);
        assertFalse(exist);
    }

}
