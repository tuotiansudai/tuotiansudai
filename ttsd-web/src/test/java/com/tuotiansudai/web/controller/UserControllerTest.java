package com.tuotiansudai.web.controller;

import com.tuotiansudai.service.UserService;
import com.tuotiansudai.web.dto.RegisterVerificationStatus;
import com.tuotiansudai.web.dto.RegisterVerifyDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        MvcResult result = this.mockMvc.perform(get("/register/email/123@abc.com/verify"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("registerVerifyDto"))
                .andDo(MockMvcResultHandlers.print()).andReturn();

        RegisterVerifyDto registerVerifyDto = ((RegisterVerifyDto)result.getModelAndView().getModel().get("registerVerifyDto"));
        boolean exist = registerVerifyDto.getData().getExist();

        assertEquals(RegisterVerificationStatus.SUCCESS, registerVerifyDto.getStatus().SUCCESS);
        assertTrue(exist);

    }

    @Test
    public void shouldUserEmailIsNotExisted() throws Exception {

        when(userService.userEmailIsExisted(anyString())).thenReturn(false);

        MvcResult result = this.mockMvc.perform(get("/register/email/123@abc.com/verify")).
                andExpect(status().isOk()).
                andExpect(model().attributeExists("registerVerifyDto")).
                andDo(MockMvcResultHandlers.print()).andReturn();

        RegisterVerifyDto registerVerifyDto = ((RegisterVerifyDto)result.getModelAndView().getModel().get("registerVerifyDto"));
        boolean exist = registerVerifyDto.getData().getExist();

        assertEquals(RegisterVerificationStatus.FAIL, registerVerifyDto.getStatus().FAIL);
        assertFalse(exist);
    }

    @Test
    public void shouldMobileNumberIsExisted() throws Exception {

        when(userService.userMobileNumberIsExisted(anyString())).thenReturn(true);

        MvcResult result = this.mockMvc.perform(get("/register/mobileNumber/13900000000/verify")).
                andExpect(status().isOk()).
                andExpect(model().attributeExists("registerVerifyDto")).
                andDo(MockMvcResultHandlers.print()).andReturn();

        RegisterVerifyDto registerVerifyDto = ((RegisterVerifyDto)result.getModelAndView().getModel().get("registerVerifyDto"));
        boolean exist = registerVerifyDto.getData().getExist();

        assertEquals(RegisterVerificationStatus.SUCCESS, registerVerifyDto.getStatus().SUCCESS);
        assertTrue(exist);
    }

    @Test
    public void shouldMobileNumberIsNotExisted() throws Exception {

        when(userService.userEmailIsExisted(anyString())).thenReturn(false);

        MvcResult result = this.mockMvc.perform(get("/register/mobileNumber/13900000000/verify")).
                andExpect(status().isOk()).
                andExpect(model().attributeExists("registerVerifyDto")).
                andDo(MockMvcResultHandlers.print()).andReturn();

        RegisterVerifyDto registerVerifyDto = ((RegisterVerifyDto)result.getModelAndView().getModel().get("registerVerifyDto"));
        boolean exist = registerVerifyDto.getData().getExist();

        assertEquals(RegisterVerificationStatus.FAIL, registerVerifyDto.getStatus().FAIL);
        assertFalse(exist);
    }

    @Test
    public void shouldReferrerIsExisted() throws Exception {

        when(userService.referrerIsExisted(anyString())).thenReturn(true);

        MvcResult result = this.mockMvc.perform(get("/register/referrer/hourglass/verify")).
                andExpect(status().isOk()).
                andExpect(model().attributeExists("registerVerifyDto")).
                andDo(MockMvcResultHandlers.print()).andReturn();

        RegisterVerifyDto registerVerifyDto = ((RegisterVerifyDto)result.getModelAndView().getModel().get("registerVerifyDto"));
        boolean exist = registerVerifyDto.getData().getExist();

        assertEquals(RegisterVerificationStatus.FAIL, registerVerifyDto.getStatus().FAIL);
        assertTrue(exist);
    }

    @Test
    public void shouldReferrerIsNotExisted() throws Exception {

        when(userService.referrerIsExisted(anyString())).thenReturn(false);

        MvcResult result = this.mockMvc.perform(get("/register/referrer/hourglass/verify")).
                andExpect(status().isOk()).
                andExpect(model().attributeExists("registerVerifyDto")).
                andDo(MockMvcResultHandlers.print()).andReturn();

        RegisterVerifyDto registerVerifyDto = ((RegisterVerifyDto)result.getModelAndView().getModel().get("registerVerifyDto"));
        boolean exist = registerVerifyDto.getData().getExist();

        assertEquals(RegisterVerificationStatus.FAIL, registerVerifyDto.getStatus().FAIL);
        assertFalse(exist);
    }

    @Test
    public void shouldJsonRegisterUser() throws Exception{
        String jsonStr =  "{\"loginName\":\"zourenzheng\",\"password\":\"123abc\",\"email\":\"zourenzheng@tuotiansudai.com\",\"mobileNumber\":\"13436964915\"}";
        MvcResult result = mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(jsonStr)).
                andExpect(status().isOk()).
                andDo(MockMvcResultHandlers.print()).andReturn();
        String expectJson = "{\"status\":\"success\",\"data\":[{\"id\":0,\"loginName\":\"zourenzheng\",\"password\":\"123abc\",\"email\":\"zourenzheng@tuotiansudai.com\",\"address\":null,\"mobileNumber\":\"13436964915\",\"lastLoginTime\":null,\"registerTime\":null,\"lastModifiedTime\":null,\"lastModifiedUser\":null,\"forbiddenTime\":null,\"avatar\":null,\"referrer\":null,\"status\":null,\"salt\":null}]}";
        String resultJson = result.getResponse().getContentAsString();
        assertEquals(expectJson,resultJson);
    }


}
