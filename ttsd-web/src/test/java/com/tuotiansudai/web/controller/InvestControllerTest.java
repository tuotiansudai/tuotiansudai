package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.security.MyUser;
import com.tuotiansudai.service.InvestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dispatcher-servlet.xml", "classpath:applicationContext.xml", "classpath:spring-security.xml"})
@WebAppConfiguration
@Transactional
public class InvestControllerTest {

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private InvestService investService;

    private MockMvc mockMvc;

    @InjectMocks
    private InvestController investController;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".ftl");

        this.mockMvc = MockMvcBuilders.standaloneSetup(this.investController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void invest() throws Exception{
        mockLoginUser("investor", "13900000000");

        AccountModel accountModel = new AccountModel();
        accountModel.setAutoInvest(true);
        accountModel.setNoPasswordInvest(false);

        BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
        baseDto.setSuccess(true);
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        payFormDataDto.setStatus(true);
        baseDto.setData(payFormDataDto);

        when(accountMapper.findByLoginName(anyString())).thenReturn(accountModel);
        when(investService.invest(any(InvestDto.class))).thenReturn(baseDto);

        this.mockMvc.perform(post("/invest")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("loginName", "investor").param("loanId", "1000000000").param("amount", "100").param("source", "WEB"))
                .andExpect(view().name("/pay"));
    }

    @Test
    public void noPasswordInvest() throws Exception{
        mockLoginUser("investor", "13900000000");

        AccountModel accountModel = new AccountModel();
        accountModel.setAutoInvest(true);
        accountModel.setNoPasswordInvest(true);

        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        payDataDto.setStatus(true);
        baseDto.setSuccess(true);
        baseDto.setData(payDataDto);

        when(investService.noPasswordInvest(any(InvestDto.class))).thenReturn(baseDto);
        when(accountMapper.findByLoginName(anyString())).thenReturn(accountModel);

        this.mockMvc.perform(post("/invest")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("loginName", "investor").param("loanId", "1000000000").param("amount", "100").param("source", "WEB"))
                .andExpect(view().name("redirect:/account"));
    }

    private void mockLoginUser(String loginName, String mobile){
        MyUser user = new MyUser(loginName,"", true, true, true, true, AuthorityUtils.createAuthorityList("ROLE_PATRON"), mobile, "fdafdsa");
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user,null);
        SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);
    }
}
